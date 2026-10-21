/**
 * CPEN 410 - Project 1
 * Clase: CartClass
 * Clase modelo que representa el carrito de compras dentro del sistema MiniAmazon.
 * Contiene una lista de elementos del carrito (CartItemClass), permitiendo agregar
 * productos, actualizar cantidades, eliminar productos y obtener el contenido del carrito.
 *
 * Esta clase se utiliza para manejar la lógica interna del carrito en la capa de aplicación.
 */

package model;

import java.util.ArrayList;

public class CartClass {

    /** Lista de elementos del carrito, cada uno compuesto por un producto y su cantidad. */
    private ArrayList<CartItemClass> items = new ArrayList<>();

    /**
     * Método: Agrega un producto al carrito. Si el producto ya existe,
     * incrementa su cantidad; de lo contrario, lo agrega como un nuevo ítem.
     * @param product producto a agregar.
     * @param quantity cantidad del producto.
     */
    public void addProduct(ProductClass product, int quantity) {
        for (CartItemClass item : items) {
            if (item.getProduct().getProductId() == product.getProductId()) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        items.add(new CartItemClass(product, quantity));
    }

    /**
     * Método: Actualiza la cantidad de un producto existente en el carrito.
     * @param productId identificador del producto.
     * @param newQuantity nueva cantidad a establecer.
     */
    public void updateQuantity(int productId, int newQuantity) {
        for (CartItemClass item : items) {
            if (item.getProduct().getProductId() == productId) {
                item.setQuantity(newQuantity);
                return;
            }
        }
    }

    /**
     * Método: Elimina un producto del carrito según su identificador.
     * @param productId identificador del producto a eliminar.
     */
    public void removeProduct(int productId) {
        items.removeIf(item -> item.getProduct().getProductId() == productId);
    }

    /**
     * Método: Obtiene la lista completa de elementos del carrito.
     * @return lista de CartItemClass.
     */
    public ArrayList<CartItemClass> getItems() {
        return items;
    }
}