package by.crearec.yandex.speech.dto;

import java.io.Serializable;

public class LongSpeechRecognitionResponseDTO implements Serializable {
	private static final long serialVersionUID = 3397238310802619631L;

	private Boolean done;
	private String id;
	private String createdAt;
	private String createdBy;
	private String modifiedAt;
	private Object error;

	public Boolean getDone() {
		return done;
	}

	public void setDone(Boolean done) {
		this.done = done;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(String modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public Object getError() {
		return error;
	}

	public void setError(Object error) {
		this.error = error;
	}

	@Override
	public String toString() {
		return "LongSpeechRecognitionResponseDTO{" + "done=" + done + ", id='" + id + '\'' + ", createdAt='" + createdAt + '\'' + ", createdBy='" + createdBy + '\'' +
		       ", modifiedAt='" + modifiedAt + '\'' + ", error=" + error + '}';
	}
}
