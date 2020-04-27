package xin.spring.bless.javafx.client.views.login;

import javafx.scene.image.Image;
import javafx.stage.Stage;
import xin.spring.bless.javafx.core.BaseApplication;
import xin.spring.bless.javafx.framework.annotation.ViewTitle;

/**
 * @author spring
 * email: 4298293220@qq.com
 * site: https://springbless.xin
 * @description denglu
 * @date 2020/04/23
 */
@ViewTitle("登录分布式私有云")
public class LoginApplication extends BaseApplication<LoginApplication> {

    @Override
    protected void initViews(Stage primaryStage) {
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("images/yun_logo.png")));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
