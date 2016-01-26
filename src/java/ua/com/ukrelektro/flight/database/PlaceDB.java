package ua.com.ukrelektro.flight.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import ua.com.ukrelektro.flight.database.abstracts.AbstractObjectDB;
import ua.com.ukrelektro.flight.spr.objects.FlightClass;
import ua.com.ukrelektro.flight.spr.objects.Place;

public class PlaceDB extends AbstractObjectDB<Place> {


   
    public final static String TABLE_SPR_PLACE = "spr_place";
    public final static String TABLE_SPR_AIRCRAFT_PLACE = "spr_aircraft_place";
    

    private PlaceDB() {
        super(TABLE_SPR_PLACE);
    }
    
    
    private static PlaceDB instance;

    public static PlaceDB getInstance() {
        if (instance == null) {
            instance = new PlaceDB();
        }

        return instance;
    }
    
    

    public PreparedStatement getStmtByFlightClass(long flightClassId) throws SQLException {
        Connection conn = AviaDB.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("select * from "+TABLE_SPR_PLACE+" where flight_class_id=?");
        stmt.setLong(1, flightClassId);
        return stmt;
    }

    public PreparedStatement getStmtByAircraftID(long aircraftId) throws SQLException {
        Connection conn = AviaDB.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM "+TABLE_SPR_PLACE+" where id in (select place_id from "+TABLE_SPR_AIRCRAFT_PLACE +" where aircraft_id=?) order by flight_class_id, seat_letter");
        stmt.setLong(1, aircraftId);
        return stmt;
    }
    
    public PreparedStatement getPlaceStmtBusy(long aircraftId, long flightId) throws SQLException{
        Connection conn = AviaDB.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT p.id, p.seat_letter, p.seat_number,p.flight_class_id, " +
            "if ((select r.id from "+ReservationDB.TABLE_RESERVATION+" r where r.flight_id=? and r.place_id=p.id)>1,1,0) as busy " +
            "FROM "+TABLE_SPR_PLACE+" p where id in (select place_id from "+TABLE_SPR_AIRCRAFT_PLACE+" a1 where a1.aircraft_id=?)  order by flight_class_id, seat_letter");
        stmt.setLong(1, flightId);
        stmt.setLong(2, aircraftId);
        return stmt;
    }
    



    @Override
    public Place fillObject(ResultSet rs) throws SQLException {
        Place place = new Place();
        place.setId(rs.getLong("id"));
        place.setSeatLetter(rs.getString("seat_letter"));
        place.setSeatNumber(rs.getInt("seat_number"));
        
        
        // поле busy - не во всех запросах, по-умолчанию место - свободно
        try {
            place.setBusy(getBooleanFromInt(rs.getInt("busy")));
        } catch (Exception e) {
            place.setBusy(false); // catch без перехвата - не очень правильное решение
        }
        
        FlightClass fc = FlightClassDB.getInstance().executeObject(FlightClassDB.getInstance().getObjectByID(rs.getInt("flight_class_id")));
        
        place.setFlightClass(fc);
        return place;
    }



}
