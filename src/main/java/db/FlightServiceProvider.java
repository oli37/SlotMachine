package db;

import application.Airline;
import application.Airport;
import application.CostFunction;
import application.Flight;

import java.sql.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class FlightServiceProvider implements ServiceProvider {

    private static Connection connection = null;
    private String query = "SELECT DISTINCT f.flight_id, f.departuretime, f.destinationtime, \n" +
            "al.airline_name, al.airline_alias, al.airline_country, \n" +
            "ap_des.airport_name AS des_airport_name, ap_des.airport_alias AS des_airport_alias, " +
            "ap_des.airport_city AS des_airport_city, ap_des.airport_country AS des_airport_country, " +
            "des_city.utc_offset AS des_airport_utcoffset,\n" +
            "ap_dep.airport_name AS dep_airport_name, ap_dep.airport_alias AS dep_airport_alias, " +
            "ap_dep.airport_city AS dep_airport_city, ap_dep.airport_country AS dep_airport_country, " +
            "dep_city.utc_offset AS dep_airport_utcoffset\n" +
            "FROM slotmachine.flight f\n" +
            "LEFT OUTER JOIN slotmachine.airline al ON f.airline = al.airline_alias\n" +
            "LEFT OUTER JOIN slotmachine.airport ap_des ON f.destinationairport = ap_des.airport_alias\n" +
            "LEFT OUTER JOIN slotmachine.airport ap_dep ON f.departureairport = ap_dep.airport_alias\n" +
            "LEFT OUTER JOIN slotmachine.city dep_city ON ap_dep.airport_city = dep_city.city_name\n" +
            "LEFT OUTER JOIN slotmachine.city des_city ON ap_des.airport_city = des_city.city_name\n";


    private String nrOfDeparturesByTimeslot =
                " SELECT COUNT(*) cnt, to_timestamp(floor((extract('epoch' from slotmachine.flight.departuretime) / 900 )) * 900) \n" +
                " AT TIME ZONE 'UTC' as interval_alias\n" +
                " FROM slotmachine.flight GROUP BY interval_alias\n" +
                " ORDER BY interval_alias";

    private CostFunctionServiceProvider cfsp = new CostFunctionServiceProvider();

    public FlightServiceProvider() {
        if (connection == null) FlightServiceProvider.connection = new DbManager().getConnection();
    }

    @Override
    public List<Flight> fetch(int offset, int limit) {
        PreparedStatement pstmt = null;
        List<Flight> flightList = new ArrayList<>();

        try {

            if (limit != 0) query = query + "OFFSET ? LIMIT ?";

            pstmt = connection.prepareStatement(query);
            if (limit != 0) {
                pstmt.setInt(1, offset);
                pstmt.setInt(2, limit);
            }
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Flight f = unwrap(rs);
                flightList.add(f);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return flightList;
    }

    @Override
    public List<Flight> fetch() {
        PreparedStatement pstmt = null;
        List<Flight> flightList = new ArrayList<>();

        try {
            pstmt = connection.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Flight f = unwrap(rs);
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
            String sqlFlight = query + "WHERE al.airline_alias = ? ";

            pstmt = connection.prepareStatement(sqlFlight);
            pstmt.setString(1, airlineAlias);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Flight f = unwrap(rs);
                flightList.add(f);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return flightList;

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

    //TODO: Write proper query for better performance
    public Integer[] getNrOfDeparturesByTimeslot() {
        int counter = 0;
        Integer[] countArray = new Integer[96];
        try {
            PreparedStatement pstmt = connection.prepareStatement(nrOfDeparturesByTimeslot);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                countArray[counter] = rs.getInt(1);
                counter += 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return countArray;
    }


    @Override
    public boolean post(Object element) {
        assert element instanceof Flight;
        Flight f = (Flight) element;

        String sqlFlightPost = "INSERT INTO slotmachine.flight (departureairport, destinationairport, airline, departuretime, destinationtime)\n" +
                "VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement pstm = connection.prepareStatement(sqlFlightPost);
            pstm.setString(1, f.getDepartureAirport().getAlias());
            pstm.setString(2, f.getDestinationAirport().getAlias());
            pstm.setString(3, f.getAirline().getAlias());
            pstm.setTimestamp(4, Timestamp.valueOf(f.getDepartureTime().toLocalDateTime()));
            pstm.setTimestamp(5, Timestamp.valueOf(f.getDestinationTime().toLocalDateTime()));
            pstm.executeUpdate();

            f.setFlightID(getFlightID(f));

            if (f.getCostFunctionList() != null) {
                for (CostFunction cf : f.getCostFunctionList()) {
                    cfsp.post(cf);
                    link(cf, f);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    private Flight unwrap(ResultSet rs) throws SQLException {

        int id = rs.getInt("flight_id");

        Timestamp departuretime = rs.getTimestamp("departuretime");
        Timestamp destinationtime = rs.getTimestamp("destinationtime");

        String airlineName = rs.getString("airline_name");
        String airlineAlias = rs.getString("airline_alias");
        String airlineCountry = rs.getString("airline_country");

        String destinationAirportName = rs.getString("des_airport_name");
        String destinationAirportAlias = rs.getString("des_airport_alias");
        String destinationAirportCity = rs.getString("des_airport_city");
        String destinationAirportCountry = rs.getString("des_airport_country");
        String destinationAirportTimeZoneOffset = rs.getString("des_airport_utcoffset");

        String departureAirportName = rs.getString("dep_airport_name");
        String departureAirportAlias = rs.getString("dep_airport_alias");
        String departureAirportCity = rs.getString("dep_airport_city");
        String departureAirportCountry = rs.getString("dep_airport_country");
        String departureAirportTimeZoneOffset = rs.getString("dep_airport_utcoffset");

        Airline al = new Airline(airlineName, airlineAlias, airlineCountry);
        Airport apDep = new Airport(departureAirportName, departureAirportCity, departureAirportAlias, departureAirportCountry, departureAirportTimeZoneOffset);
        Airport apDes = new Airport(destinationAirportName, destinationAirportCity, destinationAirportAlias, destinationAirportCountry, destinationAirportTimeZoneOffset);
        OffsetDateTime departureTime = departuretime.toLocalDateTime().atOffset(ZoneOffset.of("Z"));
        OffsetDateTime destinationTime = destinationtime.toLocalDateTime().atOffset(ZoneOffset.of("Z"));

        return new Flight(id, apDep, apDes, al, departureTime, destinationTime);
    }

    public boolean link(CostFunction cf, Flight f) {
        try {
            String postLink = "INSERT INTO slotmachine.cf_flight_attr (cf_name, flight_id) VALUES (?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(postLink);
            pstmt.setString(1, cf.getName());
            pstmt.setInt(2, f.getFlightID());
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getFlightID(Flight flight) {
        String flightIDQuery = "SELECT flight_id FROM slotmachine.flight WHERE departureairport = ? " +
                "AND destinationairport = ? AND airline = ? AND departuretime = ? AND destinationtime = ?";
        int flightID = 0;

        try {
            PreparedStatement pstm = connection.prepareStatement(flightIDQuery);

            pstm.setString(1, flight.getDepartureAirport().getAlias());
            pstm.setString(2, flight.getDestinationAirport().getAlias());
            pstm.setString(3, flight.getAirline().getAlias());
            pstm.setTimestamp(4, Timestamp.valueOf(flight.getDepartureTime().toLocalDateTime()));
            pstm.setTimestamp(5, Timestamp.valueOf(flight.getDestinationTime().toLocalDateTime()));
            ResultSet rs = pstm.executeQuery();

            while (rs.next())
                flightID = rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return flightID;
    }


}