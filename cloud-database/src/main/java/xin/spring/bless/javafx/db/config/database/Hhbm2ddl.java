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
public class Hhbm2ddl implements Serializable {

    private String auto;

    public String getAuto() {
        return auto;
    }

    public void setAuto(String auto) {
        this.auto = auto;
    }

    @Override
    public String toString() {
        return "Hhbm2ddl{" +
                "auto='" + auto + '\'' +
                '}';
    }
}
