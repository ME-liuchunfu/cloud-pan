package xin.spring.bless.javafx.client.session;

import com.alibaba.fastjson.JSON;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import xin.spring.bless.javafx.common.pojo.User;
import xin.spring.bless.javafx.common.utils.StringUtils;
import xin.spring.bless.javafx.db.config.jpa.JpaConfig;
import xin.spring.bless.javafx.framework.cache.DirCache;

import java.util.HashMap;

/**
 * 运用程序会话，缓存
 */
public final class ApplicationSession extends HashMap<Object, Object> {

    private ApplicationSession(){}

    private static final ApplicationSession INSTANCE = new ApplicationSession();

    public static ApplicationSession newInstance(){
        return INSTANCE;
    }

    @Override
    public ApplicationSession put(Object key, Object value) {
        super.put(key, value);
        return this;
    }

    @Override
    public ApplicationSession remove(Object key) {
        super.remove(key);
        return this;
    }

    public static final String USER_KEY = "USER_KEY";

    public boolean putUser(User user){
        Object put = super.put(USER_KEY, user);
        return put != null ? true : false;
    }

    public User getUser(){
        User user = (User) get(USER_KEY);
        if(user == null) {
            String userData = new DirCache().getUserData();
            if (!StringUtils.isEmpty(userData)) {
                user = JSON.parseObject(userData, User.class);
            }
        }
        return user;
    }

    public <T> T get(Class<T> clazz){
        Object o = super.get(clazz);
        return o != null ? (T)o : null;
    }

    public <T> T getComponent(Class<T> clazz){
        AnnotationConfigApplicationContext context = springIOC();
        return context.getBean(clazz);
    }

    public AnnotationConfigApplicationContext springIOC(){
        AnnotationConfigApplicationContext context = null;
        if(this.get(AnnotationConfigApplicationContext.class) == null){
            context = new AnnotationConfigApplicationContext(JpaConfig.class);
            this.put(AnnotationConfigApplicationContext.class, context);
        }
        context = this.get(AnnotationConfigApplicationContext.class);
        return context;
    }

}
