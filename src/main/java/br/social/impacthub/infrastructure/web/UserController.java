package br.social.impacthub.infrastructure.web;

import br.social.impacthub.infrastructure.web.docs.UserControllerDocs;
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
public class UserController implements UserControllerDocs {
    private UserProfileService userProfileService;
    private UserProfileMapper userProfileMapper;
    private AuthService authService;

    public UserController(UserProfileService userProfileService, UserProfileMapper userProfileMapper, AuthService authService) {
        this.userProfileService = userProfileService;
        this.userProfileMapper = userProfileMapper;
        this.authService = authService;
    }

    @GetMapping
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
    public ResponseEntity<StandardResponse<UserProfileResponse>> getAuthenticatedUser(){
        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponse.success(
                            userProfileService.getById(authenticatedUserId)
                        )
                );
    }

    @GetMapping("/{id}")
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
    public ResponseEntity<StandardResponse<Void>> deleteAuthenticatedUser(){
        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();
        userProfileService.delete(authenticatedUserId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(StandardResponse.success());
    }
}