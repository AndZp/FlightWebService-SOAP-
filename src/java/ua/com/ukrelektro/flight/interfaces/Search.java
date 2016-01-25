package ua.com.ukrelektro.flight.interfaces;

import java.util.ArrayList;
import ua.com.ukrelektro.flight.spr.objects.City;
import ua.com.ukrelektro.flight.objects.Flight;

public interface Search {

    ArrayList<Flight> searchFlight(long date, City cityFrom, City cityTo);

}
