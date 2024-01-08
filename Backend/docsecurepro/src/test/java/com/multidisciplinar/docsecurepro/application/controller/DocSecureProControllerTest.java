package com.multidisciplinar.docsecurepro.application.controller;

import com.multidisciplinar.docsecurepro.application.service.api.DocSecureProService;
import com.multidisciplinar.docsecurepro.bean.GetAllUsersResponse;
import com.multidisciplinar.docsecurepro.bean.GetUserByIdResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
public class DocSecureProControllerTest {

    @InjectMocks
    DocSecureProController docSecureProController;

    @Mock
    DocSecureProService docSecureProService;

    @Test
    public void getAllUsers() {
        GetAllUsersResponse getAllUsersResponse = new GetAllUsersResponse();
        Mockito.when(docSecureProService.getAllUsers()).thenReturn(getAllUsersResponse);
        Assertions.assertNotNull(docSecureProController.getAllUsers());
    }

    @Test
    public void getUserById() {
        GetUserByIdResponse getUserByIdResponse = new GetUserByIdResponse();
        Mockito.when(docSecureProService.getUserById(anyInt())).thenReturn(getUserByIdResponse);
        Assertions.assertNotNull(docSecureProController.getUserById("1"));
    }

}
