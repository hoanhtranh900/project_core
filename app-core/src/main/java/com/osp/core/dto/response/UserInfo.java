package com.osp.core.dto.response;

import com.osp.core.dto.wso2is.ISTokenInfo;
import com.osp.core.entity.AdmRight;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    private String username;
    private String mobileAlias;
    private String email;
    private ISTokenInfo accessTokenInfo;
    private List<AdmRight> rights;
    private List<String> authorities;

}
