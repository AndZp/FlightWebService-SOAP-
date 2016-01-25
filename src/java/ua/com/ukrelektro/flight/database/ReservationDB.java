package ua.com.ukrelektro.flight.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import ua.com.ukrelektro.flight.objects.Reservation;

public class ReservationDB {

    private ReservationDB() {
    }
    private static ReservationDB instance;

    public static ReservationDB getInstance() {
        if (instance == null) {
            instance = new ReservationDB();
        }

        return instance;
    }

    public Reservation getReservation(long id) {
        try {
            return getReservation(getReservationStmt(id));
        } catch (Exception ex) {
            Logger.getLogger(ReservationDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            AviaDB.getInstance().closeConnection();
        }
        return null;
    }

    public ArrayList<Reservation> getAllReservations() {
        try {
            return getReservations(getAllReservationsStmt());
        } catch (Exception ex) {
            Logger.getLogger(ReservationDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            AviaDB.getInstance().closeConnection();
        }
        return null;
    }

    private ArrayList<Reservation> getReservations(PreparedStatement stmt) throws SQLException {

        ArrayList<Reservation> list = new ArrayList<>();
        ResultSet rs = null;

        try {
            rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(fillReservation(rs));
            }

        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }

        return list;
    }

    private Reservation getReservation(PreparedStatement stmt) throws SQLException {

        Reservation reservation = null;
        ResultSet rs = null;

        try {
            rs = stmt.executeQuery();

            rs.next();
            if (rs.isFirst()) {
                reservation = fillReservation(rs);
            }

        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }

        return reservation;
    }
    
    
    private boolean executeInsert(PreparedStatement stmt) throws SQLException {

        try {
            int result = stmt.executeUpdate();

            if (result>0) {
                return true;
            }

        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }

        return false;
    }

    public boolean insertReservation(Reservation reservation) {
        try {
            return executeInsert(getInsertReservationStmt(reservation));
        } catch (Exception ex) {
            Logger.getLogger(ReservationDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            AviaDB.getInstance().closeConnection();
        }
 
        return false;
    }

    private Reservation fillReservation(ResultSet rs) throws SQLException {

        Reservation reservation = new Reservation();
        reservation.setId(rs.getLong("id"));
        reservation.setFlight(FlightDB.getInstance().getFlight(rs.getLong("flight_id")));
        reservation.setPassenger(PassengerDB.getInstance().getPassenger(rs.getLong("passenger_id")));
        reservation.setPlace(PlaceDB.getInstance().getPlace(rs.getLong("place_id")));
        
        Calendar c=Calendar.getInstance();
        c.setTimeInMillis(rs.getLong("reserve_datetime"));
        
        reservation.setReserveDateTime(c);

        reservation.setCode(rs.getString("code"));
        reservation.setAddInfo(rs.getString("add_info"));

        return reservation;
    }

    private PreparedStatement getReservationStmt(long id) throws SQLException {
        Connection conn = AviaDB.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("select * from reservation where id=?");
        stmt.setLong(1, id);
        return stmt;
    }

    private PreparedStatement getAllReservationsStmt() throws SQLException {
        Connection conn = AviaDB.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("select * from reservation");
        return stmt;
    }

    private PreparedStatement getInsertReservationStmt(Reservation reservation) throws SQLException {
        Connection conn = AviaDB.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("insert into reservation(flight_id, passenger_id, place_id, add_info, reserve_datetime, code) values (?,?,?,?,?,?)");
        stmt.setLong(1, reservation.getFlight().getId());
        stmt.setLong(2, reservation.getPassenger().getId());
        stmt.setLong(3, reservation.getPlace().getId());
        stmt.setString(4, reservation.getAddInfo());
        stmt.setLong(5, reservation.getReserveDateTime().getTimeInMillis());
        stmt.setString(6, reservation.getCode());
        return stmt;
    }
}
