package db;

import application.Airline;
import application.Airport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AirlineServiceProvider implements ServiceProvider {

    private static Connection connection = null;
    private String query =
            "SELECT airline_name, airline_alias, airline_country " +
                    "FROM slotmachine.airline WHERE airline_alias ~ '[A-Z0-9]' " +
                    "ORDER BY airline_alias ";

    private String queryAL =
            "SELECT airline_name, airline_alias, airline_country " +
                    "FROM slotmachine.airline " +
                    "WHERE airline_alias = ? ";

    private String count = "SELECT COUNT(*) FROM slotmachine.airline WHERE airline_alias ~ '[A-Z0-9]'";

    public AirlineServiceProvider() {
        if (connection == null) AirlineServiceProvider.connection = new DbManager().getConnection();
    }

    @Override
    public List<Airline> fetch(int offset, int limit) {
        List<Airline> list = new ArrayList<>();
        PreparedStatement pstmt;
        query += "offset ? limit ?";

        try {
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, offset);
            pstmt.setInt(2, limit);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Airline airline = unwrap(rs);
                list.add(airline);
            }

            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Airline> fetch() {
        List<Airline> list = new ArrayList<>();
        PreparedStatement pstmt;

        try {
            pstmt = connection.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Airline airline = unwrap(rs);
                list.add(airline);
            }

            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public Airline fetch(String airlineAlias) {
        PreparedStatement pstmt;

        try {
            pstmt = connection.prepareStatement(queryAL);
            pstmt.setString(1, airlineAlias);
            ResultSet rs = pstmt.executeQuery();
            Airline airport = null;

            while (rs.next()) {
                airport  = unwrap(rs);
            }
            pstmt.close();
            rs.close();

            return airport;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public int getCount() {

        PreparedStatement pstmt;
        int counter = 0;
        try {
            pstmt = connection.prepareStatement(count);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                counter = rs.getInt(1);
            }

            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return counter;
    }

    @Override
    public boolean post(Object element) {
        return false;
    }


    private Airline unwrap(ResultSet rs) throws SQLException {
        String name = rs.getString(1);
        String alias = rs.getString(2);
        String country = rs.getString(3);

        return new Airline(name, alias, country);
    }

}
