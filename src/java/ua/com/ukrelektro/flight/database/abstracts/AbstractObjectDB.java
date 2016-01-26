package ua.com.ukrelektro.flight.database.abstracts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import ua.com.ukrelektro.flight.database.AviaDB;
import ua.com.ukrelektro.flight.database.interfaces.ObjectDB;

public abstract class AbstractObjectDB<T> implements ObjectDB<T> {

    public static final int INTERVAL = 1;
    private String tableName;

    public AbstractObjectDB(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public int insert(PreparedStatement stmt) throws SQLException {
        int result = -1;
        ResultSet rs = null;

        try {
            result = stmt.executeUpdate();

            rs = stmt.getGeneratedKeys();

            rs.next();
            if (rs.isFirst()) {
                result = rs.getInt(1);// вернуть id вставленной записи
            }

            

        } finally {
            if (stmt != null) {
                stmt.close();
            }

            if (rs != null) {
                rs.close();
            }
        }

        return result;
    }


    @Override
    public ArrayList<T> executeList(PreparedStatement stmt) throws SQLException {

        ArrayList<T> list = new ArrayList<>();
        ResultSet rs = null;

        try {
            rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(fillObject(rs));
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

    @Override
    public T executeObject(PreparedStatement stmt) throws SQLException {
        T obj = null;
        ResultSet rs = null;

        try {
            rs = stmt.executeQuery();

            rs.next();
            if (rs.isFirst()) {
                obj = fillObject(rs);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }

        return obj;
    }

    @Override
    public PreparedStatement getObjectByID(long id) throws SQLException {
        Connection conn = AviaDB.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("select * from " + tableName + " where id=?");
        stmt.setLong(1, id);
        return stmt;
    }

    @Override
    public PreparedStatement getAllObjects() throws SQLException {
        Connection conn = AviaDB.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("select * from " + tableName);
        return stmt;
    }

    protected void clearTime(Calendar c) {
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
    }

    protected boolean getBooleanFromInt(int number) {
        if (number > 0) {
            return true;
        } else {
            return false;
        }
    }
}
