package ua.com.ukrelektro.flight.interfaces;

import java.util.Date;
import java.util.List;
import ua.com.ukrelektro.flight.spr.objects.City;
import ua.com.ukrelektro.flight.objects.Flight;



public interface Search {
    
    List<Flight> searchFlight(Date date, City cityFrom, City cityTo, int placeCount);  

}
