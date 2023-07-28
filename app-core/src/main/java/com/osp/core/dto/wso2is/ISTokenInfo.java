package com.osp.core.dto.wso2is;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ISTokenInfo {

    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("id_token")
    @Expose
    private String idToken;
    @SerializedName("token_type")
    @Expose
    private String tokenType;
    @SerializedName("expires_in")
    @Expose
    private Long expiresIn;
    @SerializedName("refresh_token")
    @Expose
    private String refreshToken;
    @SerializedName("scope")
    @Expose
    private String scope;
}
