package com.osp.core.dto.wso2is;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ISRegistrationUser {
    private String username;
    private String realm;
    private String password;
    private List<ISRegistrationClaims> claims;

}
