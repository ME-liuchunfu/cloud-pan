package xin.spring.bless.javafx.client.views.index;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import xin.spring.bless.javafx.common.pojo.CloudFile;
import xin.spring.bless.javafx.common.utils.DateUtils;
import xin.spring.bless.javafx.core.AbsInitializable;
import xin.spring.bless.javafx.db.repositories.CloudFileRepository;
import xin.spring.bless.javafx.dialog.AlertDialog;
import xin.spring.bless.javafx.fastdfs.FastDfsClient;
import xin.spring.bless.javafx.fastdfs.FastDfsUI;
import xin.spring.bless.javafx.framework.factory.FileDiskFactory;

import java.io.File;
import java.util.Date;

/**
 * 功能描述:首页控制器
 * $params$
 *
 * @return: $return$
 * @Author: $user$
 * @Date: $date$ $time$
 */
public class IndexAppController extends AbsInitializable {

    @FXML private Button upload;

    @FXML private Button submit;

    @FXML private TextField loginname;

    @FXML private TextField password;

    @Autowired private CloudFileRepository cloudFileRepository;

    @Override
    protected void beforeDatas() {

    }

    @Override
    protected void initListener() {
        upload.setOnAction(event -> {
            File file = FileDiskFactory.newInstance().chooseFile(fileChooser -> {
                fileChooser.setTitle("请选择相对应的文件");
            });
            if (file != null && file.isFile()){
                long start = System.currentTimeMillis();
                log.debug("上传开始：{}",start);
                FastDfsUI display = FastDfsClient.newInstance().uploadFile(file);
                log.debug("上传结果：{}", display);
                if(display != null){
                    display.getDisplay().close();
                    log.debug(display.getFileId());
                    CloudFile cloudFile = new CloudFile();
                    cloudFile.setFileName(file.getName());
                    cloudFile.setFilePath(display.getFileId());
                    cloudFile.setFileSize(file.length());
                    cloudFile.setCreateTime(new Date());
                    cloudFileRepository.save(cloudFile);
                    long end = System.currentTimeMillis();
                    log.debug("上传结束：{}", end);
                    log.debug("耗时：{}", DateUtils.getDatePoor(end, start));

                }else{
                    AlertDialog.error("文件上传提示", "文件上传失败").showAndWait();
                }
            }
        });
    }

}
