package kz.app.umbrella.api.service;

import kz.app.umbrella.api.entity.ApiRole;
import kz.app.umbrella.api.entity.ApiUser;
import kz.app.umbrella.api.repository.ApiUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final ApiUserRepository apiUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApiUser apiUser = apiUserRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с email-ом " + username + " не найден"));
        return new User(
                apiUser.getEmail(),
                apiUser.getPassword(),
                List.of(new SimpleGrantedAuthority(ApiRole.USER.name()))
        );
    }
}
