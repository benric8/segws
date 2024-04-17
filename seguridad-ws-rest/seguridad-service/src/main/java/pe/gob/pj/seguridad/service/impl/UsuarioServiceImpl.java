package pe.gob.pj.seguridad.service.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.pj.model.dao.SeguridadDao;
import pe.gob.pj.model.dto.RequestLoginDTO;
import pe.gob.pj.model.dto.RequestValidaMetodoDTO;
import pe.gob.pj.model.dto.ResponseAccesoMetodoDTO;
import pe.gob.pj.model.dto.ResponseUsuarioDTO;
import pe.gob.pj.model.dto.UsuarioDTO;
import pe.gob.pj.model.entity.Usuario;
import pe.gob.pj.model.utils.Utils;
import pe.gob.pj.seguridad.service.UsuarioService;

@Service("usuarioService")
@Transactional
public class UsuarioServiceImpl implements UsuarioService, UserDetailsService, Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(UsuarioServiceImpl.class);

	@Autowired
	private SeguridadDao seguridadDao;

	@Override
	@Transactional(transactionManager = "txManagerSeguridad", propagation = Propagation.REQUIRES_NEW, readOnly = true, rollbackFor = { Exception.class, SQLException.class })
	public Usuario findByUsername(String cuo, String username) throws Exception {
		return seguridadDao.buscarUsuario(cuo, username);
	}

	@Override
	@Transactional(transactionManager = "txManagerSeguridad", propagation = Propagation.REQUIRES_NEW, readOnly = true, rollbackFor = { Exception.class, SQLException.class })
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {        
		String cuo = Utils.obtenerCodigoUnico();
		try {
			Usuario usuario = seguridadDao.buscarUsuario(cuo, username);
			if (usuario.getUsername() == null || usuario.getPassword() == null) {
				log.error("{} Error en el login, no existe el usuario [{}] en el sistema", cuo, username);
				throw new UsernameNotFoundException("Error en el login, no existe el usuario '" + usuario.getUsername() + "' en el sistema");
			}

			List<GrantedAuthority> authorities = usuario.getRoles().stream()
					.map(role -> new SimpleGrantedAuthority(role.getNombre()))
					.peek(authority -> System.out.println("Role: " + authority.getAuthority()))
					.collect(Collectors.toList());
			return new User(usuario.getUsername(), usuario.getPassword(), true, true, true, true, authorities);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("{} Error al buscar usuario en la BD: {}", cuo, e.getMessage());
		}
		return null;
	}

	@Override
	@Transactional(transactionManager = "txManagerSeguridad", propagation = Propagation.REQUIRES_NEW, readOnly = true, rollbackFor = { Exception.class, SQLException.class })
	public ResponseUsuarioDTO recuperarInfomarcionUsuarios(String cuo, RequestLoginDTO login) throws Exception {
		return seguridadDao.recuperarInfomarcionUsuarios(cuo, login);
	}

	@Override
	@Transactional(transactionManager = "txManagerSeguridad", propagation = Propagation.REQUIRES_NEW, readOnly = true, rollbackFor = { Exception.class, SQLException.class })
	public List<ResponseAccesoMetodoDTO> validaAccesoMetodo(String cuo, RequestValidaMetodoDTO acceso) throws Exception {
		return seguridadDao.validaAccesoMetodo(cuo, acceso);
	}

	@Override
	@Transactional(transactionManager = "txManagerSeguridad", propagation = Propagation.REQUIRES_NEW, readOnly = true, rollbackFor = { Exception.class, SQLException.class })
	public UsuarioDTO buscarUsuarioPorId(String cuo, Integer idUsuario) throws Exception {
		return seguridadDao.buscarUsuarioPorId(cuo, idUsuario);
	}

	@Override
	@Transactional(transactionManager = "txManagerSeguridad", propagation = Propagation.REQUIRES_NEW, readOnly = true, rollbackFor = { Exception.class, SQLException.class })
	public List<ResponseAccesoMetodoDTO> validaAccesoMetodoPorAplicativo(String cuo, Integer idAplicativo) throws Exception {
		return seguridadDao.validaAccesoMetodoPorAplicativo(cuo, idAplicativo);
	}

}
