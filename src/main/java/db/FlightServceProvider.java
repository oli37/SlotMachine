package db;

import application.Airline;
import application.Airport;
import application.Flight;

import java.sql.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class FlightServceProvider implements ServiceProvider {

    private static Connection connection = null;

    public FlightServceProvider() {
        if (connection == null) FlightServceProvider.connection = new DbManager().getConnection();
    }

    @Override
    public List<Flight> fetch(int offset, int limit) {
        PreparedStatement pstmt = null;
        List<Flight> flightList = new ArrayList<>();

        try {
            String sqlFlight = "SELECT *\n" +
                    "FROM slotmachine.flight f\n" +
                    "LEFT OUTER JOIN slotmachine.airline al ON f.airline = al.airline_alias\n" +
                    "LEFT OUTER JOIN slotmachine.airport ap_des ON f.destinationairport = ap_des.airport_alias\n" +
                    "LEFT OUTER JOIN slotmachine.airport ap_dep ON f.departureairport = ap_dep.airport_alias\n" +
                    "LEFT OUTER JOIN slotmachine.city dep_city ON ap_dep.airport_city = dep_city.city_name \n" +
                    "LEFT OUTER JOIN slotmachine.city des_city ON ap_des.airport_city = des_city.city_name \n";
            if (limit != 0) sqlFlight = sqlFlight + "offset ? limit ?";

            pstmt = connection.prepareStatement(sqlFlight);
            if (limit != 0) {
                pstmt.setInt(1, offset);
                pstmt.setInt(2, limit);
            }
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Flight f = getFlight(rs);
                flightList.add(f);
            }

        } catch (Exception e) {
            e.fillInStackTrace();
        }
        return flightList;
    }

    @Override
    public List<?> fetchAll() {
        return fetch(0, 0);
    }

    @Override
    public int getCount() {
        return 50;
    }


    private Flight getFlight(ResultSet rs) throws SQLException {

        Timestamp departuretime = rs.getTimestamp(5);
        Timestamp destinationtime = rs.getTimestamp(6);

        String airlineName = rs.getString(8);
        String airlineAlias = rs.getString(9);
        String airlineCountry = rs.getString(10);

        String destinationAirportName = rs.getString(12);
        String destinationAirportAlias = rs.getString(13);
        String destinationAirportCity = rs.getString(14);
        String destinationAirportCountry = rs.getString(15);

        String departureAirportName = rs.getString(17);
        String departureAirportAlias = rs.getString(18);
        String departureAirportCity = rs.getString(19);
        String departureAirportCountry = rs.getString(20);
        String departureAirportTimeZoneOffset = rs.getString(24);
        String destinationAirportTimeZoneOffset = rs.getString(28);

        Airline al = new Airline(airlineName, airlineAlias, airlineCountry);
        Airport apDep = new Airport(departureAirportName, departureAirportCity, departureAirportAlias, departureAirportCountry, departureAirportTimeZoneOffset);
        Airport apDes = new Airport(destinationAirportName, destinationAirportCity, destinationAirportAlias, destinationAirportCountry, destinationAirportTimeZoneOffset);
        OffsetDateTime departureTime = departuretime.toLocalDateTime().atOffset(ZoneOffset.of(departureAirportTimeZoneOffset));
        OffsetDateTime destinationTime = destinationtime.toLocalDateTime().atOffset(ZoneOffset.of(destinationAirportTimeZoneOffset));

        return new Flight(apDep, apDes, al, departureTime, destinationTime);
    }

}
