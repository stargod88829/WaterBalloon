package game;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class ResultController extends GameController{
	@FXML private Label scoreLabel;
	@FXML private Label hsLabel;
	@FXML private ImageView imgView;
	@FXML private Button okBtn;

//	public static Stage stage2;
//	public static Stage self;

	private String hsDataStr;
	private List<Integer> hsList = new ArrayList<Integer>();
	private int highScore=0;


	private File enterFile = new File("audio/FX/sfx_menu_select2.wav");
	private Media enter= new Media(enterFile.toURI().toString());
	public static MediaPlayer enterPlayer;



	public void initialize() {
		enterPlayer= new MediaPlayer(enter);
		enterPlayer.setVolume(GameController.fxVolume);
		hsList.add(0);
//		stage2= new Stage();
		scoreLabel.setText("You've gained :"+(Player.lifeLeft*10+Player.countItem)+" points");
		imgView.setImage(new Image("image/batFox.png"));

		hsDataStr= readFile("game/HS.txt");
		Scanner hsScanner= new Scanner(hsDataStr);
		while(hsScanner.hasNext()){
			hsList.add(hsScanner.nextInt());
		}
		highScore= Collections.max(hsList);

		if((Player.lifeLeft*10+Player.countItem) > highScore){
			imgView.setImage(new Image("image/high-score-icon-24.png"));
			hsLabel.setText("New High Score !");
		}
		else{
			imgView.setImage(new Image("image/batFox.png"));
			hsLabel.setText("High Score= "+highScore);
		}

		try {
			Files.write(Paths.get("game/HS.txt"),
					(""+(Player.lifeLeft*10+Player.countItem)+" ").getBytes(), StandardOpenOption.APPEND);
		}
		catch (IOException e) {
		}


	}

	@FXML
	private void okClicked(ActionEvent e) {
		enterPlayer.seek(Duration.ZERO);
		enterPlayer.play();

		Stage self = (Stage) okBtn.getScene().getWindow();
		MainMenuController.musicPlayer.play();
		MainMenuController.musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		MainMenuController.self.show();
		self.close();
	}
}
