package ua.com.ukrelektro.flight.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import ua.com.ukrelektro.flight.database.abstracts.AbstractObjectDB;
import ua.com.ukrelektro.flight.objects.Flight;
import ua.com.ukrelektro.flight.spr.objects.Aircraft;
import ua.com.ukrelektro.flight.spr.objects.City;
import ua.com.ukrelektro.flight.spr.objects.Place;
import ua.com.ukrelektro.flight.utils.GMTCalendar;

public class FlightDB extends AbstractObjectDB<Flight> {
    public final static String TABLE_FLIGHT = "flight";

    private FlightDB() {
        super(TABLE_FLIGHT);
    }
    private static FlightDB instance;

    public static FlightDB getInstance() {
        if (instance == null) {
            instance = new FlightDB();
        }

        return instance;
    }
    private static final String MIN = " min. ";
    private static final String HOUR = " h.  ";
    private static final String DAY = " d.  ";

    @Override
    public Flight fillObject(ResultSet rs) throws SQLException {
        Calendar dateDepart = GMTCalendar.getInstance();
        Calendar dateCome = GMTCalendar.getInstance();

        dateDepart.setTimeInMillis(rs.getLong("date_depart"));
        dateCome.setTimeInMillis(rs.getLong("date_come"));

        Flight flight = new Flight();
        flight.setId(rs.getLong("id"));
        flight.setCode(rs.getString("code"));
        flight.setDateDepart(dateDepart);
        flight.setDateCome(dateCome);

        Aircraft aircraft = AircraftDB.getInstance().executeObject(AircraftDB.getInstance().getObjectByID(rs.getLong("aircraft_id")));
        flight.setAircraft(aircraft);
        
        ArrayList<Place> placeList = PlaceDB.getInstance().executeList(PlaceDB.getInstance().getPlaceStmtBusy(aircraft.getId(), flight.getId()));
        aircraft.setPlaceList(placeList);
        
        ArrayList<Place> freePlaceList = new ArrayList<>();
        
          for (Place place : placeList) {
            if (!place.isBusy()){
                flight.setExistFreePlaces(true);
                freePlaceList.add(place);
            }
        }         
          
        aircraft.setFreePlaceList(freePlaceList);       

        City city_from = CityDB.getInstance().executeObject(CityDB.getInstance().getObjectByID(rs.getLong("city_from_id")));
        flight.setCityFrom(city_from);

        City city_to = CityDB.getInstance().executeObject(CityDB.getInstance().getObjectByID(rs.getLong("city_to_id")));
        flight.setCityTo(city_to);

        StringBuilder sb = new StringBuilder();

        int dayDiff = dateCome.get(Calendar.DAY_OF_YEAR) - dateDepart.get(Calendar.DAY_OF_YEAR);
        int hourDiff = dateCome.get(Calendar.HOUR_OF_DAY) - dateDepart.get(Calendar.HOUR_OF_DAY);
        int minDiff = dateCome.get(Calendar.MINUTE) - dateDepart.get(Calendar.MINUTE);

        if (dayDiff > 0) {
            sb.append(dayDiff).append(DAY);
        }

        if (hourDiff > 0) {
            sb.append(hourDiff).append(HOUR);
        }

        if (minDiff > 0) {
            sb.append(minDiff).append(MIN);
        }

        flight.setDuration(sb.toString());

        return flight;
    }

    public PreparedStatement getStmt(Calendar dateTime, City cityFrom, City cityTo) throws SQLException {
        Connection conn = AviaDB.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("select * from " + TABLE_FLIGHT + " where date_depart>=? and  date_depart<? and city_from_id=? and city_to_id=?");


        // оставить только дату, чтобы искать рейсы за все 24 часа
        dateTime.set(Calendar.HOUR_OF_DAY, 0);
        dateTime.set(Calendar.MINUTE, 0);
        dateTime.set(Calendar.SECOND, 0);
        dateTime.set(Calendar.MILLISECOND, 0);


        // в каком интервали искать (по-умолчанию - в пределах суток)
        Calendar dateTimeInterval = (Calendar) (dateTime.clone());
        dateTimeInterval.add(Calendar.DATE, INTERVAL);


        stmt.setLong(1, dateTime.getTimeInMillis());
        stmt.setLong(2, dateTimeInterval.getTimeInMillis());
        stmt.setLong(3, cityFrom.getId());
        stmt.setLong(4, cityTo.getId());
        return stmt;
    }

    public PreparedStatement getStmtByDateDepart(Calendar dateDepart) throws SQLException {
        Connection conn = AviaDB.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("select * from " + TABLE_FLIGHT + " where date_from>=? and  date_depart<?");


        // оставить только дату, чтобы искать рейсы за все 24 часа
        clearTime(dateDepart);

        // в каком интервали искать (по-умолчанию - в пределах суток)
        Calendar dateTimeInterval = (Calendar) (dateDepart.clone());
        dateTimeInterval.add(Calendar.DATE, INTERVAL);

        stmt.setLong(1, dateDepart.getTimeInMillis());
        stmt.setLong(2, dateTimeInterval.getTimeInMillis());
        return stmt;
    } 
    
}

