package pe.gob.pj.model.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
@AllArgsConstructor
public class GlobalResponseLoginDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String codigo;	
	private String descripcion;
	private ResponseUsuarioDTO usuario;
}

