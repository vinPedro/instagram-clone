package br.edu.ifpb.instagram.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.instagram.entity.UserEntity;
import br.edu.ifpb.instagram.model.dto.UserDto;
import br.edu.ifpb.instagram.repository.UserRepository;
import br.edu.ifpb.instagram.service.UserService;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto user) {

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);

        // temporary
        userEntity.setEncryptedPassword("ENCRYPTED-PASSWORD-TEST");
        userEntity.setUserId("USER-ID-TEST");

        UserEntity storedUserEntity = userRepository.save(userEntity);

        UserDto returnUserDtoDetails = new UserDto();
        BeanUtils.copyProperties(storedUserEntity, returnUserDtoDetails);

        return returnUserDtoDetails;
    }

}
