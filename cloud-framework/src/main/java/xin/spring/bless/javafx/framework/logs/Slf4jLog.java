package xin.spring.bless.javafx.framework.logs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author spring
 * email: 4298293220@qq.com
 * site: https://springbless.xin
 * @description 日志工厂接口
 * @date 2020/04/23
 */
public interface Slf4jLog {

    /**
     * 日志对象
     */
    Logger log = LoggerFactory.getLogger(Slf4jLog.class);

}
