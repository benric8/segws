package pe.gob.pj.seguridad.apis;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pe.gob.pj.model.utils.Utils;

@RestController
@RequestMapping("mante")
public class MantenimientoApis implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(MantenimientoApis.class);

	@RequestMapping(value = "cliente", method = RequestMethod.POST)
	public ResponseEntity<String> registrarCliente() {
		String cuo = Utils.obtenerCodigoUnico();
		try {
			
		} catch (Exception e) {
			logger.error("{} Error al registrar cliente: {}", cuo, e.getMessage());
		}
		return new ResponseEntity<String>("cliente", HttpStatus.OK);
	}
	
	@RequestMapping(value = "aplicativo", method = RequestMethod.POST)
	public ResponseEntity<String> registrarAplicativo() {
		return new ResponseEntity<String>("aplicativo", HttpStatus.OK);
	}
	
	@RequestMapping(value = "rol", method = RequestMethod.POST)
	public ResponseEntity<String> registrarRoles() {
		return new ResponseEntity<String>("roles", HttpStatus.OK);
	}
	
	@RequestMapping(value = "usuario", method = RequestMethod.POST)
	public ResponseEntity<String> registrarUsuario() {
		return new ResponseEntity<String>("usuarios", HttpStatus.OK);
	}
	
	@RequestMapping(value = "apis", method = RequestMethod.POST)
	public ResponseEntity<String> registrarApis() {
		return new ResponseEntity<String>("apis", HttpStatus.OK);
	}
}
