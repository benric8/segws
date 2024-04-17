package pe.gob.pj.model.entity.seg;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the mae_rol_usuario database table.
 * 
 */
@Entity
@Table(name="mae_rol_usuario", schema = "seguridad")
@NamedQuery(name="MaeRolUsuario.findAll", query="SELECT m FROM MaeRolUsuario m")
@NamedQuery(name= MaeRolUsuario.VALIDAR_ACCESO_METODO, query="SELECT op FROM MaeRolUsuario ru "
		+ "JOIN ru.maeRol r "
		+ "JOIN r.maeOperacions op "
		+ "JOIN op.maeAplicativo ap "
		+ "JOIN ru.maeUsuario u "
		+ "WHERE u.lActivo = '1' AND ru.lActivo = '1' AND r.lActivo = '1' "
		+ "AND op.lActivo = '1' AND ap.nAplicativo = :idAplicativo AND r.cRol = :codRol AND u.cUsuario = :codUsuario AND op.xEndpoint =:operacion " )
@NamedQuery(name= MaeRolUsuario.VALIDAR_ACCESO_METODO_POR_APLICATIVO, query="SELECT op.xEndpoint, r.cRol, u.cUsuario FROM MaeRolUsuario ru "
		+ "JOIN ru.maeRol r "
		+ "JOIN r.maeOperacions op "
		+ "JOIN op.maeAplicativo ap "
		+ "JOIN ru.maeUsuario u "
		+ "WHERE u.lActivo = '1' AND ru.lActivo = '1' AND r.lActivo = '1' "
		+ "AND op.lActivo = '1' AND ap.nAplicativo = :idAplicativo " )
@NamedQuery(name= MaeRolUsuario.VALIDAR_ACCESOS_USUARIO, query="SELECT u FROM MaeRolUsuario ru "
		+ "JOIN ru.maeRol r "
		+ "JOIN r.maeOperacions op "
		+ "JOIN op.maeAplicativo ap "
		+ "JOIN ru.maeUsuario u "
		+ "JOIN u.maeCliente c "
		+ "WHERE u.lActivo = '1' AND ru.lActivo = '1' AND r.lActivo = '1' "
		+ "AND op.lActivo = '1' AND ap.lActivo = '1' AND c.lActivo = '1' "
		+ "AND ap.nAplicativo = :idAplicativo AND c.cCliente = :codCliente "
		+ "AND r.cRol = :codRol AND u.cUsuario = :codUsuario " )
public class MaeRolUsuario implements Serializable {
	private static final long serialVersionUID = 1L;

    public static final String VALIDAR_ACCESOS_USUARIO = "MaeAplicativoRolOperacion.validarAccesosUsuario";
    public static final String VALIDAR_ACCESO_METODO = "MaeAplicativoRolOperacion.validaAccesoMetodo";
    public static final String VALIDAR_ACCESO_METODO_POR_APLICATIVO = "MaeAplicativoRolOperacion.validaAccesoMetodoAplicativo";
    
	public static final String P_COD_USUARIO = "codUsuario";
	public static final String P_CLAVE = "clave";
	public static final String P_COD_CLIENTE = "codCliente";
	public static final String P_COD_ROL = "codRol";
	public static final String P_METODO_OPERACION = "operacion";
	public static final String P_ID_APLICATIVO = "idAplicativo";
	
	@Id
	@Column(name="n_rol_usuario")
	private Integer nRolUsuario;

	@Column(name="b_aud")
	private String bAud;

	@Column(name="c_aud_ip")
	private String cAudIp;

	@Column(name="c_aud_mcaddr")
	private String cAudMcaddr;

	@Column(name="c_aud_pc")
	private String cAudPc;

	@Column(name="c_aud_uid")
	private String cAudUid;

	@Column(name="c_aud_uidred")
	private String cAudUidred;

	@Column(name="f_aud")
	private Timestamp fAud;

	@Column(name="l_activo")
	private String lActivo;

	//bi-directional many-to-one association to MaeRol
	@ManyToOne
	@JoinColumn(name="n_rol")
	private MaeRol maeRol;

	//bi-directional many-to-one association to MaeUsuario
	@ManyToOne
	@JoinColumn(name="n_usuario")
	private MaeUsuario maeUsuario;

	public MaeRolUsuario() {
	}

	public Integer getNRolUsuario() {
		return this.nRolUsuario;
	}

	public void setNRolUsuario(Integer nRolUsuario) {
		this.nRolUsuario = nRolUsuario;
	}

	public String getBAud() {
		return this.bAud;
	}

	public void setBAud(String bAud) {
		this.bAud = bAud;
	}

	public String getCAudIp() {
		return this.cAudIp;
	}

	public void setCAudIp(String cAudIp) {
		this.cAudIp = cAudIp;
	}

	public String getCAudMcaddr() {
		return this.cAudMcaddr;
	}

	public void setCAudMcaddr(String cAudMcaddr) {
		this.cAudMcaddr = cAudMcaddr;
	}

	public String getCAudPc() {
		return this.cAudPc;
	}

	public void setCAudPc(String cAudPc) {
		this.cAudPc = cAudPc;
	}

	public String getCAudUid() {
		return this.cAudUid;
	}

	public void setCAudUid(String cAudUid) {
		this.cAudUid = cAudUid;
	}

	public String getCAudUidred() {
		return this.cAudUidred;
	}

	public void setCAudUidred(String cAudUidred) {
		this.cAudUidred = cAudUidred;
	}

	public Timestamp getFAud() {
		return this.fAud;
	}

	public void setFAud(Timestamp fAud) {
		this.fAud = fAud;
	}

	public String getLActivo() {
		return this.lActivo;
	}

	public void setLActivo(String lActivo) {
		this.lActivo = lActivo;
	}

	public MaeRol getMaeRol() {
		return this.maeRol;
	}

	public void setMaeRol(MaeRol maeRol) {
		this.maeRol = maeRol;
	}

	public MaeUsuario getMaeUsuario() {
		return this.maeUsuario;
	}

	public void setMaeUsuario(MaeUsuario maeUsuario) {
		this.maeUsuario = maeUsuario;
	}

}