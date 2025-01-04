package br.edu.ifpb.instagram.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.edu.ifpb.instagram.model.request.LoginRequestModel;
import br.edu.ifpb.instagram.security.JwtUtils;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    public String authenticate(LoginRequestModel loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
            )
        );

        // Gera o token JWT para o usuário autenticado
        return jwtUtils.generateToken(authentication);
    }
}
