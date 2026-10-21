/**
 * CPEN 410 - Project 1
 * Clase: CartItemClass
 * Clase modelo que representa un ítem dentro del carrito de compras
 * del sistema MiniAmazon. Cada ítem contiene un producto y la cantidad
 * seleccionada por el usuario. Esta clase se utiliza como parte 
 *de la estructura interna del carrito
 * para manejar productos y cantidades asociadas.
 */

package model;

public class CartItemClass {

    /** Producto asociado a este ítem del carrito. */
    private ProductClass product;

    /** Cantidad del producto seleccionada por el usuario. */
    private int quantity;

    /**
     * Método: Constructor que inicializa un ítem del carrito con un producto
     * y una cantidad específica.
     * @param p producto asociado al ítem.
     * @param q cantidad del producto.
     */
    public CartItemClass(ProductClass p, int q) {
        product = p;
        quantity = q;
    }

    /**
     * Método: Obtiene el producto asociado a este ítem.
     * @return producto del ítem.
     */
    public ProductClass getProduct() {
        return product;
    }

    /**
     * Método: Obtiene la cantidad del producto.
     * @return cantidad del producto.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Método: Establece una nueva cantidad para el producto.
     * @param q nueva cantidad del producto.
     */
    public void setQuantity(int q) {
        quantity = q;
    }
}