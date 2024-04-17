package pe.gob.pj.model.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class ResponseVersionDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Getter @Setter
	private String versionActual;
	
	@Getter @Setter
	private String aplicativo;

}
