package game;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
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

import java.io.File;

public class MainMenuController {
	@FXML private Label startLabel;
	@FXML private Rectangle startBtn;
	@FXML private Label settingLabel;
	@FXML private Rectangle settingBtn;
	@FXML private Label quitLabel;
	@FXML private Rectangle quitBtn;

	public static Stage stage2;
	public static Stage self;
	public static boolean started= false;

	private File musicFile = new File("audio/BGM/2019-01-02_-_8_Bit_Menu_-_David_Renda_-_FesliyanStudios.com.mp3");
	private Media music;
	public static MediaPlayer musicPlayer;

	private File selectionFile = new File("audio/FX/sfx_menu_move2.wav");
	private Media selection= new Media(selectionFile.toURI().toString());
	public static MediaPlayer selectionPlayer;
	public static MediaPlayer selectionPlayer2;
	private File enterFile = new File("audio/FX/sfx_menu_select2.wav");
	private Media enter= new Media(enterFile.toURI().toString());
	public static MediaPlayer enterPlayer;


	public static double volume= 1;

	public void initialize() {
//		GameController.bgmPlayer.stop();
		music= new Media(musicFile.toURI().toString());
		musicPlayer= new MediaPlayer(music);
		musicPlayer.setVolume(volume);
		musicPlayer.setAutoPlay(true);
		musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);

		selectionPlayer= new MediaPlayer(selection);
		selectionPlayer.setVolume(GameController.fxVolume);
		selectionPlayer2= new MediaPlayer(selection);
		selectionPlayer2.setVolume(GameController.fxVolume);
		enterPlayer= new MediaPlayer(enter);
		enterPlayer.setVolume(GameController.fxVolume);

		stage2= new Stage();
	}

	@FXML
	private void startClicked(MouseEvent e) throws Exception {
		enterPlayer.seek(Duration.ZERO);
		enterPlayer.play();
		Parent root =
				FXMLLoader.load(getClass().getResource("MainGame.fxml"));

		Scene scene = new Scene(root);

		self = (Stage) startBtn.getScene().getWindow();

		stage2.setTitle("GameWindow");
		stage2.setScene(scene);
		stage2.setOnCloseRequest(we -> {
			System.out.println("Game is closed");
			GameController.bgmPlayer.stop();
			musicPlayer.play();
			musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
			self.show();
		});
		musicPlayer.stop();
		started=true;
		stage2.show();
		self.hide();

	}

	@FXML
	private void settingClicked(MouseEvent e) throws Exception {
		enterPlayer.seek(Duration.ZERO);
		enterPlayer.play();
		Parent root =
				FXMLLoader.load(getClass().getResource("SettingsPage.fxml"));

		Scene scene = new Scene(root);
		Stage stage= new Stage();
		stage.setTitle("Settings");
		stage.setScene(scene);
		stage.setOnCloseRequest(we -> {
			System.out.println("Setting cancelled");
			musicPlayer.setVolume(SettingsController.defaultVol);
		});
		stage.show();
	}

	@FXML
	private void quitClicked(MouseEvent e){
		enterPlayer.seek(Duration.ZERO);
		enterPlayer.play();
		Platform.exit();
		System.exit(0);
	}

	@FXML
	private void startEntered(MouseEvent e){

		startLabel.setLayoutX(startLabel.getLayoutX()-20);
		startBtn.setLayoutX(startBtn.getLayoutX()-20);

		selectionPlayer2.seek(Duration.ZERO);
		selectionPlayer.play();
	}

	@FXML
	private void startExited(MouseEvent e){
		startLabel.setLayoutX(startLabel.getLayoutX()+20);
		startBtn.setLayoutX(startBtn.getLayoutX()+20);
	}

	@FXML
	private void settingEntered(MouseEvent e){

		settingLabel.setLayoutX(settingLabel.getLayoutX()-10);
		settingBtn.setLayoutX(settingBtn.getLayoutX()-10);

		selectionPlayer2.seek(Duration.ZERO);
		selectionPlayer2.play();
	}

	@FXML
	private void settingExited(MouseEvent e){
		settingLabel.setLayoutX(settingLabel.getLayoutX()+10);
		settingBtn.setLayoutX(settingBtn.getLayoutX()+10);
	}

	@FXML
	private void quitEntered(MouseEvent e){

		quitLabel.setLayoutX(quitLabel.getLayoutX()-10);
		quitBtn.setLayoutX(quitBtn.getLayoutX()-10);

		selectionPlayer.seek(Duration.ZERO);
		selectionPlayer.play();
	}
	@FXML
	private void quitExited(MouseEvent e){
		quitLabel.setLayoutX(quitLabel.getLayoutX()+10);
		quitBtn.setLayoutX(quitBtn.getLayoutX()+10);
	}
}
