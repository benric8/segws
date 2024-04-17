package pe.gob.pj.model.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
@AllArgsConstructor
public class ResponseUsuarioDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer idUsuario;
	private String codigoUsuario;
	private String nombreUsuario;
	private String nombreCliente;
	private String nombreAplicativo;
	private String estado;
	private List<ResponseRolDTO> roles;
	
	public ResponseUsuarioDTO() {
		super();
	}	
}
