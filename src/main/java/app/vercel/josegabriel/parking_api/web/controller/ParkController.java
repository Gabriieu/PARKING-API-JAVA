package app.vercel.josegabriel.parking_api.web.controller;

import app.vercel.josegabriel.parking_api.entity.client.Client;
import app.vercel.josegabriel.parking_api.entity.clientPark.ClientPark;
import app.vercel.josegabriel.parking_api.entity.clientPark.dto.ClientParkCreateDTO;
import app.vercel.josegabriel.parking_api.entity.clientPark.dto.ClientParkResponseDTO;
import app.vercel.josegabriel.parking_api.jwt.JwtUserDetails;
import app.vercel.josegabriel.parking_api.service.ClientParkService;
import app.vercel.josegabriel.parking_api.service.ClientService;
import app.vercel.josegabriel.parking_api.service.JasperService;
import app.vercel.josegabriel.parking_api.service.ParkService;
import app.vercel.josegabriel.parking_api.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/parks")
@RequiredArgsConstructor
@Tag(name = "Estacionar", description = "Operações relacionadas a estacionamento")
public class ParkController {

    private final ParkService parkService;
    private final ClientService clientService;
    private final ClientParkService clientParkService;
    private final JasperService jasperService;

    @Operation(summary = "Operações de check-in", description = "Realiza o check-in de um cliente",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Check-in realizado",
                            headers = @Header(name = HttpHeaders.LOCATION, description = "Localização do recurso criado"),
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientParkResponseDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso disponível apenas para administradores",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Possíveis causas: <br/>" +
                            "- Cliente não encontrado <br/>" +
                            "- Não há vagas disponíveis",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Campo(s) inválido(s)",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @PostMapping("/check-in")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClientParkResponseDTO> checkIn(@RequestBody @Valid ClientParkCreateDTO dto) {
        Client client = clientService.findByCpf(dto.clientCpf());
        ClientPark clientPark = new ClientPark(dto);
        clientPark.setClient(client);
        parkService.checkIn(clientPark);

        ClientParkResponseDTO responseDTO = new ClientParkResponseDTO(clientPark, null, null, null);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{receipt}")
                .buildAndExpand(clientPark.getReceipt()).toUri();

        return ResponseEntity.created(uri).body(responseDTO);
    }

    @Operation(summary = "Obter recibo", description = "Obtém o recibo pelo número",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "receipt", description = "Número do recibo do cliente",
                            required = true, example = "XXX0000-00000000-000000"),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recibo encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ClientParkResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Possíveis causas: <br/>" +
                            "- Recibo não encontrado <br/>" +
                            "- Check-out já foi realizado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @GetMapping("/check-in/{receipt}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity<ClientParkResponseDTO> getReceipt(@PathVariable String receipt) {
        ClientPark clientPark = clientParkService.findByReceipt(receipt);
        ClientParkResponseDTO responseDTO = new ClientParkResponseDTO(clientPark, null, null, null);
        return ResponseEntity.ok(responseDTO);
    }

    @Operation(summary = "Operações de check-out", description = "Realiza o check-out de um cliente",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "receipt", description = "Número do recibo do cliente",
                            required = true, example = "XXX0000-00000000-000000"),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Check-out realizado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientParkResponseDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso disponível apenas para administradores",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Possíveis causas: <br/>" +
                            "- Recibo não encontrado <br/>" +
                            "- Check-out já foi realizado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @PutMapping("/check-out/{receipt}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClientParkResponseDTO> checkOut(@PathVariable String receipt) {
        ClientPark clientPark = parkService.checkOut(receipt);
        ClientParkResponseDTO responseDTO = new ClientParkResponseDTO(clientPark, LocalDateTime.now().toString(),
                clientPark.getValue().toString(), clientPark.getDiscount().toString());
        return ResponseEntity.ok(responseDTO);
    }

    @Operation(summary = "Operações de busca pelo cpf do cliente",
            description = "Busca todos os registros de um cliente pelo cpf",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "cpf", description = "CPF do cliente",
                            required = true, example = "00000000000"),
                    @Parameter(in = ParameterIn.QUERY, name = "page", description = "Número da página",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "0")),
                            example = "0"),
                    @Parameter(in = ParameterIn.QUERY, name = "size", description = "Tamanho da página",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "5")),
                            example = "10"),
                    @Parameter(in = ParameterIn.QUERY, name = "sort", description = "Ordenação da página",
                            content = @Content(schema = @Schema(type = "string", defaultValue = "entryTime,asc")),
                            example = "exitTime,desc"),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Retorna a lista de registros de estacionamento",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientParkResponseDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso disponível apenas para administradores",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @GetMapping("/cpf/{cpf}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ClientParkResponseDTO>> getAllParksByCpf(@PathVariable String cpf,
                                                                        @PageableDefault(size = 5,
                                                                                sort = "entryTime",
                                                                                direction = Sort.Direction.ASC) Pageable page) {
        var parks = clientParkService.findAllByClientCpf(cpf, page)
                .map(ClientParkResponseDTO::new);

        return ResponseEntity.ok(parks);
    }

    @Operation(summary = "Buscar registros de estacionamento do cliente logado",
            description = "Busca todos os registros de estacionamento do cliente logado",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(in = ParameterIn.QUERY, name = "page", description = "Número da página",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "0")),
                            example = "0"),
                    @Parameter(in = ParameterIn.QUERY, name = "size", description = "Tamanho da página",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "5")),
                            example = "10"),
                    @Parameter(in = ParameterIn.QUERY, name = "sort", description = "Ordenação da página",
                            content = @Content(schema = @Schema(type = "string", defaultValue = "entryTime,asc")),
                            example = "exitTime,desc",
                            hidden = true),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Retorna a lista de registros de estacionamento",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientParkResponseDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso disponível apenas para clientes",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @GetMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Page<ClientParkResponseDTO>> getAllClientParks(@AuthenticationPrincipal JwtUserDetails userDetails,
                                                                         @PageableDefault(size = 5,
                                                                                 sort = "entryTime",
                                                                                 direction = Sort.Direction.ASC) Pageable page) {
        System.out.println(userDetails.getId());
        var parks = clientParkService.findAllParksByUserId(userDetails.getId(), page)
                .map(ClientParkResponseDTO::new);

        return ResponseEntity.ok(parks);
    }

    @GetMapping("/report")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Void> getReport(HttpServletResponse response,
                                          @AuthenticationPrincipal JwtUserDetails userDetails) throws IOException {

        String userCpf = clientService.findByUserId(userDetails.getId()).getCpf();
        jasperService.addParams("cpf", userCpf);
        byte[] bytes = jasperService.generatePdf();

        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + userCpf + "_" + System.currentTimeMillis() + ".pdf");
        response.getOutputStream().write(bytes);

        return ResponseEntity.ok().build();
    }
}
