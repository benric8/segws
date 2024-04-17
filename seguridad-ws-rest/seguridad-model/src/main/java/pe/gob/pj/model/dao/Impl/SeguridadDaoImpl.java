package pe.gob.pj.model.dao.Impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import pe.gob.pj.model.dao.SeguridadDao;
import pe.gob.pj.model.dto.RequestLoginDTO;
import pe.gob.pj.model.dto.RequestValidaMetodoDTO;
import pe.gob.pj.model.dto.ResponseAccesoMetodoDTO;
import pe.gob.pj.model.dto.ResponseRolDTO;
import pe.gob.pj.model.dto.ResponseUsuarioDTO;
import pe.gob.pj.model.dto.RoleDTO;
import pe.gob.pj.model.dto.UsuarioDTO;
import pe.gob.pj.model.entity.Role;
import pe.gob.pj.model.entity.Usuario;
import pe.gob.pj.model.entity.seg.MaeOperacion;
import pe.gob.pj.model.entity.seg.MaeRolUsuario;
import pe.gob.pj.model.entity.seg.MaeUsuario;
import pe.gob.pj.model.utils.EncryptUtils;
import pe.gob.pj.model.utils.Utils;

/**
 * Clase que sirve para realizar operaciones sobre bd de seguridad 
 * @author Melvin A. Diaz
 * @date 15/10/2021
 */
@Component("seguridadDao")
public class SeguridadDaoImpl implements SeguridadDao, Serializable{

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(SeguridadDaoImpl.class);
		
	@Autowired
	@Qualifier("sessionSeguridad")
	private SessionFactory sf;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;	
	
	@Override
	public Usuario buscarUsuario(String cuo, String codigoUsuario) throws Exception {
		Usuario us = new Usuario();
		try {
			TypedQuery<MaeUsuario> query = this.sf.getCurrentSession().createNamedQuery(MaeUsuario.FIND_BY_CODIGO, MaeUsuario.class);
			query.setParameter(MaeUsuario.P_N_CODIGO, codigoUsuario);
			MaeUsuario u = query.getSingleResult();		
			String clave = EncryptUtils.decryptPastFrass(u.getCClave());
			us.setId((long) u.getNUsuario());
			us.setEnabled(u.getLActivo().equals("1") ? true: false);
			us.setPassword(passwordEncoder.encode(clave));
			us.setNombre(u.getMaeCliente().getXCliente());
			us.setApellido(u.getMaeCliente().getCCliente());
			us.setUsername(codigoUsuario);
			List<Role> roles = new ArrayList<Role>();
			for (MaeRolUsuario ru : u.getMaeRolUsuarios()) {
				Role r = new Role();
				r.setId((long) ru.getMaeRol().getNRol());
				r.setNombre("ROLE_"+ru.getMaeRol().getCRol());
				roles.add(r);
			}
			us.setRoles(roles);
			logger.info("{} recupera accesos para: {}", cuo, codigoUsuario);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("{} Error al recuperar datos de usuarios: {}", cuo, e.getMessage());
		}
		return us;
	}
	
	@Override
	public UsuarioDTO buscarUsuarioPorId(String cuo, Integer idUsuario) throws Exception {
		UsuarioDTO us = new UsuarioDTO();
		try {
			TypedQuery<MaeUsuario> query = this.sf.getCurrentSession().createNamedQuery(MaeUsuario.FIND_BY_ID, MaeUsuario.class);
			query.setParameter(MaeUsuario.P_N_USUARIO, idUsuario);
			MaeUsuario u = query.getSingleResult();		
			//String clave = EncryptUtils.decryptPastFrass(u.getCClave());
			us.setId((long) u.getNUsuario());
			us.setEnabled(u.getLActivo().equals("1") ? true: false);
			//us.setPassword(clave);
			us.setPassword(u.getCClave());
			us.setNombre(u.getMaeCliente().getXCliente());
			us.setApellido(u.getMaeCliente().getCCliente());
			us.setUsername(u.getCUsuario());
			List<RoleDTO> roles = new ArrayList<RoleDTO>();
			for (MaeRolUsuario ru : u.getMaeRolUsuarios()) {
				RoleDTO r = new RoleDTO();
				r.setId((long) ru.getMaeRol().getNRol());
				r.setNombre(ru.getMaeRol().getCRol());
				roles.add(r);
			}
			us.setRoles(roles);
			logger.info("{} recupera accesos por id: {}", cuo, idUsuario);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("{} Error al recuperar datos de usuarios: {}", cuo, e.getMessage());
		}
		return us;
	}

	@Override
	public ResponseUsuarioDTO recuperarInfomarcionUsuarios(String cuo, RequestLoginDTO login) throws Exception {
		ResponseUsuarioDTO user = new ResponseUsuarioDTO();
		Object[] params = { login.getIdAplicativo(), login.getCodigoCliente(), login.getCodigoRol(), login.getCodigoUsuario() };
		try {	
			TypedQuery<MaeUsuario> query = this.sf.getCurrentSession().createNamedQuery(MaeRolUsuario.VALIDAR_ACCESOS_USUARIO, MaeUsuario.class);
			query.setParameter(MaeRolUsuario.P_ID_APLICATIVO, login.getIdAplicativo());
			query.setParameter(MaeRolUsuario.P_COD_CLIENTE, login.getCodigoCliente());
			query.setParameter(MaeRolUsuario.P_COD_ROL, login.getCodigoRol());
			query.setParameter(MaeRolUsuario.P_COD_USUARIO, login.getCodigoUsuario());			
			MaeUsuario usr =  query.getSingleResult();				
			if(usr != null) {
				//String claveFinal = EncryptUtils.encryptPastFrass();
				if(Utils.isNull(login.getClave()).equals(usr.getCClave())) {
					user.setIdUsuario(usr.getNUsuario());
					user.setEstado(usr.getLActivo());
					user.setNombreUsuario(usr.getCUsuario());
					user.setCodigoUsuario(login.getCodigoUsuario());
					user.setNombreCliente(usr.getMaeCliente().getXCliente());
					user.setNombreAplicativo("");
					List<ResponseRolDTO> lista = new ArrayList<ResponseRolDTO>();
					for (MaeRolUsuario ru : usr.getMaeRolUsuarios()) {
						ResponseRolDTO r = new ResponseRolDTO(ru.getNRolUsuario(), ru.getMaeRol().getCRol(), ru.getMaeRol().getXRol(), ru.getLActivo());
						lista.add(r);
					}
					user.setRoles(lista);
				} 
			}
			logger.info(Arrays.toString(params));
		} catch (NoResultException not) {
			logger.info(cuo.concat("No se encontro usuario registrado en BD").concat(Arrays.toString(params)));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(cuo.concat(e.getMessage()));
		}
		return user;
	}

	@Override
	public List<ResponseAccesoMetodoDTO> validaAccesoMetodo(String cuo, RequestValidaMetodoDTO acceso) throws Exception {
		List<ResponseAccesoMetodoDTO> lista = new ArrayList<ResponseAccesoMetodoDTO>();
		Object[] params = { acceso.getCodigoUsuario(), acceso.getCodigoRol(), acceso.getApi(), acceso.getIdAplicativo() };
		try {	
			TypedQuery<MaeOperacion> query = this.sf.getCurrentSession().createNamedQuery(MaeRolUsuario.VALIDAR_ACCESO_METODO, MaeOperacion.class);
			query.setParameter(MaeRolUsuario.P_ID_APLICATIVO, acceso.getIdAplicativo());
			query.setParameter(MaeRolUsuario.P_COD_USUARIO, acceso.getCodigoUsuario());
			query.setParameter(MaeRolUsuario.P_COD_ROL, acceso.getCodigoRol());
			query.setParameter(MaeRolUsuario.P_METODO_OPERACION, acceso.getApi());	
			List<MaeOperacion> usr =  query.getResultList();			
			for (MaeOperacion ac : usr) {
				ResponseAccesoMetodoDTO res = new ResponseAccesoMetodoDTO(ac.getXEndpoint(), (ac.getLActivo().equals("1") ? true: false), acceso.getCodigoRol(), acceso.getCodigoUsuario());
				lista.add(res);
			}
			logger.info(Arrays.toString(params));
		} catch (NoResultException not) {
			logger.info(cuo.concat("No se encontro usuario registrado en BD").concat(Arrays.toString(params)));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(cuo.concat(e.getMessage()));
		}
		return lista;
	}
	
	@Override
	public List<ResponseAccesoMetodoDTO> validaAccesoMetodoPorAplicativo(String cuo, Integer idAplicativo) throws Exception {
		List<ResponseAccesoMetodoDTO> lista = new ArrayList<ResponseAccesoMetodoDTO>();
		Object[] params = { idAplicativo };
		try {	
			TypedQuery<Object[]> query = this.sf.getCurrentSession().createNamedQuery(MaeRolUsuario.VALIDAR_ACCESO_METODO_POR_APLICATIVO, Object[].class);
			query.setParameter(MaeRolUsuario.P_ID_APLICATIVO, idAplicativo);
			List<Object[]> usr =  query.getResultList();			
			for (Object[] ac : usr) {
				ResponseAccesoMetodoDTO res = new ResponseAccesoMetodoDTO(Utils.isNull(ac[0]), true, Utils.isNull(ac[1]), Utils.isNull(ac[2]));
				lista.add(res);
			}
			logger.info(Arrays.toString(params));
		} catch (NoResultException not) {
			logger.info(cuo.concat("No se encontro usuario registrado en BD").concat(Arrays.toString(params)));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(cuo.concat(e.getMessage()));
		}
		return lista;
	}
}
