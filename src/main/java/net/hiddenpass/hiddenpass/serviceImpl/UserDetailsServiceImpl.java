package net.hiddenpass.hiddenpass.serviceImpl;

import net.hiddenpass.hiddenpass.models.UserEntity;
import net.hiddenpass.hiddenpass.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //load user by username
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(email)
                .orElseThrow(() -> new UsernameNotFoundException("user " + email + " not exist"));

        Collection<? extends GrantedAuthority> authorities = userEntity.getRoles()
                .stream()
                .map( role -> new SimpleGrantedAuthority("ROLE_".concat(role.getRole().name())))
                .collect(Collectors.toSet());

        return new User(userEntity.getUsername(),
                userEntity.getPassword(),
                true,
                true,
                true,
                true,
                authorities);
    }
}
