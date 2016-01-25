package ua.com.ukrelektro.flight.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import ua.com.ukrelektro.flight.database.abstracts.AbstractObjectDB;
import ua.com.ukrelektro.flight.spr.objects.Company;

public class CompanyDB extends AbstractObjectDB<Company> {

    
    public final static String TABLE_SPR_COMPANY = "spr_company";

    private CompanyDB() {
        super(TABLE_SPR_COMPANY);
    }
    private static CompanyDB instance;

    public static CompanyDB getInstance() {
        if (instance == null) {
            instance = new CompanyDB();
        }
        return instance;
    }

    @Override
    public Company fillObject(ResultSet rs) throws SQLException {
        Company company = new Company();
        company.setId(rs.getLong("id"));
        company.setName(rs.getString("name"));
        company.setDesc(rs.getString("desc"));
        return company;
    }
}