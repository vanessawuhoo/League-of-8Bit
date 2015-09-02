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
	private Scene myBattle;
	private ImageView myBG, ahri, eChampion;
	private Button orb, charm, fire, spirit;
	private double aH, eH;
	private static int BOUNCER_SPEED = 15;
	private ProgressBar aHealth, eHealth;
	public static final int FRAMES_PER_SECOND = 60;
	private boolean myTurn = true;
	private Group root;
    private int height = 256, width = 640;

	
	public ImageView importPics(String title){
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(title));
		ImageView myIV = new ImageView(image);
		return myIV;
	}
	public Scene init(Stage stage, Timeline t, int width, int height) {
		root = new Group();
		myBattle = new Scene(root, width, height, Color.GREEN);
		myBG = importPics("battle_forest.jpg");
		ahri = importPics("ahri.png");
		root.getChildren().add(myBG);
		root.getChildren().add(ahri);
		ahri.setX(width / 4 - ahri.getBoundsInLocal().getWidth() / 2);
		ahri.setY(height / 1.5 - ahri.getBoundsInLocal().getHeight() / 2);
	    eChampion = importPics("Cait.png");
		root.getChildren().add(eChampion);
		eChampion.setX(width / 4 * 3 - eChampion.getBoundsInLocal().getWidth() / 2);
		eChampion.setY(height / 1.5 - eChampion.getBoundsInLocal().getHeight() / 2);
		orb = new Button("Orb of Deception");
		orb.setLayoutX(width / 5 * .5);
		charm = new Button("Charm");
		charm.setLayoutX(width / 5 * 3);
		fire = new Button("Foxfire");
		fire.setLayoutX(width / 5 * 2);
		spirit = new Button("Spirit Rush");
		spirit.setLayoutX(width / 5 * 4);
		styleSet(orb);
		styleSet(charm);
		styleSet(fire);
		styleSet(spirit);
		root.getChildren().add(orb);
		root.getChildren().add(charm);
		root.getChildren().add(fire);
		root.getChildren().add(spirit);
		// set health bars
		aH = 10;
		eH = 10;
		aHealth = new ProgressBar();
		eHealth = new ProgressBar();
		aHealth.setProgress(aH / 10);
		eHealth.setProgress(eH / 10);
		aHealth.setLayoutX(width / 6);
		aHealth.setLayoutY(height / 5 * 4.35);
		eHealth.setLayoutX(width / 6 * 4);
		eHealth.setLayoutY(height / 5 * 4.35);
		aHealth.setStyle("-fx-accent: red;");
		eHealth.setStyle("-fx-accent: red;");
		root.getChildren().add(aHealth);
		root.getChildren().add(eHealth);
//        myBattle.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
		return myBattle;
	}
	
	public void styleSet(Button b){
		b.setLayoutY(height / 4);
		b.setStyle("-fx-background-color:#000000,"
				+ "linear-gradient(#7ebcea, #2f4b8f),"
				+ "linear-gradient(#426ab7, #263e75),"
				+ "linear-gradient(#395cab, #223768);"
				+ "-fx-background-insets: 0,1,2,3;"
				+ "-fx-background-radius: 3,2,2,2;"
				+ "-fx-padding: 7 20 7 20;"
				+ "-fx-text-fill: white;"
				+ "-fx-font-size: 10px;");
	}
	
	public void attacks(String type){
		if (type.equals("ORB")) {
			eH -= 2;
			eHealth.setProgress(eH / 10);
			switcher();
		}
		if (type.equals("CHARM")){
			if (aH < 10) {
				aH += 2;
				aHealth.setProgress(aH / 10);
				switcher();
			}};
			
		if (type.equals("FIRE")){
			eH -= 1;
			eHealth.setProgress(eH / 10);
			switcher();
		} 
		if (type.equals("SPIRIT")) {
			eH -= 3;
			eHealth.setProgress(eH / 10);
			switcher();
		}
		}
	
	public void buttonPress() {
			orb.setOnMouseClicked(e -> attacks("ORB"));
			charm.setOnMouseClicked(e -> attacks("CHARM"));
			fire.setOnMouseClicked(e -> attacks("FIRE"));
			spirit.setOnMouseClicked(e -> attacks("SPIRIT"));
		}
		
    private void handleKeyInput (KeyCode code) {
        switch (code) {
            case ENTER:
            	eH = 0;
            	break;
            default:
        }
    }
	public void cont(Stage stage, Timeline timeline) {
		myBattle.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
		if (aH > 0 && eH > 0) {
			if (myTurn) {
				buttonPress();
			} else {
				enemyAttack();
				switcher();
			}
		}
		else if (aH <= 0) {
			Defeat defeat = new Defeat();
			Scene myGO = defeat.init(stage, width, height);
			stage.setScene(myGO);
		}
		else if (eH <= 0) {
				Victory victory = new Victory();
				Scene myWin = victory.init(stage, timeline, width, height);
				stage.setScene(myWin);
		}
	}
	

	
	public void enemyAttack() {
		aH -= 1;
		aHealth.setProgress(aH / 10);
	}

	public void step(Stage stage, Timeline timeline, double elapsedTime) {
		if (ahri.getY() >= 140) {
			BOUNCER_SPEED *= -1;
		} else if (ahri.getY() <= 130) {
			BOUNCER_SPEED *= -1;
		}
		ahri.setY(ahri.getY() + BOUNCER_SPEED * elapsedTime);
		eChampion.setY(eChampion.getY() - BOUNCER_SPEED * elapsedTime);
		cont(stage, timeline);
	}
	
	public void switcher(){
		myTurn= !myTurn;
	}

}
