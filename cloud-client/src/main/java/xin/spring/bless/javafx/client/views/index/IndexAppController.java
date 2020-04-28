package xin.spring.bless.javafx.client.views.index;

import com.alibaba.fastjson.JSON;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import xin.spring.bless.javafx.client.session.ApplicationSession;
import xin.spring.bless.javafx.client.views.folder.FolderAppController;
import xin.spring.bless.javafx.client.views.folder.FolderApplication;
import xin.spring.bless.javafx.common.pojo.CloudFile;
import xin.spring.bless.javafx.common.pojo.FolderFile;
import xin.spring.bless.javafx.common.pojo.User;
import xin.spring.bless.javafx.common.utils.ColorUtils;
import xin.spring.bless.javafx.common.utils.DateUtils;
import xin.spring.bless.javafx.common.utils.EventListenerUtils;
import xin.spring.bless.javafx.common.utils.StringUtils;
import xin.spring.bless.javafx.core.AbsInitializable;
import xin.spring.bless.javafx.db.repositories.CloudFileRepository;
import xin.spring.bless.javafx.db.repositories.FolderFileRepository;
import xin.spring.bless.javafx.dialog.AlertDialog;
import xin.spring.bless.javafx.fastdfs.FastDfsClient;
import xin.spring.bless.javafx.fastdfs.FastDfsUI;
import xin.spring.bless.javafx.framework.factory.FileDiskFactory;

import java.io.File;
import java.util.*;

/**
 * 功能描述:首页控制器
 * $params$
 *
 * @return: $return$
 * @Author: $user$
 * @Date: $date$ $time$
 */
public class IndexAppController extends AbsInitializable {

    @FXML private BorderPane rootParent;

    @FXML private HBox contPositionHBox;

    @FXML private Pane contentParent;

    @FXML private FlowPane contFlow;

    @FXML private Pane topParent;

    @FXML private ScrollPane scrollPane;

    @Autowired private CloudFileRepository cloudFileRepository;

    @Autowired private FolderFileRepository folderFileRepository;

    private String path = "";

    /**
     * 文档父节点
     */
    private Long folderPid = 0L;

    private VBox mVBox = new VBox();

    private ContextMenu contextMenu = new ContextMenu();

    @Override
    protected void beforeDatas() {

//        topParent.setBorder(
//                new Border(
//                        new BorderStroke(
//                                null,
//                                BorderStrokeStyle.SOLID,
//                                new CornerRadii(0),
//                                new BorderWidths(0,0,1,0)
//                                )
//                )
//        );

        Label newFolder = new Label("新建文件夹");
        newFolder.setPadding(new Insets(5,5,5,5));
        newFolder.setAlignment(Pos.CENTER);
        newFolder.setTextFill(Color.WHITE);
        HBox.setMargin(newFolder, new Insets(0,5,0,3));

        // 设置创建文件夹事件
        Color colorEnter = Color.rgb(138, 192, 27);
        Color colorOver = Color.rgb(240,173,78);
        newFolder.setBackground(new Background(new BackgroundFill(colorOver, new CornerRadii(8), null)));
        EventListenerUtils.handerEventBackFill(newFolder,new CornerRadii(8), colorEnter, colorOver, (Node node, MouseEvent event)->{
            try {
                ApplicationSession.newInstance().put(FolderAppController.FOLDER_PATH_KEY, path);
                new FolderApplication().start(new Stage());
                FolderAppController.newInstance().setResultListener((res, code) -> {
                    if(code == FolderAppController.ok){
                        User user = ApplicationSession.newInstance().getUser();
                        FolderFile folder = folderFileRepository.findByFolderNameAndFolderPidAndUserId(res, 0L, user.getUserId());
                        if(folder != null){
                            Alert error = AlertDialog.error("创建失败", "已存在当前目录");
                            error.showAndWait();
                            return;
                        }
                        log.debug("结果：{}", res);
                        FolderFile folderFile = new FolderFile();
                        folderFile.setCreateTime(new Date());
                        folderFile.setFolderName(res);
                        folderFile.setFolderPid(folderPid);
                        Optional<FolderFile> f = folderFileRepository.findById(folderPid);
                        if(f.isPresent() && f.get() != null){
                            folderFile.setLavel(f.get().getLavel() + 1);
                            String pa = "";
                            if(!StringUtils.isEmpty(f.get().getFolderParents())){
                                pa = "/" + f.get().getFolderParents() + "/" + f.get().getFolderName();
                            }else {
                                pa = "/" + f.get().getFolderName();
                            }
                            folderFile.setFolderParents(pa);
                            folderFile.setFolderPname(f.get().getFolderName());
                        }else{
                            folderFile.setLavel(1);
                        }
                        folderFile.setType("FOLDER");
                        folderFile.setUserId(user.getUserId());
                        folderFileRepository.save(folderFile);
                        flushFolderFile();
                    }else{
                        log.debug("取消操作");
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        });

        // 上传文件
        Label upload = new Label("上传");
        upload.setPadding(new Insets(5,5,5,5));
        upload.setAlignment(Pos.CENTER);
        upload.setTextFill(Color.WHITE);
        HBox.setMargin(upload, new Insets(0,5,0,3));
        Color enter = Color.rgb(49, 176, 213, 0.5);
        Color over = Color.rgb(49, 176, 213, 0.7);
        upload.setBackground(new Background(new BackgroundFill(over, new CornerRadii(8), null)));
        EventListenerUtils.handerEventBackFill(upload,new CornerRadii(8), enter, over, ((node, event) -> {
            uploadFile();
        }));

        // 设置全部文件事件
        Label allFile = new Label("全部文件");
        allFile.setPadding(new Insets(5,5,5,5));
        EventListenerUtils.handerEventBackFill(allFile, new CornerRadii(8), colorEnter, Color.TRANSPARENT, (Node label, MouseEvent event)->{
            folderPid = 0L;
            removeNext(label);
            flushFolderFile();
        });

        //allFile
        contPositionHBox.getChildren().addAll(newFolder, upload, allFile);
        contPositionHBox.setAlignment(Pos.CENTER_LEFT);

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        createmenuItem();
        flushFolderFile();
    }

    private void createmenuItem(){
        contextMenu.getItems().removeAll(contextMenu.getItems());
        ArrayList<MenuItem> menuItems = new ArrayList<>();
        String id = mVBox.getId();
        FolderFile file = JSON.parseObject(id, FolderFile.class);
        if (file != null) {
            MenuItem item1 = new MenuItem("打开");
            MenuItem item2 = new MenuItem("下载");
            MenuItem item3 = new MenuItem("分享");
            MenuItem item4 = new MenuItem("删除");
            menuItems.add(item1);
            menuItems.add(item2);
            menuItems.add(item3);
            menuItems.add(item4);
            contextMenu.setMinWidth(80);
            item1.setOnAction(ev -> {
                log.debug("打开");
                openNextTag(mVBox);
            });
            item2.setOnAction(ev -> log.debug("下载"));
            item3.setOnAction(ev -> log.debug("分享"));
            item4.setOnAction(ev -> {
                log.debug("删除");
                Alert alert = AlertDialog.confi("删除提示", "您确定要删除" + file.getFolderName() + "吗？这将不可恢复。");
                Optional<ButtonType> buttonType = alert.showAndWait();
                if(buttonType.isPresent() && buttonType.get() == ButtonType.OK){
                    User user = ApplicationSession.newInstance().getUser();
                    if("FOLDER".equals(file.getType())){
                        Collection<FolderFile> folderFiles = tempDeleteFolders(file.getFolderId(), user.getUserId());
                        folderFileRepository.deleteAll(folderFiles);
                        folderFileRepository.deleteByFolderIdAndUserId(file.getFolderId(), user.getUserId());
                    }else{
                        folderFileRepository.deleteByFolderIdAndUserId(file.getFolderId(), user.getUserId());
                    }
                    flushFolderFile();
                }
            });
            contextMenu.getItems().addAll(menuItems);
        }
    }

    /**
     * 查询待删除的文件或文件夹
     * @param folderId
     * @param userId
     * @return
     */
    private Collection<FolderFile> tempDeleteFolders(Long folderId, Long userId){
        ArrayList<FolderFile> folders = folderFileRepository.findByFolderPidAndUserId(folderId, userId);
        if (folders != null){
            ArrayList<FolderFile> files = new ArrayList<>();
            folders.forEach(i->{
                if (i.getType().equals("FOLDER")){
                    files.add(i);
                }
            });
            List<FolderFile> res = findFolderItems(files);
            folders.addAll(res);
        }
        HashSet<FolderFile> folderFiles = new HashSet<>();
        folderFiles.addAll(folders);
        return folderFiles;
    }

    private List<FolderFile> findFolderItems(List<FolderFile> files) {
        List<FolderFile> folderFiles = new ArrayList<>();
        if (null != files && files.size() > 0){
            files.forEach(i->{
                ArrayList<FolderFile> fs = folderFileRepository.findByFolderPidAndUserId(i.getFolderId(), i.getUserId());
                folderFiles.addAll(fs);
            });
            files.addAll(folderFiles);
            ArrayList<FolderFile> nextFolders = new ArrayList<>();
            folderFiles.forEach(i->{
                if (i.getType().equals("FOLDER")){
                    nextFolders.add(i);
                }
            });
            List<FolderFile> fsresult = findFolderItems(nextFolders);
            //return fsresult;
            files.addAll(fsresult);
            return files;
        }
        return files;
    }

    private void flushFolderFile(){
        contFlow.getChildren().removeAll(contFlow.getChildren());
        User user = ApplicationSession.newInstance().getUser();
        ArrayList<FolderFile> folders = folderFileRepository.findByFolderPidAndUserId(folderPid, user.getUserId());
        int mar = 10;
        for (int i=0;i<folders.size(); i++) {
            VBox vBox = new VBox();
            vBox.setPrefWidth(100);
            vBox.setPrefHeight(100);
            vBox.setAlignment(Pos.CENTER);
            String icon = "images/folder.png";
            FolderFile folderFile = folders.get(i);
            String json = JSON.toJSONString(folderFile);
            vBox.setId(json);
            if("FILE".equals(folderFile.getType().toUpperCase())){
                switch (folderFile.getFileType().toUpperCase()){
                    case "PDF":
                        icon = "images/file_pdf.png";
                        break;
                    case "TXT":
                        icon = "images/file_text.png";
                        break;
                    case "DOC":
                    case "DOCX":
                        icon = "images/file_word.png";
                        break;
                    case "XLS":
                    case "XLSX":
                        icon = "images/file_xlx.png";
                        break;
                    case "MP4":
                    case "FLV":
                    case "RMVB":
                    case "MOV":
                    case "WMV":
                        icon = "images/file_video.png";
                        break;
                    default:
                        icon = "images/file_other.png";
                        break;
                }
            }
            ImageView imageView = new ImageView(icon);
            imageView.setFitHeight(65);
            imageView.setFitWidth(65);

            Label label = new Label(folderFile.getFolderName());
            label.setTextAlignment(TextAlignment.CENTER);

            Color color = Color.rgb(0, 0, 0);
            Color targetColor = ColorUtils.translucent(color, 0.5);
            EventListenerUtils.handerEventBack(vBox, targetColor, Color.WHITE, (Node node, MouseEvent event)-> {
                MouseButton button = event.getButton();
                String name = button.name();
                mVBox = (VBox)node;
                if(name.equals(MouseButton.PRIMARY.name())) {
                    // 左键
                    openNextTag(node);
                }else if (MouseButton.SECONDARY.name().equals(name)){
                    createmenuItem();
                    contextMenu.show(node, event.getScreenX(), event.getScreenY());
                    //contextMenu.hide();
                }
            });

            vBox.getChildren().addAll(imageView,label);
            contFlow.getChildren().add(vBox);
            FlowPane.setMargin(vBox, new Insets((int)mar/2, mar, (int)mar/2, mar));
        }
    }

    /**
     * 创建新的文档窗体
     * @param node
     */
    private void openNextTag(Node node) {
        String id = node.getId();
        FolderFile file = JSON.parseObject(id, FolderFile.class);
        log.debug("数据传递：{}", id);
        if ("FOLDER".equals(file.getType())) {
            // 文件夹
            folderPid = file.getFolderId();
            // 创建箭头指示
            Label tag = new Label(">");
            tag.setPadding(new Insets(5, 5, 5, 5));
            // 创建tag标签
            Label f = new Label(file.getFolderName());
            f.setPadding(new Insets(5, 5, 5, 5));
            Color enter = Color.rgb(49, 176, 213, 0.7);
            EventListenerUtils.handerEventBackFill(f, new CornerRadii(8), enter, Color.TRANSPARENT, (Node n, MouseEvent e) -> {
                folderPid = file.getFolderId();
                removeNext(n);
                flushFolderFile();
            });

            //添加节点
            contPositionHBox.getChildren().addAll(tag, f);

            flushFolderFile();
        } else {
            // 其他类型
        }
    }

    @Override
    protected void initListener() {
        windowChange();
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

    /**
     * 文件上传
     */
    private void uploadFile(){
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
                // 创建文件
                User user = ApplicationSession.newInstance().getUser();
                FolderFile folder = folderFileRepository.findByFolderNameAndFolderPidAndUserId(file.getName(), folderPid, user.getUserId());
                FolderFile folderFile = new FolderFile();
                folderFile.setCreateTime(new Date());
                folderFile.setFolderPid(folderPid);
                Optional<FolderFile> f = folderFileRepository.findById(folderPid);
                if(f.isPresent() && f.get() != null){
                    folderFile.setLavel(f.get().getLavel() + 1);
                    String pa = "";
                    if(!StringUtils.isEmpty(f.get().getFolderParents())){
                        pa = "/" + f.get().getFolderParents() + "/" + f.get().getFolderName();
                    }else {
                        pa = "/" + f.get().getFolderName();
                    }
                    folderFile.setFolderParents(pa);
                    folderFile.setFolderPname(f.get().getFolderName());
                }else{
                    folderFile.setLavel(1);
                }
                folderFile.setType("FILE");
                folderFile.setFileType(display.getFileType());
                folderFile.setUserId(user.getUserId());
                if(folder != null){
                    folderFile.setFolderName(folder.getFolderName() + "_a_" + 1);
                }else {
                    folderFile.setFolderName(file.getName());
                }
                folderFileRepository.save(folderFile);
                flushFolderFile();
            }else{
                AlertDialog.error("文件上传提示", "文件上传失败").showAndWait();
            }
        }
    }

    /**
     * 去除后续节点
     * @param node
     */
    private void removeNext(Node node){
        if(node == null){
            return;
        }
        ObservableList<Node> children = contPositionHBox.getChildren();
        if(children != null && !children.isEmpty()){
            int i1 = children.indexOf(node);
            if(i1 != -1){
                ArrayList<Node> nodes = new ArrayList<>();
                for (int i = i1+1 ; i<children.size();i++){
                    nodes.add(children.get(i));
                }
                children.removeAll(nodes);
            }
        }
    }

}