package br.edu.ifpb.instagram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.instagram.model.request.LoginRequestModel;
import br.edu.ifpb.instagram.service.AuthService;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestModel loginRequest) {

        String token = authService.authenticate(loginRequest);

        return ResponseEntity.ok(token);
    }

}
