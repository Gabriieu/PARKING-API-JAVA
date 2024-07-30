package app.vercel.josegabriel.parking_api.web.controller;

import app.vercel.josegabriel.parking_api.entity.client.Client;
import app.vercel.josegabriel.parking_api.entity.client.dto.ClientCreateDTO;
import app.vercel.josegabriel.parking_api.entity.client.dto.ClientResponseDTO;
import app.vercel.josegabriel.parking_api.jwt.JwtUserDetails;
import app.vercel.josegabriel.parking_api.service.ClientService;
import app.vercel.josegabriel.parking_api.service.UserService;
import app.vercel.josegabriel.parking_api.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
@Tag(name = "Cliente", description = "Operações relacionadas a clientes")
public class ClientController {

    private final ClientService clientService;
    private final UserService userService;

    @Operation(summary = "Cadastrar clientes", description = "Cadastra um novo cliente",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Cliente cadastrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientResponseDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso autorizado apenas para clientes",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "409", description = "Usuário já cadastrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Campo inválido",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ClientResponseDTO> create(@RequestBody @Valid ClientCreateDTO clientData,
                                                    @AuthenticationPrincipal JwtUserDetails userDetails) {

        Client client = new Client(clientData);
        client.setUser(userService.findById(userDetails.getId()));
        clientService.save(client);
        return ResponseEntity.status(201).body(new ClientResponseDTO(client));
    }

    @Operation(summary = "Encontrar clientes", description = "Encontra um cliente pelo id",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cliente encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientResponseDTO.class))),
                    @ApiResponse(responseCode = "401", description = "Requer autenticação",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientResponseDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não autorizado para clientes",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClientResponseDTO> findById(@PathVariable Long id) {
        var client = clientService.findById(id);
        return ResponseEntity.ok(new ClientResponseDTO(client));
    }

    @Operation(summary = "Listar clientes", description = "Lista todos os clientes",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(in = ParameterIn.QUERY,
                            name = "page", description = "Número da página",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "0"))),
                    @Parameter(in = ParameterIn.QUERY,
                            name = "size", description = "Tamanho da página",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "10"))),
                    @Parameter(in = ParameterIn.QUERY, hidden = true,
                            name = "sort", description = "Ordenação da página",
                            content = @Content(schema = @Schema(type = "string", defaultValue = "id,asc"))),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Clientes listados",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientResponseDTO.class))),
                    @ApiResponse(responseCode = "401", description = "Requer autenticação",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não autorizado para clientes",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ClientResponseDTO>> findAll(@Parameter(hidden = true) @PageableDefault(size = 5, sort = "name") Pageable pagination) {
        var clients = clientService.findAll(pagination).map(ClientResponseDTO::new);
        return ResponseEntity.ok(clients);
    }

    @Operation(summary = "Listar detalhes do cliente", description = "Lista os detalhes do cliente autenticado",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Clientes listados",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientResponseDTO.class))),
                    @ApiResponse(responseCode = "401", description = "Requer autenticação",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não autorizado para administradores",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @GetMapping("/details")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ClientResponseDTO> getDetails(@AuthenticationPrincipal JwtUserDetails userDetails) {
        var client = clientService.findByUserId(userDetails.getId());
        return ResponseEntity.ok(new ClientResponseDTO(client));
    }
}
