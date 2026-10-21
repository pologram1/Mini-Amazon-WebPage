/**
 * CPEN 410 - Project 1
 * Clase: UserDAO
 * Clase encargada de manejar todas las operaciones relacionadas con los usuarios
 * dentro del sistema MiniAmazon. Permite verificar existencia de usuarios,
 * registrar nuevos usuarios y recuperar información de un usuario específico.
 * Esta clase utiliza {@link ut.MySQLConnector} para ejecutar consultas SQL
 * y construye objetos {@link model.UserClass} para representar usuarios
 * obtenidos desde la base de datos.
 */

package dao;

import model.UserClass;
import ut.MySQLConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    /**
     * Método: Verifica si un nombre de usuario ya existe en la base de datos.
     * @param username nombre de usuario a verificar.
     * @return true si el usuario existe, false si no.
     * @throws Exception si ocurre un error durante la consulta.
     */
    public boolean existsByUsername(String username) throws Exception {
        MySQLConnector db = new MySQLConnector();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            db.doConnection();

            String sql = "SELECT id_user FROM users WHERE username = ?";
            ps = db.getConnection().prepareStatement(sql);
            ps.setString(1, username);

            rs = ps.executeQuery();
            return rs.next();

        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            db.closeConnection();
        }
    }

    /**
     * Método: Inserta un nuevo usuario en la base de datos.
     * También registra su rol por defecto en la tabla roleuser.
     *
     * @param user objeto UserClass con los datos del usuario.
     * @return true si el usuario fue insertado correctamente, false si no.
     * @throws Exception si ocurre un error durante la inserción.
     */
    public boolean insertUser(UserClass user) throws Exception {
        MySQLConnector db = new MySQLConnector();
        PreparedStatement psUser = null;
        PreparedStatement psRole = null;

        try {
            db.doConnection();
            db.getConnection().setAutoCommit(false);

            String sqlUser = "INSERT INTO users (name, email, password, address, telephone, username, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
            psUser = db.getConnection().prepareStatement(sqlUser);

            psUser.setString(1, user.getName());
            psUser.setString(2, user.getEmail());
            psUser.setString(3, user.getPasswordhash());
            psUser.setString(4, user.getAddress());
            psUser.setString(5, user.getTelephone());
            psUser.setString(6, user.getUsername());
            psUser.setString(7, "user");

            int rows = psUser.executeUpdate();

            if (rows <= 0) {
                db.getConnection().rollback();
                return false;
            }

            String sqlRole = "INSERT INTO roleuser (UserName, RoleId) VALUES (?, ?)";
            psRole = db.getConnection().prepareStatement(sqlRole);
            psRole.setString(1, user.getUsername());
            psRole.setString(2, "rol2");
            psRole.executeUpdate();

            db.getConnection().commit();
            return true;

        } catch (Exception e) {
            if (db.getConnection() != null) {
                db.getConnection().rollback();
            }
            throw e;

        } finally {
            if (psRole != null) psRole.close();
            if (psUser != null) psUser.close();
            if (db.getConnection() != null) {
                db.getConnection().setAutoCommit(true);
            }
            db.closeConnection();
        }
    }

    /**
     * Método: Busca un usuario según su nombre de usuario.
     * @param username nombre de usuario a buscar.
     * @return objeto UserClass si existe, o null si no se encuentra.
     * @throws Exception si ocurre un error durante la consulta.
     */
    public UserClass findByUsername(String username) throws Exception {
        MySQLConnector db = new MySQLConnector();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            db.doConnection();

            String sql = "SELECT id_user, username, name, email, password, address, telephone, role FROM users WHERE username = ?";
            ps = db.getConnection().prepareStatement(sql);
            ps.setString(1, username);

            rs = ps.executeQuery();

            if (rs.next()) {
                UserClass user = new UserClass();
                user.setId_user(rs.getInt("id_user"));
                user.setUsername(rs.getString("username"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPasswordhash(rs.getString("password"));
                user.setAddress(rs.getString("address"));
                user.setTelephone(rs.getString("telephone"));
                user.setRole(rs.getString("role"));
                return user;
            }

            return null;

        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            db.closeConnection();
        }
    }
}