package by.crearec.yandex.speech.dto;

public class ResultRecognitionResponseDTO extends OperationDTO {
	private static final long serialVersionUID = 24437238181402394L;

	private ResponseDTO response;

	public ResponseDTO getResponse() {
		return response;
	}

	public void setResponse(ResponseDTO response) {
		this.response = response;
	}

	@Override
	public String toString() {
		return "ResultRecognitionResponseDTO{" + "response=" + response + "} " + super.toString();
	}
}
