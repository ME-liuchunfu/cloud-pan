package xin.spring.bless.javafx.client.views.login;

import javafx.scene.image.Image;
import javafx.stage.Stage;
import xin.spring.bless.javafx.core.BaseApplication;

/**
 * @author spring
 * email: 4298293220@qq.com
 * site: https://springbless.xin
 * @description denglu
 * @date 2020/04/23
 */
public class LoginApplication extends BaseApplication {
    @Override
    protected void initViews(Stage primaryStage) {
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("images/earth.jpg")));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
