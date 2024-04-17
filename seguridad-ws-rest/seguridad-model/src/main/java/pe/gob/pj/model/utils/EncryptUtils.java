package pe.gob.pj.model.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

@SuppressWarnings("restriction")
public class EncryptUtils {

	static final Logger logger = Logger.getLogger(EncryptUtils.class.getName());
	
	private static String secretKey = ConfiguracionPropiedades.getInstance().getProperty("configuracion.seguridad.secretToken").toString();
	
	//private static final char[] PASSWORD = "31819fcb00a6d1e00cb926b17cb9136a89ac58ba2b907e9f87c1f1775914b881f892af0061a12a2005000225896d1b41de900eb94b57cae08e972e39f230b95c4b58b8".toCharArray(); 
//	private static final char[] PASSWORD = (SECRET_TOKEN).toCharArray();
    private static final byte[] SALT = { (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12, (byte) 0xde, (byte) 0x33,(byte) 0x10, (byte) 0x12, };

	public static String MD5 = "MD5";
	public static String SHA512 = "SHA-512";

	/**
	 * Convert array of byte to hexadecimal
	 * @param digest Array of Byte
	 * @return The String hexadecimal
	 */
	private static String toHexadecimal(byte[] digest) {

		StringBuilder hash = new StringBuilder();

		byte[] arrayOfByte = digest;
		int j = digest.length;

		for (int i = 0; i < j; i++) {
			byte aux = arrayOfByte[i];
			int b = aux & 0xFF;
			if (Integer.toHexString(b).length() == 1) {
				hash.append("0");
			}
			hash.append(Integer.toHexString(b));
		}
		return hash.toString();
	}

	/**
	 * Convert array of byte to hexadecimal 5012
	 * @param digest Array of byte
	 * @return The string hexadecimal 512
	 */
	private static String toHexadecimal512(byte[] digest) {

		StringBuilder hash = new StringBuilder();

		byte[] arrayOfByte = digest;
		int j = digest.length;

		for (int i = 0; i < j; i++) {
			byte aux = arrayOfByte[i];
			int b = aux & 0xFF;
			if (Integer.toHexString(b).length() == 1) {
				hash.append("00");
			}
			hash.append(Integer.toHexString(b));
		}
		return hash.toString();
	}

	/**
	 * Encryption MD5
	 * @param message The message to encrypt
	 * @return The encrypted message
	 */
	public static String encryptMd5Hash(String message) {

		byte[] digest = null;
		byte[] buffer = message.getBytes();

		try {
			MessageDigest messageDigest = MessageDigest.getInstance(MD5);
			messageDigest.reset();
			messageDigest.update(buffer);
			digest = messageDigest.digest();
		} catch (NoSuchAlgorithmException ex) {
			logger.log(Level.WARNING, "Error creating digest MD5");
		}
		return toHexadecimal(digest);
	}

	/**
	 * Encryption SHA512
	 * @param message The message to encrypt
	 * @return The encrypted message
	 */
	public static String encryptSHA512Hash(String message) {

		byte[] digest = null;
		byte[] buffer = message.getBytes();

		try {
			MessageDigest messageDigest = MessageDigest.getInstance(SHA512);
			messageDigest.reset();
			messageDigest.update(buffer);
			digest = messageDigest.digest();
		} catch (NoSuchAlgorithmException ex) {
			logger.log(Level.WARNING, "Error creating digest SHA512");
		}
		return toHexadecimal512(digest);
	}

	/**
	 * Custom encryption for two messages
	 * @param firstMessage  The first message
	 * @param secondMessage The second message
	 * @return The encrypted message
	 * @throws GeneralSecurityException 
	 * @throws UnsupportedEncodingException 
	 */
	public static String encrypt(String firstMessage, String secondMessage) throws UnsupportedEncodingException, GeneralSecurityException {
		String clave = encryptPastFrass(secondMessage);
		return encryptSHA512Hash(encryptMd5Hash(firstMessage.toLowerCase()) + clave);
	}
	
	public static String encryptPastFrass(String property) throws GeneralSecurityException, UnsupportedEncodingException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec((secretKey.toCharArray())));
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
        pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
        return base64Encode(pbeCipher.doFinal(property.getBytes("UTF-8")));
    }

	
	public static String encryptPastFrass(String token, String clave) throws GeneralSecurityException, UnsupportedEncodingException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec((token.toCharArray())));
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
        pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
        return base64Encode(pbeCipher.doFinal(clave.getBytes("UTF-8")));
    }
	
	
	public static String decryptPastFrass(String property) throws GeneralSecurityException, IOException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(secretKey.toCharArray()));
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
        pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
        return new String(pbeCipher.doFinal(base64Decode(property)), "UTF-8");
    }
	
	
	public static String decryptPastFrass(String token, String clave) throws GeneralSecurityException, IOException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(token.toCharArray()));
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
        pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
        String claveDesencriptada=new String(pbeCipher.doFinal(base64Decode(clave)), "UTF-8");
        return claveDesencriptada;
    }
	
	private static String base64Encode(byte[] bytes) {
        return new BASE64Encoder().encode(bytes);
    }

	private static byte[] base64Decode(String property) throws IOException {
        return new BASE64Decoder().decodeBuffer(property);
    }	
}