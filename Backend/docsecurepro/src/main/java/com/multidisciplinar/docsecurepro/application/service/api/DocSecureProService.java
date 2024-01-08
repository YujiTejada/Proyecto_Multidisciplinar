package com.multidisciplinar.docsecurepro.application.service.api;

import com.multidisciplinar.docsecurepro.api.dao.User;
import com.multidisciplinar.docsecurepro.application.service.ftp.FtpService;
import com.multidisciplinar.docsecurepro.bean.GetAllUsersResponse;
import com.multidisciplinar.docsecurepro.application.service.database.UserService;
import com.multidisciplinar.docsecurepro.bean.GetUserByIdResponse;
import com.multidisciplinar.docsecurepro.bean.InsertUserRequest;
import com.multidisciplinar.docsecurepro.bean.UserLoginRequest;
import com.multidisciplinar.docsecurepro.util.DocSecureProUtil;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.misc.Utils;
import org.apache.commons.lang3.StringUtils;
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

    public GetUserByIdResponse getUserById (int id) {
        return new GetUserByIdResponse(this.userService.findById(id));
    }

    public String userLogin(UserLoginRequest userLoginRequest) {
        var userFound = this.userService.findByNombreUsuario(userLoginRequest.getNombreUsuario());
        if (userFound != null) {
            if (userFound.getContrasenya().equals(userLoginRequest.getContrasenya())) {
                return "1";
            } else {
                return "2";
            }
        } else {
            return "0";
        }
    }

    public void listFiles() {
        this.ftpService.printTree("/");
    }
    public String insertUser(InsertUserRequest user) {
        return String.valueOf(userService.insert(user));
    }

}
