package com.daniel.dev.services;

import com.daniel.dev.dto.RoleDTO;
import com.daniel.dev.dto.UserDTO;
import com.daniel.dev.dto.UserInsertDTO;
import com.daniel.dev.dto.UserUpdateDTO;
import com.daniel.dev.entities.Role;
import com.daniel.dev.entities.User;
import com.daniel.dev.projections.UserDetailsProjection;
import com.daniel.dev.repositories.RoleRepository;
import com.daniel.dev.repositories.UserRepository;
import com.daniel.dev.services.exceptions.DatabaseException;
import com.daniel.dev.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Value("${eu.sou.o.mio}")
    String omelho;

    @Transactional(readOnly = true)
    public Page<UserDTO> findAll(Pageable pageable){
        return userRepository.findAll(pageable).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        System.out.println(omelho);
        return new UserDTO(userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado")));
    }

    @Transactional
    public UserDTO insert(UserInsertDTO userDTO){
        User user = new User();
        copyDTOToEntity(userDTO, user);
        user.setPassword(passwordEncoder().encode(userDTO.getPassword()));
        user.addRole(roleRepository.getReferenceById(1L));
        return new UserDTO(userRepository.save(user));
    }

    @Transactional
    public UserDTO update (Long id, UserUpdateDTO userDTO){
        if(!userRepository.existsById(id)){
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
        User user = userRepository.getReferenceById(id);
        copyDTOToEntity(userDTO, user);
        return new UserDTO(userRepository.save(user));
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete (Long id){
        if(!userRepository.existsById(id)){
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
        try {
            userRepository.deleteById(id);
        } catch (DataIntegrityViolationException e){
            throw new DatabaseException("Violação da integridade referencial");
        }
    }

    public void copyDTOToEntity(UserDTO userDTO, User user){
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setBirthDate(userDTO.getBirthDate());
        user.setPhone(userDTO.getPhone());
    }

    @Transactional
    public void giveAdminPrivileges(Long id){
        userRepository.getReferenceById(id).addRole(roleRepository.getReferenceById(2L));
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
            return userRepository.findByEmail(username);
        } catch (Exception e) {
            throw new UsernameNotFoundException("Email not found");
        }
    }

    @Transactional
    public UserDTO getMe() {
        return new UserDTO(authenticated()  );
    }

    PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }
}
