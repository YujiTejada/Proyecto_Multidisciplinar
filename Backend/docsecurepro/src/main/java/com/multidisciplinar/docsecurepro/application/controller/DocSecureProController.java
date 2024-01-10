package com.multidisciplinar.docsecurepro.application.controller;

import com.multidisciplinar.docsecurepro.api.dao.User;
import com.multidisciplinar.docsecurepro.bean.docsecurepro.GetAllUsersResponse;
import com.multidisciplinar.docsecurepro.application.service.api.DocSecureProService;
import com.multidisciplinar.docsecurepro.bean.docsecurepro.GetUserByIdResponse;
import com.multidisciplinar.docsecurepro.bean.docsecurepro.InsertUserRequest;
import com.multidisciplinar.docsecurepro.bean.docsecurepro.UserLoginRequest;
import com.multidisciplinar.docsecurepro.constants.ApiConstants;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200",
        maxAge = 1800,
        allowCredentials = "true",
        allowedHeaders = {"*"},
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.HEAD,
                RequestMethod.TRACE, RequestMethod.OPTIONS, RequestMethod.PATCH},
        exposedHeaders = {"set-cookie"})
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
        if (httpSession.getAttribute("login") == null) {
            User userFound = this.docSecureProService.userLogin(userLoginRequest);
            if (userFound != null) {
                if (userFound.getContrasenya().equals(userLoginRequest.getContrasenya())) {
                    httpSession.setAttribute("login", true);
                    httpSession.setAttribute("idUsuario", userFound.getIdUsuario());
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
        } else {
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("login_succesful");
        }
    }

    @PostMapping("/users/insert")
    public ResponseEntity<String> insertUser(@RequestBody InsertUserRequest insertUserRequest) {
        String userInsertResult = this.docSecureProService.insertUser(insertUserRequest);
        ResponseEntity<String> responseEntity;
        if (userInsertResult.equals("1")) {
            responseEntity = new ResponseEntity<>("user_creation_succesful", HttpStatus.OK);
        } else if (userInsertResult.equals("2")) {
            responseEntity = new ResponseEntity<>("user_creation_unsuccesful", HttpStatus.EXPECTATION_FAILED);
        } else {
            responseEntity = new ResponseEntity<>("user_already_exists", HttpStatus.EXPECTATION_FAILED);
        }
        responseEntity.getHeaders().setContentType(MediaType.TEXT_PLAIN);
        return responseEntity;
    }

}
