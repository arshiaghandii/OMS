package ir.tejarattrd.arshiaghandi.demo.repository;

import ir.tejarattrd.arshiaghandi.demo.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);
}