package xin.spring.bless.javafx.client.views.index;

import com.alibaba.fastjson.JSON;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
import xin.spring.bless.javafx.common.utils.*;
import xin.spring.bless.javafx.conf.FastDfsServer;
import xin.spring.bless.javafx.conf.ServerConf;
import xin.spring.bless.javafx.core.AbsInitializable;
import xin.spring.bless.javafx.db.repositories.CloudFileRepository;
import xin.spring.bless.javafx.db.repositories.FolderFileRepository;
import xin.spring.bless.javafx.db.repositories.UserRepository;
import xin.spring.bless.javafx.dialog.AlertDialog;
import xin.spring.bless.javafx.fastdfs.FastDfsClient;
import xin.spring.bless.javafx.fastdfs.FastDfsUI;
import xin.spring.bless.javafx.framework.cache.DirCache;
import xin.spring.bless.javafx.framework.exception.FastDfsClientException;
import xin.spring.bless.javafx.framework.factory.FileDiskFactory;
import xin.spring.bless.javafx.framework.factory.YmlFactory;
import xin.spring.bless.javafx.framework.http.HttpDownLoad;
import xin.spring.bless.javafx.framework.http.OnDownloadListener;

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

    @FXML private Pane leftPanel;

    @FXML private ScrollPane scrollPane;

    @Autowired private CloudFileRepository cloudFileRepository;

    @Autowired private FolderFileRepository folderFileRepository;

    @Autowired private UserRepository userRepository;

    private String path = "";

    /**
     * 文档父节点
     */
    private Long folderPid = 0L;

    private VBox mVBox = new VBox();

    private ContextMenu contextMenu = new ContextMenu();

    @Override
    protected void beforeDatas() {
        createLeftPanel();
        createContentPanel();
    }

    /**
     * 创建并初始化左边面板
     */
    private void createLeftPanel() {

        leftPanel.getChildren().removeAll(leftPanel.getChildren());

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setPrefWidth(leftPanel.getPrefWidth());

        // 用户信息
        User user = ApplicationSession.newInstance().getUser();
        Label uName = new Label(user.getUserName());
        uName.setAlignment(Pos.CENTER_LEFT);
        uName.setPadding(new Insets(0,10,0,10));
        uName.setPrefWidth(vBox.getPrefWidth());
        VBox.setMargin(uName, new Insets(10,0,5,0));

        // 创建进度
        ProgressBar disk = new ProgressBar(0);
        disk.setPadding(new Insets(0,10,0,10));
        disk.setPrefWidth(vBox.getPrefWidth());
        Task worker = createWorker(user.getCurrentDiskSize(), user.getMaxDiskSize());
        disk.progressProperty().unbind();
        disk.progressProperty().bind(worker.progressProperty());

        // 创建容量文字
        String covent = DiskCoventUtils.covent(user.getCurrentDiskSize(), user.getMaxDiskSize());
        Label label = new Label(covent);
        label.setPrefWidth(vBox.getPrefWidth());
        label.setAlignment(Pos.CENTER_LEFT);
        label.setPadding(new Insets(0,10,0,10));
        VBox.setMargin(label, new Insets(5,0,5,0));
        worker.messageProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                log.info(newValue);
                label.setText(newValue);
            }
        });
        new Thread(worker).start();

        // 正在下载
        Label down = new Label("正在下载");
        Label zz = new Label("赞助作者");
        Label lx = new Label("联系客服");

        down.setAlignment(Pos.CENTER);
        down.setPadding(new Insets(10,10,10,10));
        down.setPrefWidth(vBox.getPrefWidth());
        zz.setAlignment(Pos.CENTER);
        zz.setPadding(new Insets(10,10,10,10));
        zz.setPrefWidth(vBox.getPrefWidth());

        lx.setAlignment(Pos.CENTER);
        lx.setPadding(new Insets(10,10,10,10));
        lx.setPrefWidth(vBox.getPrefWidth());

//        VBox.setMargin(down, new Insets(5,0,5,0));
//        VBox.setMargin(zz, new Insets(5,0,5,0));

        Color denter = Color.rgb(0, 0, 0, 0.08);
        down.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null,null)));
        EventListenerUtils.handerEventBackEach(denter, Color.TRANSPARENT, (node, event) -> {
            log.debug("left：" + ((Label)node).getText());
        },down, zz, lx);

        vBox.getChildren().addAll(uName, disk, label, down, zz, lx);
        leftPanel.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        leftPanel.setBorder(new Border(new BorderStroke(denter, BorderStrokeStyle.SOLID, new CornerRadii(2), new BorderWidths(0,1,0,0))));
        leftPanel.getChildren().addAll(vBox);
    }

    /**
     * 更新容量进度条
     * @param c
     * @param t
     * @return
     */
    public Task createWorker(long c, long t) {
        return new Task() {
            private boolean flag = false;
            @Override
            protected Object call() throws Exception {
                if (!flag) {
                    updateMessage(DiskCoventUtils.covent(c,t));
                    updateProgress(c, t);
                    flag = true;
                }
                return true;
            }
        };
    }

    /**
     * 创建并初始化主页面内容
     */
    private void createContentPanel() {
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

    /**
     * 创建文件夹菜单
     */
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
            item2.setOnAction(ev -> {
                log.debug("下载");
                File f = FileDiskFactory.newInstance().chooseFolder(null);
                if(f != null){
                    User user = ApplicationSession.newInstance().getUser();
                    Collection<FolderFile> folderFiles = tempDeleteFolders(file.getFolderId(), user.getUserId());
                    ArrayList<FolderFile> fold = folderFileRepository.findByFolderIdAndUserId(file.getFolderId(), user.getUserId());
                    if (fold != null){
                        fold.addAll(folderFiles);
                        FastDfsServer load = (FastDfsServer) YmlFactory.newInstance().load("/application.yml", FastDfsServer.class);
                        ServerConf server = load.getServer();
                        ArrayList<FolderFile> downFile = new ArrayList<>();
                        fold.forEach(it->{
                            if ("FILE".equals(it.getType())){
                                Optional<CloudFile> cloudFile = cloudFileRepository.findById(it.getFileId());
                                if (cloudFile.isPresent() && cloudFile.get() != null){
                                    it.setCloudFile(cloudFile.get());
                                    downFile.add(it);
                                }
                            }
                        });
                        downFile.forEach(d->{
                            if(d.getCloudFile() != null){
                                new Thread() {
                                    @Override
                                    public void run() {
                                        String taPath = f.getAbsolutePath();
                                        if(!StringUtils.isEmpty(d.getFolderParents())){
                                            taPath += d.getFolderParents();
                                        }
                                        HttpDownLoad.newInstance().downLoadFromUrl(
                                                server.getHttpAddr() + d.getCloudFile().getFilePath(),
                                                d.getFolderName(),
                                                taPath, new OnDownloadListener() {
                                                    @Override
                                                    public void onDownloadSuccess(File file) {
                                                        log.debug("下载成功：" + file.getAbsolutePath());
                                                    }

                                                    @Override
                                                    public void onDownloading(int progress) {
                                                        log.debug("下载中：" + progress);
                                                    }

                                                    @Override
                                                    public void onDownloadFailed(Exception e) {
                                                        log.error("下载出错：" + e.getMessage());
                                                    }
                                                }
                                        );
                                    }
                                }.start();
                            }
                        });
                    }
                }
            });
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

    /**
     * 地柜查找云端文件
     * @param files
     * @return
     */
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

    /**
     * 创建显示页文件或文件夹
     */
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
            // 创建文件
            User user = ApplicationSession.newInstance().getUser();
            if(file.length() > (user.getMaxDiskSize() - user.getCurrentDiskSize())){
                // 超出剩余存储空间
                AlertDialog.error("上传出错", "当前剩余空间不足").showAndWait();
                return;
            }
            long start = System.currentTimeMillis();
            log.debug("上传开始：{}",start);
            FastDfsClient.newInstance().uploadFile(file.getAbsolutePath(),
                new FastDfsClient.OnUploadListener() {

                    @Override
                    public void ready(FastDfsUI ui) {}

                    @Override
                    public void success(Object o, FastDfsUI ui) {
                        uiThread(new UIThread<FastDfsUI>() {
                            @Override
                            public void run(FastDfsUI fastDfsUI) {
                                log.debug("上传结果：{}", ui);
                                ui.getDisplay().close();
                                log.debug(ui.getFileId());
                                CloudFile cloudFile = new CloudFile();
                                cloudFile.setFileName(file.getName());
                                cloudFile.setFilePath(ui.getFileId());
                                cloudFile.setFileSize(file.length());
                                cloudFile.setCreateTime(new Date());
                                cloudFile.setFileType(ui.getFileType());
                                cloudFileRepository.save(cloudFile);
                                long end = System.currentTimeMillis();
                                log.debug("上传结束：{}", end);
                                log.debug("耗时：{}", DateUtils.getDatePoor(end, start));

                                FolderFile folder = folderFileRepository.findByFolderNameAndFolderPidAndUserId(file.getName(), folderPid, user.getUserId());
                                FolderFile folderFile = new FolderFile();
                                folderFile.setCreateTime(new Date());
                                folderFile.setFolderPid(folderPid);
                                Optional<FolderFile> f = folderFileRepository.findById(folderPid);
                                if(f.isPresent() && f.get() != null){
                                    folderFile.setLavel(f.get().getLavel() + 1);
                                    String pa = "";
                                    if(!StringUtils.isEmpty(f.get().getFolderParents())){
                                        pa = f.get().getFolderParents() + "/" + f.get().getFolderName();
                                    }else {
                                        pa = "/" + f.get().getFolderName();
                                    }
                                    folderFile.setFolderParents(pa);
                                    folderFile.setFolderPname(f.get().getFolderName());
                                }else{
                                    folderFile.setLavel(1);
                                }
                                folderFile.setType("FILE");
                                folderFile.setFileType(ui.getFileType());
                                folderFile.setUserId(user.getUserId());
                                if(folder != null){
                                    folderFile.setFolderName(folder.getFolderName() + "_a_" + 1);
                                }else {
                                    folderFile.setFolderName(file.getName());
                                }
                                folderFile.setFileId(cloudFile.getFileId());
                                folderFileRepository.save(folderFile);
                                userRepository.updateCurrentDiskSizeByUserId(user.getCurrentDiskSize() + file.length(), user
                                        .getUserId());
                                Optional<User> u = userRepository.findById(user.getUserId());
                                if (u.isPresent() && u.get() != null){
                                    ApplicationSession.newInstance().putUser(u.get());
                                    String json = JSON.toJSONString(user);
                                    new DirCache().cacheUser(json);
                                    createLeftPanel();
                                }
                                flushFolderFile();
                            }
                        }, ui);
                    }

                    @Override
                    public void faild(FastDfsClientException e, FastDfsUI ui) {
                        uiThread(new UIThread<FastDfsUI>() {
                            @Override
                            public void run(FastDfsUI t) {
                                if (t != null){
                                    t.getDisplay().close();
                                }
                                AlertDialog.error("文件上传提示", "文件上传失败").showAndWait();
                            }
                        }, ui);
                    }
                });
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
