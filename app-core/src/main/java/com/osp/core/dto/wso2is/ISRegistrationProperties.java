package com.osp.core.dto.wso2is;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ISRegistrationProperties {
    private String key;
    private Object value;
}
