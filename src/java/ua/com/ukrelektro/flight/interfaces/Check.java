package ua.com.ukrelektro.flight.interfaces;

import ua.com.ukrelektro.flight.objects.Reservation;



public interface Check {
    
    Reservation checkReservation(String code);

}
