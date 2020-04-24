package xin.spring.bless.javafx.db;

import xin.spring.bless.javafx.db.config.database.DataBase;
import xin.spring.bless.javafx.framework.factory.YmlFactory;

/**
 * 功能描述:
 * $params$
 *
 * @return: $return$
 * @Author: $user$
 * @Date: $date$ $time$
 */
public class YamlDataBase {

    private static final YamlDataBase instance = new YamlDataBase();

    public static YamlDataBase newInstance(){
        return instance;
    }

    private YamlDataBase(){}

    public  Object decode(String path, Class t){
        Object load = YmlFactory.newInstance().load(path, t);
        return load;
    }

    public DataBase decode(){
        DataBase load = (DataBase)YmlFactory.newInstance().load("/application-datebase.yml", DataBase.class);
        return load;
    }

}
