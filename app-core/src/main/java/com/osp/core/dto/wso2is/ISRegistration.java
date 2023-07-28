package com.osp.core.dto.wso2is;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ISRegistration {
    private ISRegistrationUser user;
    private List<ISRegistrationProperties> properties;

}
