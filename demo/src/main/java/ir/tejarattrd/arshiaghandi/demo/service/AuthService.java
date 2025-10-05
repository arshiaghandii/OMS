package ir.tejarattrd.arshiaghandi.demo.service;

import ir.tejarattrd.arshiaghandi.demo.model.Role;
import ir.tejarattrd.arshiaghandi.demo.model.User;
import ir.tejarattrd.arshiaghandi.demo.repository.RoleRepository;
import ir.tejarattrd.arshiaghandi.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;

    }

    public User registerNewUser(User user) {
        // هش کردن پسورد قبل از ذخیره
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // پیدا کردن و اختصاص دادن نقش پیش‌فرض CUSTOMER
        Role userRole = roleRepository.findByName("Customer")
                .orElseThrow(() -> new RuntimeException("Error: Role 'CUSTOMER' is not found."));
        user.setRoles(Set.of(userRole));

        return userRepository.save(user);
    }
}