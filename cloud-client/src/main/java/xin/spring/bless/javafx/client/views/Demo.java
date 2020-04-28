package xin.spring.bless.javafx.client.views;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class Demo extends Application {

    Label fNameLbl = new Label("First Name:");
    Label lNameLbl = new Label("Last Name:");
    Label bDateLbl = new Label("Birth Date:");
    Label genderLbl = new Label("Gender:");

    TextField fNameFld = new TextField();
    TextField lNameFld = new TextField();
    DatePicker bDateFld = new DatePicker();
    ChoiceBox<String> genderFld = new ChoiceBox<>();
    TextArea dataFld = new TextArea();

    Button saveBtn = new Button("Save");
    Button closeBtn = new Button("Close");

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        genderFld.getItems().addAll("Male", "Fenale", "Unknwon");

        dataFld.setPrefColumnCount(30);
        dataFld.setPrefRowCount(5);

        GridPane grid = new GridPane();
        grid.setHgap(5);
        grid.setVgap(5);
        String[]  music={"http://39.108.106.34:9870/source/M00/00/00/rBAVlF6nuruAY1JwAC6hlVFm4mc181.mp3",
                "http://39.108.106.34:9870/source/M00/00/00/rBAVlF6nuqyAfCL8ACvmS87bXp0760.mp3"};
        Media media = new Media("/Users/mac/Downloads/C400000KevkL3W11bX.mp3");//"/Users/mac/Desktop/来遇见他 - 胡66.mp3"
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(0.8);

        mediaPlayer.play();
        // Place the controls in the grid
        grid.add(fNameLbl, 0, 0);  // column=0, row=0
        grid.add(lNameLbl, 0, 1);  // column=0, row=1
        grid.add(bDateLbl, 0, 2);  // column=0, row=2
        grid.add(genderLbl, 0, 3); // column=0, row=3

        grid.add(fNameFld, 1, 0);  // column=1, row=0
        grid.add(lNameFld, 1, 1);  // column=1, row=1
        grid.add(bDateFld, 1, 2);  // column=1, row=2
        grid.add(genderFld, 1, 3); // column=1, row=3
        grid.add(dataFld, 1, 4, 3, 2); // column=1, row=4, colspan=3, rowspan=3

        VBox buttonBox = new VBox(saveBtn, closeBtn);
        saveBtn.setMaxWidth(Double.MAX_VALUE);
        closeBtn.setMaxWidth(Double.MAX_VALUE);

        grid.add(buttonBox, 2, 0, 1, 2); // column=2, row=0, colspan=1, rowspan=2

        saveBtn.setOnAction(e -> showData());

        closeBtn.setOnAction(e -> stage.hide());

        Scene scene = new Scene(grid);
        stage.setScene(scene);
        stage.setTitle("Person Details");
        stage.sizeToScene();
        stage.show();
    }

    private void showData() {
        String data = "First Name = " + fNameFld.getText() +
                "\nLast Name=" + lNameFld.getText() +
                "\nBirth Date=" + bDateFld.getValue() +
                "\nGender=" + genderFld.getValue();
        dataFld.setText(data);
    }

}
