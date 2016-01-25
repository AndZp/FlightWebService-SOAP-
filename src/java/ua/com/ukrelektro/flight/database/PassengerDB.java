package ua.com.ukrelektro.flight.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import ua.com.ukrelektro.flight.objects.Passenger;

public class PassengerDB {

    private PassengerDB() {
    }
    private static PassengerDB instance;

    public static PassengerDB getInstance() {
        if (instance == null) {
            instance = new PassengerDB();
        }
        return instance;
    }

    public Passenger getPassenger(long id) {
        try {
            return getPassenger(getPassengerStmt(id));
        } catch (Exception ex) {
            Logger.getLogger(PassengerDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            AviaDB.getInstance().closeConnection();
        }
        return null;
    }

    public ArrayList<Passenger> getAllPassengers() {
        try {
            return getPassengers(getAllPassengersStmt());
        } catch (Exception ex) {
            Logger.getLogger(PassengerDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            AviaDB.getInstance().closeConnection();
        }
        return null;
    }

    private ArrayList<Passenger> getPassengers(PreparedStatement stmt) throws SQLException {

        ArrayList<Passenger> list = new ArrayList<>();
        ResultSet rs = null;

        try {
            rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(fillPassenger(rs));
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

    private Passenger getPassenger(PreparedStatement stmt) throws SQLException {

        Passenger passenger = null;
        ResultSet rs = null;

        try {
            rs = stmt.executeQuery();

            rs.next();
            if (rs.isFirst()) {
                passenger = fillPassenger(rs);
            }

        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
        return passenger;
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

    public boolean insertPassenger(Passenger passenger) {
        try {
            return executeInsert(getInsertPassengerStmt(passenger));
        } catch (Exception ex) {
            Logger.getLogger(PassengerDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            AviaDB.getInstance().closeConnection();
        }
 
        return false;
    }

    private Passenger fillPassenger(ResultSet rs) throws SQLException {

        Passenger passenger = new Passenger();
        passenger.setId(rs.getLong("id"));
        passenger.setDocumentNumber(rs.getString("document_number"));
        passenger.setEmail(rs.getString("email"));
        passenger.setFamilyName( rs.getString("family_name"));
        passenger.setGivenName(rs.getString("given_name"));
        passenger.setMiddleName(rs.getString("middle_name"));
        passenger.setPhone(rs.getString("phone"));

        return passenger;
    }

    private PreparedStatement getPassengerStmt(long id) throws SQLException {
        Connection conn = AviaDB.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("select * from passenger where id=?");
        stmt.setLong(1, id);
        return stmt;
    }

    private PreparedStatement getAllPassengersStmt() throws SQLException {
        Connection conn = AviaDB.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("select * from passenger");
        return stmt;
    }

    private PreparedStatement getInsertPassengerStmt(Passenger passenger) throws SQLException {
        Connection conn = AviaDB.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("insert into passenger(given_name, middle_name, family_name, document_number, email, phone) values (?,?,?,?,?,?)");
        stmt.setString(1, passenger.getGivenName());
        stmt.setString(2, passenger.getMiddleName());
        stmt.setString(3, passenger.getFamilyName());
        stmt.setString(4, passenger.getDocumentNumber());
        stmt.setString(5, passenger.getEmail());
        stmt.setString(6, passenger.getPhone());
        return stmt;
    }
}