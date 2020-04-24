package xin.spring.bless.javafx.framework.factory;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import xin.spring.bless.javafx.framework.logs.Slf4jLog;

/**
 * 功能描述:
 * $params$
 *
 * @return: $return$
 * @Author: $user$
 * @Date: $date$ $time$
 */
public class InjectFactory implements Slf4jLog {

    private static final InjectFactory instance = new InjectFactory();

    private InjectFactory(){}

    public static InjectFactory newInstance(){
        return instance;
    }

    public <T> T inject(AnnotationConfigApplicationContext context, Class<?> type) {
        Object bean = context.getBean(type);
        return (T) bean;
    }

}
