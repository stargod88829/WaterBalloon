package game;

import java.util.concurrent.atomic.AtomicReference;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public abstract class CanvasRepaint<T> extends AnimationTimer {

	private AtomicReference<T> atomRefData = new AtomicReference<>(null);
	private Canvas drawingCanvas;

	public CanvasRepaint(Canvas drawingCanvas) {
		this.drawingCanvas = drawingCanvas;
	}

	public void requestRepaint(T toPaint) {
		atomRefData.set(toPaint);
		start();
	}

	public void handle(long now) {
		T toPaint = atomRefData.getAndSet(null);
		if (toPaint != null) {
			repaint(drawingCanvas.getGraphicsContext2D(), toPaint);
		}
	}

	public abstract void repaint(GraphicsContext gc, T data);
}
