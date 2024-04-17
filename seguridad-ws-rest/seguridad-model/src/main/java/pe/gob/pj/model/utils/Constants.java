package pe.gob.pj.model.utils;

public class Constants {
    
	public static final String VERSION = "{\"aplicativo\":\"CONSULTA-REQUISITORIA-REST\",\"version\":\"1.0.0\"}";
	
	public static final String QUERY_RESULT = "result";
    
    public static final String AUD_CUO="cuo";
    public static final String AUD_LOG="log";
    public static final String HASH_MAP_LOCAL="infoLocal";
    public static final String REMOTE_IP = "ipRemota";
    public static final String TOKEN_ADMIN = "tokenAdmin";
    public static final String VALID_TOKEN_ADMIN="validTokenAdmin";
    
    public static final String C_500 = "500";
    public static final String C_404 = "404";
    public static final String C_200 = "200";
    public static final String C_400 = "400";
    public static final String C_401 = "401"; 
    
    public static final String INACTIVO		= "0";
	public static final String LETRA_VACIO 	= "";
	
	public class Mensajes {		
		public static final String MSG_ERROR_GENERICO_CONVERSION = "El tipo de dato de entrada es incorrecto";
	}
	
	public class Seguridad {		
		public static final String JDNI_SEGURIDAD = "configuracion.conexion.seguridad.jndi";
		public static final String ID_APLICATIVO = "configuracion.seguridad.idaplicativo";
		public static final String SECRET_TOKEN = "configuracion.seguridad.secretToken";
	}
	
	public class Reniec {		
		public static final String RENIEC_ENDPOINT = "configuracion.reniec.endpoint";
		public static final String RENIEC_DNI_CONSULTA = "configuracion.reniec.dni";
		public static final String RENIEC_USUARIO_CONSULTA = "configuracion.reniec.usuario";
	}
	
	public class Ftp {
		public static final String FTP_HOST = "configuracion.ftp.ip";
		public static final String FTP_USUARIO = "configuracion.ftp.usuario";
		public static final String FTP_CLAVE = "configuracion.ftp.clave";
	}
	
	
}