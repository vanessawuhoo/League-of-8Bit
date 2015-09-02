import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Walk {
	public static final int KEY_INPUT_SPEED = 7;
	private static int BOUNCER_SPEED = 130;
    public static final int FRAMES_PER_SECOND = 60;
    private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    private static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    private int height = 256, width = 640;
    
    
	private Scene myScene;
	private ImageView myChar;
	private ImageView myEnemy;

	public ImageView importPics(String title){
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(title));
		ImageView myIV = new ImageView(image);
		return myIV;
	}
	
	public Scene init(Stage stage, Timeline timeline, int width, int height){
		Group root = new Group();
		myScene = new Scene(root, width, height, Color.GREEN);
		myChar = importPics("Ahrisprite.gif");
		myEnemy = importPics("minion.png");
		ImageView myBG = importPics("forest.png");
		myChar.setX(0);
		myChar.setY(height / 4 - myChar.getBoundsInLocal().getHeight() / 2);
		myEnemy.setX(width / 2 + myEnemy.getBoundsInLocal().getWidth() / 2);
		myEnemy.setY(height / 2 + myEnemy.getBoundsInLocal().getHeight() / 2);
		root.getChildren().add(myBG);
		root.getChildren().add(myChar);
		root.getChildren().add(myEnemy);
		myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
		return myScene;
	}

	public void step(Stage stage, Timeline timeline, double elapsedTime) {
		if (myEnemy.getY() >= 215) {
			BOUNCER_SPEED *= -1;
		} else if (myEnemy.getY() <= 0) {
			BOUNCER_SPEED *= -1;
		}
		myEnemy.setY(myEnemy.getY() + BOUNCER_SPEED * elapsedTime);

		if (myChar.getBoundsInParent().intersects(myEnemy.getBoundsInParent())) {
			timeline.stop();
			Timeline newTimeline = new Timeline();
			Battle battle = new Battle();
			stage.setScene(battle.init(stage, newTimeline, width, height));
	        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY),
	                e -> battle.step(stage, timeline, SECOND_DELAY));
	        newTimeline.setCycleCount(Timeline.INDEFINITE);
	        newTimeline.getKeyFrames().addAll(frame);
	        newTimeline.play(); 
		}
		if (myChar.getX()>width){
			goToVictory(stage, timeline);
		}
	}
	
	private void goToVictory(Stage stage, Timeline timeline){
		Victory victory = new Victory();
		Scene myWin = victory.init(stage, timeline, width, height);
		stage.setScene(myWin);
	}
	private void handleKeyInput(KeyCode code) {
		switch (code) {
		case SPACE: 
			myChar.setX(myEnemy.getX());
			myChar.setY(myEnemy.getY());
			break;
		case RIGHT:
			myChar.setX(myChar.getX() + KEY_INPUT_SPEED);
			break;
		case LEFT:
			if (myChar.getX()>=5){
				myChar.setX(myChar.getX() - KEY_INPUT_SPEED);
			}
			break;
		case UP:
			if (myChar.getY()>= 0) {
				myChar.setY(myChar.getY() - KEY_INPUT_SPEED);	
			}
			break;
		case DOWN:
			if (myChar.getY()<= (height-myChar.getBoundsInLocal().getHeight())){
				myChar.setY(myChar.getY() + KEY_INPUT_SPEED);
			}
			break;
		default:
			// do nothing
		}
	}
}
