package sample.java;

import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Controller {
	public Pane mainPane;
	public Circle daCircle;
	public Label statusLabel;
	public Rectangle canNotTouchThis;

	public void doDrag(MouseEvent mouseEvent) {
		Dragboard dragboard = daCircle.startDragAndDrop(TransferMode.MOVE);
		ClipboardContent clipboardContent = new ClipboardContent();
		clipboardContent.putString(daCircle.getId());
		dragboard.setContent(clipboardContent);
		mouseEvent.consume();
	}

	public void acceptDragTo(DragEvent dragEvent) {
		if (dragEvent.getTarget().equals(mainPane) && dragEvent.getDragboard().hasString()) {
			dragEvent.acceptTransferModes(TransferMode.MOVE);
			statusLabel.setText("Dragging...");
		}
	}

	public void signalDroppableEntered(DragEvent dragEvent) {
		if (dragEvent.getTarget().equals(mainPane) && dragEvent.getDragboard().hasString()) {
			mainPane.getStyleClass().add("infocus");
		}
	}

	public void signalDroppableExited(DragEvent dragEvent) {
		if (dragEvent.getTarget().equals(mainPane) && dragEvent.getDragboard().hasString()) {
			mainPane.getStyleClass().clear();
		}
	}

	public void handleDragDropped(DragEvent dragEvent) {
		Dragboard dragboard = dragEvent.getDragboard();
		boolean done = false;
		if (dragboard.hasString()) {
			daCircle.setLayoutY(dragEvent.getY());
			daCircle.setLayoutX(dragEvent.getX());
			done = true;
		}
		dragEvent.setDropCompleted(done);
		dragEvent.consume();
	}

	public void stopDrag(DragEvent dragEvent) {
		if (dragEvent.getTransferMode() == TransferMode.MOVE) {
			statusLabel.setText("Damn dawg, you dragged! You drag queen, you!");
		} else {
			statusLabel.setText("Well, no luck dragging there.");
		}
		dragEvent.consume();
	}

	/**
	 * For this nice keyboard-move to happen, the object (circle) needs to be setFocusTraversable(true).
	 */
	public void doWasd(KeyEvent keyEvent) {
		// Reset color.
		statusLabel.setTextFill(Color.web("#27610b"));

		if (this.checkForCollision()){
			keyEvent.consume();
			System.out.println("Oops, collision!");
			return;
		}

		switch (keyEvent.getCode()) {
			case W:
			case UP:
				statusLabel.setText("Moving up");
				daCircle.setLayoutY(daCircle.getLayoutY() - 1);
				break;

			case A:
			case LEFT:
				statusLabel.setText("Moving left");
				daCircle.setLayoutX(daCircle.getLayoutX() - 1);
				break;

			case S:
			case DOWN:
				statusLabel.setText("Moving down");
				daCircle.setLayoutY(daCircle.getLayoutY() + 1);
				break;

			case D:
			case RIGHT:
				statusLabel.setText("Moving right");
				daCircle.setLayoutX(daCircle.getLayoutX() + 1);
				break;

			default:
				statusLabel.setText("Argh! Don't press that!");
				statusLabel.setTextFill(Color.web("#ff451f"));
				break;
		}
	}

	/**
	 * Check for collision with a pseudo-circle - this is to avoid entering the collision state with the actual
	 * circle object, which would prevent further operability.
	 */
	private boolean checkForCollision() {
		boolean collision = false;

		// @todo: here's the thing - we need to prevent, not detect, so either daCircle or canNotTouchThis must
		// use the future collision potential coordinates for this check - but I have to also convert back and forth,
		// and Bounds don't have... methods... and... and... eugh.
		Bounds obstacleBounds = canNotTouchThis.localToScene(canNotTouchThis.getBoundsInLocal());
		if (daCircle.intersects(daCircle.sceneToLocal(obstacleBounds))){
			daCircle.setLayoutX(daCircle.getLayoutX()-1);
			daCircle.setLayoutY(daCircle.getLayoutY()-1);
			statusLabel.setText("What exactly do you think you're trying to do?!");
			statusLabel.setTextFill(Color.web("#ff451f"));
			collision = true;
		}
		return collision;
	}
}
