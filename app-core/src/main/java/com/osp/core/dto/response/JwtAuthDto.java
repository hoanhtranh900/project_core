package com.osp.core.dto.response;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * 
 * @author DELL
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtAuthDto {

  private String issuer;
  private Long ui;
  private String uname;
  private List roles;
  private String email;
  private Date expi;
  private String usAgent;
  private String ipAddress;
  private String ss;
  private String fullname;

}
