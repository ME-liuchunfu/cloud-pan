package xin.spring.bless.javafx.core;

import javafx.fxml.Initializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import xin.spring.bless.javafx.client.session.ApplicationSession;
import xin.spring.bless.javafx.framework.exception.InjectException;
import xin.spring.bless.javafx.framework.factory.InjectFactory;
import xin.spring.bless.javafx.framework.logs.Slf4jLog;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author spring
 * email: 4298293220@qq.com
 * site: https://springbless.xin
 * @description 抽象控制器
 * @date 2020/04/23
 */
public abstract class AbsInitializable implements Initializable, Slf4jLog {

    @Override
    public void initialize(URL location, ResourceBundle resources){
        autoInject();
        beforeDatas();
        initListener();
        afterListener();
    }

    /**
     * 监听前初始化数据
     */
    protected abstract void beforeDatas();

    /**
     * 监听事件
     */
    protected abstract void initListener();

    /**
     * 监听后执行
     */
    protected void afterListener(){}

    /**
     * 依赖注入
     */
    protected void autoInject() throws InjectException{
        Field[] fields = this.getClass().getDeclaredFields();
        AnnotationConfigApplicationContext context = ApplicationSession.newInstance().springIOC();
        for (Field field : fields){
            boolean autowired = field.isAnnotationPresent(Autowired.class);
            if(autowired){
                Object value = InjectFactory.newInstance().inject(context, field.getType());
                field.setAccessible(true);
                try {
                    field.set(this,value );
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    throw new InjectException(e, this.getClass().getName() + field.getName() + "注入异常");
                }
            }
        }
    }

}
