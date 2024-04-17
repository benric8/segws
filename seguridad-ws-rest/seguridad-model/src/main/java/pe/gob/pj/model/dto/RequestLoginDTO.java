package pe.gob.pj.model.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Validated
@Setter @Getter
@AllArgsConstructor
public class RequestLoginDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@NotNull(message = "El id de aplicativo es requerido")
	private Integer idAplicativo;
	
	@NotNull(message = "El codigo de cliente es requerido")
	private String codigoCliente;
	
	@NotNull(message = "El codigo de rol es requerido")
	private String codigoRol;
	
	@NotNull(message = "El codigo de usuario es requerido")
	private String codigoUsuario;
	
	@NotNull(message = "La clave de usuario es requerido requerido")
	private String clave;

	public RequestLoginDTO() {
		super();
		// TODO Auto-generated constructor stub
	}		
}
