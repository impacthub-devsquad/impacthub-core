package br.social.impacthub.infrastructure.web;

import br.social.impacthub.model.dto.*;
import br.social.impacthub.service.OngInviteService;
import br.social.impacthub.service.OngService;
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

@RestController
@RequestMapping("/api/v1/ongs")
@Tag(name = "Ongs", description = "Endpoints relacionados às ONGs")
public class OngController {
    private OngInviteService ongInviteService;
    private OngService ongService;
    private AuthService authService;

    public OngController(OngInviteService ongInviteService, OngService ongService, AuthService authService) {
        this.ongInviteService = ongInviteService;
        this.ongService = ongService;
        this.authService = authService;
    }

    @GetMapping
    @Operation(
            summary = "Busca todas as Ong ",
            description = "Retorna os dados completos de todas as Ong."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ongs retornadas com sucesso."),
            @ApiResponse(responseCode = "404", description = "Não foi encontrada Ongs registradas na base de dados.")
    })
    public ResponseEntity<StandardResponse<PagedResponse<OngResponse>>> getAll(Pageable pageable){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponse.success(
                            ongService.getAll(pageable)
                        )
                );
    }

    @GetMapping("/{ongId}")
    @Operation(
            summary = "Busca Ong por Id",
            description = "Retorna os dados completos de uma Ong pelo seu Id."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ong encontrada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Ong não encontrada.")
    })
    public ResponseEntity<StandardResponse<OngResponse>> getByID(@PathVariable UUID ongId){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponse.success(
                                ongService.getById(ongId)
                        )
                );
    }


    @PostMapping
    @Operation(
            summary = "Cria uma nova Ong",
            description = "Cria uma nova Ong na base de dados.."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<StandardResponse<OngResponse>> create(@Valid @RequestBody CreateOngRequest request){
        UUID userId = authService.getAuthenticatedUser().userId();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        StandardResponse.success(
                                ongService.create(userId, request.name(), request.title(), request.description())
                        )
                );
    }

    @PatchMapping("/{ongId}")
    @Operation(
            summary = "Atualiza uma Ong",
            description = "Altera informações de uma Ong existente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Updated Successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<StandardResponse<OngResponse>> update(@PathVariable UUID ongId, @Valid @RequestBody UpdateOngRequest request){
        UUID userId = authService.getAuthenticatedUser().userId();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponse.success(
                                ongService.update(ongId, request, userId)
                        )
                );
    }

    @DeleteMapping("/{ongId}")
    @Operation(
            summary = "Deleta uma Ong",
            description = "Remove uma Ong existente da base de dados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Ong não encontrada"),
            @ApiResponse(responseCode = "403", description = "quando o usuário autenticado não tem permissão para deletar a Ong")
    })
    public ResponseEntity<StandardResponse<Void>> delete(@PathVariable UUID ongId){
        UUID userId = authService.getAuthenticatedUser().userId();

        ongService.delete(ongId, userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponse.success()
                );
    }

    @PostMapping("/{ongId}/followers/me")
    @Operation(
            summary = "Seguir uma Ong",
            description = "Permite que um usuário autenticado siga uma Ong"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Sucesso, mas sem retorno"),
            @ApiResponse(responseCode = "400", description = "Erro de validação, como seguir uma Ong inexistente")
    })
    public ResponseEntity<StandardResponse<Void>> followOng(@PathVariable UUID ongId){
        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();
        ongService.followOng(authenticatedUserId, ongId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        StandardResponse.success()
                );
    }

    @DeleteMapping("/{ongId}/followers/me")
    @Operation(
            summary = "Deixa de seguir uma Ong",
            description = "Permite que um usuário autenticado deixe de seguir uma Ong que ele já segue"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Erro de validação, como deixar de seguir uma Ong que o usuário não segue")
    })
    public ResponseEntity<StandardResponse<Void>> unfollowOng(@PathVariable UUID ongId){
        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();
        ongService.unfollowOng(authenticatedUserId, ongId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponse.success()
                );
    }
}
