package br.com.cervejamatch.main.users.repository;

import br.com.cervejamatch.main.users.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
}
