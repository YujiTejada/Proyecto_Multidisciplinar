package com.multidisciplinar.docsecurepro.application.controller;

import com.multidisciplinar.docsecurepro.api.dao.User;
import com.multidisciplinar.docsecurepro.bean.docsecurepro.*;
import com.multidisciplinar.docsecurepro.application.service.api.DocSecureProService;
import com.multidisciplinar.docsecurepro.constants.ApiConstants;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstants.DOCSECUREPRO_ENDPOINT)
public class DocSecureProController {

    private DocSecureProService docSecureProService;

    @Autowired
    public DocSecureProController(DocSecureProService docSecureProService) {
        this.docSecureProService = docSecureProService;
    }

    @GetMapping("/users")
    public ResponseEntity<GetAllUsersResponse> getAllUsers() {
        return new ResponseEntity<>(this.docSecureProService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<GetUserByIdResponse> getUserById(@PathVariable String id) {
        return new ResponseEntity<>(this.docSecureProService.getUserById(Integer.valueOf(id)), HttpStatus.OK);
    }

    @PostMapping("/users/login")
    public ResponseEntity<String> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpSession httpSession) {
        User userFound = this.docSecureProService.userLogin(userLoginRequest);
        if (userFound != null) {
            if (userFound.getContrasenya().equals(userLoginRequest.getContrasenya())) {
                httpSession.setAttribute("login", true);
                httpSession.setAttribute("usuario", userFound);
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .body("login_succesful");
            } else {
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .body("password_incorrect");
            }
        } else {
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("user_does_not_exist");
        }
    }

    @PostMapping("/users/insert")
    public ResponseEntity<String> insertUser(@RequestBody InsertUserRequest insertUserRequest, HttpSession httpSession) {
        String userInsertResult = this.docSecureProService.insertUser(insertUserRequest);
        if (userInsertResult.equals("2")) {
            httpSession.setAttribute("login", true);
            User userFound = this.docSecureProService.
                    findUserByNombreUsuario(insertUserRequest.getNombreUsuario());
            httpSession.setAttribute("usuario", userFound);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("user_creation_succesful");
        } else if (userInsertResult.equals("1")) {
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("user_creation_unsuccesful");
        } else {
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("user_already_exists");
        }
    }
    @GetMapping("/users/checkSession")
    public ResponseEntity<String> checkSession(HttpSession httpSession) {
        if (httpSession.getAttribute("login") != null
                && (boolean) httpSession.getAttribute("login") == true) {
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("user_logged");
        } else {
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("user_not_logged");
        }
    }

    @GetMapping("/users/logout")
    public ResponseEntity<String> logout(HttpSession httpSession) {
        if (httpSession.getAttribute("login") != null) {
            httpSession.setAttribute("login", false);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("user_logged_out");
        } else {
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("user_was_already_logged_out");
        }
    }

    @PostMapping("/mail/sendMail")
    public ResponseEntity<String> sendMail(@RequestBody SendMailRequest sendMailRequest, HttpSession httpSession) {
        User usuario = (User) httpSession.getAttribute("usuario");
        if (usuario != null && this.docSecureProService.sendMail(sendMailRequest, usuario.getCorreo())) {
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("mail_sent_succesfully");
        } else {
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("error_sending_mail");
        }
    }

}
