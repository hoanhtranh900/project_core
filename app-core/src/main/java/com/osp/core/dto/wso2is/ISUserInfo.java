package com.osp.core.dto.wso2is;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ISUserInfo {

	private String sub;
	private String name;
	private String email;

}
