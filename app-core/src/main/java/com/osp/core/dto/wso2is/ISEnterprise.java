package com.osp.core.dto.wso2is;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ISEnterprise {
    @JsonProperty("manager")
    private ISManager manager;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

}
