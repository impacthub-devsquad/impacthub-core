package br.social.impacthub.controller;

import br.social.impacthub.model.dto.PagedResponse;
import br.social.impacthub.model.dto.StandardResponse;
import br.social.impacthub.model.dto.UpdateUserRequest;
import br.social.impacthub.model.dto.UserProfileResponse;
import br.social.impacthub.service.UserProfileService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// TODO implement this

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private UserProfileService userProfileService;

    public UserController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping()
    public ResponseEntity<StandardResponse<PagedResponse<UserProfileResponse>>> getAll(Pageable pageable){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponse.success(
                                userProfileService.getAll(pageable)
                        )
                );
    }

    @GetMapping("/me")
    public ResponseEntity<StandardResponse<UserProfileResponse>> getAuthenticatedUser(){
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<StandardResponse<UserProfileResponse>> getUserById(@PathVariable(name = "id") Long id){
        return null;
    }

    @PatchMapping("/me")
    public ResponseEntity<StandardResponse<UserProfileResponse>> updateAuthenticatedUser(@RequestBody UpdateUserRequest request){
        return null;
    }

    @DeleteMapping("/me")
    public ResponseEntity<StandardResponse<Void>> deleteAuthenticatedUser(){
        return null;
    }
}