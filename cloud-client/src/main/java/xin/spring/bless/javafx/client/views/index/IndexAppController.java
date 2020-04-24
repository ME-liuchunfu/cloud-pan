package xin.spring.bless.javafx.client.views.index;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import xin.spring.bless.javafx.core.AbsInitializable;
import xin.spring.bless.javafx.fastdfs.FastDfsClient;
import xin.spring.bless.javafx.framework.factory.FileDiskFactory;

import java.io.File;

/**
 * 功能描述:首页控制器
 * $params$
 *
 * @return: $return$
 * @Author: $user$
 * @Date: $date$ $time$
 */
public class IndexAppController extends AbsInitializable {

    @FXML private Button register;

    @FXML private Button submit;

    @FXML private TextField loginname;

    @FXML private TextField password;

    @Override
    protected void beforeDatas() {

    }

    @Override
    protected void initListener() {
        register.setOnAction(event -> {
            File file = FileDiskFactory.newInstance().chooseFile(fileChooser -> {
                fileChooser.setTitle("请选择相对应的文件");
            });
            if (file != null && file.isFile()){
                String fileId = FastDfsClient.newInstance().uploadFile(file);
                log.debug("文件节点id：{}", fileId);
            }
        });
    }

}
