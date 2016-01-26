package ua.com.ukrelektro.flight.ws;

import java.util.ArrayList;
import java.util.Calendar;
import javax.jws.WebService;
import ua.com.ukrelektro.flight.interfaces.Buy;
import ua.com.ukrelektro.flight.interfaces.Check;
import ua.com.ukrelektro.flight.interfaces.Search;
import ua.com.ukrelektro.flight.interfaces.impls.BuyImpl;
import ua.com.ukrelektro.flight.interfaces.impls.CheckImpl;
import ua.com.ukrelektro.flight.interfaces.impls.SearchImpl;
import ua.com.ukrelektro.flight.objects.Flight;
import ua.com.ukrelektro.flight.objects.Passenger;
import ua.com.ukrelektro.flight.objects.Reservation;
import ua.com.ukrelektro.flight.spr.objects.City;
import ua.com.ukrelektro.flight.spr.objects.Place;
import ua.com.ukrelektro.flight.utils.GMTCalendar;


@WebService(serviceName = "SearchWS")
//@BindingType(value = SOAPBinding.SOAP12HTTP_MTOM_BINDING)
//@HandlerChain(file = "SearchWS_handler.xml")
public class SearchWS implements Search, Buy, Check {

    private SearchImpl searchImpl = new SearchImpl();
    private BuyImpl buyImpl = new BuyImpl();
    private CheckImpl checkImpl = new CheckImpl();

    @Override
    public ArrayList<Flight> searchFlight(long date, City cityFrom, City cityTo) {

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
    public boolean buyTicket(Flight flight, Place place, Passenger passenger, String addInfo) {
        boolean result = false;

        result = buyImpl.buyTicket(flight, place, passenger, addInfo);

        return result;
    }

    @Override
    public Reservation checkReservationByCode(String code) {
        return checkImpl.checkReservationByCode(code);
    }
}
