package br.edu.ifpb.instagram.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.instagram.model.dto.UserDto;
import br.edu.ifpb.instagram.model.request.UserDetailsRequestModel;
import br.edu.ifpb.instagram.model.response.UserDetailsResponseModel;
import br.edu.ifpb.instagram.service.UserService;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public String getUser(){
        return "get user was called";
    }

    @PostMapping
    public UserDetailsResponseModel createUser(@RequestBody UserDetailsRequestModel userDetailsRequestModel){

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

    @PutMapping
    public String updateUser(){
        return "update user was called";
    }

    @DeleteMapping
    public String deleteUser(){
        return "delete user was called";
    }

}
