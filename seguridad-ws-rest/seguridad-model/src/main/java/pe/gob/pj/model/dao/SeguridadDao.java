package pe.gob.pj.model.dao;

import java.util.List;

import pe.gob.pj.model.dto.RequestLoginDTO;
import pe.gob.pj.model.dto.RequestValidaMetodoDTO;
import pe.gob.pj.model.dto.ResponseAccesoMetodoDTO;
import pe.gob.pj.model.dto.ResponseUsuarioDTO;
import pe.gob.pj.model.dto.UsuarioDTO;
import pe.gob.pj.model.entity.Usuario;

public interface SeguridadDao {
	
	public Usuario buscarUsuario(String cuo, String codigoUsuario) throws Exception;
	
	public ResponseUsuarioDTO recuperarInfomarcionUsuarios(String cuo,RequestLoginDTO login) throws Exception;
	
	public List<ResponseAccesoMetodoDTO> validaAccesoMetodo(String cuo,RequestValidaMetodoDTO acceso) throws Exception;
	
	public UsuarioDTO buscarUsuarioPorId(String cuo, Integer idUsuario) throws Exception;
	
	public List<ResponseAccesoMetodoDTO> validaAccesoMetodoPorAplicativo(String cuo, Integer idAplicativo) throws Exception;

	/*public String autenticarUsuario(String codigoCliente, String codigoRol, String usuario, String clave,String cuo) throws Exception;

	public MovUsuario recuperaInfoUsuario(String id, String cuo) throws Exception;

	public List<MaeRol> recuperarRoles(String id, String cuo) throws Exception;
	
	public boolean validarAccesoMetodo(String usuario, String rol, String ruta, String cuo) throws Exception; */
}
