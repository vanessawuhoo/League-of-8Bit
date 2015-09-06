// This entire file is part of my masterpiece.
// Vanessa Wu

import java.util.Random;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

//@author Vanessa Wu
//used to initialize a fight with an enemy champion in the game
//depends on both Defeat and Victory classes
//used by calling an instance and initializing it with .init()

public class Battle {
	private Group root;
	private Scene myBattle;
	private ImageView myBackground, ahri, enemyChampion, myAttack, eAttack;
	private Button orb, charm, fire, spirit;
	private double aH = 10, eH = 10;
	private static int BOUNCER_SPEED = 15, PROJECTILE_SPEED = 400;
	private ProgressBar ahriHealth, enemyHealth;
	public static final int FRAMES_PER_SECOND = 60;
	private boolean myTurn = true, enemyTurn = false;
	private int height = 256, width = 640, delay = 0;
	
	//initializes and returns the Scene for the Battle screen, will fail if 
	//bg image cannot be found
	public Scene init(Stage stage, Timeline timeline, int width, int height) {
		root = new Group();
		myBattle = new Scene(root, width, height, Color.GREEN);
		myAttack = new ImageView();
		eAttack = new ImageView();
		myBackground = importPics("battle_forest.jpg");
		ahri = importPics("ahri.png");
		enemyChampion = importPics("Cait.png");
		ahri.setX(width / 4 - ahri.getBoundsInLocal().getWidth() / 2);
		ahri.setY(height / 1.5 - ahri.getBoundsInLocal().getHeight() / 2);
		enemyChampion.setX(width / 4 * 3 - enemyChampion.getBoundsInLocal().getWidth() / 2);
		enemyChampion.setY(height / 1.5 - enemyChampion.getBoundsInLocal().getHeight() / 2);
		root.getChildren().add(myBackground);
		root.getChildren().add(ahri);
		root.getChildren().add(enemyChampion);
		orb = new Button("Orb of Deception");
		charm = new Button("Charm");
		fire = new Button("Foxfire");
		spirit = new Button("Spirit Rush");
		charm.setLayoutX(width / 5 * 3);
		orb.setLayoutX(width / 5 * .5);
		fire.setLayoutX(width / 5 * 2);
		spirit.setLayoutX(width / 5 * 4);
		styleButton(orb);
		styleButton(charm);
		styleButton(fire);
		styleButton(spirit);
		ahriHealth = healthInitialize(aH);
		enemyHealth = healthInitialize(eH);
		ahriHealth.setLayoutX(width / 6);
		ahriHealth.setLayoutY(height / 5 * 4.35);
		enemyHealth.setLayoutX(width / 6 * 4);
		enemyHealth.setLayoutY(height / 5 * 4.35);
		myBattle.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
		return myBattle;
	}

	//helper method to quickly initialize new ImageViews and return them
	public ImageView importPics(String title) {
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(title));
		ImageView myIV = new ImageView(image);
		return myIV;
	}

	//helper method to initialize ProgressBars, format them, add them to the
	//screen, and return them
	public ProgressBar healthInitialize(double health) {
		ProgressBar progress = new ProgressBar();
		progress.setProgress(health / 10);
		progress.setStyle("-fx-accent: red;");
		root.getChildren().add(progress);
		return progress;
	}

	//helper method to style buttons quickly and add them to the screen
	public void styleButton(Button button) {
		button.setLayoutY(height / 4);
		button.setStyle("-fx-background-color:#000000," + "linear-gradient(#7ebcea, #2f4b8f),"
				+ "linear-gradient(#426ab7, #263e75)," + "linear-gradient(#395cab, #223768);"
				+ "-fx-background-insets: 0,1,2,3;" + "-fx-background-radius: 3,2,2,2;" + "-fx-padding: 7 20 7 20;"
				+ "-fx-text-fill: white;" + "-fx-font-size: 10px;");
		root.getChildren().add(button);
	}

	//helper method that determines what discrete attacks do in terms of damage
	//and triggers attack animations
	public void myAttacks(String type) {
		if (type.equals("ORB")) {
			eH -= 2;
			enemyHealth.setProgress(eH / 10);
			attackAnimation("orb.png");
		}
		if (type.equals("CHARM")) {
			if (aH < 10) {
				aH += 2;
				ahriHealth.setProgress(aH / 10);
				attackAnimation("charm.png");
			}
		}
		if (type.equals("FIRE")) {
			eH -= 1;
			enemyHealth.setProgress(eH / 10);
			attackAnimation("foxfire.png");
		}
		if (type.equals("SPIRIT")) {
			eH -= 3;
			enemyHealth.setProgress(eH / 10);
			attackAnimation("spirit.png");
		}
		switcher();
	}

	//helper method that initializes attack animations and adds them to the scene
	public void attackAnimation(String img) {
		myAttack = importPics(img);
		myAttack.setX(ahri.getX());
		myAttack.setY(ahri.getY());
		root.getChildren().add(myAttack);
	}

	//helper method that initializes enemy attack animations and adds them to the scene
	public void enemyAttack(String img) {
		eAttack = importPics(img);
		eAttack.setX(enemyChampion.getX());
		eAttack.setY(enemyChampion.getY());
		root.getChildren().add(eAttack);
	}

	//helper method that determines whether to continue the battle or not
	//will redirect to game over or victory screens depending on whether the
	//player wins or loses. 
	public void cont(Stage stage, Timeline timeline) {
		myBattle.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
		if (aH > 0 && eH > 0) {
			if (myTurn) {
				buttonPress();
			} else {
				enemyTurn = true;
				if (delay > 90) {
					enemyAttack();
					switcher();
					delay = 0;
					enemyTurn = false;
				}
			}
		} else if (aH <= 0) {
			Defeat defeat = new Defeat();
			Scene myGO = defeat.init(stage, width, height);
			stage.setScene(myGO);
		} else if (eH <= 0) {
			Victory victory = new Victory();
			Scene myWin = victory.init(stage, timeline, width, height);
			stage.setScene(myWin);
		}
	}

	//helper method that determines what happens when the enemy attacks based on
	//randomly generated integers
	public void enemyAttack() {
		Random random = new Random();
		int randomInt = random.nextInt((10) + 1);
		if (randomInt <= 5) {
			aH -= 1;
			enemyAttack("trap.png");
			ahriHealth.setProgress(aH / 10);
		} else if (randomInt < 9) {
			aH -= 2;
			enemyAttack("net.png");
			ahriHealth.setProgress(aH / 10);
		} else {
			aH -= 3;
			enemyAttack("ace.png");
			ahriHealth.setProgress(aH / 10);
		}
	}

	//method that sets up the animation for the scene
	public void step(Stage stage, Timeline timeline, double elapsedTime) {
		if (ahri.getY() >= 140) {
			BOUNCER_SPEED *= -1;
		} else if (ahri.getY() <= 130) {
			BOUNCER_SPEED *= -1;
		}
		ahri.setY(ahri.getY() + BOUNCER_SPEED * elapsedTime);
		enemyChampion.setY(enemyChampion.getY() - BOUNCER_SPEED * elapsedTime);
		myAttack.setX(myAttack.getX() + PROJECTILE_SPEED * elapsedTime);
		eAttack.setX(eAttack.getX() - PROJECTILE_SPEED * elapsedTime);
		cont(stage, timeline);
		if (enemyTurn) {
			delay++;
		}
	}
	
	//helper method that routes what happens when the attack buttons are pressed
	public void buttonPress() {
		orb.setOnMouseClicked(e -> myAttacks("ORB"));
		charm.setOnMouseClicked(e -> myAttacks("CHARM"));
		fire.setOnMouseClicked(e -> myAttacks("FIRE"));
		spirit.setOnMouseClicked(e -> myAttacks("SPIRIT"));
	}
	
	//method that handles user key input
	private void handleKeyInput(KeyCode code) {
		switch (code) {
		case ENTER:
			eH = 1;
			enemyHealth.setProgress(eH / 10);
			break;
		default:
		}
	}

	//method that changes whose turn it is
	public void switcher() {
		myTurn = !myTurn;
	}

}


