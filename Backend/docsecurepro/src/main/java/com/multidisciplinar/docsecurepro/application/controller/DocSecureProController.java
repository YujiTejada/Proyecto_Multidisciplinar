package com.multidisciplinar.docsecurepro.application.controller;

import com.multidisciplinar.docsecurepro.bean.GetAllUsersResponse;
import com.multidisciplinar.docsecurepro.application.service.api.DocSecureProService;
import com.multidisciplinar.docsecurepro.bean.GetUserByIdResponse;
import com.multidisciplinar.docsecurepro.bean.InsertUserRequest;
import com.multidisciplinar.docsecurepro.bean.UserLoginRequest;
import com.multidisciplinar.docsecurepro.constants.ApiConstants;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500",
        maxAge = 1800,
        allowCredentials = "true",
        allowedHeaders = {"*"},
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.HEAD,
                RequestMethod.TRACE, RequestMethod.OPTIONS, RequestMethod.PATCH},
        exposedHeaders = {"set-cookie"})
@RequestMapping(ApiConstants.ENDPOINT)
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
    public ResponseEntity<String> userLogin(@RequestBody UserLoginRequest userLoginRequest) {
        String loginResult = this.docSecureProService.userLogin(userLoginRequest);
        if (loginResult.equals("1")) {
            return new ResponseEntity<>("Login succesful", HttpStatus.OK);
        } else if (loginResult.equals("2")) {
            return new ResponseEntity<>("Password incorrect", HttpStatus.EXPECTATION_FAILED);
        } else {
            return new ResponseEntity<>("User does not exist", HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PostMapping("/users/insert")
    public String insertUser(@RequestBody InsertUserRequest insertUserRequest) {
        return docSecureProService.insertUser(insertUserRequest);
    }

    @GetMapping("/listFiles")
    public void listFiles() {
        this.docSecureProService.listFiles();
    }

}
