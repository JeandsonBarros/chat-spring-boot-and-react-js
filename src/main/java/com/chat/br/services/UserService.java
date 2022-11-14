package com.chat.br.services;

import com.chat.br.dtos.ChangeForgottenPasswordDto;
import com.chat.br.dtos.UserDto;
import com.chat.br.enums.RoleEnum;
import com.chat.br.enums.StatusMessage;
import com.chat.br.models.EmailModel;
import com.chat.br.models.RoleModel;
import com.chat.br.models.UserModel;
import com.chat.br.repository.EmailRepository;
import com.chat.br.repository.RoleRepository;
import com.chat.br.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private RoleRepository roleRepository;

    @Autowired
    EmailRepository emailRepository;
    @Autowired
    private JavaMailSender emailSender;

    public UserModel useData(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        var user = userRepository.findByEmail(auth.getName()).get();
        user.setPassword("");
        return user;
    }

    public List<UserModel> listAllUsers(){
       var users = userRepository.findAll();

       users.forEach(user->{
           user.setPassword("");
       });

       return users;

    }

    @Transactional
    public Boolean userRegister(UserModel userModel){
       try {

           List<RoleModel> roleList = new ArrayList<>();
           roleList.add(roleRepository.findByRoleName(RoleEnum.ROLE_USER));
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
    public Boolean userUpdate(UserDto userDto){
        try {

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            var userOld = userRepository.findByEmail(auth.getName()).get();
            BeanUtils.copyProperties(userDto, userOld);

            userOld.setEmail(auth.getName());
            userOld.setPassword(new BCryptPasswordEncoder().encode(userOld.getPassword()));
            userRepository.save(userOld);

            return true;

        }catch(Exception e){
            System.out.println(e);
            return false;
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
