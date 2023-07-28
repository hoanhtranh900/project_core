package com.osp.core.dto.response;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfigSystem {

    private Long id;
    private String name;
    private String value;

}
