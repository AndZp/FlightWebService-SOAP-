package ua.com.ukrelektro.flight.ws;

import java.util.ArrayList;
import java.util.Calendar;
import javax.jws.WebService;
import javax.xml.ws.soap.MTOM;
import ua.com.ukrelektro.flight.interfaces.impls.BuyImpl;
import ua.com.ukrelektro.flight.interfaces.impls.CheckImpl;
import ua.com.ukrelektro.flight.interfaces.impls.SearchImpl;
import ua.com.ukrelektro.flight.interfaces.sei.FlightSEI;
import ua.com.ukrelektro.flight.objects.Flight;
import ua.com.ukrelektro.flight.objects.Passenger;
import ua.com.ukrelektro.flight.objects.Reservation;
import ua.com.ukrelektro.flight.spr.objects.City;
import ua.com.ukrelektro.flight.spr.objects.Place;
import ua.com.ukrelektro.flight.utils.GMTCalendar;
import ua.com.ukrelektro.flight.ws.exceptions.ArgumentException;

@MTOM
@WebService(endpointInterface = "ua.com.ukrelektro.flight.interfaces.sei.FlightSEI")
//@BindingType(value = SOAPBinding.SOAP12HTTP_MTOM_BINDING)
//@HandlerChain(file = "FlightWS_handler.xml")
public class FlightWS implements FlightSEI {

    private SearchImpl searchImpl = new SearchImpl();
    private BuyImpl buyImpl = new BuyImpl();
    private CheckImpl checkImpl = new CheckImpl();

    @Override
    public ArrayList<Flight> searchFlight(Long date, City cityFrom, City cityTo) throws ArgumentException {

        if (date == null || date <= 0) {
            throw new ArgumentException("Date is empty or less then zero");
        }

        if (cityFrom == null) {
            throw new ArgumentException("CityFrom is empty");
        }


        if (cityTo == null) {
            throw new ArgumentException("CityTo is empty");
        }

        ArrayList<Flight> list = new ArrayList<>();
        Calendar c = GMTCalendar.getInstance();
        c.setTimeInMillis(date);

        list.addAll(searchImpl.searchFlight(date, cityFrom, cityTo));

        return list;
    }

    @Override
    public ArrayList<City> getAllCities() {
        ArrayList<City> list = new ArrayList<>();
        list.addAll(searchImpl.getAllCities());
        return list;
    }

    @Override
    public boolean buyTicket(Flight flight, Place place, Passenger passenger, String addInfo)throws ArgumentException {

        if (flight == null) {
            throw new ArgumentException("Flight object is empty");
        }

        if (passenger == null) {
            throw new ArgumentException("Passenger object is empty");
        }
        boolean result = false;

        result = buyImpl.buyTicket(flight, place, passenger, addInfo);

        return result;
    }

    @Override
    public Reservation checkReservationByCode(String code) throws ArgumentException {

        if (code == null || code.isEmpty()) {
            throw new ArgumentException("Reservation code is empty");
        }
        
        return checkImpl.checkReservationByCode(code);
    }
}
