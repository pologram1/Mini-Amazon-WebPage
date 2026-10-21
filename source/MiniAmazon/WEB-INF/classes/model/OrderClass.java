/**
 * CPEN 410 - Project 1
 * Clase: OrderClass
 * Este se encarga de representar una orden realizada por un usario o "user".
 * Cada orden posee información del usuario, total, estado y fecha de creación de la orden.
 */

package model;

public class OrderClass {

    /** Variable identificador único de la orden. */
    private int id_order;

    /**Variable identificador del nombre de usuario asociado a la orden. */
    private String username;

    /**Variable identificador de la cantidad total de cobro de la orden.*/
    private double total;

    /**Variable identificador del estado actual de la orden ("pending", "paid", "shipped").*/
    private String status;

    /**Variable identificador de la fecha en la que se creó la orden. */
    private String created_at;

    /**
     * Método: Constructor vacío para inicializar una orden.
     */
    public OrderClass() {
    }

    /**
     * Método: Obtiene el identificador único de la orden.
     * @return id de la orden.
     */
    public int getId_order() {
        return id_order;
    }

    /**
     * Método: Establece el identificador único de la orden.
     * @param id_order variable del id de la orden.
     */
    public void setId_order(int id_order) {
        this.id_order = id_order;
    }

    /**
     * Método: Obtiene el nombre de usuario asociado a la orden.
     * @return nombre de usuario.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Método: Establece el nombre de usuario asociado a la orden.
     * @param username variable del nombre de usuario.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Método: Obtiene el total monetario de la orden.
     * @return total de la orden.
     */
    public double getTotal() {
        return total;
    }

    /**
     * Método: Establece el total monetario de la orden.
     * @param total variable del total.
     */
    public void setTotal(double total) {
        this.total = total;
    }

    /**
     * Método: Obtiene el estado actual de la orden.
     * @return estado de la orden.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Método: Establece el estado actual de la orden.
     * @param status variable del estado.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Método: Obtiene la fecha de creación de la orden.
     * @return fecha de creación.
     */
    public String getCreated_at() {
        return created_at;
    }

    /**
     * Método: Establece la fecha de creación de la orden.
     * @param created_at variable de la fecha de creación.
     */
    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
