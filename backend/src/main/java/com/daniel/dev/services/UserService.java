package com.daniel.dev.services;

import com.daniel.dev.dto.UserDTO;
import com.daniel.dev.entities.Role;
import com.daniel.dev.entities.User;
import com.daniel.dev.projections.UserDetailsProjection;
import com.daniel.dev.repositories.UserRepository;
import com.daniel.dev.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<UserDTO> findAll(Pageable pageable){
        return userRepository.findAll(pageable).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        return new UserDTO(userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado")));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserDetailsProjection> users = userRepository.searchUserAndRolesByEmail(username);
        if (users.isEmpty()) {
            throw new UsernameNotFoundException("Usuário não encontrado!");
        }
        User user = new User();
        user.setEmail(users.getFirst().getUsername());
        user.setPassword(users.getFirst().getPassword());
        for (UserDetailsProjection projection : users) {
            user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
        }
        return user;
    }

    @Transactional
    protected User authenticated() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Jwt jwtPrincipal = (Jwt) authentication.getPrincipal();
            String username = jwtPrincipal.getClaim("username");
            return userRepository.findByEmail(username).get();
        } catch (Exception e) {
            throw new UsernameNotFoundException("Email not found");
        }
    }

    @Transactional
    public UserDTO getMe() {
        return new UserDTO(authenticated());
    }
}
