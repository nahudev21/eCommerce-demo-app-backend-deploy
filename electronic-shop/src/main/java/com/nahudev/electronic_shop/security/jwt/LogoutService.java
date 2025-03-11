package com.nahudev.electronic_shop.security.jwt;

import com.nahudev.electronic_shop.repository.ITokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
public class LogoutService implements LogoutHandler {

    @Autowired
    private ITokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {

        String jwt = parseJwt(request);
        var storedToken = tokenRepository.findByAccessToken(jwt).orElse(null);

        if (storedToken != null) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
            tokenRepository.delete(storedToken);
        }

    }

    public String parseJwt(HttpServletRequest request) {
        String tokenHeader = request.getHeader("Authorization");

        if(tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            return tokenHeader.substring(7);
        }

        return null;
    }

}
