import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Splash {
	private Scene mySplash;
	private ImageView myBG;
	private Instructions instructions;
	public static final String TITLE = "League of 8Bit";
    public static final int FRAMES_PER_SECOND = 60;
    private int height = 256, width = 640;

	public String getTitle() {
		return TITLE;
	}
    
	public Scene init(Stage stage, Timeline timeline, int width, int height) {
		Group root = new Group();
		mySplash = new Scene(root, width, height, Color.GREEN);
		Image bg = new Image(getClass().getClassLoader().getResourceAsStream("splash.png"));
		myBG = new ImageView(bg);
		root.getChildren().add(myBG);
		goToInfo(stage,timeline);
		return mySplash;
	}
	
	public void goToInfo(Stage stage, Timeline timeline) {
		instructions = new Instructions();
		Scene info = instructions.init(stage, timeline, width, height);
		mySplash.setOnMouseClicked(e -> stage.setScene(info));
	}

}
