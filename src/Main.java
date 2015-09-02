import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private Splash splash;
    private int height = 256, width = 640;
    public static final int FRAMES_PER_SECOND = 60;
    
	public void start (Stage stage) {
        splash = new Splash();
        Timeline animation = new Timeline();
        stage.setTitle(splash.getTitle());

        Scene splashScreen = splash.init(stage, animation, width, height);
        stage.setScene(splashScreen);
        stage.show();
    }
	
    public static void main (String[] args) {
        launch(args);
    }
}
