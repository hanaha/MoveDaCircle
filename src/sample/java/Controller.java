package sample.java;

import javafx.geometry.BoundingBox;
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
	final private double speed = 2;

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

		if (this.checkForCollision(keyEvent.getCode())) {
			keyEvent.consume();
			statusLabel.setText("What exactly do you think you're trying to do?!");
			statusLabel.setTextFill(Color.web("#ff451f"));
			return;
		}

		switch (keyEvent.getCode()) {
			case W:
			case UP:
				statusLabel.setText("Moving up");
				daCircle.setLayoutY(daCircle.getLayoutY() - speed);
				break;

			case A:
			case LEFT:
				statusLabel.setText("Moving left");
				daCircle.setLayoutX(daCircle.getLayoutX() - speed);
				break;

			case S:
			case DOWN:
				statusLabel.setText("Moving down");
				daCircle.setLayoutY(daCircle.getLayoutY() + speed);
				break;

			case D:
			case RIGHT:
				statusLabel.setText("Moving right");
				daCircle.setLayoutX(daCircle.getLayoutX() + speed);
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
	 *
	 * @param code
	 */
	private boolean checkForCollision(KeyCode code) {
		boolean collision = false;

		Bounds paneBounds = mainPane.getLayoutBounds();
		Bounds circleBounds = daCircle.getBoundsInParent();
		Bounds obstacleBounds = canNotTouchThis.getBoundsInParent();

		Bounds b;
		boolean paneCollision = false;

		switch (code) {
			case W:
			case UP:
				b = new BoundingBox(
						circleBounds.getMinX(),
						circleBounds.getMinY() - speed,
						circleBounds.getWidth(),
						circleBounds.getHeight());
				paneCollision = (paneBounds.getMinY() > (circleBounds.getMinY() + speed));
				break;

			case A:
			case LEFT:
				b = new BoundingBox(
						circleBounds.getMinX() - speed,
						circleBounds.getMinY(),
						circleBounds.getWidth(),
						circleBounds.getHeight());
				paneCollision = (paneBounds.getMinX() > (circleBounds.getMinX() + speed));
				break;

			case S:
			case DOWN:
				b = new BoundingBox(
						circleBounds.getMinX(),
						circleBounds.getMinY() + speed,
						circleBounds.getWidth(),
						circleBounds.getHeight());
				paneCollision = (paneBounds.getMaxY() < (circleBounds.getMaxY() + speed));
				break;

			case D:
			case RIGHT:
				b = new BoundingBox(
						circleBounds.getMinX() + speed,
						circleBounds.getMinY(),
						circleBounds.getWidth(),
						circleBounds.getHeight());
				paneCollision = (paneBounds.getMaxX() < (circleBounds.getMaxX() + speed));
				break;

			default:
				b = new BoundingBox(
						circleBounds.getMinX(),
						circleBounds.getMinY(),
						circleBounds.getWidth(),
						circleBounds.getHeight());
				break;
		}
		collision = obstacleBounds.intersects(b) || paneCollision;

		return collision;
	}
}
