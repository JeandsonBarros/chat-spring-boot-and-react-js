package com.chat.br.controllers;

import com.chat.br.dtos.ChangeForgottenPasswordDto;
import com.chat.br.dtos.EmailDto;
import com.chat.br.dtos.LoginDto;
import com.chat.br.dtos.UserDto;
import com.chat.br.models.EmailModel;
import com.chat.br.models.UserModel;
import com.chat.br.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/auth/")
@Tag(name = "Auth")
public class UserController {

   @Autowired
   private UserService userService;

    //https://programadev.com.br/spring-security-jwt/
    @Operation(
    		summary = "User and admin login",
    		description = "User and admin login with email and password"
    		)
    @PostMapping("/login")
    public ResponseEntity<String>  userLogin(@RequestBody @Valid LoginDto loginDto){

        var token = userService.userLogin(loginDto.getEmail(), loginDto.getPassword());

        return ResponseEntity.ok(token);

    }

    //@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @Operation(summary = "Get authenticated user data")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/data")
    public ResponseEntity<UserModel> userData(){
        return new ResponseEntity<>(userService.useData(), HttpStatus.OK);
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "List all user (function only for ADMIN)")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/all-users")
    public ResponseEntity<List<UserModel>> listAllUsers(){
        return new ResponseEntity<>(userService.listAllUsers(), HttpStatus.OK);
    }

    @Operation(summary = "New user registration")
    @PostMapping("/register")
    public ResponseEntity<String> userRegister(@RequestBody @Valid UserDto userDto){

        var userModel = new UserModel();
        BeanUtils.copyProperties(userDto, userModel);
        Boolean isRegistration = userService.userRegister(userModel);

        if(isRegistration)
            return new ResponseEntity<>("Successfully registered", HttpStatus.CREATED);
        else
            return new ResponseEntity<>("Error when registering", HttpStatus.BAD_REQUEST);

    }

    //@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @Operation(summary = "Update user data")
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/update")
    public ResponseEntity<String> userUpdate(@RequestBody UserDto userDto){

        return userService.userUpdate(userDto);

    }

    //@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @Operation(summary = "Delete account")
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/delete")
    public ResponseEntity<String> userDelete(){

        Boolean isRegistration = userService.userDelete();

        if(isRegistration)
            return new ResponseEntity<>("Successfully deleted", HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>("Error deleting", HttpStatus.BAD_REQUEST);
    }

    //@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @Operation(summary = "Delete user account (function only for ADMIN)")
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/admin/delete/{email}")
    public ResponseEntity<String> deleteUserForAdmin(@PathVariable String email){

        var status = userService.deleteUserForAdmin(email);

        if(status == 204)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else if (status == 404)
            return new ResponseEntity<>("User '"+email+"' not found", HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>("Error deleting", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Operation(
    		summary = "Send email for password recovery",
    		description = "Send an email with a code valid for 15 minutes to be able to change the forgotten password"
    		)
    @PostMapping("/forgot-password/sending-email-code")
    public ResponseEntity<String> sendingEmail(@RequestBody @Valid EmailDto emailDto){

        EmailModel emailModel = new EmailModel();
        emailModel.setEmailTo(emailDto.getEmailTo());
        emailModel.setSubject("Password recovery for "+emailDto.getEmailTo());

        return userService.sendEmail(emailModel);
    }

    @Operation(
    		summary = "Change forgotten password",
    		description = "Change the forgotten password using the code that was sent to the email"
    		)
    @PostMapping("/forgot-password/change-password")
    public ResponseEntity<String> changeForgottenPassword(@RequestBody @Valid ChangeForgottenPasswordDto changeForgottenPasswordDto){

        var status = userService.changeForgottenPassword(changeForgottenPasswordDto);

        if(status == 200)
            return new ResponseEntity<>("Password changed successfully.", HttpStatus.OK);
        else if (status == 404)
            return new ResponseEntity<>("This verification code or user does not exist.", HttpStatus.NOT_FOUND);
        else if (status == 400)
            return new ResponseEntity<>("Invalid code.", HttpStatus.BAD_REQUEST);
        else
            return new ResponseEntity<>("INTERNAL SERVER ERROR.", HttpStatus.INTERNAL_SERVER_ERROR);

    }

}
