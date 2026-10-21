/**
 * CPEN 410 - Project 1
 * Clase: ProductClass
 * Clase modelo que representa un producto dentro del sistema MiniAmazon.
 * Contiene los atributos principales asociados a un producto, tales como:
 * nombre, descripción, fabricante, año, inventario, precio, categoría e imagen.
 *
 * Esta clase se utiliza para transportar información entre la capa de datos
 * y la capa de lógica/aplicación.
 */

package model;

public class ProductClass {

    /** Identificador único del producto. */
    private int productId;

    /** Nombre del producto. */
    private String name;

    /** Descripción del producto. */
    private String description;

    /** Fabricante o marca del producto. */
    private String manufacturer;

    /** Año de fabricación o lanzamiento del producto. */
    private int year;

    /** Cantidad disponible en inventario. */
    private int stock;

    /** Precio del producto. */
    private double price;

    /** Identificador de la categoría a la que pertenece el producto. */
    private int categoryId;

    /** URL o nombre del archivo de la imagen del producto. */
    private String image;

    /**
     * Método: Constructor vacío requerido para instanciación simple
     * y compatibilidad con frameworks o librerías que utilicen reflexión.
     */
    public ProductClass() {
    }

    /**
     * Método: Constructor que inicializa un producto con todos sus atributos.
     *
     * @param productId identificador único del producto.
     * @param name nombre del producto.
     * @param description descripción del producto.
     * @param manufacturer fabricante o marca.
     * @param year año de fabricación.
     * @param stock cantidad disponible en inventario.
     * @param price precio del producto.
     * @param categoryId identificador de la categoría.
     * @param image imagen asociada al producto.
     */
    public ProductClass(int productId, String name, String description,
                        String manufacturer, int year, int stock,
                        double price, int categoryId, String image) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.manufacturer = manufacturer;
        this.year = year;
        this.stock = stock;
        this.price = price;
        this.categoryId = categoryId;
        this.image = image;
    }

    /**
     * Método: Obtiene el ID del producto.
     * @return identificador del producto.
     */
    public int getProductId() {
        return productId;
    }

    /**
     * Método: Establece el ID del producto.
     * @param productId identificador del producto.
     */
    public void setProductId(int productId) {
        this.productId = productId;
    }

    /**
     * Método: Obtiene el nombre del producto.
     * @return nombre del producto.
     */
    public String getName() {
        return name;
    }

    /**
     * Método: Establece el nombre del producto.
     * @param name nombre del producto.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Método: Obtiene la descripción del producto.
     * @return descripción del producto.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Método: Establece la descripción del producto.
     * @param description descripción del producto.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Método: Obtiene el fabricante del producto.
     * @return fabricante o marca.
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * Método: Establece el fabricante del producto.
     * @param manufacturer fabricante o marca.
     */
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    /**
     * Método: Obtiene el año del producto.
     * @return año de fabricación.
     */
    public int getYear() {
        return year;
    }

    /**
     * Método: Establece el año del producto.
     * @param year año de fabricación.
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Método: Obtiene la cantidad disponible en inventario.
     * @return cantidad en stock.
     */
    public int getStock() {
        return stock;
    }

    /**
     * Método: Establece la cantidad disponible en inventario.
     * @param stock cantidad en stock.
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Método: Obtiene el precio del producto.
     * @return precio del producto.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Método: Establece el precio del producto.
     * @param price precio del producto.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Método: Obtiene el ID de la categoría del producto.
     * @return identificador de categoría.
     */
    public int getCategoryId() {
        return categoryId;
    }

    /**
     * Método: Establece el ID de la categoría del producto.
     * @param categoryId identificador de categoría.
     */
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * Método: Obtiene la imagen del producto.
     * @return imagen del producto.
     */
    public String getImage() {
        return image;
    }

    /**
     * Método: Establece la imagen del producto.
     * @param image imagen del producto.
     */
    public void setImage(String image) {
        this.image = image;
    }
}