package db;

import application.Airport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AirportServiceProvider implements ServiceProvider {

    private static Connection connection = null;
    private String query =
            "SELECT air.airport_name, air.airport_alias, air.airport_city, air.airport_country, c.utc_offset " +
                    "FROM  slotmachine.airport AS air " +
                    "LEFT JOIN slotmachine.city AS c ON c.city_name = air.airport_city " +
                    "ORDER BY air.airport_alias ";

    private String queryAP =
            "SELECT air.airport_name, air.airport_alias, air.airport_city, air.airport_country, c.utc_offset " +
                    "FROM  slotmachine.airport AS air " +
                    "LEFT JOIN slotmachine.city AS c ON c.city_name = air.airport_city " +
                    "WHERE air.airport_alias = ?";


    private String count = "SELECT COUNT(*) FROM slotmachine.airport";

    public AirportServiceProvider() {
        if (connection == null) AirportServiceProvider.connection = new DbManager().getConnection();
    }

    @Override
    public List<Airport> fetch(int offset, int limit) {
        query += "offset ? limit ?";
        PreparedStatement pstmt;
        List<Airport> airportList = new ArrayList<>();

        try {
            pstmt = connection.prepareStatement(query);
            if (limit != 0) {
                pstmt.setInt(1, offset);
                pstmt.setInt(2, limit);
            }
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Airport airport = unwrap(rs);
                airportList.add(airport);
            }

            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return airportList;
    }

    @Override
    public List<Airport> fetch() {
        PreparedStatement pstmt;
        List<Airport> airportList = new ArrayList<>();

        try {
            pstmt = connection.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Airport airport = unwrap(rs);
                airportList.add(airport);
            }
            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return airportList;
    }


    public Airport fetch(String airportAlias) {
        PreparedStatement pstmt;

        try {
            pstmt = connection.prepareStatement(queryAP);
            pstmt.setString(1, airportAlias);
            ResultSet rs = pstmt.executeQuery();
            Airport airport = null;

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


    private Airport unwrap(ResultSet rs) throws SQLException {
        String name = rs.getString(1);
        String alias = rs.getString(2);
        String city = rs.getString(3);
        String country = rs.getString(4);
        String utcOffset = rs.getString(5);
        return new Airport(name, city, alias, country, utcOffset);
    }
}


