package com.osp.core.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotEmpty(message = "{error.required.username}")
    private String username;
    @NotEmpty(message = "{error.required.password}")
    private String password;
    @JsonIgnore
    private String ip;
    @JsonIgnore
    private String userAgent;
}
