package br.edu.ifpb.instagram.service.impl;

import java.util.ArrayList;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.edu.ifpb.instagram.model.dto.UserDto;
import br.edu.ifpb.instagram.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(
            userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username)),
            userDto
        );

        // creates a empty arraylist to satisfy User Spring Security requirements
        User user = new User(userDto.getUsername(), userDto.getEncryptedPassword(), new ArrayList<>());

        return user;
    }
}
