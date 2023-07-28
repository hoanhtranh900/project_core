package com.osp.core.dialect;

import org.hibernate.type.StandardBasicTypes;
import org.springframework.context.annotation.Configuration;

import java.sql.Types;

/**
 * TODO: write you class description here
 *
 * @author
 */

@Configuration
public class Oracle10gUnicodeDialect extends org.hibernate.dialect.Oracle10gDialect {
    public Oracle10gUnicodeDialect() {
        super();
        registerColumnType(Types.CHAR, "nchar(1)");
        registerColumnType(Types.LONGVARCHAR, "nvarchar2(max)" );
        registerColumnType(Types.VARCHAR, 4000, "nvarchar2($l)");
        registerColumnType(Types.VARCHAR, "nvarchar2(max)");
        registerColumnType(Types.CLOB, "nvarchar2(max)");

        registerColumnType(Types.NCHAR, "nchar(1)");
        registerColumnType(Types.LONGNVARCHAR, "nvarchar2(max)");
        registerColumnType(Types.NVARCHAR, 4000, "nvarchar2($l)");
        registerColumnType(Types.NVARCHAR, "nvarchar2(max)");
        registerColumnType(Types.NCLOB, "nvarchar2(max)");

        registerHibernateType(Types.NCHAR, StandardBasicTypes.CHARACTER.getName());
        registerHibernateType(Types.LONGNVARCHAR, StandardBasicTypes.TEXT.getName());
        registerHibernateType(Types.NVARCHAR, StandardBasicTypes.STRING.getName());
        registerHibernateType(Types.NCLOB, StandardBasicTypes.CLOB.getName() );
    }
}
