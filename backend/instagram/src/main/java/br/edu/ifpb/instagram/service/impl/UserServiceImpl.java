package br.edu.ifpb.instagram.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.edu.ifpb.instagram.model.entity.UserEntity;
import br.edu.ifpb.instagram.model.dto.UserDto;
import br.edu.ifpb.instagram.repository.UserRepository;
import br.edu.ifpb.instagram.service.UserService;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDto createUser(UserDto user) {

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);

        userEntity.setEncryptedPassword(passwordEncoder.encode(user.getPassword()));

        UserEntity storedUserEntity = userRepository.save(userEntity);

        UserDto returnUserDtoDetails = new UserDto();
        BeanUtils.copyProperties(storedUserEntity, returnUserDtoDetails);

        return returnUserDtoDetails;
    }

    public List<UserDto> findAll() {
        List<UserEntity> userEntities = userRepository.findAll();
        List<UserDto> userDtos = new ArrayList<>();

        if (userEntities.isEmpty()) {
            throw new RuntimeException("Users not found");
        }
        for (UserEntity userEntity : userEntities) {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(userEntity, userDto);
            userDtos.add(userDto);
        }

        return userDtos;
    }

}
