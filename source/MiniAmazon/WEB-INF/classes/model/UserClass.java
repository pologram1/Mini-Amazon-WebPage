/**
 * CPEN 410 - Project 1
 * Clase: UserClass
 * Clase que representa un usuario dentro del sistema MiniAmazon.
 * Contiene los atributos principales asociados a un usuario, tales como:
 * nombre de usuario, hash de contraseña, información personal y rol asignado.
 *
 * Esta clase se utiliza para transportar información entre la capa de datos
 * y la capa de lógica/aplicación.
 */

package model;

public class UserClass {

    /** Identificador único del usuario en la base de datos. */
    private int id_user;

    /** Nombre de usuario utilizado para iniciar sesión. */
    private String username;

    /** Hash SHA-256 de la contraseña del usuario. */
    private String passwordhash;

    /** Nombre completo del usuario. */
    private String name;

    /** Correo electrónico del usuario. */
    private String email;

    /** Dirección física del usuario. */
    private String address;

    /** Número telefónico del usuario. */
    private String telephone;

    /** Rol asignado al usuario dentro del sistema. */
    private String role;

    /**
     * Método: Constructor vacío requerido para instanciación simple
     * y compatibilidad con frameworks o librerías que utilicen reflexión.
     */
    public UserClass() {
    }

    /**
     * Método: Constructor que inicializa un usuario con todos sus atributos,
     * excepto el id, el cual es asignado por la base de datos.
     *
     * @param username nombre de usuario.
     * @param passwordhash hash de la contraseña.
     * @param name nombre completo del usuario.
     * @param email correo electrónico.
     * @param address dirección física.
     * @param telephone número telefónico.
     * @param role rol asignado al usuario.
     */
    public UserClass(String username, String passwordhash, String name, String email,
                     String address, String telephone, String role) {
        this.username = username;
        this.passwordhash = passwordhash;
        this.name = name;
        this.email = email;
        this.address = address;
        this.telephone = telephone;
        this.role = role;
    }

    /**
     * Método: Obtiene el ID del usuario.
     * @return id del usuario.
     */
    public int getId_user() {
        return id_user;
    }

    /**
     * Método: Establece el ID del usuario.
     * @param id_user identificador único del usuario.
     */
    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    /**
     * Método: Obtiene el nombre de usuario.
     * @return nombre de usuario.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Método: Establece el nombre de usuario.
     * @param username nombre de usuario.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Método: Obtiene el hash de la contraseña.
     * @return hash de la contraseña.
     */
    public String getPasswordhash() {
        return passwordhash;
    }

    /**
     * Método: Establece el hash de la contraseña.
     * @param passwordhash hash SHA-256 de la contraseña.
     */
    public void setPasswordhash(String passwordhash) {
        this.passwordhash = passwordhash;
    }

    /**
     * Método: Obtiene el nombre completo del usuario.
     * @return nombre completo.
     */
    public String getName() {
        return name;
    }

    /**
     * Método: Establece el nombre completo del usuario.
     * @param name nombre completo.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Método: Obtiene el correo electrónico del usuario.
     * @return correo electrónico.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Método: Establece el correo electrónico del usuario.
     * @param email correo electrónico.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Método: Obtiene la dirección física del usuario.
     * @return dirección física.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Método: Establece la dirección física del usuario.
     * @param address dirección física.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Método: Obtiene el número telefónico del usuario.
     * @return número telefónico.
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * Método: Establece el número telefónico del usuario.
     * @param telephone número telefónico.
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * Método: Obtiene el rol asignado al usuario.
     * @return rol del usuario.
     */
    public String getRole() {
        return role;
    }

    /**
     * Método: Establece el rol del usuario.
     * @param role rol asignado.
     */
    public void setRole(String role) {
        this.role = role;
    }
}