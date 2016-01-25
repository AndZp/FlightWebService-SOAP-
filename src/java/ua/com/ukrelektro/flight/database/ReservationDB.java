package ua.com.ukrelektro.flight.database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import ua.com.ukrelektro.flight.database.abstracts.AbstractObjectDB;
import ua.com.ukrelektro.flight.objects.Flight;
import ua.com.ukrelektro.flight.objects.Passenger;
import ua.com.ukrelektro.flight.objects.Reservation;
import ua.com.ukrelektro.flight.spr.objects.Place;

public class ReservationDB extends AbstractObjectDB<Reservation> {

    public final static String TABLE_RESERVATION = "reservation";

    private ReservationDB() {
        super(TABLE_RESERVATION);
    }
    private static ReservationDB instance;

    public static ReservationDB getInstance() {
        if (instance == null) {
            instance = new ReservationDB();
        }

        return instance;
    }

    public PreparedStatement getInsertStmt(Reservation reservation) throws SQLException {
        Connection conn = AviaDB.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("insert into "+TABLE_RESERVATION+"(flight_id, passenger_id, place_id, add_info, reserve_datetime, code) values (?,?,?,?,?,?)");
        stmt.setLong(1, reservation.getFlight().getId());
        stmt.setLong(2, reservation.getPassenger().getId());
        stmt.setLong(3, reservation.getPlace().getId());
        stmt.setString(4, reservation.getAddInfo());
        stmt.setLong(5, reservation.getReserveDateTime().getTimeInMillis());
        stmt.setString(6, reservation.getCode());
        return stmt;
    }
    
    
     public PreparedStatement getStmtByDocumentNumber(String docNumber) throws SQLException {
        
        Connection conn = AviaDB.getInstance().getConnection();
        
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM reservation r inner join passenger p on p.id=r.passenger_id ");
        sb.append(" where p.document_number=?");
        
        PreparedStatement stmt = conn.prepareStatement(sb.toString());
        stmt.setString(1, docNumber);
        
        return stmt;
    }
    
     public PreparedStatement getStmtByFamilyName(String familyName) throws SQLException {
        
        Connection conn = AviaDB.getInstance().getConnection();
        
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM reservation r inner join passenger p on p.id=r.passenger_id ");
        sb.append(" where p.family_name=?");
        
        PreparedStatement stmt = conn.prepareStatement(sb.toString());
        stmt.setString(1, familyName);
        
        return stmt;
    }
     
     
      public PreparedStatement getStmtByCode(String code) throws SQLException {
        
        Connection conn = AviaDB.getInstance().getConnection();
        
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM reservation r inner join passenger p on p.id=r.passenger_id where r.code=?");
        stmt.setString(1, code);
        
        return stmt;
    }

      
       public PreparedStatement getStmtByDateReserv(Calendar dateReserv) throws SQLException {
        Connection conn = AviaDB.getInstance().getConnection();
        
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM reservation where reserve_datetime>=? and  reserve_datetime<?");


        // оставить только дату, чтобы искать рейсы за все 24 часа
        clearTime(dateReserv);

        // в каком интервали искать (по-умолчанию - в пределах суток)
        Calendar dateTimeInterval = (Calendar) (dateReserv.clone());
        dateTimeInterval.add(Calendar.DATE, INTERVAL);


        stmt.setLong(1, dateReserv.getTimeInMillis());
        stmt.setLong(2, dateTimeInterval.getTimeInMillis());
        return stmt;
    } 
      
      
    @Override
    public Reservation fillObject(ResultSet rs) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setId(rs.getLong("id"));

        Flight flight = FlightDB.getInstance().executeObject(FlightDB.getInstance().getObjectByID(rs.getLong("flight_id")));
        reservation.setFlight(flight);

        Passenger passenger = PassengerDB.getInstance().executeObject(PassengerDB.getInstance().getObjectByID(rs.getLong("passenger_id")));
        reservation.setPassenger(passenger);

        Place place = PlaceDB.getInstance().executeObject(PlaceDB.getInstance().getObjectByID(rs.getLong("place_id")));
        reservation.setPlace(place);

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(rs.getLong("reserve_datetime"));

        reservation.setReserveDateTime(c);

        reservation.setCode(rs.getString("code"));
        reservation.setAddInfo(rs.getString("add_info"));

        return reservation;
    }
}
