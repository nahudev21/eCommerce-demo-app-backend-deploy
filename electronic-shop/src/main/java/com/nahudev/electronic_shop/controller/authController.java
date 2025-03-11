package com.nahudev.electronic_shop.controller;

import com.nahudev.electronic_shop.model.Token;
import com.nahudev.electronic_shop.model.TokenType;
import com.nahudev.electronic_shop.model.User;
import com.nahudev.electronic_shop.repository.ITokenRepository;
import com.nahudev.electronic_shop.repository.IUserRepository;
import com.nahudev.electronic_shop.request.LoginRequest;
import com.nahudev.electronic_shop.response.ApiResponse;
import com.nahudev.electronic_shop.response.JwtResponse;
import com.nahudev.electronic_shop.security.jwt.JwtUtils;
import com.nahudev.electronic_shop.security.user.ShopUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/auth")
public class authController {

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final IUserRepository userRepository;

    private final ITokenRepository tokenRepository;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {

        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateTokenFromUser(authentication);
            ShopUserDetails userDetails = (ShopUserDetails) authentication.getPrincipal();
            User user = userRepository.findByEmail(request.getEmail());

            Token tokenUser = new Token();
            tokenUser.setAccessToken(jwt);
            tokenUser.setTokenType(TokenType.BEARER);
            tokenUser.setExpired(false);
            tokenUser.setRevoked(false);
            tokenUser.setUser(user);

            revokeAllUserTokens(user);
            tokenRepository.save(tokenUser);

            JwtResponse jwtResponse = new JwtResponse(userDetails.getId(), jwt);
            return ResponseEntity.ok(new ApiResponse("Login successfully!", jwtResponse));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(e.getMessage(), "Email or password invalid!"));
        }

    }

    public void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());

        if (validUserTokens.isEmpty())
            return;

        validUserTokens.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

}
