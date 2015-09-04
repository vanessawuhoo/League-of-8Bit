import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

//@author Vanessa Wu
//Used to indicate player has won game
//Does not depend on any class
//used by calling an instance and initializing it with .init()

public class Victory {
	private Scene myWin;
	private ImageView myBG;
    public static final int FRAMES_PER_SECOND = 60;
    
    
    //used to set up the winning screen, will fail if graphic cannot be found
    //returns a Scene
	public Scene init(Stage stage, Timeline timeline, int width, int height) {
		Group root = new Group();
		myWin = new Scene(root, width, height, Color.GREEN);
		Image bg = new Image(getClass().getClassLoader().getResourceAsStream("victory.png"));
		myBG = new ImageView(bg);
		root.getChildren().add(myBG);
		myBG.setOnMouseClicked(e -> stage.close());
		return myWin;
	}
}
