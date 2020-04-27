package xin.spring.bless.javafx.client.views.folder;

import javafx.stage.Stage;
import xin.spring.bless.javafx.core.BaseApplication;
import xin.spring.bless.javafx.framework.annotation.ViewTitle;

@ViewTitle("创建文件夹")
public class FolderApplication extends BaseApplication<FolderApplication> {

    @Override
    protected void initViews(Stage primaryStage) {

    }

    public static void main(String[] args) {
        launch(args);
    }

}
