package xin.spring.bless.javafx.db.config.database;

import java.io.Serializable;

/**
 * @author spring
 * email: 4298293220@qq.com
 * site: https://springbless.xin
 * @description 数据库版本类
 * @date 2019/12/26
 */
public class DataBase implements Serializable {

    private Hibernate hibernate;

    public Hibernate getHibernate() {
        return hibernate;
    }

    public void setHibernate(Hibernate hibernate) {
        this.hibernate = hibernate;
    }

    @Override
    public String toString() {
        return "DataBase{" +
                "hibernate=" + hibernate +
                '}';
    }

}