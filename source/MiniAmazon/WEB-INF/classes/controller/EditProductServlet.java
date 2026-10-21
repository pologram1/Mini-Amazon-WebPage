/**
 * CPEN 410 - Project 1
 * Clase: EditProductServlet
 * Servlet encargado de manejar la edición de productos dentro del sistema
 * MiniAmazon. Permite cargar los datos de un producto existente para edición
 * (método GET) y actualizarlo en la base de datos (método POST), incluyendo
 * la gestión opcional de una nueva imagen.
 * Este servlet utiliza {@link dao.ProductDAO} para obtener y actualizar
 * información de productos, y emplea {@link jakarta.servlet.http.Part}
 * para manejar archivos enviados mediante multipart/form-data.
 */

package controller;

import dao.ProductDAO;
import model.ProductClass;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.File;
import java.io.IOException;

@WebServlet("/EditProductServlet")
@MultipartConfig
public class EditProductServlet extends HttpServlet {

    /**
     * Método: Carga los datos del producto seleccionado para edición.
     * Valida el parámetro id, obtiene el producto desde la base de datos
     * y lo envía a la vista editProduct.jsp.
     * @param request  solicitud HTTP con el id del producto.
     * @param response respuesta HTTP para redirección o despliegue de vista.
     * @throws ServletException si ocurre un error interno.
     * @throws IOException si ocurre un error al redirigir o reenviar.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");

        if (idParam == null || idParam.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/listProducts.jsp");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            ProductDAO dao = new ProductDAO();
            ProductClass product = dao.getProductById(id);

            if (product == null) {
                response.sendRedirect(request.getContextPath() + "/listProducts.jsp");
                return;
            }

            request.setAttribute("product", product);
            request.getRequestDispatcher("/editProduct.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error loading product for edit.", e);
        }
    }

    /**
     * Método: Procesa la actualización de un producto.
     * Obtiene los datos enviados desde el formulario, maneja la carga opcional
     * de una nueva imagen y actualiza el producto mediante ProductDAO.
     *
     * @param request  solicitud HTTP con los datos del formulario.
     * @param response respuesta HTTP para redirección o manejo de errores.
     * @throws ServletException si ocurre un error interno.
     * @throws IOException si ocurre un error al procesar archivos o redirigir.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int productId = Integer.parseInt(request.getParameter("id"));
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            double price = Double.parseDouble(request.getParameter("price"));
            int stock = Integer.parseInt(request.getParameter("stock"));

            ProductDAO dao = new ProductDAO();
            ProductClass existingProduct = dao.getProductById(productId);

            // Mantener imagen actual si no se sube una nueva
            String fileName = existingProduct != null ? existingProduct.getImage() : null;

            // Manejo de imagen nueva
            Part filePart = request.getPart("image");
            if (filePart != null && filePart.getSubmittedFileName() != null
                    && !filePart.getSubmittedFileName().isBlank()) {

                fileName = filePart.getSubmittedFileName();

                String uploadPath = getServletContext().getRealPath("") + File.separator + "images";
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }

                filePart.write(uploadPath + File.separator + fileName);
            }

            boolean updated = dao.updateProduct(productId, name, description, price, stock, fileName);

            if (updated) {
                response.sendRedirect(request.getContextPath() + "/listProducts.jsp");
            } else {
                throw new ServletException("Product was not updated.");
            }

        } catch (Exception e) {
            throw new ServletException("Error updating product.", e);
        }
    }
}