package app.vercel.josegabriel.parking_api.web.controller;

import app.vercel.josegabriel.parking_api.entity.user.User;
import app.vercel.josegabriel.parking_api.entity.user.dto.UserCreateDTO;
import app.vercel.josegabriel.parking_api.entity.user.dto.UserResponseDTO;
import app.vercel.josegabriel.parking_api.entity.user.dto.UserUpdatePasswordDTO;
import app.vercel.josegabriel.parking_api.service.UserService;
import app.vercel.josegabriel.parking_api.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Operações relacionadas aos usuários")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Cria um novo usuário", responses = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Email já cadastrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "422", description = "Campos inválidos",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    }, description = "Cria um novo usuário, autenticação não é requerida")
    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@RequestBody @Valid UserCreateDTO user) {
        User userEntity = userService.save(new User(user));

        return ResponseEntity.status(HttpStatus.CREATED).body(new UserResponseDTO(userEntity));
    }

    @Operation(summary = "Obtém todos os usuários",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Retorna todos os usuários",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Permissão insuficiente",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }, description = "Obtém todos os usuários, autenticação é requerida e apenas um admin pode ver todos os usuários")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserResponseDTO>> getAll(@PageableDefault(size = 5)Pageable page) {
        Page<UserResponseDTO> users = userService.findAll(page).map(UserResponseDTO::new);

        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Obtém um usuário pelo id",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Permissão insuficiente",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }, description = "Obtém um usuário pelo id, autenticação é requerida e apenas o usuário ou um admin pode ver o usuário")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') OR #id == principal.id")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable Long id) {
        User user = userService.findById(id);

        return ResponseEntity.ok(new UserResponseDTO(user));
    }

    @Operation(summary = "Atualiza a senha do usuário",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Senha atualizada com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "400", description = "Senhas não conferem ou senha inválida",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Permissão insuficiente",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }, description = "Atualiza a senha do usuário, autenticação é requerida e apenas o usuário pode atualizar sua senha")
    @PatchMapping("/{id}")
    @PreAuthorize("#id == principal.id")
    public ResponseEntity<Void> updatePassword(@RequestBody @Valid UserUpdatePasswordDTO data,
                                               @PathVariable Long id) {
        userService.updatePassword(id,
                data.currentPassword(),
                data.newPassword(),
                data.confirmPassword());

        return ResponseEntity.noContent().build();
    }
}
