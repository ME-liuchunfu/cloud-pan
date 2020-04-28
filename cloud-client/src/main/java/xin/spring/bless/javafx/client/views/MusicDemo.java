package xin.spring.bless.javafx.client.views;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class MusicDemo extends Application {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(MusicDemo.class, args);
    }
    public  String[]  music={"http://39.108.106.34:9870/source/M00/00/00/rBAVlF6nuruAY1JwAC6hlVFm4mc181.mp3",
            "http://39.108.106.34:9870/source/M00/00/00/rBAVlF6nuqyAfCL8ACvmS87bXp0760.mp3"};
    public static  int i=0;
    public void start(final Stage stage) {
        final Group g=new Group();
        final Scene scene=new Scene(g,250,100, Color.rgb(54, 138, 201));
        final MediaPlayer[] mediaPlayers=new MediaPlayer[music.length];
        final ImageView playButton=new ImageView();
        final ImageView beforeButton=new ImageView();
        final ImageView afterButton=new ImageView();
        final Image Play=new Image("src/Images/play.png");
        final Image Stop=new Image("src/Images/pause.png");
        final Image before=new Image("src/Images/before.png");
        final Image after=new Image("src/Images/next.png");
        playButton.setImage(Play);
        playButton.setTranslateX(scene.getWidth()/2-20);
        playButton.setTranslateY(30);
        beforeButton.setImage(before);
        beforeButton.setTranslateX(scene.getWidth()/5-20);
        beforeButton.setTranslateY(30);
        afterButton.setImage(after);
        afterButton.setTranslateX(scene.getWidth()*3/4);
        afterButton.setTranslateY(30);
        for(int m=0;m<music.length;m++)
        {
            Media media=new Media(getClass().getResource(music[m]).toExternalForm());
            mediaPlayers[m]=(new MediaPlayer(media));
        }
        playButton.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            public void handle(MouseEvent e)
            {
                if(playButton.getImage()==Play)
                {
                    mediaPlayers[i].stop();
                    playButton.setImage(Stop);
                    mediaPlayers[i].play();

                }
                else
                {
                    playButton.setImage(Play);
                    mediaPlayers[i].stop();
                }
            }
        });
        beforeButton.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            public  void handle(MouseEvent event)
            {
                if(i>0)
                {
                    mediaPlayers[i].stop();
                    i--;
                    mediaPlayers[i].play();
                }

                else
                {
                    mediaPlayers[i].stop();
                    i=music.length-1;
                    mediaPlayers[i].play();
                }
            }
        });
        afterButton.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            public  void handle(MouseEvent event)
            {
                if(i<music.length-1)
                {
                    mediaPlayers[i].stop();
                    i++;
                    mediaPlayers[i].play();
                }
                else
                {
                    mediaPlayers[i].stop();
                    i=0;
                    mediaPlayers[i].play();;
                }
            }
        });
        stage.setOnHidden(new  EventHandler<WindowEvent>() {
            public void handle(WindowEvent event) {
                mediaPlayers[i].stop();
            }
        });
        g.getChildren().add(playButton);
        g.getChildren().add(beforeButton);
        g.getChildren().add(afterButton);
        stage.setTitle("Media");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}