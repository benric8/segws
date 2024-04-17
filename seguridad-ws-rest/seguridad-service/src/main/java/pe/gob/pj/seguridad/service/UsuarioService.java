package pe.gob.pj.seguridad.service;

import java.util.List;

import pe.gob.pj.model.dto.RequestLoginDTO;
import pe.gob.pj.model.dto.RequestValidaMetodoDTO;
import pe.gob.pj.model.dto.ResponseAccesoMetodoDTO;
import pe.gob.pj.model.dto.ResponseUsuarioDTO;
import pe.gob.pj.model.dto.UsuarioDTO;
import pe.gob.pj.model.entity.Usuario;

public interface UsuarioService {

	public Usuario findByUsername(String cuo, String username) throws Exception;
	
	public ResponseUsuarioDTO recuperarInfomarcionUsuarios(String cuo, RequestLoginDTO login) throws Exception;
	
	public List<ResponseAccesoMetodoDTO> validaAccesoMetodo(String cuo, RequestValidaMetodoDTO acceso) throws Exception;
	
	public UsuarioDTO buscarUsuarioPorId(String cuo, Integer idUsuario) throws Exception;
	
	public List<ResponseAccesoMetodoDTO> validaAccesoMetodoPorAplicativo(String cuo, Integer idAplicativo) throws Exception;
}
