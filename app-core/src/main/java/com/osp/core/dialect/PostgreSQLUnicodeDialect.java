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
public class PostgreSQLUnicodeDialect extends org.hibernate.dialect.PostgreSQLDialect {
    public PostgreSQLUnicodeDialect() {
        super();
        registerColumnType(Types.CHAR, "varchar(1)");
        registerColumnType(Types.LONGVARCHAR, "text" );
        registerColumnType(Types.VARCHAR, 4000, "text" );
        registerColumnType(Types.VARCHAR, "text");
        registerColumnType(Types.CLOB, "text");

        registerColumnType(Types.NCHAR, "varchar(1)");
        registerColumnType(Types.LONGNVARCHAR, "text");
        registerColumnType(Types.NVARCHAR, 4000, "text" );
        registerColumnType(Types.NVARCHAR, "text");
        registerColumnType(Types.NCLOB, "text");

        registerHibernateType(Types.NCHAR, StandardBasicTypes.CHARACTER.getName());
        registerHibernateType(Types.LONGNVARCHAR, StandardBasicTypes.TEXT.getName());
        registerHibernateType(Types.NVARCHAR, StandardBasicTypes.STRING.getName());
        registerHibernateType(Types.NCLOB, StandardBasicTypes.CLOB.getName() );
    }
}
