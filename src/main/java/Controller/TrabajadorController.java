package Controller;

import DAO.TrabajadorDAO;
import Model.Trabajador;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "TrabajadorController", urlPatterns = {"/TrabajadorController"})
public class TrabajadorController extends HttpServlet {

    private final TrabajadorDAO traDAO = new TrabajadorDAO();
    private static final String PAG_LISTAR = "/Views/Trabajador/ListaTrabajador.jsp";
    private static final String PAG_NUEVO = "/Views/Trabajador/NuevoTrabajador.jsp";
    private static final String PAG_ACTUALIZAR = "/Views/Trabajador/ActualizarTrabajador.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        String accion = request.getParameter("accion");
        switch (accion) {
            case "listar":
                listar(request, response);
                break;
            case "nuevo":
                nuevo(request, response);
                break;
            case "actualizar":
                actualizar(request, response);
                break;
            case "buscar":
                buscar(request, response);
                break;
            case "eliminar":
                eliminar(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
                break;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(TrabajadorController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(TrabajadorController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void listar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Trabajador> trabajador = traDAO.listarTrabajadores();
        request.setAttribute("listaTrabajador", trabajador);
        request.getRequestDispatcher(PAG_LISTAR).forward(request, response);

    }

    private void nuevo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        try {
            String nombre = request.getParameter("nombre");
            String apellidos = request.getParameter("apellidos");
            String nroIdentificacion = request.getParameter("nroIdentificacion");
            String email = request.getParameter("email");
            String direccion = request.getParameter("direccion");
            String telefono = request.getParameter("telefono");
            String cargo = request.getParameter("cargo");

            String sueldoParam = request.getParameter("sueldo");
            Double sueldo = 0.0;

            if (sueldoParam != null && !sueldoParam.trim().isEmpty()) {
                try {
                    sueldo = Double.parseDouble(sueldoParam.trim());
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "El sueldo debe ser un número válido.");
                    request.getRequestDispatcher(PAG_NUEVO).forward(request, response);
                    return;
                }
            } else {
                request.setAttribute("error", "El sueldo es obligatorio.");
                request.getRequestDispatcher(PAG_NUEVO).forward(request, response);
                return;
            }

            if (nombre == null || nombre.isEmpty() || sueldo <= 0) {
                request.setAttribute("error", "El nombre y el sueldo válido son obligatorios.");
                request.getRequestDispatcher(PAG_NUEVO).forward(request, response);
                return;
            }

            TrabajadorDAO trabajadorDAO = new TrabajadorDAO();
            trabajadorDAO.registrarTrabajador(nombre, apellidos, nroIdentificacion, email, direccion, telefono, cargo, sueldo);

            response.sendRedirect("TrabajadorController?accion=listar");

        } catch (ServletException | IOException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error inesperado: " + e.getMessage());
            request.getRequestDispatcher(PAG_NUEVO).forward(request, response);
        }
    }

    private void actualizar(HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    private void buscar(HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    private void eliminar(HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

}
