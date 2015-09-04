import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

//@author Vanessa Wu
//Used to indicate that a player has lost the game
//Does not depend on a class
//used by calling an instance and initializing it with .init()

public class Defeat {
	private Scene myGO;
	private ImageView myBG;
    public static final int FRAMES_PER_SECOND = 60;
    
    //used to set up the game over screen, will fail if lose.png cannot be found
    //returns a Scene
	public Scene init(Stage stage, int width, int height) {
		Group root = new Group();
		myGO = new Scene(root, width, height, Color.GREEN);
		Image bg = new Image(getClass().getClassLoader().getResourceAsStream("lose.png"));
		myBG = new ImageView(bg);
		root.getChildren().add(myBG);
		myBG.setOnMouseClicked(e -> stage.close());
		return myGO;
	}
	

}
