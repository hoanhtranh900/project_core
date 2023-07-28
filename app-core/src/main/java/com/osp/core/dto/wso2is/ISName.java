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
public class ISName {

    @JsonProperty("givenName")
    private String givenName;
    @JsonProperty("familyName")
    private String familyName;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

}
