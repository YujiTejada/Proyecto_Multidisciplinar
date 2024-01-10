package com.multidisciplinar.docsecurepro.application.service.api;

import com.multidisciplinar.docsecurepro.api.dao.User;
import com.multidisciplinar.docsecurepro.application.service.ftp.FtpService;
import com.multidisciplinar.docsecurepro.bean.docsecurepro.GetAllUsersResponse;
import com.multidisciplinar.docsecurepro.application.service.database.UserService;
import com.multidisciplinar.docsecurepro.bean.docsecurepro.GetUserByIdResponse;
import com.multidisciplinar.docsecurepro.bean.docsecurepro.InsertUserRequest;
import com.multidisciplinar.docsecurepro.bean.docsecurepro.UserLoginRequest;
import com.multidisciplinar.docsecurepro.util.DocSecureProUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DocSecureProService {

    private UserService userService;
    private FtpService ftpService;

    @Autowired
    public DocSecureProService(UserService userService, FtpService ftpService) {
        this.userService = userService;
        this.ftpService = ftpService;
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

    public User userLogin(UserLoginRequest userLoginRequest) {
        return this.userService.findByNombreUsuario(userLoginRequest.getNombreUsuario());
    }

    public String insertUser(InsertUserRequest insertUserRequest) {
        var userCheck = this.userService.findByNombreUsuario(insertUserRequest.getNombreUsuario());
        if (userCheck != null) {
            int userInsertResult = this.userService.insert(insertUserRequest);
            if (userInsertResult != 0) {
                return "1";
            } else {
                return "2";
            }
        } else {
            return "0";
        }
    }

}
