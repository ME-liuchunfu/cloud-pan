package xin.spring.bless.javafx.client.views.index;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;
import xin.spring.bless.javafx.core.BaseApplication;
import xin.spring.bless.javafx.framework.annotation.ViewTitle;

/**
 * 功能描述:s主页
 * $params$
 *
 * @return: $return$
 * @Author: $user$
 * @Date: $date$ $time$
 */
@ViewTitle("首页")
public class IndexApplication extends BaseApplication<IndexApplication> {

    @Override
    protected void initViews(Stage primaryStage) {
        primaryStage.setResizable(true);
        primaryStage.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (changeLinstener != null){
                    changeLinstener.changed(primaryStage, observable, oldValue, newValue);
                }
            }
        });
    }

    private WindowChangeLinstener changeLinstener;

    public void setChangeLinstener(WindowChangeLinstener changeLinstener){
        this.changeLinstener = changeLinstener;
    }

    /**
     * 监听窗体改变
     */
    public static interface WindowChangeLinstener{
        public void changed(Stage primaryStage, ObservableValue<? extends Number> observable, Number oldValue, Number newValue);
    }

}
