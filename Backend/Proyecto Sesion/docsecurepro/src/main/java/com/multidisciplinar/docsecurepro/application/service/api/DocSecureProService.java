package com.multidisciplinar.docsecurepro.application.service.api;

import com.multidisciplinar.docsecurepro.api.dao.User;
import com.multidisciplinar.docsecurepro.bean.docsecurepro.*;
import com.multidisciplinar.docsecurepro.application.service.database.UserService;
import com.multidisciplinar.docsecurepro.email.sender.Sender;
import com.multidisciplinar.docsecurepro.util.DocSecureProUtil;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Service
@Slf4j
public class DocSecureProService {

    private UserService userService;

    @Autowired
    public DocSecureProService(UserService userService) {
        this.userService = userService;
    }
    public GetAllUsersResponse getAllUsers() {
        log.info("getAllUsers - begin");
        GetAllUsersResponse getAllUsersResponse = GetAllUsersResponse.builder()
                .usersRetrieved(this.userService.selectAll())
                .build();
        log.info("getAllUsers - end - GetAllUsersResponse: {}", DocSecureProUtil.prettyJson(getAllUsersResponse));
        return getAllUsersResponse;
    }

    public GetUserByIdResponse getUserById(int id) {
        return new GetUserByIdResponse(this.userService.findByIdUsuario(id));
    }

    public User findUserByNombreUsuario(String nombreUsuario) {
        return this.userService.findByNombreUsuario(nombreUsuario);
    }

    public User userLogin(UserLoginRequest userLoginRequest) {
        return this.userService.findByNombreUsuario(userLoginRequest.getNombreUsuario());
    }

    public String insertUser(InsertUserRequest insertUserRequest) {
        var userCheck = this.userService.findByNombreUsuario(insertUserRequest.getNombreUsuario());
        if (userCheck == null) {
            int userInsertResult = this.userService.insert(insertUserRequest);
            if (userInsertResult != 0) {
                return "2";
            } else {
                return "1";
            }
        } else {
            return "0";
        }
    }

    public boolean sendMail(SendMailRequest sendMailRequest, String userSender) {
        Sender mailSender = new Sender();
        return mailSender.send(userSender, sendMailRequest.getRecipient(), sendMailRequest.getSubject(), sendMailRequest.getMessage());
    }

}
