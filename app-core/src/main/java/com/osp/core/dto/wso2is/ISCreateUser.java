package com.osp.core.dto.wso2is;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ISCreateUser {

    @JsonProperty("userName")
    private String userName;
    @JsonProperty("password")
    private String password;
    @JsonProperty("schemas")
    private List<Object> schemas = new ArrayList<>();
    @JsonProperty("name")
    private ISName name;
    @JsonProperty("emails")
    private List<Object> emails = new ArrayList<>();
    @JsonProperty("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User")
    private ISEnterprise isEnterprise;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

}
