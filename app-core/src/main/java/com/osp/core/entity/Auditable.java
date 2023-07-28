package com.osp.core.entity;

import com.osp.core.utils.UtilsDate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable implements Serializable {

    private static final long serialVersionUID = 5282450495494154675L;

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CREATED_DATE")//, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createdDate;

    @Column(name = "MODIFIED_DATE")//, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    protected Date modifiedDate;

    @Column(name = "CREATE_BY", columnDefinition = "VARCHAR(255) DEFAULT 'Unknown'", length = 100)
    @CreatedBy
    protected String createBy;

    @Column(name = "MODIFIED_BY", columnDefinition = "VARCHAR(255) DEFAULT 'Unknown'", length = 100)
    @LastModifiedBy
    protected String modifiedBy;

    @Transient
    private String createdDateStr;
    @Transient
    private String modifiedDateStr;

    public String getCreatedDateStr() {
        this.createdDateStr = UtilsDate.date2str(this.createdDate, "dd/MM/yyyy HH:mm:ss");
        return this.createdDateStr;
    }

    public String getModifiedDateStr() {
        this.modifiedDateStr = UtilsDate.date2str(this.modifiedDate, "dd/MM/yyyy HH:mm:ss");
        return this.modifiedDateStr;
    }
}
