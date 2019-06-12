package game;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
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

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class SettingsController {
	@FXML private Slider bgmSlider;
	@FXML private Slider fxSlider;
	@FXML private ComboBox fpsSelect;
	@FXML private Button okBtn;
	@FXML private Label fxLabel;
	@FXML private ImageView muteIcon;

	private double volume= 1;
	private double oldVolume= 1;
	private double fxVolume= 1;
	private int fps=120;
	private boolean muted= false;
	private Image unmutedImg= new Image("image/86932.png");
	private Image mutedImg= new Image("image/86932_mute.png");

	public void initialize() {
		bgmSlider.setValue(GameController.volume*100);
		fxSlider.setValue(GameController.fxVolume*100);

		bgmSlider.valueProperty().addListener(
				(ov, oldValue, newValue) -> {
					int volPercent = newValue.intValue();
					volume= volPercent/100.0;
					MainMenuController.musicPlayer.setVolume(volume);
				}
		);
		fxSlider.valueProperty().addListener(
				(ov, oldValue, newValue) -> {
					int fxVolPercent = newValue.intValue();
					fxVolume= fxVolPercent/100.0;
				}
		);

		fpsSelect.getItems().addAll("High","Normal","Low");
		switch (GameController.fps){
			case 120:
				fpsSelect.setValue("High");
				break;
			case 20:
				fpsSelect.setValue("Normal");
				break;
			case 5:
				fpsSelect.setValue("Low");
				break;
			default:
				fpsSelect.setValue("High");
				break;
		}


	}

	@FXML
	private void fpsOnAction(ActionEvent e){
		String selected= fpsSelect.getSelectionModel().getSelectedItem().toString();
		switch (selected) {
			case "High":
				fps = 120;
				break;
			case "Normal":
				fps = 20;
				break;
			case "Low":
				fps = 5;
				break;
		}

	}

	@FXML
	private void fxClicked(MouseEvent e){
		File fxFile = new File("audio/FX/sfx_movement_footsteps5.wav");
		Media fx= new Media(fxFile.toURI().toString());
		MediaPlayer fxPlayer= new MediaPlayer(fx);
		fxPlayer.setVolume(fxVolume);
		fxPlayer.play();
	}
	@FXML
	private void fxEntered(MouseEvent e){

		fxLabel.setFont(new Font("Arial Bold",38));
	}
	@FXML
	private void fxExited(MouseEvent e){
		fxLabel.setFont(new Font("Arial Bold",32));
	}

	@FXML
	private void muteClicked(MouseEvent e){

		if(muted){
			muteIcon.setImage(unmutedImg);
			bgmSlider.setValue(oldVolume*100);
			muted= !muted;
		}
		else {
			muteIcon.setImage(mutedImg);
			oldVolume= volume;
			bgmSlider.setValue(0);
			muted= !muted;
		}
	}
	@FXML
	private void muteEntered(MouseEvent e){

		muteIcon.setFitHeight(60);
		muteIcon.setFitWidth(60);
	}
	@FXML
	private void muteExited(MouseEvent e){
		muteIcon.setFitHeight(50);
		muteIcon.setFitWidth(50);
	}

	@FXML
	private void okPressed(ActionEvent e){
		GameController.volume= volume;
		GameController.fxVolume= fxVolume;
		GameController.fps= fps;
		Stage stage = (Stage) okBtn.getScene().getWindow();
		stage.close();
	}


}
