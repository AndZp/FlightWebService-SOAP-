package ua.com.ukrelektro.flight.interfaces;

import ua.com.ukrelektro.flight.objects.Flight;
import ua.com.ukrelektro.flight.objects.Passenger;
import ua.com.ukrelektro.flight.spr.objects.Place;

public interface Buy {

    boolean buyTicket(Flight flight, Place place, Passenger passenger, String addInfo);

}
