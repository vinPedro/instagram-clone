package br.edu.ifpb.instagram.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.edu.ifpb.instagram.model.entity.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    List<UserEntity> findAll();
}
