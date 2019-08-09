package db;

import application.Airline;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AirlineServiceProvider implements ServiceProvider {

    private static Connection connection = null;

    public AirlineServiceProvider() {
        if (connection == null) AirlineServiceProvider.connection = new DbManager().getConnection();
    }

    @Override
    public List<Airline> fetch(int offset, int limit) {

        String sqlAirline = "select * from  slotmachine.airline order by airline_alias\n";
        if (limit != 0) sqlAirline = sqlAirline + "offset ? limit ?";

        PreparedStatement pstmt = null;
        List<Airline> airlineList = new ArrayList<>();

        try {
            pstmt = connection.prepareStatement(sqlAirline);
            if (limit != 0) {
                pstmt.setInt(1, offset);
                pstmt.setInt(2, limit);
            }
            ResultSet res = pstmt.executeQuery();

            while (res.next()) {
                String name = res.getString(2);
                String alias = res.getString(3);
                String country = res.getString(4);

                Airline airline = new Airline(name, alias, country);
                airlineList.add(airline);
            }

            pstmt.close();
            res.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return airlineList;
    }

    @Override
    public List<?> fetchAll() {
        return fetch(0, 0);
    }

    @Override
    public int getCount() {
        return 1000;
    }

}
