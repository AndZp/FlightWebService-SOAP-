package ua.com.ukrelektro.flight.interfaces.sei;

import java.util.ArrayList;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import ua.com.ukrelektro.flight.objects.Flight;
import ua.com.ukrelektro.flight.objects.Passenger;
import ua.com.ukrelektro.flight.objects.Reservation;
import ua.com.ukrelektro.flight.spr.objects.City;
import ua.com.ukrelektro.flight.spr.objects.Place;

@WebService(name="FlightWS", targetNamespace = "http://ukrelektro.com.ua/flight/ws")
@SOAPBinding(style=Style.DOCUMENT, use=Use.LITERAL)
public interface FlightSEI{
    
    Reservation checkReservationByCode(@WebParam(name = "code") String code);

    ArrayList<City> getAllCities();

    ArrayList<Flight> searchFlight(@WebParam(name = "date") long date, @WebParam(name = "cityFrom") City cityFrom, @WebParam(name = "cityTo") City cityTo);
   
    boolean buyTicket(@WebParam(name = "flight") Flight flight, @WebParam(name = "place") Place place, @WebParam(name = "passenger") Passenger passenger, @WebParam(name = "addInfo") String addInfo);

}
