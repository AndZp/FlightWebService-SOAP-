package ua.com.ukrelektro.flight.interfaces;

import ua.com.ukrelektro.flight.objects.Reservation;

public interface Check {

    Reservation checkReservationByCode(String code);

   // Reservation checkReservationByDateReserv(String docNumber);

   // Reservation checkReservationByDateReserv(long date);

   Reservation checkReservationByFamilyName(String familyName);

}
