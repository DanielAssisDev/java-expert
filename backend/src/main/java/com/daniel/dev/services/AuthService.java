package com.daniel.dev.services;

import com.daniel.dev.dto.EmailDTO;
import com.daniel.dev.dto.NewPasswordDTO;
import com.daniel.dev.entities.PasswordRecover;
import com.daniel.dev.entities.User;
import com.daniel.dev.repositories.PasswordRecoverRepository;
import com.daniel.dev.repositories.UserRepository;
import com.daniel.dev.services.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordRecoverRepository passwordRecoverRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${email.password-recover.token.minutes}")
    private Long tokenMinutes;

    @Value("${email.password-recover.uri}")
    private String basicPath;

    @Transactional
    public void createRecoverToken(EmailDTO emailDTO) {
        User user = userRepository.findByEmail(emailDTO.getEmail());
        if (user == null) {
            throw new ResourceNotFoundException("Email não encontrado");
        }

        String token = UUID.randomUUID().toString();

        PasswordRecover passwordRecover = new PasswordRecover();
        passwordRecover.setEmail(emailDTO.getEmail());
        passwordRecover.setToken(token);
        passwordRecover.setExpiration(Instant.now().plusSeconds(tokenMinutes * 60L));
        passwordRecoverRepository.save(passwordRecover);

        String text = "Acesse o link em seu email: " + emailDTO.getEmail() + " para definir uma nova senha:\n" + basicPath + token + "\nValidade de: " + tokenMinutes + " minutos";

        emailService.sendEmail(emailDTO.getEmail(), "Recuperação de senha", text);
    }

    @Transactional
    public void saveNewPassword(NewPasswordDTO obj) {
        List<PasswordRecover> recoverList = passwordRecoverRepository.searchValidTokens(obj.getToken(), Instant.now());
        if(recoverList.isEmpty()){
            throw new ResourceNotFoundException("Token inválido");
        }

        User user = userRepository.findByEmail(recoverList.getFirst().getEmail());
        user.setPassword(passwordEncoder.encode(obj.getPassword()));
        userRepository.save(user);
    }
}
