package pe.gob.pj.seguridad.config;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import pe.gob.pj.model.entity.Usuario;
import pe.gob.pj.model.utils.Utils;
import pe.gob.pj.seguridad.service.UsuarioService;
import pe.gob.pj.seguridad.service.impl.UsuarioServiceImpl;

@Component
public class AgregarInformacionJwt implements TokenEnhancer{

	private static final Logger logger = LoggerFactory.getLogger(UsuarioServiceImpl.class);
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		String cuo = Utils.obtenerCodigoUnico();
		Map<String, Object> info = new HashMap<String, Object>();		
		Usuario usuario;
		try {	
			usuario = usuarioService.findByUsername(cuo, authentication.getName());
			info.put("nombre", usuario.getNombre());
			info.put("apellido", usuario.getApellido());
			info.put("correo", usuario.getEmail());
			logger.info("{} Usuario autenticado :{}", cuo, usuario.getUsername());
			((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);	
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("{} Error al recuperar data de usuario: {}", cuo, e.getMessage());
		}			
		return accessToken;
	}

	
}