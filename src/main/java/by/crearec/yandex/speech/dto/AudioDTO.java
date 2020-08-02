package by.crearec.yandex.speech.dto;

import java.io.Serializable;

public class AudioDTO implements Serializable {
	private static final long serialVersionUID = -5412608963255299618L;

	private String uri;

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	@Override
	public String toString() {
		return "AudioDTO{" + "uri='" + uri + '\'' + '}';
	}
}
