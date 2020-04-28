package xin.spring.bless.javafx.fastdfs;

import javafx.scene.control.Alert;

/**
 * 功能描述: fastdfs上传ui组件
 * $params$
 *
 * @return: $return$
 * @Author: $user$
 * @Date: $date$ $time$
 */
public class FastDfsUI {

    /**
     * 文件节点id
     */
    private String fileId;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 文件名，不包含后缀
     */
    private String prefName;

    /**
     * ui
     */
    private Alert display;

    public FastDfsUI(){
        display = new Alert(Alert.AlertType.INFORMATION);
        display.setTitle("文件上传");
        display.setContentText("文件上传中，请耐心等待。");
        display.setHeaderText(null);
    }

    public FastDfsUI(Alert display){
        this.display = display;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public Alert getDisplay() {
        return display;
    }

    public void setDisplay(Alert display) {
        this.display = display;
    }

    public String getFileType() {
        return fileType;
    }

    public String getPrefName() {
        return prefName;
    }

    public void setPrefName(String prefName) {
        this.prefName = prefName;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Override
    public String toString() {
        return "FastDfsUI{" +
                "fileId='" + fileId + '\'' +
                ", fileType='" + fileType + '\'' +
                ", prefName='" + prefName + '\'' +
                ", display=" + display +
                '}';
    }
}
