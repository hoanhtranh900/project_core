package com.osp.core.dto.response;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccessTokenInfo {

    private String accessToken;

    private String idToken;

    private String tokenType;

    private Long expiresIn;

    private String refreshToken;

    private String scope;

}
