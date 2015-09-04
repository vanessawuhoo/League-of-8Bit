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

// @author Vanessa Wu
//Controls the walking around portion of the game 
//Depends on Battle and Victory class
//used by calling an instance and initializing it with .init()

public class Walk {
	public static final int KEY_INPUT_SPEED = 7;
	private static int BOUNCER_SPEED = 170;
	public static final int FRAMES_PER_SECOND = 60;
	private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
	private static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
	private int height = 256, width = 640;
	private Scene myScene;
	private ImageView myCharacter;
	private ImageView myEnemy;
	
    //used to set up the walking around screen, will fail if graphic cannot be found
    //returns a Scene
	public Scene init(Stage stage, Timeline timeline, int width, int height) {
		Group root = new Group();
		myScene = new Scene(root, width, height, Color.GREEN);
		myCharacter = importPics("Ahrisprite.gif");
		myEnemy = importPics("minion.png");
		ImageView myBG = importPics("forest.png");
		myCharacter.setX(0);
		myCharacter.setY(height / 4 - myCharacter.getBoundsInLocal().getHeight() / 2);
		myEnemy.setX(width / 2 + myEnemy.getBoundsInLocal().getWidth() / 2);
		myEnemy.setY(height / 2 + myEnemy.getBoundsInLocal().getHeight() / 2);
		root.getChildren().add(myBG);
		root.getChildren().add(myCharacter);
		root.getChildren().add(myEnemy);
		myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
		return myScene;
	}
	
	//helper method to quickly initialize new ImageViews and return them
	public ImageView importPics(String title) {
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(title));
		ImageView myIV = new ImageView(image);
		return myIV;
	}

	//method that sets up the animation for the scene
	public void step(Stage stage, Timeline timeline, double elapsedTime) {
		if (myEnemy.getY() >= 215) {
			BOUNCER_SPEED *= -1;
		} else if (myEnemy.getY() <= 0) {
			BOUNCER_SPEED *= -1;
		}
		myEnemy.setY(myEnemy.getY() + BOUNCER_SPEED * elapsedTime);
		if (myCharacter.getBoundsInParent().intersects(myEnemy.getBoundsInParent())) {
			goToBattle(stage, timeline);
		}
		if (myCharacter.getX() > width) {
			goToVictory(stage, timeline);
		}
	}

	//method that initializes and redirects to a Battle scene
	private void goToBattle(Stage stage, Timeline timeline) {
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

	//method that initializes and redirects to the Victory scene
	private void goToVictory(Stage stage, Timeline timeline) {
		Victory victory = new Victory();
		Scene myWin = victory.init(stage, timeline, width, height);
		stage.setScene(myWin);
	}

	//method that interprets and handles user key input
	private void handleKeyInput(KeyCode code) {
		switch (code) {
		case SPACE:
			myCharacter.setX(myEnemy.getX());
			myCharacter.setY(myEnemy.getY());
			break;
		case RIGHT:
			myCharacter.setX(myCharacter.getX() + KEY_INPUT_SPEED);
			break;
		case LEFT:
			if (myCharacter.getX() >= 5) {
				myCharacter.setX(myCharacter.getX() - KEY_INPUT_SPEED);
			}
			break;
		case UP:
			if (myCharacter.getY() >= 0) {
				myCharacter.setY(myCharacter.getY() - KEY_INPUT_SPEED);
			}
			break;
		case DOWN:
			if (myCharacter.getY() <= (height - myCharacter.getBoundsInLocal().getHeight())) {
				myCharacter.setY(myCharacter.getY() + KEY_INPUT_SPEED);
			}
			break;
		default:
		}
	}
}
