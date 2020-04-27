package xin.spring.bless.javafx.client.views.index;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import sun.jvm.hotspot.tools.jcore.PackageNameFilter;
import xin.spring.bless.javafx.client.session.ApplicationSession;
import xin.spring.bless.javafx.client.views.folder.FolderAppController;
import xin.spring.bless.javafx.client.views.folder.FolderApplication;
import xin.spring.bless.javafx.common.pojo.CloudFile;
import xin.spring.bless.javafx.common.utils.ColorUtils;
import xin.spring.bless.javafx.common.utils.DateUtils;
import xin.spring.bless.javafx.common.utils.EventListenerUtils;
import xin.spring.bless.javafx.core.AbsInitializable;
import xin.spring.bless.javafx.db.repositories.CloudFileRepository;
import xin.spring.bless.javafx.dialog.AlertDialog;
import xin.spring.bless.javafx.fastdfs.FastDfsClient;
import xin.spring.bless.javafx.fastdfs.FastDfsUI;
import xin.spring.bless.javafx.framework.factory.FileDiskFactory;

import java.io.File;
import java.util.Date;
import java.util.LinkedList;
import java.util.UUID;

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

    @FXML private HBox contPositionHBox;

    @FXML private Pane contentParent;

    @FXML private FlowPane contFlow;

    @FXML private ScrollPane scrollPane;

    @Autowired private CloudFileRepository cloudFileRepository;

    @Override
    protected void beforeDatas() {
        Label newFolder = new Label("新建文件夹");
        newFolder.setPadding(new Insets(5,5,5,5));
        newFolder.setAlignment(Pos.CENTER);
        newFolder.setTextFill(Color.WHITE);
        HBox.setMargin(newFolder, new Insets(0,5,0,3));

        Label allFile = new Label("全部文件");
        allFile.setPadding(new Insets(5,5,5,5));
        //allFile
        contPositionHBox.getChildren().addAll(newFolder, allFile);
        contPositionHBox.setAlignment(Pos.CENTER_LEFT);

        // 设置创建文件夹事件
        Color colorEnter = Color.rgb(138, 192, 27);
        Color colorOver = Color.rgb(240,173,78);
        newFolder.setBackground(new Background(new BackgroundFill(colorOver, new CornerRadii(8), null)));
        EventListenerUtils.handerEventBackFill(newFolder,new CornerRadii(8), colorEnter, colorOver, (Node node, MouseEvent event)->{
            try {
                ApplicationSession.newInstance().put(FolderAppController.FOLDER_PATH_KEY, "/iam/aaa/bbb");
                new FolderApplication().start(new Stage());
                FolderAppController.newInstance().setResultListener((res, code) -> {
                    if(code == FolderAppController.ok){
                        log.debug("结果：{}", res);
                    }else{
                        log.debug("取消操作");
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        });

        // 设置全部文件事件
        EventListenerUtils.handerEventBackFill(allFile, new CornerRadii(8), colorEnter, Color.TRANSPARENT, (Node label, MouseEvent event)->{
            flushFolderFile();
        });

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        flushFolderFile();
    }


    private void flushFolderFile(){
        contFlow.getChildren().removeAll(contFlow.getChildren());
        int row = 6;
        int colum = 2;
        int mar = 10;
        for (int i=0;i<row*colum; i++) {
            VBox vBox = new VBox();
            vBox.setPrefWidth(100);
            vBox.setPrefHeight(100);
            vBox.setAlignment(Pos.CENTER);
            String icon = "images/folder.png";
            if(i % 2 == 0){
                icon = "images/file_other.png";
            }
            ImageView imageView = new ImageView(icon);
            imageView.setFitHeight(65);
            imageView.setFitWidth(65);
            Color color = Color.rgb(0, 0, 0);
            Color targetColor = ColorUtils.translucent(color, 0.5);
            EventListenerUtils.handerEventBack(vBox, targetColor, Color.WHITE, (Node node, MouseEvent event)-> {
                EventType<? extends MouseEvent> eventType = event.getEventType();
                System.out.println("vBox.setOnMouseClicked:"+eventType);
            });
            Label label = new Label(UUID.randomUUID().toString());
            label.setTextAlignment(TextAlignment.CENTER);
            vBox.getChildren().addAll(imageView,label);
            contFlow.getChildren().add(vBox);
            FlowPane.setMargin(vBox, new Insets((int)mar/2, mar, (int)mar/2, mar));
        }
    }

    @Override
    protected void initListener() {
        windowChange();

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
                    cloudFile.setFileType(display.getFileType());
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

    private void windowChange() {
        IndexApplication indexApplication = IndexApplication.getInstance();
        indexApplication.setChangeLinstener((primaryStage, observable, oldValue, newValue) -> {
            double width = contentParent.getWidth();
            double height = contentParent.getHeight();
            double mar = contPositionHBox.getHeight();
            log.debug("width:" + width + "   ,height:" + height + "  ,mar:"+mar);
            contFlow.setPrefHeight(height -mar);
            contFlow.setPrefWidth(width);
            scrollPane.setPrefHeight(height - mar);
            scrollPane.setPrefWidth(width);
        });
    }

}
