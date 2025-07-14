package gr.aueb.cf.insuranceapp.authentication;

import gr.aueb.cf.insuranceapp.core.exceptions.AppObjectNotAuthorizedException;
import gr.aueb.cf.insuranceapp.dto.AuthenticationRequestDTO;
import gr.aueb.cf.insuranceapp.dto.AuthenticationResponseDTO;
import gr.aueb.cf.insuranceapp.model.User;
import gr.aueb.cf.insuranceapp.repository.UserRepository;
import gr.aueb.cf.insuranceapp.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;


    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO dto)
            throws AppObjectNotAuthorizedException {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new AppObjectNotAuthorizedException("User", "User not authorized"));

        String token = jwtService.generateToken(authentication.getName(), user.getRole().name());
        return new AuthenticationResponseDTO(user.getFirstname(), user.getLastname(), token);
    }
}