package db;

import application.Airport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AirportServiceProvider implements ServiceProvider {

    private static Connection connection = null;

    public AirportServiceProvider() {
        if (connection == null) AirportServiceProvider.connection = new DbManager().getConnection();
    }

    @Override
    public List<Airport> fetch(int offset, int limit) {
        String sqlAirport = "SELECT air.airport_name, air.airport_alias, air.airport_city, air.airport_country, c.utc_offset \n" +
                "FROM  slotmachine.airport AS air\n" +
                "LEFT JOIN slotmachine.city AS c ON c.city_name = air.airport_city\n" +
                "ORDER BY air.airport_alias\n";

        if (limit != 0) sqlAirport = sqlAirport + "offset ? limit ?";

        PreparedStatement pstmt;
        List<Airport> airportList = new ArrayList<>();

        try {
            pstmt = connection.prepareStatement(sqlAirport);
            if (limit != 0) {
                pstmt.setInt(1, offset);
                pstmt.setInt(2, limit);
            }
            ResultSet res = pstmt.executeQuery();

            while (res.next()) {
                String name = res.getString(1);
                String alias = res.getString(2);
                String city = res.getString(3);
                String country = res.getString(4);
                String utcOffset = res.getString(5);
                Airport airport = new Airport(name, city, alias, country, utcOffset);
                airportList.add(airport);
            }

            pstmt.close();
            res.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return airportList;
    }

    @Override
    public List<Airport> fetchAll() {
        return fetch(0, 0);
    }

    @Override
    public int getCount() {
        return 1000;
    }
}
