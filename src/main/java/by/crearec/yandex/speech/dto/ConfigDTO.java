package by.crearec.yandex.speech.dto;

import java.io.Serializable;

public class ConfigDTO implements Serializable {
	private static final long serialVersionUID = 4479273652014640566L;

	private SpecificationDTO specification;

	public SpecificationDTO getSpecification() {
		return specification;
	}

	public void setSpecification(SpecificationDTO specification) {
		this.specification = specification;
	}

	@Override
	public String toString() {
		return "ConfigDTO{" + "specification=" + specification + '}';
	}
}
