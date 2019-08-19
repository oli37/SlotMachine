package db;

import application.Airline;
import application.Airport;
import application.Flight;

import java.sql.*;
import java.time.LocalDateTime;
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
                    "LEFT OUTER JOIN slotmachine.city dep_city ON ap_dep.airport_city = dep_city.city_name\n" +
                    "LEFT OUTER JOIN slotmachine.city des_city ON ap_des.airport_city = des_city.city_name\n" +
                    "ORDER BY al.airline_alias\n";
            if (limit != 0) sqlFlight = sqlFlight + "OFFSET ? LIMIT ?";

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
            e.printStackTrace();
        }
        return flightList;
    }

    public List<Flight> fetchByAirline(String airlineAlias) {
        PreparedStatement pstmt = null;
        List<Flight> flightList = new ArrayList<>();

        try {
            String sqlFlight = "SELECT *\n" +
                    "FROM slotmachine.flight f\n" +
                    "LEFT OUTER JOIN slotmachine.airline al ON f.airline = al.airline_alias\n" +
                    "LEFT OUTER JOIN slotmachine.airport ap_des ON f.destinationairport = ap_des.airport_alias\n" +
                    "LEFT OUTER JOIN slotmachine.airport ap_dep ON f.departureairport = ap_dep.airport_alias\n" +
                    "LEFT OUTER JOIN slotmachine.city dep_city ON ap_dep.airport_city = dep_city.city_name\n" +
                    "LEFT OUTER JOIN slotmachine.city des_city ON ap_des.airport_city = des_city.city_name\n" +
                    "WHERE f.airline = ?\n" +
                    "ORDER BY al.airline_alias\n";

            pstmt = connection.prepareStatement(sqlFlight);
            pstmt.setString(1, airlineAlias);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Flight f = getFlight(rs);
                flightList.add(f);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return flightList;

    }

    @Override
    public List<Flight> fetchAll() {
        return fetch(0, 0);
    }


    @Override
    public int getCount() {

        String sqlFlightCount = "SELECT COUNT(*) FROM slotmachine.flight";
        int count = 0;
        try {
            PreparedStatement pstmt = connection.prepareStatement(sqlFlightCount);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public boolean post(String airlineName, String depAirportName, String desAirportName, LocalDateTime depTime, LocalDateTime desTime) {
        String sqlFlightPost = "INSERT INTO slotmachine.flight (departureairport, destinationairport, airline, departuretime, destinationtime)\n" +
                "VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstm = connection.prepareStatement(sqlFlightPost);
            pstm.setString(1, depAirportName);
            pstm.setString(2, desAirportName);
            pstm.setString(3, airlineName);
            pstm.setTimestamp(4, Timestamp.valueOf(depTime));
            pstm.setTimestamp(5, Timestamp.valueOf(desTime));
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    private Flight getFlight(ResultSet rs) throws SQLException {

        //int id =

        Timestamp departuretime = rs.getTimestamp(5);
        Timestamp destinationtime = rs.getTimestamp(6);

        String airlineName = rs.getString(7);
        String airlineAlias = rs.getString(8);
        String airlineCountry = rs.getString(9);

        String destinationAirportName = rs.getString(10);
        String destinationAirportAlias = rs.getString(11);
        String destinationAirportCity = rs.getString(12);
        String destinationAirportCountry = rs.getString(13);

        String departureAirportName = rs.getString(14);
        String departureAirportAlias = rs.getString(15);
        String departureAirportCity = rs.getString(16);
        String departureAirportCountry = rs.getString(17);

        String departureAirportTimeZoneOffset = rs.getString(20);
        String destinationAirportTimeZoneOffset = rs.getString(23);

        Airline al = new Airline(airlineName, airlineAlias, airlineCountry);
        Airport apDep = new Airport(departureAirportName, departureAirportCity, departureAirportAlias, departureAirportCountry, departureAirportTimeZoneOffset);
        Airport apDes = new Airport(destinationAirportName, destinationAirportCity, destinationAirportAlias, destinationAirportCountry, destinationAirportTimeZoneOffset);
        OffsetDateTime departureTime = departuretime.toLocalDateTime().atOffset(ZoneOffset.of(departureAirportTimeZoneOffset));
        OffsetDateTime destinationTime = destinationtime.toLocalDateTime().atOffset(ZoneOffset.of(destinationAirportTimeZoneOffset));

        return new Flight(apDep, apDes, al, departureTime, destinationTime);
    }


}
