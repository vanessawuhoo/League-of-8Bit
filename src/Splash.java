import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

//@author Vanessa Wu
//Used to hide loading of resources and introduce game title
//Depends on Instructions class
//used by calling an instance and initializing it with .init()

public class Splash {
	private Scene mySplash;
	private ImageView myBackground;
	private Instructions instructions;
	public static final String TITLE = "League of 8-Bit";
    public static final int FRAMES_PER_SECOND = 60;
    private int height = 256, width = 640;

    //returns the String title of the program 
	public String getTitle() {
		return TITLE;
	}
    
	//initializes and returns the Scene for the splash screen, will fail if 
	//bg image cannot be found
	public Scene init(Stage stage, Timeline timeline, int width, int height) {
		Group root = new Group();
		mySplash = new Scene(root, width, height, Color.GREEN);
		Image bg = new Image(getClass().getClassLoader().getResourceAsStream("splash.png"));
		myBackground = new ImageView(bg);
		root.getChildren().add(myBackground);
		goToInfo(stage,timeline);
		return mySplash;
	}
	
	//initializes the instruction scene and directs user to it upon clicking
	public void goToInfo(Stage stage, Timeline timeline) {
		instructions = new Instructions();
		Scene info = instructions.init(stage, timeline, width, height);
		mySplash.setOnMouseClicked(e -> stage.setScene(info));
	}

}
