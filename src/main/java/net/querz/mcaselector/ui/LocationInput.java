package net.querz.mcaselector.ui;

import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import net.querz.mcaselector.util.Point2i;
import java.util.function.Consumer;

public class LocationInput extends HBox {

	private Point2i value;
	private Integer x, z;
	private TextField xValue, zValue;

	private Consumer<Boolean> validityCheckAction;

	private boolean emptyIsZero;

	public LocationInput(boolean emptyIsZero) {
		this.emptyIsZero = emptyIsZero;

		if (emptyIsZero) {
			x = z = 0;
			value = new Point2i(0, 0);
		}

		getStyleClass().add("location-input");

		xValue = new TextField();
		xValue.setPromptText("X");
		xValue.textProperty().addListener((a, o, n) -> onXInput(o, n));
		zValue = new TextField();
		zValue.setPromptText("Z");
		zValue.textProperty().addListener((a, o, n) -> onZInput(o, n));

		getChildren().addAll(xValue, zValue);
	}

	public boolean emptyIsZero() {
		return emptyIsZero;
	}

	public void setX(Integer x) {
		this.x = x;
		if (x != null) {
			xValue.setText(x.toString());
		}
		setValue();
	}

	public void setZ(Integer z) {
		this.z = z;
		if (z != null) {
			zValue.setText(z.toString());
		}
		setValue();
	}

	public void requestFocus() {
		xValue.requestFocus();
	}

	//allow empty textfield
	//allow only + / -

	private void onXInput(String o, String n) {
		if (emptyIsZero && n.isEmpty()) {
			x = 0;
			setValue();
			return;
		}
		try {
			x = Integer.parseInt(n);
		} catch (NumberFormatException ex) {
			x = null;
			if (!"-".equals(n) && !"+".equals(n) && !"".equals(n)) {
				xValue.setText(o);
			}
		}
		setValue();
	}

	private void onZInput(String o, String n) {
		if (emptyIsZero && n.isEmpty()) {
			z = 0;
			setValue();
			return;
		}
		try {
			z = Integer.parseInt(n);
		} catch (NumberFormatException ex) {
			z = null;
			if (!"-".equals(n) && !"+".equals(n) && !"".equals(n)) {
				zValue.setText(o);
			}
		}
		setValue();
	}

	public Point2i getValue() {
		return value;
	}

	public void setOnValidityCheck(Consumer<Boolean> action) {
		validityCheckAction = action;
	}

	private void setValue() {
		if (x == null || z == null) {
			value = null;
			if (validityCheckAction != null) {
				validityCheckAction.accept(false);
			}
		} else {
			value = new Point2i(x, z);
			if (validityCheckAction != null) {
				validityCheckAction.accept(true);
			}
		}
	}
}
