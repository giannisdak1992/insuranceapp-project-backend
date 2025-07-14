package gr.aueb.cf.insuranceapp.rest;

import gr.aueb.cf.insuranceapp.authentication.AuthenticationService;
import gr.aueb.cf.insuranceapp.core.exceptions.AppObjectNotAuthorizedException;
import gr.aueb.cf.insuranceapp.dto.AuthenticationRequestDTO;
import gr.aueb.cf.insuranceapp.dto.AuthenticationResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> login (
            @Valid @RequestBody AuthenticationRequestDTO authenticationRequestDTO
            ) throws AppObjectNotAuthorizedException {
        AuthenticationResponseDTO authenticationResponseDTO = authenticationService.authenticate(authenticationRequestDTO);
        return ResponseEntity.ok(authenticationResponseDTO);
    }
}
