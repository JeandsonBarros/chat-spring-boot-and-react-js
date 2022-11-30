package com.chat.br.services;

import com.chat.br.dtos.ChangeForgottenPasswordDto;
import com.chat.br.dtos.UserDto;
import com.chat.br.enums.StatusMessage;
import com.chat.br.models.EmailModel;
import com.chat.br.models.UserModel;
import com.chat.br.repository.EmailRepository;
import com.chat.br.repository.UserRepository;
import com.chat.br.security.UserDetailsServiceImplement;
import jakarta.validation.constraints.Email;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    EmailRepository emailRepository;
    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private UserDetailsServiceImplement userDetailsServiceImplement;
    @Autowired
    private TokenService tokenService;

    public UserModel useData(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(auth.getName()).get();
    }

    public List<UserModel> listAllUsers(){
       var users = userRepository.findAll();

       users.forEach(user->{
           user.setPassword("");
       });

       return users;

    }

    AuthenticationManager myAuthenticationManager(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder());
        return provider::authenticate;
    }

    public String userLogin(String email, String password){

        var authenticationManager = myAuthenticationManager(userDetailsServiceImplement);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        String token = tokenService.generateToken(authentication);

        return token;

    }

    @Transactional
    public Boolean userRegister(UserModel userModel){
       try {

           List<String> roleList = new ArrayList<>();
           roleList.add("ROLE_ADMIN");
           userModel.setRoles(roleList);

           userModel.setPassword(new BCryptPasswordEncoder().encode(userModel.getPassword()));
           userRepository.save(userModel);

           return true;
       }catch(Exception e){
           System.out.println(e);
           return false;
       }
    }

    @Transactional
    public ResponseEntity<String> userUpdate(UserDto userDto){
        try {

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            var userOld = userRepository.findByEmail(auth.getName()).get();

            if(userDto.getEmail() != null && !userDto.getEmail().isEmpty()) {
                if (userRepository.findByEmail(userDto.getEmail()).isPresent() && !userDto.getEmail().equals(auth.getName())) {
                    return new ResponseEntity<>("This email is unavailable", HttpStatus.BAD_REQUEST);
                }else{
                    userOld.setEmail(userDto.getEmail());
                }
            }
            if(userDto.getName() != null &&!userDto.getName().isEmpty())
                userOld.setName(userDto.getName());
            if(userDto.getPassword() != null && !userDto.getPassword().isEmpty())
                userOld.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));

            userRepository.save(userOld);

            return new ResponseEntity<>("Successfully updated", HttpStatus.OK);

        }catch(Exception e){
            System.out.println(e);
            return new ResponseEntity<>("Error updating", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public Boolean userDelete() {

        try {

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            var user = userRepository.findByEmail(auth.getName()).get();
            userRepository.delete(user);

            return true;

        }catch (Exception e){
            System.out.println(e);
            return false;
        }


    }

    @Transactional
    public Integer deleteUserForAdmin(String email) {
        try {

            var user = userRepository.findByEmail(email);

            if(user.isPresent()) {
                userRepository.delete(user.get());
                return 204;
            }
            else
                return 404;

        }catch (Exception e){
            System.out.println(e);
            return 500;
        }

    }

    @Transactional
    public String sendEmail(EmailModel emailModel) {
        emailModel.setSendDateEmail(LocalDateTime.now());

        try{

            int recoveryCode = ThreadLocalRandom.current().nextInt(1000000, 2000000);

            var emailIsPresent = emailRepository.findByEmailTo(emailModel.getEmailTo());
            emailIsPresent.ifPresent(model -> emailRepository.delete(model));

            emailModel.setRecoveryCode(recoveryCode);
            emailModel.setText("You recovery code is "+recoveryCode+", valid for 15 minutes.");
            emailModel.setCodeExpirationTime(System.currentTimeMillis() + 900000);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("jeandson.developer@gmail.com");
            message.setTo(emailModel.getEmailTo());
            message.setSubject(emailModel.getSubject());
            message.setText(emailModel.getText());
            emailSender.send(message);

            emailModel.setStatusMessage(StatusMessage.SENT);

        } catch (MailException e){
            emailModel.setStatusMessage(StatusMessage.ERROR);
        } finally {
            return emailRepository.save(emailModel).getStatusMessage().toString();
        }
    }

    @Transactional
    public int changeForgottenPassword(ChangeForgottenPasswordDto changeForgottenPasswordDto){

        try {
            var email = emailRepository.findByEmailToAndRecoveryCode(changeForgottenPasswordDto.getEmail(), changeForgottenPasswordDto.getCode());

            if(!email.isPresent() || !userRepository.findByEmail(changeForgottenPasswordDto.getEmail()).isPresent()){
                return 404;
            }else if(System.currentTimeMillis() > email.get().getCodeExpirationTime()){
                return 400;
            }else{

                var user = userRepository.findByEmail(changeForgottenPasswordDto.getEmail()).get();
                user.setPassword(new BCryptPasswordEncoder().encode(changeForgottenPasswordDto.getNewPassword()));
                userRepository.save(user);

                return 200;
            }

        }catch (Exception e){
            return 500;
        }
    }

}
