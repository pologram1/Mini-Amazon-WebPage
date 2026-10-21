/**
 *CPEN 410 - Project 1
 * Clase: PasswordUtil
 Clase utilitaria encargada de generar hashing SHA-256 para contraseñas
 * y verificar coincidencias entre contraseñas en texto plano y hashes
 * previamente almacenados. Esta clase se utiliza como apoyo en los procesos
 * de autenticación del sistema MiniAmazon.
 */
 
package ut;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
public class PasswordUtil {

    /**
     * Método: Genera un hash SHA-256 en formato hexadecimal a partir
     * de una contraseña en texto plano.
     * @param password contraseña en texto plano.
     * @return hash SHA-256 en formato hexadecimal.
     * @throws Exception si ocurre un error al obtener la instancia del algoritmo.
     */
    public static String hashPassword(String password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * Método: Verifica si una contraseña en texto plano coincide con
     * un hash previamente almacenado, aplicando hashing SHA-256 al valor recibido.
     * @param rawPassword contraseña en texto plano ingresada por el usuario.
     * @param storedHash hash almacenado en la base de datos.
     * @return true si ambos valores coinciden; false en caso contrario.
     * @throws Exception si ocurre un error durante el proceso de hashing.
     */
    public static boolean matches(String rawPassword, String storedHash) throws Exception {
        return hashPassword(rawPassword).equals(storedHash);
    }
}