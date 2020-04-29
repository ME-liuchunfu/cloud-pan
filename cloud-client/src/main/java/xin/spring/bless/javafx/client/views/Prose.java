package xin.spring.bless.javafx.client.views;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * 功能描述:
 * $params$
 *
 * @return: $return$
 * @Author: $user$
 * @Date: $date$ $time$
 */
public class Prose extends Application {

    Task copyWorker;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root, 330, 120, Color.WHITE);

        BorderPane mainPane = new BorderPane();
        root.getChildren().add(mainPane);

        final Label label = new Label("Files Transfer:");
        final ProgressBar progressBar = new ProgressBar(0);

        final HBox hb = new HBox();
        hb.setSpacing(5);
        hb.setAlignment(Pos.CENTER);
        hb.getChildren().addAll(label, progressBar);
        mainPane.setTop(hb);

        final Button startButton = new Button("Start");
        final Button cancelButton = new Button("Cancel");
        final HBox hb2 = new HBox();
        hb2.setSpacing(5);
        hb2.setAlignment(Pos.CENTER);
        hb2.getChildren().addAll(startButton, cancelButton);
        mainPane.setBottom(hb2);
        startButton.setOnAction( event -> {
            startButton.setDisable(true);
            progressBar.setProgress(0);
            cancelButton.setDisable(false);
            copyWorker = createWorker();
            progressBar.progressProperty().unbind();
            progressBar.progressProperty().bind(copyWorker.progressProperty());
            copyWorker.messageProperty().addListener(new ChangeListener<String>() {
                public void changed(ObservableValue<? extends String> observable,
                                    String oldValue, String newValue) {
                    System.out.println(newValue);
                }
            });
            new Thread(copyWorker).start();
        });
        cancelButton.setOnAction(event -> {
            startButton.setDisable(false);
            cancelButton.setDisable(true);
            copyWorker.cancel(true);
            progressBar.progressProperty().unbind();
            progressBar.setProgress(0);
            System.out.println("cancelled.");
        });
        primaryStage.setScene(scene);
        primaryStage.show();

        Alert display = new Alert(Alert.AlertType.INFORMATION);
        display.setTitle("文件上传");
        display.setContentText("文件上传中，请耐心等待。");
        display.setHeaderText(null);
        display.show();
    }

    public Task createWorker() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(2000);
                    updateMessage("current:" + i + "Thread.sleep===2000 milliseconds");
                    updateProgress(i + 1, 10);
                }
                ///Users/mac/Desktop/uploadPath
//                HttpDownLoad.newInstance().downloadAsyn("http://39.108.106.34:9870/source/M00/00/00/rBAVlF6nuruAY1JwAC6hlVFm4mc181.mp3",
//                        "/Users/mac/Desktop/uploadPath", );
                return true;
            }
        };
    }
}
