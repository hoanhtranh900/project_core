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
public class MySQLUnicodeDialect extends org.hibernate.dialect.MySQLDialect {
    public MySQLUnicodeDialect() {
        super();
        registerColumnType(Types.CHAR, "char(1)");
        registerColumnType(Types.LONGVARCHAR, "varchar(max)" );
        registerColumnType(Types.VARCHAR, 4000, "varchar($l)");
        registerColumnType(Types.VARCHAR, "varchar(max)");
        registerColumnType(Types.CLOB, "varchar(max)");

        registerColumnType(Types.NCHAR, "char(1)");
        registerColumnType(Types.LONGNVARCHAR, "varchar(max)");
        registerColumnType(Types.NVARCHAR, 4000, "varchar($l)");
        registerColumnType(Types.NVARCHAR, "varchar(max)");
        registerColumnType(Types.NCLOB, "varchar(max)");

        registerHibernateType(Types.NCHAR, StandardBasicTypes.CHARACTER.getName());
        registerHibernateType(Types.LONGNVARCHAR, StandardBasicTypes.TEXT.getName());
        registerHibernateType(Types.NVARCHAR, StandardBasicTypes.STRING.getName());
        registerHibernateType(Types.NCLOB, StandardBasicTypes.CLOB.getName() );
    }
}
