package xin.spring.bless.javafx.common.dialog;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * 功能描述: 弹窗
 * $params$
 *
 * @return: $return$
 * @Author: $user$
 * @Date: $date$ $time$
 */
public class AlertDialog {

    private AlertDialog(){}

    public static void display(String title , String message){
        display(title, message, window -> {
            window.setMinWidth(300);
            window.setMinHeight(150);
            Button button = new Button("点击关闭");
            button.setOnAction(e -> window.close());
            Label label = new Label(message);
            VBox layout = new VBox(10);
            layout.getChildren().addAll(label , button);
            layout.setAlignment(Pos.CENTER);
            Scene scene = new Scene(layout);
            window.setScene(scene);
        });
    }

    public static void displayDelay(String title , String message, long time){
        Stage window = new Stage();
        window.setTitle(title);
        //modality要使用Modality.APPLICATION_MODEL
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(300);
        window.setMinHeight(150);
        Label label = new Label(message);
        AnchorPane layout = new AnchorPane();
        layout.getChildren().add(label);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.show();
        long start = System.currentTimeMillis();
        while (true){
            long end = System.currentTimeMillis();
            if(end - time > start){
                window.close();
                break;
            }
        }
    }

    /**
     * 显示定义
     * @param title
     * @param message
     * @param listener
     */
    public static void display(String title , String message, DisplayListener listener){
        Stage window = new Stage();
        window.setTitle(title);
        //modality要使用Modality.APPLICATION_MODEL
        window.initModality(Modality.APPLICATION_MODAL);
        if(listener != null){
            try{
                listener.onLoad(window);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        //使用showAndWait()先处理这个窗口，而如果不处理，main中的那个窗口不能响应
        window.showAndWait();
    }

    /**
     * 自定义显示监听
     */
    public static interface DisplayListener{
        void onLoad(Stage window);
    }

}
