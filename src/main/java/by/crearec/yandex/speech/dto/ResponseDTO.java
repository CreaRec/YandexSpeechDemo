package by.crearec.yandex.speech.dto;

import java.io.Serializable;
import java.util.List;

public class ResponseDTO implements Serializable {
	private static final long serialVersionUID = -3312975221924436102L;

	private List<ChunkDTO> chunks;

	public List<ChunkDTO> getChunks() {
		return chunks;
	}

	public void setChunks(List<ChunkDTO> chunks) {
		this.chunks = chunks;
	}

	@Override
	public String toString() {
		return "ResponseDTO{" + "chunks=" + chunks + '}';
	}
}
