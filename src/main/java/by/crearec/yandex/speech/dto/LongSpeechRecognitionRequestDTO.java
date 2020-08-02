package by.crearec.yandex.speech.dto;

import java.io.Serializable;

public class LongSpeechRecognitionRequestDTO implements Serializable {
	private static final long serialVersionUID = -6576947870657482188L;

	private ConfigDTO config;
	private AudioDTO audio;

	public ConfigDTO getConfig() {
		return config;
	}

	public void setConfig(ConfigDTO config) {
		this.config = config;
	}

	public AudioDTO getAudio() {
		return audio;
	}

	public void setAudio(AudioDTO audio) {
		this.audio = audio;
	}

	@Override
	public String toString() {
		return "LongSpeechRecognitionRequestDTO{" + "config=" + config + ", audio=" + audio + '}';
	}
}
