package xin.spring.bless.javafx.client.views.folder;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import xin.spring.bless.javafx.client.session.ApplicationSession;
import xin.spring.bless.javafx.common.utils.StringUtils;
import xin.spring.bless.javafx.core.AbsInitializable;

/**
 * 创建文件夹 控制器
 */
public class FolderAppController extends AbsInitializable {

    public static final String FOLDER_PATH_KEY = "FOLDER_PATH_KEY";

    public static final int ok = 0;

    public static final int cancel = 1;

    private static FolderAppController instance = null;

    @FXML private Button btnSave;

    @FXML private Button cancelBtn;

    @FXML private Label currentPath;

    @FXML private TextField editFolder;

    @Override
    protected void beforeDatas() {
        instance = this;
        btnSave.setTextFill(Color.WHITE);
        cancelBtn.setTextFill(Color.WHITE);
        String path = (String)ApplicationSession.newInstance().get(FOLDER_PATH_KEY);
        String text = "当前所在路径：";
        if(path != null && !"".equals(path)){
            text += path;
        }else {
            text += "根目录";
        };
        currentPath.setText(text);
        editFolder.setText("");
        editFolder.requestFocus();
    }

    @Override
    protected void initListener() {
        cancelBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (resultListener != null){
                resultListener.result(null, cancel);
                Stage stage = (Stage)cancelBtn.getScene().getWindow();
                stage.close();
            }
        });
        btnSave.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (StringUtils.isEmpty(editFolder.getText().trim())){
                editFolder.setText("");
                editFolder.requestFocus();
                return;
            }
            if (resultListener != null){
                resultListener.result(editFolder.getText().trim(), ok);
                Stage stage = (Stage)btnSave.getScene().getWindow();
                stage.close();
            }
        });
    }

    private ResultListener resultListener;

    public void setResultListener(ResultListener resultListener){
        this.resultListener = resultListener;
    }

    public static FolderAppController newInstance(){
        return instance;
    }

    /**
     * 响应结果
     */
    public static interface ResultListener{
        public void result(String res, int code);
    }

}
