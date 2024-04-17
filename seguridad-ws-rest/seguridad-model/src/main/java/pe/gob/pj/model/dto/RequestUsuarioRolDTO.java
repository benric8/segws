package pe.gob.pj.model.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class RequestUsuarioRolDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull(message = "Se requiere enviar el id de usuario")
	private Integer idUsuario;

	public RequestUsuarioRolDTO() {
		super();
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}
}