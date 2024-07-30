package app.vercel.josegabriel.parking_api.service;

import app.vercel.josegabriel.parking_api.entity.user.User;
import app.vercel.josegabriel.parking_api.exception.EntityNotFoundException;
import app.vercel.josegabriel.parking_api.exception.InvalidPasswordException;
import app.vercel.josegabriel.parking_api.exception.UsernameUniqueViolationException;
import app.vercel.josegabriel.parking_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User save(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        } catch (
                DataIntegrityViolationException exception) {
            throw new UsernameUniqueViolationException("Username já cadastrado");
        }
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }

    @Transactional(readOnly = true)
    public User.Role findRoleByUsername(String username) {
        return userRepository.findRoleByUsername(username);
    }

    @Transactional
    public void updatePassword(Long id, String password, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new InvalidPasswordException("Senhas não conferem");
        }

        User user = findById(id);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidPasswordException("Senha inválida");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
    }

    @Transactional(readOnly = true)
    public Page<User> findAll(Pageable page) {
        return userRepository.findAll(page);
    }
}
