package ua.com.ukrelektro.flight.interfaces;

import ua.com.ukrelektro.flight.objects.Flight;
import ua.com.ukrelektro.flight.spr.objects.Place;
import ua.com.ukrelektro.flight.objects.Reservation;



public interface Buy {
    
    Reservation buyTicket(Flight flight, Place place, String addInfo);

}
