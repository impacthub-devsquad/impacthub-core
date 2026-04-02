package br.social.impacthub.infrastructure.web;

import br.social.impacthub.model.dto.PagedResponse;
import br.social.impacthub.model.dto.StandardResponse;
import br.social.impacthub.model.dto.UpdateUserRequest;
import br.social.impacthub.model.dto.UserProfileResponse;
import br.social.impacthub.service.UserProfileService;
import br.social.impacthub.service.mapper.UserProfileMapper;
import br.social.impacthub.service.security.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

// TODO implement this

@RestController
@RequestMapping("/api/v1/users")
@Tag(name  = "Users", description = "Endpoints relacionados aos usuários")
public class UserController {
    private UserProfileService userProfileService;
    private UserProfileMapper userProfileMapper;
    private AuthService authService;

    public UserController(UserProfileService userProfileService, UserProfileMapper userProfileMapper, AuthService authService) {
        this.userProfileService = userProfileService;
        this.userProfileMapper = userProfileMapper;
        this.authService = authService;
    }

    @GetMapping
    @Operation(
            summary = "Busca todos os usuários",
            description = "Retorna os dados completos de todos os usuários"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuários retornados com sucesso."),
        @ApiResponse(responseCode = "404", description = "Não foi encontrado usuários registrados na base de dados.")
    })
    public ResponseEntity<StandardResponse<PagedResponse<UserProfileResponse>>> getAll(
            @RequestParam(name = "q", required = false) String query,
            Pageable pageable
    ){
        PagedResponse<UserProfileResponse> response;

        if (query == null)
            response = userProfileService.getAll(pageable);
        else
            response = userProfileService.search(query, pageable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponse.success(
                                response
                        )
                );
    }

    @GetMapping("/me")
    @Operation(
            summary = "Busca o usuário autenticado",
            description = "Retorna os dados completos do usuário autenticado"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuários retornados com sucesso."),
            @ApiResponse(responseCode = "404", description = "Não foi encontrado usuários registrados na base de dados.")
    })
    public ResponseEntity<StandardResponse<UserProfileResponse>> getAuthenticatedUser(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponse.success(
                            userProfileMapper.toResponse(
                                    authService.getAuthenticatedUser()
                            )
                        )
                );
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Busca usuário por Id",
            description = "Retorna os dados completos do usuário buscado por Id"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário retornado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Não foi encontrado usuário registrado com este Id."),
            @ApiResponse(responseCode = "400", description = "ID do usuário é inválido.")
    })
    public ResponseEntity<StandardResponse<UserProfileResponse>> getUserById(@PathVariable(name = "id") UUID id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponse.success(
                                userProfileService.getById(id)
                        )
                );
    }

    @PatchMapping("/me")
    @Operation(
            summary = "Atualiza informações do usuário",
            description = "Atualiza os dados de um usuários através de seu Id"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuários retornados com sucesso."),
            @ApiResponse(responseCode = "404", description = "Não foi encontrado usuários registrados na base de dados."),
            @ApiResponse(responseCode = "400", description = "ID do usuário é inválido.")
    })
    public ResponseEntity<StandardResponse<UserProfileResponse>> updateAuthenticatedUser(@Valid @RequestBody UpdateUserRequest request){
        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponse.success(
                                userProfileService.patch(authenticatedUserId, request)
                        )
                );
    }

    @DeleteMapping("/me")
    @Operation(
            summary = "Deleta o usuário",
            description = "Apaga o usuário autenticado da base de dados"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuários retornados com sucesso."),
            @ApiResponse(responseCode = "202", description = "A operação de deleção foi aceita."),
            @ApiResponse(responseCode = "204", description = "Nenhum conteúdo a retornar."),
            @ApiResponse(responseCode = "404", description = "Não foi encontrado usuário registrado com este Id."),
            @ApiResponse(responseCode = "400", description = "Erro de validação. ID do usuário é inválido.")

    })
    public ResponseEntity<StandardResponse<Void>> deleteAuthenticatedUser(){
        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();
        userProfileService.delete(authenticatedUserId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(StandardResponse.success());
    }
}