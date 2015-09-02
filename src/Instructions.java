import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Instructions {
	private Scene myInfo;
	private Walk walk;
	private ImageView myBG;
	public static final int FRAMES_PER_SECOND = 60;
	private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
	private static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    private int height = 256, width = 640;
    
	public Scene init(Stage stage, Timeline timeline, int width, int height) {
		// Create a scene graph to organize the scene
		Group root = new Group();
		// Create a place to see the shapes
		myInfo = new Scene(root, width, height, Color.GREEN);
		// Make some shapes and set their properties
		Image bg = new Image(getClass().getClassLoader().getResourceAsStream("instructions.png"));
		myBG = new ImageView(bg);
		root.getChildren().add(myBG);
		goToMap(stage, timeline);
		return myInfo;
		
	}
	public void goToMap(Stage stage, Timeline timeline) {
		walk = new Walk();
		Scene walkScene = walk.init(stage, timeline, width, height);
		myInfo.setOnMouseClicked(e -> stage.setScene(walkScene));
		Timeline newTimeline = new Timeline();
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY),
                e -> walk.step(stage, newTimeline, SECOND_DELAY));
        newTimeline.setCycleCount(Timeline.INDEFINITE);
        newTimeline.getKeyFrames().addAll(frame);
        newTimeline.play(); 
	}
	
	

}
