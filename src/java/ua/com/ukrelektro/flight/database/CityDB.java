package ua.com.ukrelektro.flight.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import ua.com.ukrelektro.flight.database.abstracts.AbstractObjectDB;
import ua.com.ukrelektro.flight.spr.objects.City;
import ua.com.ukrelektro.flight.spr.objects.Country;

public class CityDB extends AbstractObjectDB<City> {

    public final static String TABLE_SPR_CITY = "spr_city";

    private CityDB() {
        super(TABLE_SPR_CITY);
    }
    private static CityDB instance;

    public static CityDB getInstance() {
        if (instance == null) {
            instance = new CityDB();
        }
        return instance;
    }

    public PreparedStatement getStmtByName(String name) throws SQLException {
        Connection conn = AviaDB.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("select * from " + TABLE_SPR_CITY + " where name=?");
        stmt.setString(1, name);
        return stmt;
    }

    @Override
    public City fillObject(ResultSet rs) throws SQLException {
        City city = new City();
        city.setId(rs.getLong("id"));
        city.setCode(rs.getString("code"));

        Country country = CountryDB.getInstance().executeObject(CountryDB.getInstance().getObjectByID(rs.getLong("country_id")));
        
        city.setCountry(country);
        city.setDesc(rs.getString("desc"));
        city.setName(rs.getString("name"));
        return city;
    }   
}
