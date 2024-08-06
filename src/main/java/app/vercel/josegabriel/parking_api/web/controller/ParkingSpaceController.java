package app.vercel.josegabriel.parking_api.web.controller;

import app.vercel.josegabriel.parking_api.entity.parking.ParkingSpace;
import app.vercel.josegabriel.parking_api.entity.parking.dto.ParkingSpaceCreateDTO;
import app.vercel.josegabriel.parking_api.entity.parking.dto.ParkingSpaceResponseDTO;
import app.vercel.josegabriel.parking_api.service.ParkingSpaceService;
import app.vercel.josegabriel.parking_api.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/parking-spaces")
@RequiredArgsConstructor
@Tag(name = "Vagas", description = "Operações relacionadas às vagas de estacionamento")
public class ParkingSpaceController {

    private final ParkingSpaceService parkingSpaceService;

    @Operation(summary = "Cria uma vaga de estacionamento", description = "Cria uma vaga de estacionamento",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Vaga criada com sucesso",
                            headers = @Header(name = HttpHeaders.LOCATION, description = "URL do recurso criado")),
                    @ApiResponse(responseCode = "409", description = "Código da vaga já cadastrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Campos inválidos",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)))})
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> create(@RequestBody @Valid ParkingSpaceCreateDTO parkingSpaceCreateDTO) {
        var parkingSpace = new ParkingSpace(parkingSpaceCreateDTO);
        parkingSpaceService.save(parkingSpace);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{code}")
                .buildAndExpand(parkingSpace.getCode()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "Obtém a vaga pelo código", description = "Obtém a vaga pelo código",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Vaga encontrada",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ParkingSpaceResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Vaga não encontrada",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)))})
    @GetMapping("/{code}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParkingSpaceResponseDTO> getByCode(@PathVariable String code) {
        var parkingSpace = parkingSpaceService.findByCode(code);
        return ResponseEntity.ok(new ParkingSpaceResponseDTO(parkingSpace));
    }

    @Operation(summary = "Excluir vagas", description = "Exclui uma vaga de estacionamento",
            security = @SecurityRequirement(name = "security"),
            parameters = @Parameter(name = "code", description = "Código da vaga", required = true),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Vaga excluída com sucesso",
                            content = @Content(mediaType = "void")),
                    @ApiResponse(responseCode = "404", description = "Vaga não encontrada",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)))})
    @DeleteMapping("/{code}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParkingSpaceResponseDTO> delete(@PathVariable String code) {
        parkingSpaceService.delete(code);
        return ResponseEntity.noContent().build();
    }
}
