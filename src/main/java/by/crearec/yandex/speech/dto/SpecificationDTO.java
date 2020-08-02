package by.crearec.yandex.speech.dto;

import java.io.Serializable;

public class SpecificationDTO implements Serializable {
	private static final long serialVersionUID = -3625150601315652154L;

	private String languageCode;
	private Boolean profanityFilter;
	private String audioEncoding;
	private Integer sampleRateHertz;
	private Integer audioChannelCount;

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public Boolean getProfanityFilter() {
		return profanityFilter;
	}

	public void setProfanityFilter(Boolean profanityFilter) {
		this.profanityFilter = profanityFilter;
	}

	public String getAudioEncoding() {
		return audioEncoding;
	}

	public void setAudioEncoding(String audioEncoding) {
		this.audioEncoding = audioEncoding;
	}

	public Integer getSampleRateHertz() {
		return sampleRateHertz;
	}

	public void setSampleRateHertz(Integer sampleRateHertz) {
		this.sampleRateHertz = sampleRateHertz;
	}

	public Integer getAudioChannelCount() {
		return audioChannelCount;
	}

	public void setAudioChannelCount(Integer audioChannelCount) {
		this.audioChannelCount = audioChannelCount;
	}

	@Override
	public String toString() {
		return "SpecificationDTO{" + "languageCode='" + languageCode + '\'' + ", profanityFilter='" + profanityFilter + '\'' + ", audioEncoding='" + audioEncoding +
		       '\'' + ", sampleRateHertz=" + sampleRateHertz + ", audioChannelCount=" + audioChannelCount + '}';
	}
}
