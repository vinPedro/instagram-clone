package br.edu.ifpb.instagram.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.instagram.model.dto.UserDto;
import br.edu.ifpb.instagram.model.request.LoginRequestModel;
import br.edu.ifpb.instagram.model.request.UserDetailsRequestModel;
import br.edu.ifpb.instagram.model.response.LoginResponseModel;
import br.edu.ifpb.instagram.model.response.UserDetailsResponseModel;
import br.edu.ifpb.instagram.service.AuthService;
import br.edu.ifpb.instagram.service.UserService;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<LoginResponseModel> signIn(@RequestBody LoginRequestModel loginRequest) {
        String token = authService.authenticate(loginRequest);
        LoginResponseModel loginResponseModel = new LoginResponseModel(loginRequest.getUsername(), token);

        return ResponseEntity.ok(loginResponseModel);
    }

    @PostMapping("/signup")
    public UserDetailsResponseModel signUp(@RequestBody UserDetailsRequestModel userDetailsRequestModel){

        // response that we will send back to frontend
        UserDetailsResponseModel userDetailsResponseModel = new UserDetailsResponseModel();

        // object to move across several layers
        UserDto userDto = new UserDto();

        // copies properties from userDetailsRequestModel to userDto
        BeanUtils.copyProperties(userDetailsRequestModel, userDto);

        // saves the object in the database
        UserDto createdUserDto = userService.createUser(userDto);

        // copies properties from createdUserDto to userDetailsResponseModel
        BeanUtils.copyProperties(createdUserDto, userDetailsResponseModel);

        // returns to frontend
        return userDetailsResponseModel;
    }

}
