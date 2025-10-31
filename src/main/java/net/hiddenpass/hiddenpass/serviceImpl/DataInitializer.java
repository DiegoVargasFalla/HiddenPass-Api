package net.hiddenpass.hiddenpass.serviceImpl;

import net.hiddenpass.hiddenpass.enumerations.ERol;
import net.hiddenpass.hiddenpass.models.RoleEntity;
import net.hiddenpass.hiddenpass.models.UserEntity;
import net.hiddenpass.hiddenpass.repository.RoleRepository;
import net.hiddenpass.hiddenpass.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Value("${username.super_admin}")
    private String mail;

    @Value("${password.super_admin}")
    private String password;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }


    @Override
    @Transactional
    public void run(String... args) throws Exception {

        if(roleRepository.count() == 0) {
            RoleEntity roleUser = new RoleEntity();
            RoleEntity roleAdmin = new RoleEntity();
            RoleEntity roleCompany = new RoleEntity();
            RoleEntity roleSuperAdmin = new RoleEntity();

            roleUser.setRole(ERol.USER);
            roleAdmin.setRole(ERol.ADMIN);
            roleCompany.setRole(ERol.ADMIN_COMPANY);
            roleSuperAdmin.setRole(ERol.SUPER_ADMIN);

            roleRepository.save(roleUser);
            roleRepository.save(roleAdmin);
            roleRepository.save(roleCompany);
            roleRepository.save(roleSuperAdmin);
        }

        if(!userRepository.existsByUsername(mail)) {
            Optional<RoleEntity> role = roleRepository.findByRole(ERol.SUPER_ADMIN);
            if(role.isPresent()) {
                RoleEntity roleSuperAdmin = new RoleEntity();

                Set<RoleEntity> roles = new HashSet<>();

                UserEntity user = new UserEntity();
                user.setName("Administrator");
                user.setUsername(mail);
                user.setPassword(passwordEncoder.encode(password));
                user.setAccountNonExpired(true);
                user.setAccountNonLocked(true);
                user.setCredentialsNonExpired(true);
                user.setEnabled(true);

                roleSuperAdmin.setRole(ERol.ADMIN);
                roles.add(roleSuperAdmin);
                user.setRoles(roles);
                userRepository.save(user);
            }

        }


    }
}
