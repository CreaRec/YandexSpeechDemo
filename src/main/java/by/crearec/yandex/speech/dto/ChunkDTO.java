package by.crearec.yandex.speech.dto;

import java.io.Serializable;
import java.util.List;

public class ChunkDTO implements Serializable {
	private static final long serialVersionUID = -8293376015188317764L;

	private List<AlternativeDTO> alternatives;

	public List<AlternativeDTO> getAlternatives() {
		return alternatives;
	}

	public void setAlternatives(List<AlternativeDTO> alternatives) {
		this.alternatives = alternatives;
	}

	@Override
	public String toString() {
		return "ChunkDTO{" + "alternatives=" + alternatives + '}';
	}
}
