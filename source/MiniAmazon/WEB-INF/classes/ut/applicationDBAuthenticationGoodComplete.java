/**
 *CPEN 410 - Project 1
 * Clase:applicationDBAuthenticationGoodComplete
 * Clase encargada de manejar la autenticación de usuarios,
 * la verificación de navegación y la inserción de nuevos usuarios
 * en la base de datos del sistema MiniAmazon. Este utiliza la clase {@link MySQLConnector} 
 *para ejecutar consultas SQL y Apache DigestUtils para generar hashing SHA-256.
 */
 package ut;

import java.sql.ResultSet;
import org.apache.commons.codec.digest.DigestUtils;

public class applicationDBAuthenticationGoodComplete {

    /** Conector a la base de datos MySQL. */
    private MySQLConnector myDBConn;

    /**
     * Método:Constructor que inicializa el conector y abre la conexión a la base de datos.
     * @throws Exception si ocurre un error al conectar con la base de datos.
     */
    public applicationDBAuthenticationGoodComplete() throws Exception {
        myDBConn = new MySQLConnector();
        myDBConn.doConnection();
    }

    /**
     * Método:Autentica un usuario verificando su nombre y contraseña hasheada.
     * @param userName nombre de usuario.
     * @param userPass contraseña en texto plano.
     * @return ResultSet con los datos del usuario si la autenticación es válida.
     * @throws Exception si ocurre un error en la consulta SQL.
     */
    public ResultSet authenticate(String userName, String userPass) throws Exception {

        String fields, tables, whereClause, hashingVal;

        tables = "usergood, roleuser";
        fields = "usergood.UserName, roleuser.RoleId, usergood.Name";
        hashingVal = hashingSha256(userName + userPass);

        whereClause = "usergood.UserName = roleuser.UserName " +
                      "AND usergood.UserName = '" + userName + "' " +
                      "AND usergood.hashing = '" + hashingVal + "'";

        return myDBConn.doSelect(fields, tables, whereClause);
    }

    /**
     * Método:Obtiene los elementos del menú permitidos para un usuario según su rol.
     * @param userName nombre del usuario.
     * @return ResultSet con las páginas permitidas.
     * @throws Exception si ocurre un error en la consulta SQL.
     */
    public ResultSet menuElements(String userName) throws Exception {

        String fields, tables, whereClause, orderBy;

        tables = "roleuser, roles, rolewebpagegood, webpagegood";
        fields = "rolewebpagegood.pageURL, webpagegood.pageTitle";
        whereClause = "roleuser.RoleId = roles.roleId " +
                      "AND roles.roleId = rolewebpagegood.RoleId " +
                      "AND rolewebpagegood.pageURL = webpagegood.pageURL " +
                      "AND roleuser.UserName = '" + userName + "'";
        orderBy = "webpagegood.pageTitle";

        return myDBConn.doSelect(fields, tables, whereClause, orderBy);
    }

    /**
     * Método: Verifica si un usuario tiene permiso para acceder a una página actual
     * considerando también la página previa desde la cual navega.
     * @param userName nombre del usuario.
     * @param currentPage URL de la página actual.
     * @param previousPage URL de la página previa.
     * @return ResultSet con los datos del usuario si la navegación es válida.
     * @throws Exception si ocurre un error en la consulta SQL.
     */
    public ResultSet verifyUser(String userName, String currentPage, String previousPage) throws Exception {

        String fields, tables, whereClause;

        tables = "roleuser, roles, rolewebpagegood, webpagegood, usergood, webpageprevious";
        fields = "usergood.UserName, roleuser.RoleId, usergood.Name";

        whereClause = "usergood.UserName = roleuser.UserName " +
                      "AND usergood.UserName = '" + userName + "' " +
                      "AND roles.roleId = roleuser.RoleId " +
                      "AND rolewebpagegood.RoleId = roles.roleId " +
                      "AND rolewebpagegood.pageURL = webpagegood.pageURL " +
                      "AND webpagegood.pageURL = '" + currentPage + "' " +
                      "AND webpageprevious.previouspageURL = '" + previousPage + "' " +
                      "AND webpageprevious.currentpageURL = webpagegood.pageURL";

        return myDBConn.doSelect(fields, tables, whereClause);
    }

    /**
     *Método: Inserta un nuevo usuario en la base de datos aplicando hashing SHA-256
     * a la combinación del nombre de usuario y la contraseña.
     * @param userName nombre del usuario.
     * @param completeName nombre completo del usuario.
     * @param userPass contraseña en texto plano.
     * @param userTelephone teléfono del usuario.
     * @return true si la inserción fue exitosa; false en caso contrario.
     * @throws Exception si ocurre un error en la consulta SQL.
     */
    public boolean addUser(String userName, String completeName, String userPass, String userTelephone) throws Exception {

        String table, values, hashingValue;

        hashingValue = hashingSha256(userName + userPass);
        table = "usergood";
        values = "'" + userName + "', '" + hashingValue + "', '" + completeName + "', '" + userTelephone + "', '', ''";

        boolean res = myDBConn.doInsert(table, values);
        System.out.println("Insertion result: " + res);
        return res;
    }

    /**
     * Método:Genera un hash SHA-256 en formato hexadecimal.
     * @param plainText texto a hashear.
     * @return hash SHA-256 en formato hexadecimal.
     */
    private String hashingSha256(String plainText) {
        return DigestUtils.sha256Hex(plainText);
    }

    /**
     * Método:Cierra la conexión a la base de datos.
     * @throws Exception si ocurre un error al cerrar la conexión.
     */
    public void close() throws Exception {
        myDBConn.closeConnection();
    }
}