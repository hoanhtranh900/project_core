package com.osp.core.dto.wso2is;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ISRegistrationClaims {
    private String uri;
    private Object value;

}
