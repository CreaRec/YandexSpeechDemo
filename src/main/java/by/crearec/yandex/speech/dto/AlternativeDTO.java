package by.crearec.yandex.speech.dto;

import java.io.Serializable;

public class AlternativeDTO implements Serializable {
	private static final long serialVersionUID = -2301067935380571091L;

	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "AlternativeDTO{" + "text='" + text + '\'' + '}';
	}
}
