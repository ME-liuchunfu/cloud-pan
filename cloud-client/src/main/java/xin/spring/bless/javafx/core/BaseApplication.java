package xin.spring.bless.javafx.core;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import xin.spring.bless.javafx.framework.annotation.ViewTitle;

import java.io.IOException;
import java.net.URL;

/**
 * @author spring
 * email: 4298293220@qq.com
 * site: https://springbless.xin
 * @description javafx-application基类
 * @date 2020/04/23
 */
public abstract class BaseApplication<T> extends Application {

    private static Object instance;

    /**
     * 窗体标题
     */
    protected String frameTitle;

    @Override
    public void start(Stage primaryStage) {
        instance = this;
        if(this.getClass().getDeclaredAnnotation(ViewTitle.class) != null) {
            ViewTitle title = this.getClass().getAnnotation(ViewTitle.class);
            this.frameTitle = title.value();
        }
        try {
            String path = "/" + this.getClass().getName().replace(".","/");
            URL resource = this.getClass().getResource(path+ ".fxml");
            Parent root = FXMLLoader.load(this.getClass().getResource(path + ".fxml"));
            Scene scene = new Scene(root);

            scene.getStylesheets().add(this.getClass().getResource(path + ".css").toExternalForm());
            primaryStage.setTitle(frameTitle != null ? frameTitle : this.getClass().getName());
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            // 自定义其他显示样式
            initViews(primaryStage);
            primaryStage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 获取实例对象
     * @param <T>
     * @return
     */
    public static <T> T getInstance(){
        return (T) instance;
    }

    /**
     * 初始化视图
     */
    protected abstract void initViews(Stage primaryStage);

}
