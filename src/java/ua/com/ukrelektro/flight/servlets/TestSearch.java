/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.com.ukrelektro.flight.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ua.com.ukrelektro.flight.database.FlightDB;
import ua.com.ukrelektro.flight.database.PassengerDB;
import ua.com.ukrelektro.flight.database.PlaceDB;
import ua.com.ukrelektro.flight.database.ReservationDB;
import ua.com.ukrelektro.flight.objects.Flight;
import ua.com.ukrelektro.flight.objects.Passenger;
import ua.com.ukrelektro.flight.objects.Reservation;
import ua.com.ukrelektro.flight.spr.objects.Place;
/**
 *
 * @author Tim
 */
@WebServlet(name = "TestSearch", urlPatterns = {"/TestSearch"})
public class TestSearch extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
//            /* TODO output your page here. You may use following sample code. */
//            out.println("<!DOCTYPE html>");
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>Servlet TestSearch</title>");            
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Servlet TestSearch at " + request.getContextPath() + "</h1>");
//            out.println("</body>");
//            out.println("</html>");
            
           
            
            Flight flight = FlightDB.getInstance().executeObject(FlightDB.getInstance().getObjectByID(3));

            Place place = PlaceDB.getInstance().executeObject(PlaceDB.getInstance().getObjectByID(2));

            Passenger passenger = PassengerDB.getInstance().executeObject(PassengerDB.getInstance().getObjectByID(1));

            long dateLong = 1453800100000L;  // 26 JAN 2016 г. 9:21:40 GMT
            
            Calendar date = Calendar.getInstance();
            date.setTimeInMillis(dateLong);

            Reservation reserv = new Reservation();
            reserv.setAddInfo("Without dinner");
            reserv.setCode(UUID.randomUUID().toString());
            reserv.setPassenger(passenger);
            reserv.setReserveDateTime(date);
            reserv.setPlace(place);
            reserv.setFlight(flight);

           //ReservationDB.getInstance().insert(ReservationDB.getInstance().getInsertStmt(reserv));

            reserv = ReservationDB.getInstance().executeObject(ReservationDB.getInstance().getStmtByCode("d7026ba3-9102-439c-bf5f-cfb715ee3d8e"));
            System.out.println(reserv.getFlight().getAircraft().getName());

            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(dateLong);

            reserv = ReservationDB.getInstance().executeObject(ReservationDB.getInstance().getStmtByDateReserv(date));
            System.out.println(reserv.getFlight().getAircraft().getName());

            reserv = ReservationDB.getInstance().executeObject(ReservationDB.getInstance().getStmtByDocumentNumber("336351226"));
            System.out.println(reserv.getFlight().getAircraft().getName());

            reserv = ReservationDB.getInstance().executeObject(ReservationDB.getInstance().getStmtByFamilyName("Oliinyk"));
            System.out.println(reserv.getFlight().getAircraft().getName());
            
            
        } finally {            
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(TestSearch.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(TestSearch.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
