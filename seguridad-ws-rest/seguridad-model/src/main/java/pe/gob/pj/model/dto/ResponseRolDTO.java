package pe.gob.pj.model.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;

import lombok.Getter;

import lombok.Setter;

@Setter @Getter
@AllArgsConstructor
public class ResponseRolDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer idRol;
	private String codigoRol;
	private String rol;
	private String estado;

}
