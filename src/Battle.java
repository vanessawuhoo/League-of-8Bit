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

public class Battle {
	private Group root;
	private Scene myBattle;
	private ImageView myBG, ahri, eChampion, myAttack, eAttack;
	private Button orb, charm, fire, spirit;
	private double aH = 10, eH = 10;
	private static int BOUNCER_SPEED = 15, PROJECTILE_SPEED = 400;
	private ProgressBar aHealth, eHealth;
	public static final int FRAMES_PER_SECOND = 60;
	private boolean myTurn = true, eTurn = false;
	private int height = 256, width = 640, delay = 0;
	
	public ImageView importPics(String title){
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(title));
		ImageView myIV = new ImageView(image);
		return myIV;
	}
	
	public Scene init(Stage stage, Timeline t, int width, int height) {
		root = new Group();
		myBattle = new Scene(root, width, height, Color.GREEN);
		myAttack = new ImageView();
		eAttack = new ImageView();
		myBG = importPics("battle_forest.jpg");
		ahri = importPics("ahri.png");
		eChampion = importPics("Cait.png");
		ahri.setX(width / 4 - ahri.getBoundsInLocal().getWidth() / 2);
		ahri.setY(height / 1.5 - ahri.getBoundsInLocal().getHeight() / 2);
		eChampion.setX(width / 4 * 3 - eChampion.getBoundsInLocal().getWidth() / 2);
		eChampion.setY(height / 1.5 - eChampion.getBoundsInLocal().getHeight() / 2);
		root.getChildren().add(myBG);
		root.getChildren().add(ahri);
		root.getChildren().add(eChampion);
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
		aHealth = healthInitialize(aH);
		eHealth = healthInitialize(eH);
		aHealth.setLayoutX(width / 6);
		aHealth.setLayoutY(height / 5 * 4.35);
		eHealth.setLayoutX(width / 6 * 4);
		eHealth.setLayoutY(height / 5 * 4.35);
		myBattle.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
		return myBattle;
	}
	
	
	public ProgressBar healthInitialize(double health) {
		ProgressBar progress = new ProgressBar();
		progress.setProgress(health/10);
		progress.setStyle("-fx-accent: red;");
		root.getChildren().add(progress);
		return progress; 
	}

	public void styleButton(Button  button) {
		button.setLayoutY(height / 4);
		button.setStyle("-fx-background-color:#000000," + "linear-gradient(#7ebcea, #2f4b8f),"
				+ "linear-gradient(#426ab7, #263e75)," + "linear-gradient(#395cab, #223768);"
				+ "-fx-background-insets: 0,1,2,3;" + "-fx-background-radius: 3,2,2,2;" + "-fx-padding: 7 20 7 20;"
				+ "-fx-text-fill: white;" + "-fx-font-size: 10px;");
		root.getChildren().add(button);
	}

	public void myAttacks(String type) {
		if (type.equals("ORB")) {
			eH -= 2;
			eHealth.setProgress(eH / 10);
			attackAnimation("orb.png");
		}
		if (type.equals("CHARM")) {
			if (aH < 10) {
				aH += 2;
				aHealth.setProgress(aH / 10);
				attackAnimation("charm.png");
			}
		}
		if (type.equals("FIRE")) {
			eH -= 1;
			eHealth.setProgress(eH / 10);
			attackAnimation("foxfire.png");
		}
		if (type.equals("SPIRIT")) {
			eH -= 3;
			eHealth.setProgress(eH / 10);
			attackAnimation("spirit.png");
		}
		switcher();
	}
	
	public void attackAnimation(String img) {
			myAttack = importPics(img);
			myAttack.setX(ahri.getX());
			myAttack.setY(ahri.getY());
			root.getChildren().add(myAttack);
	}
	
	public void enemyAttack(String img){
		eAttack = importPics(img);
		eAttack.setX(eChampion.getX());
		eAttack.setY(eChampion.getY());
		root.getChildren().add(eAttack);
	}

	public void cont(Stage stage, Timeline timeline) {
		myBattle.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
		if (aH > 0 && eH > 0) {
			if (myTurn) {
				buttonPress();
			} else {
				eTurn = true;
				if (delay > 90){
					enemyAttack();
					switcher();	
					delay = 0;
					eTurn = false;
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

	public void enemyAttack() {
		Random random = new Random();
		int randomInt = random.nextInt((10) + 1);
		if (randomInt <= 5) {
			aH -= 1;
			enemyAttack("trap.png");
			aHealth.setProgress(aH / 10);			
		} else if (randomInt < 9 ) {
			aH -= 2;
			enemyAttack("net.png");
			aHealth.setProgress(aH / 10);
		} else {
			aH -= 3;
			enemyAttack("ace.png");
			aHealth.setProgress(aH / 10);	
		}
		
	}

	public void step(Stage stage, Timeline timeline, double elapsedTime) {
		if (ahri.getY() >= 140) {
			BOUNCER_SPEED *= -1;
		} else if (ahri.getY() <= 130) {
			BOUNCER_SPEED *= -1;
		}
		ahri.setY(ahri.getY() + BOUNCER_SPEED * elapsedTime);
		eChampion.setY(eChampion.getY() - BOUNCER_SPEED * elapsedTime);
		myAttack.setX(myAttack.getX()+PROJECTILE_SPEED * elapsedTime);
		eAttack.setX(eAttack.getX()-PROJECTILE_SPEED*elapsedTime);
		cont(stage, timeline);
		if (eTurn){
			delay++;
		}
	}
	 
	public void buttonPress() {
		orb.setOnMouseClicked(e -> myAttacks("ORB"));
		charm.setOnMouseClicked(e -> myAttacks("CHARM"));
		fire.setOnMouseClicked(e -> myAttacks("FIRE"));
		spirit.setOnMouseClicked(e -> myAttacks("SPIRIT"));
	}

	private void handleKeyInput(KeyCode code) {
		switch (code) {
		case ENTER:
			eH = 1;
			eHealth.setProgress(eH / 10);
			break;
		default:
		}
	}
	
	public void switcher() {
		myTurn = !myTurn;
	}

}
