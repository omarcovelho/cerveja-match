package br.com.cervejamatch.main.users.controller;

import br.com.cervejamatch.main.users.controller.converter.UserConverter;
import br.com.cervejamatch.main.users.controller.dto.DefinicoesDeFiltroDTO;
import br.com.cervejamatch.main.users.controller.dto.UserDTO;
import br.com.cervejamatch.main.users.model.DefinicoesDeFiltro;
import br.com.cervejamatch.main.users.model.User;
import br.com.cervejamatch.main.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {

    private final UserRepository userRepository;
    private final UserConverter userConverter;

    @GetMapping
    public List<UserDTO> getUsers() {
        return userRepository.findAll().stream().map(userConverter::toDto).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        if(userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email ja está em uso");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userConverter.toDto(userRepository.save(userConverter.toModel(userDTO))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody UserDTO userDTO) {
        Optional<User> optUser = userRepository.findById(id);
        if(optUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User user = optUser.get();
        return ResponseEntity.ok(userConverter.toDto(userRepository.save(user.updateInfo(userDTO.getNome(), userDTO.getSobrenome()))));
    }

    @PutMapping("/{id}/filtros")
    public ResponseEntity<?> updateUserFiltros(@PathVariable String id, @RequestBody DefinicoesDeFiltroDTO definicoesDeFiltroDTO) {
        Optional<User> optUser = userRepository.findById(id);
        if(optUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User user = optUser.get();
        return ResponseEntity.ok(userConverter.toDto(userRepository.save(user.updateDefinicoesDeFiltro(userConverter.definicoesDeFitlroToModel(definicoesDeFiltroDTO)))));
    }
}
