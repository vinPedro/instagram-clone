package br.edu.ifpb.instagram.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public List<UserDetailsResponseModel> getUsers(){

        List<UserDto> userDtos = userService.findAll(); // get all users from database
        List<UserDetailsResponseModel> userDetailsResponseModels = new ArrayList<>(); // response that we will send back to frontend

        for (UserDto userDto : userDtos) {
            UserDetailsResponseModel userDetailsResponseModel = new UserDetailsResponseModel(); // object to move across several layers
            BeanUtils.copyProperties(userDto, userDetailsResponseModel); // copies properties from userDto to userDetailsResponseModel
            userDetailsResponseModels.add(userDetailsResponseModel); // adds userDetailsResponseModel to userDetailsResponseModels
        }

        return userDetailsResponseModels; // returns to frontend
    }


    @GetMapping("/{id}")
    public UserDetailsResponseModel getUser(@PathVariable Long id){

        UserDto userDto = userService.findById(id);
        UserDetailsResponseModel userDetailsResponseModel = new UserDetailsResponseModel();
        BeanUtils.copyProperties(userDto, userDetailsResponseModel);

        return userDetailsResponseModel;
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
    public UserDetailsResponseModel updateUser(@RequestBody UserDetailsRequestModel userDetailsRequestModel){
        // response that we will send back to frontend
        UserDetailsResponseModel userDetailsResponseModel = new UserDetailsResponseModel();

        // object to move across several layers
        UserDto userDto = new UserDto();

        // copies properties from userDetailsRequestModel to userDto
        BeanUtils.copyProperties(userDetailsRequestModel, userDto);

        // saves the object in the database
        UserDto updatedUserDto = userService.updateUser(userDto);

        // copies properties from createdUserDto to userDetailsResponseModel
        BeanUtils.copyProperties(updatedUserDto, userDetailsResponseModel);

        // returns to frontend
        return userDetailsResponseModel;
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id){

        userService.deleteUser(id);

        return "user was deleted!";
    }

}
