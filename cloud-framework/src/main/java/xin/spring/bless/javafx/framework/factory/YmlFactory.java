package xin.spring.bless.javafx.framework.factory;


import org.ho.yaml.Yaml;
import xin.spring.bless.javafx.framework.exception.YamlFormatException;
import xin.spring.bless.javafx.framework.logs.Slf4jLog;

import java.io.FileNotFoundException;

/**
 * 读取yml文件工厂
 */
public class YmlFactory implements Slf4jLog {

    private static final YmlFactory instance = new YmlFactory();

    private YmlFactory(){}

    public static YmlFactory newInstance() {
        return instance;
    }

    /**
     * 加载yaml文件
     * @param path  文件路径
     * @param t 解析类型
     * @return 返回实例， 如果解析成功， 返回实例对象，解析失败，抛出异常
     * @throws YamlFormatException
     */
    public Object load(String path,Class t) throws YamlFormatException {
        try {
            Object type = Yaml.loadType(t.getResourceAsStream(path), t);
            return type;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new YamlFormatException(e, "yaml解析异常，路径：" + path);
        }
    }

}
