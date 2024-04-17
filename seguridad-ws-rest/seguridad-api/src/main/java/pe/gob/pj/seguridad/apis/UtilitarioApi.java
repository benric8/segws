package pe.gob.pj.seguridad.apis;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pe.gob.pj.model.dto.GlobalResponseEncriptarDTO;
import pe.gob.pj.model.dto.GlobalResponseVersionDTO;
import pe.gob.pj.model.dto.ResponseClaveDTO;
import pe.gob.pj.model.dto.ResponseVersionDTO;
import pe.gob.pj.model.utils.ConfiguracionPropiedades;
import pe.gob.pj.model.utils.EncryptUtils;
import pe.gob.pj.model.utils.Utils;

@RestController
public class UtilitarioApi implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(UtilitarioApi.class);
	

//	@RequestMapping(value = "encriptar", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<GlobalResponseEncriptarDTO> genrarClave(@RequestParam String usuario, @RequestParam String clave) {
//		GlobalResponseEncriptarDTO res = new GlobalResponseEncriptarDTO(HttpStatus.OK.toString(), "Credenciales recuperadas correctamente", null);
//		String cuo = Utils.obtenerCodigoUnico();
//		try {
//			ResponseClaveDTO d = new ResponseClaveDTO();
//			d.setClaveGenerada(EncryptUtils.encrypt(usuario, clave));
//			res.setClave(d);
//		} catch (Exception e) {
//			logger.error("{} error al encriptar clave usando usuario y clave: {}", cuo , Utils.isNull(e.getCause()).concat(e.getMessage()));
//			e.printStackTrace();
//			res.setCodigo(HttpStatus.INTERNAL_SERVER_ERROR.toString());
//			res.setDescripcion(e.getMessage());
//			res.setClave(null);
//		}
//		return new ResponseEntity<GlobalResponseEncriptarDTO>(res, HttpStatus.OK);
//	}
	
	@RequestMapping(value = "/verificarVersion", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GlobalResponseVersionDTO> verificarConexiones() {
		GlobalResponseVersionDTO res = new GlobalResponseVersionDTO(HttpStatus.OK.toString(), "Version actual del aplicativo recuperada correctamente",null);
		String cuo = Utils.obtenerCodigoUnico();
		try {
			ResponseVersionDTO version=new ResponseVersionDTO();
			version.setVersionActual(ConfiguracionPropiedades.getInstance().getProperty("configuracion.version.actual").toString());
			version.setAplicativo("SEGURIDAD API");
			res.setVersion(version);

		} catch (Exception e) {
			logger.error("{} error al recuperar la version actual del aplicativo: {}", cuo , Utils.isNull(e.getCause()).concat(e.getMessage()));
			e.printStackTrace();
			res.setCodigo(HttpStatus.INTERNAL_SERVER_ERROR.toString());
			res.setDescripcion(e.getMessage());
			res.setVersion(null);
		}
		return new ResponseEntity<GlobalResponseVersionDTO>(res, HttpStatus.OK);
	}

	
	@RequestMapping(value = "encriptarClave", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GlobalResponseEncriptarDTO> cifrarClave(@RequestParam String token, @RequestParam String clave) {
		GlobalResponseEncriptarDTO res = new GlobalResponseEncriptarDTO(HttpStatus.OK.toString(), "Credenciales recuperadas correctamente", null);
		String cuo = Utils.obtenerCodigoUnico();
		try {		
			ResponseClaveDTO d = new ResponseClaveDTO();
			d.setClaveGenerada(EncryptUtils.encryptPastFrass(token,clave));
			res.setClave(d);
		} catch (Exception e) {
			logger.error("{} error al encriptar clave usando Token y valor: {}", cuo , Utils.isNull(e.getCause()).concat(e.getMessage()));
			e.printStackTrace();
			res.setCodigo(HttpStatus.INTERNAL_SERVER_ERROR.toString());
			res.setDescripcion(e.getMessage());
			res.setClave(null);
		}
		return new ResponseEntity<GlobalResponseEncriptarDTO>(res, HttpStatus.OK);
	}
	

	@RequestMapping(value = "desencriptarClave", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GlobalResponseEncriptarDTO> descriptarClave(@RequestParam String token, @RequestParam String claveEncriptada) {
		GlobalResponseEncriptarDTO res = new GlobalResponseEncriptarDTO(HttpStatus.OK.toString(), "Credenciales recuperadas correctamente", null);
		String cuo = Utils.obtenerCodigoUnico();
		try {
			ResponseClaveDTO d = new ResponseClaveDTO();
			d.setClaveGenerada(EncryptUtils.decryptPastFrass(token,claveEncriptada));
			res.setClave(d);
		} catch (Exception e) {
			logger.error("{} error al des encriptar clave usando Token y Valor: {}", cuo , Utils.isNull(e.getCause()).concat(e.getMessage()));
			e.printStackTrace();
			res.setCodigo(HttpStatus.INTERNAL_SERVER_ERROR.toString());
			res.setDescripcion(e.getMessage());
			res.setClave(null);
		}
		return new ResponseEntity<GlobalResponseEncriptarDTO>(res, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "generarClaveAccesoServicio", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GlobalResponseEncriptarDTO> generarClaveAccesoServicio(@RequestParam String clave) {
		GlobalResponseEncriptarDTO res = new GlobalResponseEncriptarDTO(HttpStatus.OK.toString(), "Credenciales recuperadas correctamente", null);
		String cuo = Utils.obtenerCodigoUnico();
		try {
			ResponseClaveDTO d = new ResponseClaveDTO();
			d.setClaveGenerada(EncryptUtils.encryptPastFrass(clave));
			res.setClave(d);
		} catch (Exception e) {
			logger.error("{} error al des encriptar clave usando Token y Valor: {}", cuo , Utils.isNull(e.getCause()).concat(e.getMessage()));
			e.printStackTrace();
			res.setCodigo(HttpStatus.INTERNAL_SERVER_ERROR.toString());
			res.setDescripcion(e.getMessage());
			res.setClave(null);
		}
		return new ResponseEntity<GlobalResponseEncriptarDTO>(res, HttpStatus.OK);
	}
	
	
	
	
	
}
