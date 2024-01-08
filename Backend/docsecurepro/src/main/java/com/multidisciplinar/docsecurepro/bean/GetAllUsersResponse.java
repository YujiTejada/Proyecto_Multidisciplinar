package com.multidisciplinar.docsecurepro.bean;

import com.multidisciplinar.docsecurepro.api.dao.User;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetAllUsersResponse {

    private List<User> usersRetrieved;

}
