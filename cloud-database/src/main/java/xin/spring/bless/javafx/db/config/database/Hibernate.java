package xin.spring.bless.javafx.db.config.database;

import java.io.Serializable;

/**
 * 功能描述:
 * $params$
 *
 * @return: $return$
 * @Author: $user$
 * @Date: $date$ $time$
 */
public class Hibernate implements Serializable {

    private Hconnection connection;

    private String dialect;

    private Hc3p0 c3p0;

    private Hhbm2ddl hbm2ddl;

    private String showSql;

    private String formatSql;

    public Hconnection getConnection() {
        return connection;
    }

    public void setConnection(Hconnection connection) {
        this.connection = connection;
    }

    public String getDialect() {
        return dialect;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

    public Hc3p0 getC3p0() {
        return c3p0;
    }

    public void setC3p0(Hc3p0 c3p0) {
        this.c3p0 = c3p0;
    }

    public Hhbm2ddl getHbm2ddl() {
        return hbm2ddl;
    }

    public void setHbm2ddl(Hhbm2ddl hbm2ddl) {
        this.hbm2ddl = hbm2ddl;
    }

    public String getShowSql() {
        return showSql;
    }

    public void setShowSql(String showSql) {
        this.showSql = showSql;
    }

    public String getFormatSql() {
        return formatSql;
    }

    public void setFormatSql(String formatSql) {
        this.formatSql = formatSql;
    }

    @Override
    public String toString() {
        return "Hibernate{" +
                "connection=" + connection +
                ", dialect='" + dialect + '\'' +
                ", c3p0=" + c3p0 +
                ", hbm2ddl=" + hbm2ddl +
                ", showSql='" + showSql + '\'' +
                ", formatSql='" + formatSql + '\'' +
                '}';
    }
}