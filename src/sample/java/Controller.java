package sample.java;

import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Controller {
	public Pane mainPane;
	public Circle daCircle;
	public Label statusLabel;

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

		switch (keyEvent.getCode()) {
			case W:
				statusLabel.setText("Moving up");
				daCircle.setLayoutY(daCircle.getLayoutY() - 1);
				break;

			case A:
				statusLabel.setText("Moving left");
				daCircle.setLayoutX(daCircle.getLayoutX() - 1);
				break;

			case S:
				statusLabel.setText("Moving down");
				daCircle.setLayoutY(daCircle.getLayoutY() + 1);
				break;

			case D:
				statusLabel.setText("Moving right");
				daCircle.setLayoutX(daCircle.getLayoutX() + 1);
				break;

			default:
				statusLabel.setText("Argh! Don't press that!");
				statusLabel.setTextFill(Color.web("#ff451f"));
				break;
		}
	}
}
