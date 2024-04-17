package pe.gob.pj.model.utils;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.text.Normalizer;
import java.util.regex.Pattern;

public class Utils {
	
	public static String obtenerCodigoUnico() {
		Date fechaActual = new Date();
		SimpleDateFormat formato = new SimpleDateFormat("yyyyMMddHHmmssSSSSSS");
		String strFechaActual = formato.format(fechaActual);
		Random random = new Random();
		int aleatorio = random.nextInt(999) + 1;
		StringBuilder cuo = new StringBuilder();
		cuo.append(strFechaActual).append(String.valueOf(aleatorio));
		return "["+cuo.toString()+"]";
	}
	
	public static void setParameter(CallableStatement cs, int index, int type, Object value) throws Exception {
		setParameter(cs, index, type, value, false);
	}
	
	public static void setParameter(CallableStatement cs, int index, int type, Object value,
			boolean parseDateToTimestamp) throws Exception {
		try {
			if (parseDateToTimestamp == true && type == Types.DATE) {
				type = Types.TIMESTAMP;
			}

			if (value == null || value.toString().length() == 0) {
				cs.setNull(index, type);
				return;
			}
			switch (type) {
			case Types.VARCHAR:
				cs.setString(index, value.toString());
				break;
			case Types.CHAR:
				cs.setString(index, value.toString());
				break;		
			case Types.INTEGER:
				cs.setInt(index, parseInteger(value));
				break;
			case Types.NUMERIC:
				cs.setBigDecimal(index, parseBigDecimal(value));
				break;
			case Types.DOUBLE:
				cs.setDouble(index, parseDouble(value));
				break;
			case Types.LONGVARCHAR:
				cs.setLong(index, parseLong(value));
				break;	
			case Types.TIMESTAMP:
				if (value.getClass().getName().equals("java.util.Date")) {
					cs.setTimestamp(index, getSqlTimestamp((java.util.Date) value));
				}
				else if(value.getClass().getName().equals("java.sql.Timestamp")){
					cs.setTimestamp(index, (java.sql.Timestamp) value);
				}
				else {
					cs.setTimestamp(index, getSqlTimestamp(value.toString()));
				}
				break;
			case Types.DATE:
				if (value.getClass().getName().equals("java.util.Date")) {
					cs.setDate(index, getSqlDate((java.util.Date) value));
					// stmt.setTimestamp(index,
					// getSqlTimestamp((java.util.Date)value));
				} else {
					cs.setDate(index, getSqlDate(value.toString()));
				}
				break;
			}
		} catch (Exception e) {
			cs.setNull(index, type);
		}
	}
	
	public static void getParameter(CallableStatement cs, int index, int type) throws Exception {
		
		try {
			switch (type) {
			case Types.VARCHAR:
				cs.registerOutParameter(index, Types.VARCHAR);
				break;
			case Types.INTEGER:
				cs.registerOutParameter(index, Types.INTEGER);
				break;
			case Types.NUMERIC:
				cs.registerOutParameter(index, Types.NUMERIC);
				break;
			case Types.DOUBLE:
				cs.registerOutParameter(index, Types.DOUBLE);
				break;
			case Types.BIGINT:
				cs.registerOutParameter(index, Types.BIGINT);
				break;	
			case Types.TIMESTAMP:
				cs.registerOutParameter(index, Types.TIMESTAMP);
				break;
			case Types.DATE:
				cs.registerOutParameter(index, Types.DATE);
				break;
			}			
		} catch (Exception e) {
		}
	}
	
	/**
	 * Metodo que retorna una lista de objectos Maps que es recogido desde la base de datos. .
	 * @param rs variable ResultSet de SQL
	 * @throws Exception
	 */
	public static List<Map<String, Object>> getMapedObjects(ResultSet rs) throws SQLException {
		List<Map<String, Object>> list = null;
		ResultSetMetaData rsmd = null;
		try {
			if (rs != null) {
				rsmd = rs.getMetaData();
				list = new ArrayList<>();
				while (rs.next()) {
					Map<String, Object> row = new HashMap<>();
					for (int j = 1; j <= rsmd.getColumnCount(); ++j) {
						//System.out.println(rsmd.getColumnLabel(j).toString());
						if (rs.getObject(j) != null) {
							row.put(rsmd.getColumnLabel(j), rs.getObject(j));
						} else {
							row.put(rsmd.getColumnLabel(j), "");
						}
					}
					list.add(row);
				}
			}
		} catch (SQLException e) {
				throw new SQLException("Error de SQL al mapear: "+ e.getMessage());
		}
		return list;
	}
	
	public static Integer parseInteger(Object value, Integer defaultValue) throws Exception {
		try {
			return Integer.parseInt(value.toString());
		} catch (Exception e) {
			throw new Exception(Constants.Mensajes.MSG_ERROR_GENERICO_CONVERSION+" ["+e.getMessage()+"]");
		}
	}

	public static Integer parseInteger(Object value) throws Exception {
		try {
			return Integer.parseInt(value.toString());
		} catch (Exception e) {
			throw new Exception(Constants.Mensajes.MSG_ERROR_GENERICO_CONVERSION+" ["+e.getMessage()+"]");
		}
	}

	public static BigDecimal parseBigDecimal(Object value) throws Exception {
		try {
			//return BigDecimal.valueOf(Double.parseDouble(value.toString()));
			return new BigDecimal(value.toString());
		} catch (Exception e) {
			throw new Exception(Constants.Mensajes.MSG_ERROR_GENERICO_CONVERSION+" ["+e.getMessage()+"]");
		}
	}

	public static Double parseDouble(Object value) throws Exception {
		try {
			return Double.parseDouble(value.toString());
		} catch (Exception e) {
			throw new Exception(Constants.Mensajes.MSG_ERROR_GENERICO_CONVERSION+" ["+e.getMessage()+"]");
		}
	}
	
	public static Long parseLong(Object value) throws Exception {
		try {
			return Long.parseLong(value.toString());
		} catch (Exception e) {
			throw new Exception(Constants.Mensajes.MSG_ERROR_GENERICO_CONVERSION+" ["+e.getMessage()+"]");
		}
	}

	public static java.sql.Date getSqlDate(String fecha) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		try {
			java.util.Date aDate = format.parse(fecha);
			java.text.SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			java.sql.Date sqlDate = java.sql.Date.valueOf(sdf.format(aDate));
			return sqlDate;
		} catch (ParseException e) {
			throw new Exception(Constants.Mensajes.MSG_ERROR_GENERICO_CONVERSION+" ["+e.getMessage()+"]");
		}
	}

	public static java.sql.Date getSqlDateFull(String fecha) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		try {
			java.util.Date aDate = format.parse(fecha);
			java.text.SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			java.sql.Date sqlDate = java.sql.Date.valueOf(sdf.format(aDate));
			return sqlDate;
		} catch (ParseException e) {
			throw new Exception(Constants.Mensajes.MSG_ERROR_GENERICO_CONVERSION+" ["+e.getMessage()+"]");
		}
	}
	
	public static Date getSqlDateFullDos(String fecha) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		try {
			java.util.Date aDate = format.parse(fecha);
			return aDate;
		} catch (ParseException e) {
			throw new Exception(Constants.Mensajes.MSG_ERROR_GENERICO_CONVERSION+" ["+e.getMessage()+"]");
		}
	}
	
	public static java.sql.Date getSqlDate(java.util.Date fecha) {
		try {
			return new java.sql.Date(fecha.getTime());
		} catch (Exception e) {
			return null;
		}
	}

	public static java.sql.Timestamp getSqlTimestamp(java.util.Date fecha) {
		try {
			return new java.sql.Timestamp(fecha.getTime());
		} catch (Exception e) {
			return null;
		}
	}

	public static java.sql.Timestamp getSqlTimestamp(String fecha) {
		try {
			return getSqlTimestamp(getSqlDateFull(fecha));
		} catch (Exception e) {
			return null;
		}
	}

	public static String getSqlDate(java.sql.Date fecha) {
		if (fecha == null) {
			return "";
		}
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date jDate = new java.util.Date(fecha.getTime());
		return format.format(jDate);
	}
	
	/**
	 * Si el objeto de entrada es nulo devuelve String "", caso contrario, devuelve el o.toString().trim() 
	 * @param o es el valor object
	 */
	public static String validateStr(Object o) {
		if (o == null) {
			return "";
		} else {
			return o.toString().trim();
		}
	}
	
	/**
	 * Si el objeto de entrada es nulo devuelve un Objecto string "", caso contrario, devuelve el objecto mismo
	 * @param o es el valor object
	 */
	public static Object validateObj(Object o) {
		if (o == null) {
			return "";
		} else {
			if (o instanceof String) {
		        return ((String) o).trim();
		    }
		    else {
		    	return o;
		    }
		}
	}
	
	/**
	 * Metodo que completa los a uns string la cantidad de caracteres indicada a la izquierda.
	 * @param str es el objecto a completar
	 * @param len es la longitud solicitada
	 * @param pad caracter completado a la izquierda
	 */
	public static String lpad(Object str, int len,String pad) {
    	String padTemp = ""; 
    	for(int i=1;i<len;i++){
    		padTemp=padTemp+pad;
    	}
    	String unido = padTemp + str.toString();
        return (unido).substring(unido.length() - len);
    }
	
	public static String convertExceptionToString(Exception e) {
		String exception="";
		if(e != null) {
			CharArrayWriter cw = new CharArrayWriter();
	        PrintWriter w = new PrintWriter(cw);
	        e.printStackTrace(w);
	        w.close();
	        exception = cw.toString();
		} else {
			exception="SE HA PRODUCIDO UNA EXCEPTION NO IDENTIFICADA";
		}
        return exception;
	}
	
	public static String generateCodDataBase(String codDistrito, String codInstacia, String codEspecialidad) {
		String codBaseDatos = null;
		if(validateStr(codDistrito).length() > 0 && validateStr(codInstacia).length()>0 && validateStr(codEspecialidad).length() > 0) {
			codBaseDatos= codDistrito+codInstacia+codEspecialidad;
		} 
		return codBaseDatos;
	}
	
	public static String isNull(Object val) {
		if(val == null) {
			return "";
		} else {
			return val.toString();
		}
	}
	
	public static Integer isInt(Object val) {
		if(val == null) {
			return 0;
		} else {
			return Integer.parseInt(val.toString());
		}
	}
	
	public static long isLong(Object val) {
		if(val == null) {
			return 0L;
		} else {
			return Long.parseLong(val.toString());
		}
	}
	
	public static BigDecimal isBigDecimal(Object val) {
		if(val == null) {
			return BigDecimal.ZERO;
		} else {
			return new BigDecimal(val.toString());
		}
	}
	
	public static Timestamp isDate(Object val) throws Exception {
		if(val == null) {
			return null;
		} else {
			return new Timestamp(getSqlDateFullDos(val.toString()).getTime());
		}
	}
	
	public static String quitarCaracteres(String val) {
    	if(val != null) {
    		//val.replaceAll("[^a-zA-Z]+",""); //.replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u").replace("ñ", "n");
    		Normalizer.normalize(val, Normalizer.Form.NFD);
            val.replaceAll("[^\\p{ASCII}]", "");
            Pattern DIACRITICS_AND_FRIENDS
            = Pattern.compile("[\\p{InCombiningDiacriticalMarks}\\p{IsLm}\\p{IsSk}]+");
            val = Normalizer.normalize(val, Normalizer.Form.NFD);
            val = DIACRITICS_AND_FRIENDS.matcher(val).replaceAll("");
    	}
    	return val;	
    }	
	
	public static String getMac() {
		String result = "MAC";
		return result;
	}

	public static String getPcName() {
		String result = "ServidorWAS7";
		result = "ServidorJboss6.4";
		return result;
	}

	public static String getIp() {
		String result = "127.0.0.1";
		InetAddress localIp;
		try {
			localIp = InetAddress.getLocalHost();
			result = localIp.getHostAddress().toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
}