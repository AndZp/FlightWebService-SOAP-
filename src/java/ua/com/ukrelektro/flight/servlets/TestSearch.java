/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.com.ukrelektro.flight.servlets;

import java.util.UUID;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ua.com.ukrelektro.flight.database.CityDB;
import ua.com.ukrelektro.flight.database.FlightDB;
import ua.com.ukrelektro.flight.database.PassengerDB;
import ua.com.ukrelektro.flight.database.PlaceDB;
import ua.com.ukrelektro.flight.database.ReservationDB;
import ua.com.ukrelektro.flight.objects.Flight;
import ua.com.ukrelektro.flight.objects.Passenger;
import ua.com.ukrelektro.flight.objects.Reservation;
import ua.com.ukrelektro.flight.spr.objects.City;
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
            throws ServletException, IOException {
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
            
                 
            
            
            
            
            
            
            
            
            
           //	26 JAN 2016 г. 17:55:00 GMT  = 1453830900000
            City c8 = CityDB.getInstance().getCity(8);//Tel Aviv
            City c3 = CityDB.getInstance().getCity(3);// Kiev
            
            long date = 1453800100000L;  // 26 JAN 2016 г. 9:21:40 GMT
            
            ArrayList<Flight> list = FlightDB.getInstance().getFlights(date, c3, c8);
            
            for (Flight flight : list) {
                System.out.println(flight.getAircraft().getName());
            }
            
            
            Flight flight = list.get(0);
            
            Place place = PlaceDB.getInstance().getPlace(2);
            
            Passenger passenger = PassengerDB.getInstance().getPassenger(1);
            
            Calendar dateFlight = Calendar.getInstance();
            dateFlight.setTimeInMillis(date);
            
            Reservation reserv = new Reservation();
            reserv.setAddInfo("Without dinner");
            reserv.setCode(UUID.randomUUID().toString());
            reserv.setPassenger(passenger);
            reserv.setReserveDateTime(dateFlight);
            reserv.setPlace(place);
            reserv.setFlight(flight);
            
            ReservationDB.getInstance().insertReservation(reserv);
            
            
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
        processRequest(request, response);
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
        processRequest(request, response);
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
