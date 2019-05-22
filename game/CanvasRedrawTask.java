package game;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.util.concurrent.atomic.AtomicReference;

public abstract  class CanvasRedrawTask<T> extends AnimationTimer {

	private final AtomicReference<T> data = new AtomicReference<>(null);
	private final Canvas canvas;

	public CanvasRedrawTask(Canvas canvas) {
		this.canvas = canvas;
	}

	public void requestRedraw(T toDraw) {
		data.set(toDraw);
		start();
	}

	public void handle(long now) {
		// check if new data is available
		T toDraw = data.getAndSet(null);
		if (toDraw != null) {
			redraw(canvas.getGraphicsContext2D(), toDraw);
		}
	}

	public abstract void redraw(GraphicsContext gc, T data);
}
