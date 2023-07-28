package com.osp.core.dto.request;

import lombok.*;

/**
 * TODO: write you class description here
 *
 * @author
 */

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SentMailInfo {

    private String mailContent;
    private String subject;
    private String filePaths;

}
