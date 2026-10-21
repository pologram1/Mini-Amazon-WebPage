/**
 * CPEN 410 - Project 1
 * Clase: MySQLConnector 
 *Clase encargada de manejar la conexión con la base de datos MySQL
 * utilizada por el sistema MiniAmazon. Provee métodos para ejecutar
 * operaciones básicas como SELECT, INSERT y UPDATE.
 * Esta clase establece la conexión mediante JDBC y permite la ejecución
 * de consultas utilizando un objeto {@link Statement}.
 */
 package ut;

import java.sql.*;

public class MySQLConnector {

    /** URL de conexión a la base de datos MySQL. */
    private String DB_URL = System.getenv("MINIAMAZON_DB_URL");

    /** Usuario de la base de datos. */
    private String USER = System.getenv("MINIAMAZON_DB_USER");

    /** Contraseña del usuario de la base de datos. */
    private String PASS = System.getenv("MINIAMAZON_DB_PASSWORD");

    /** Objeto Connection que mantiene la conexión activa. */
    private Connection conn;

    /** Objeto Statement utilizado para ejecutar consultas SQL. */
    private Statement stmt;

    /**
     * Método: Constructor que inicializa los objetos de conexión
     * sin establecer aún la conexión a la base de datos.
     */
    public MySQLConnector() {
        conn = null;
        stmt = null;
    }

    /**
     * Método: Establece la conexión con la base de datos MySQL
     * y crea un Statement con capacidad de desplazamiento y actualización.
     * @throws Exception si ocurre un error al cargar el driver o conectar.
     */
    public void doConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");

        conn = DriverManager.getConnection(DB_URL, USER, PASS);

        stmt = conn.createStatement(
            ResultSet.TYPE_SCROLL_INSENSITIVE,
            ResultSet.CONCUR_UPDATABLE
        );
    }

    /**
     * Método: Devuelve el objeto Connection activo.
     * @return conexión activa a la base de datos.
     */
    public Connection getConnection() {
        return conn;
    }

    /**
     * Método: Cierra el Statement y la conexión a la base de datos.
     * @throws Exception si ocurre un error al cerrar los recursos.
     */
    public void closeConnection() throws Exception {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
    }

    /**
     * Método: Ejecuta una consulta SELECT básica sin cláusula WHERE.
     * @param fields campos a seleccionar.
     * @param tables tablas desde donde se seleccionan los datos.
     * @return ResultSet con los resultados de la consulta.
     * @throws Exception si ocurre un error al ejecutar la consulta.
     */
    public ResultSet doSelect(String fields, String tables) throws Exception {
        String sql = "SELECT " + fields + " FROM " + tables;
        System.out.println(sql);
        return stmt.executeQuery(sql);
    }

    /**
     * Método: Ejecuta una consulta SELECT con cláusula WHERE.
     *
     * @param fields campos a seleccionar.
     * @param tables tablas desde donde se seleccionan los datos.
     * @param where condición WHERE de la consulta.
     * @return ResultSet con los resultados de la consulta.
     * @throws Exception si ocurre un error al ejecutar la consulta.
     */
    public ResultSet doSelect(String fields, String tables, String where) throws Exception {
        String sql = "SELECT " + fields + " FROM " + tables + " WHERE " + where;
        System.out.println(sql);
        return stmt.executeQuery(sql);
    }

    /**
     * Método: Ejecuta una consulta SELECT con cláusula WHERE y ORDER BY.
     * @param fields campos a seleccionar.
     * @param tables tablas desde donde se seleccionan los datos.
     * @param where condición WHERE de la consulta.
     * @param orderBy campo por el cual se ordenarán los resultados.
     * @return ResultSet con los resultados de la consulta.
     * @throws Exception si ocurre un error al ejecutar la consulta.
     */
    public ResultSet doSelect(String fields, String tables, String where, String orderBy) throws Exception {
        String sql = "SELECT " + fields + " FROM " + tables + " WHERE " + where + " ORDER BY " + orderBy;
        System.out.println(sql);
        return stmt.executeQuery(sql);
    }

    /**
     * Método: Ejecuta una operación INSERT en la base de datos.
     * @param table tabla donde se insertarán los datos.
     * @param values valores a insertar, en formato SQL.
     * @return true si la inserción fue exitosa; false en caso contrario.
     * @throws Exception si ocurre un error al ejecutar la consulta.
     */
    public boolean doInsert(String table, String values) throws Exception {
        String sql = "INSERT INTO " + table + " VALUES (" + values + ")";
        System.out.println(sql);
        return stmt.executeUpdate(sql) > 0;
    }

    /**
     * Método: Ejecuta una consulta UPDATE, DELETE o cualquier instrucción SQL
     * que modifique datos o estructura.
     * @param sql instrucción SQL completa a ejecutar.
     * @return número de filas afectadas por la operación.
     * @throws Exception si ocurre un error al ejecutar la consulta.
     */
    public int doUpdate(String sql) throws Exception {
        System.out.println(sql);
        return stmt.executeUpdate(sql);
    }
}