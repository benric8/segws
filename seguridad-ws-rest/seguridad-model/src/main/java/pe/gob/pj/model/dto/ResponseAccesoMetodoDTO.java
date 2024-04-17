package pe.gob.pj.model.dto;

import java.io.Serializable;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Validated
@Setter
@Getter
@AllArgsConstructor
public class ResponseAccesoMetodoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String api;
	private boolean tieneAcceso;
	private String codigoRol;
	private String codigoUsuario;

}
