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
        BeanUtils.copyProperties(user, userEntity, "id");

        userEntity.setEncryptedPassword(passwordEncoder.encode(user.getPassword()));

        UserEntity storedUserEntity = userRepository.save(userEntity);

        UserDto returnUserDtoDetails = new UserDto();
        BeanUtils.copyProperties(storedUserEntity, returnUserDtoDetails);

        return returnUserDtoDetails;
    }

    @Override
    public UserDto updateUser(UserDto user) {

        if(user.getPassword() != null && !user.getPassword().isEmpty()){
            user.setEncryptedPassword(passwordEncoder.encode(user.getPassword()));
        }else {
            user.setEncryptedPassword(null);
        }

        Integer linhasModificadas = userRepository.updatePartialUser(
                user.getFullName(),
                user.getEmail(),
                user.getUsername(),
                user.getEncryptedPassword(),
                user.getId()
        );

        if (linhasModificadas == 0) {
            throw new RuntimeException("User not found");
        }

        return user;
    }

    @Override
    public void deleteUser(Long id) {

        userRepository.deleteById(id);
    }

    @Override
    public UserDto findById(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userEntity, userDto);
        return userDto;
    }

    @Override
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
