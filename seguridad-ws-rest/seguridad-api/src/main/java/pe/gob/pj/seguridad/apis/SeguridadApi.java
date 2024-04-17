package pe.gob.pj.seguridad.apis;

import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pe.gob.pj.model.dto.GloablResponseDatosUsuarioDTO;
import pe.gob.pj.model.dto.GlobalResponseLoginDTO;
import pe.gob.pj.model.dto.GlobalResponseMetodoDTO;
import pe.gob.pj.model.dto.RequestLoginDTO;
import pe.gob.pj.model.dto.RequestUsuarioRolDTO;
import pe.gob.pj.model.dto.RequestValidaMetodoDTO;
import pe.gob.pj.model.dto.ResponseAccesoMetodoDTO;
import pe.gob.pj.model.dto.ResponseUsuarioDTO;
import pe.gob.pj.model.dto.UsuarioDTO;
import pe.gob.pj.model.utils.Utils;
import pe.gob.pj.seguridad.service.UsuarioService;

@RestController
@RequestMapping("seguridad")
@CrossOrigin(origins = "*")
public class SeguridadApi implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(SeguridadApi.class);
	
	@Autowired
	private UsuarioService usuarioService;
	
	@RequestMapping(value = "verificaAcceso", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GlobalResponseLoginDTO> usuario(@RequestBody RequestLoginDTO login) {
		GlobalResponseLoginDTO res = new GlobalResponseLoginDTO(HttpStatus.OK.toString(), "Credenciales recuperadas correctamente", null);
		String cuo = Utils.obtenerCodigoUnico();
		try {
			ResponseUsuarioDTO us = usuarioService.recuperarInfomarcionUsuarios(cuo, login);
			res.setUsuario(us);
		} catch (Exception e) {
			logger.error("{} Error al recuperar información de login: {}", cuo, e.getMessage());
			e.printStackTrace();
			res.setCodigo(HttpStatus.INTERNAL_SERVER_ERROR.toString());
			res.setDescripcion(e.getMessage());
			res.setUsuario(null);
			return new ResponseEntity<GlobalResponseLoginDTO>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<GlobalResponseLoginDTO>(res, HttpStatus.OK);
	}
		
	@RequestMapping(value = "verificaAccesoMetodo", method = RequestMethod.POST,  consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GlobalResponseMetodoDTO> validadAccesoMetodo(@RequestBody RequestValidaMetodoDTO acceso) {
		GlobalResponseMetodoDTO res = new GlobalResponseMetodoDTO(HttpStatus.OK.toString(), "Credenciales recuperadas correctamente", null);
		String cuo = Utils.obtenerCodigoUnico();
		try {
			List<ResponseAccesoMetodoDTO> lista = usuarioService.validaAccesoMetodo(cuo, acceso);
			res.setAccesos(lista);
		} catch (Exception e) {
			logger.error("{} Error al recuperar información de acceso al login: {}", cuo, e.getMessage());
			e.printStackTrace();
			res.setCodigo(HttpStatus.INTERNAL_SERVER_ERROR.toString());
			res.setDescripcion(e.getMessage());
			res.setAccesos(null);
			return new ResponseEntity<GlobalResponseMetodoDTO>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<GlobalResponseMetodoDTO>(res, HttpStatus.OK);
	}	
	
	@RequestMapping(value = "verificaAccesoMetodoPorAplicativo", method = RequestMethod.POST,  consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GlobalResponseMetodoDTO> validadAccesoMetodoPorAplicativo(@RequestBody RequestValidaMetodoDTO acceso) {
		GlobalResponseMetodoDTO res = new GlobalResponseMetodoDTO(HttpStatus.OK.toString(), "Credenciales recuperadas correctamente", null);
		String cuo = Utils.obtenerCodigoUnico();
		try {
			List<ResponseAccesoMetodoDTO> lista = usuarioService.validaAccesoMetodoPorAplicativo(cuo, acceso.getIdAplicativo());
			res.setAccesos(lista);
		} catch (Exception e) {
			logger.error("{} Error al recuperar información de accesos por aplicativo: {}", cuo, e.getMessage());
			e.printStackTrace();
			res.setCodigo(HttpStatus.INTERNAL_SERVER_ERROR.toString());
			res.setDescripcion(e.getMessage());
			res.setAccesos(null);
			return new ResponseEntity<GlobalResponseMetodoDTO>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<GlobalResponseMetodoDTO>(res, HttpStatus.OK);
	}	
	
	@RequestMapping(value = "recuperaUsuario", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GloablResponseDatosUsuarioDTO> buscarUsuarioPorId(@RequestBody RequestUsuarioRolDTO usuario) {
		GloablResponseDatosUsuarioDTO res = new GloablResponseDatosUsuarioDTO(HttpStatus.OK.toString(), "Credenciales recuperadas correctamente", null);
		String cuo = Utils.obtenerCodigoUnico();
		try {
			UsuarioDTO us = usuarioService.buscarUsuarioPorId(cuo, usuario.getIdUsuario());
			res.setUsuario(us);
		} catch (Exception e) {
			logger.error("{} Error al recuperar información de usuario: {}", cuo, e.getMessage());
			e.printStackTrace();
			res.setCodigo(HttpStatus.INTERNAL_SERVER_ERROR.toString());
			res.setDescripcion(e.getMessage());
			res.setUsuario(null);
			return new ResponseEntity<GloablResponseDatosUsuarioDTO>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<GloablResponseDatosUsuarioDTO>(res, HttpStatus.OK);
	}
}
