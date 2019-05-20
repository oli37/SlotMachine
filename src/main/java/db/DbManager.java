package db;

import application.Airline;
import application.Airport;
import application.Flight;
import utils.TestDataGenerator;

import java.sql.*;
import java.time.*;

public class DbManager {

    private final String url = "jdbc:postgresql://localhost/postgres";
    private final String user = "postgres";
    private final String password = "postgres";
    private Connection connection = null;


    private String sqlCountry = "insert into slotmachine.country (country_name) values (?);";
    private String sqlCity = "insert into slotmachine.city (city_name, country, timezoneregion) values (?, ?, ?);";
    private String sqlAirline = "insert into slotmachine.airline (airline_name, airline_alias, airline_country) values (?, ?, ?);";
    private String sqlAirport = "insert into slotmachine.airport (airport_name, airport_alias, airport_city, airport_country) values (?, ?, ?, ?);";
    private String sqlFlight = "insert into slotmachine.flight (departureAirport, destinationAirport, airline, departureTime, destinationTime) values (?,?,?,?,?);";


    public DbManager() {
        this.connection = connect();
    }


    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }


    public boolean disconnect() {
        try {
            connection.close();
            System.out.println("Disconnected");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    public boolean populate(Airport departureAirport) {
        return false;

    }

    public boolean populateCountry(String country) throws SQLException {
        PreparedStatement pstmt = null;

        try {
            pstmt = connection.prepareStatement(sqlCountry);
            pstmt.setString(1, country);
            pstmt.executeUpdate();
            pstmt.close();
            return true;
        } catch (Exception ignore) {
            return false;
        }

    }

    public boolean populateCity(String country,
                                String city,
                                String timezone) {
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement(sqlCity);
            pstmt.setString(1, city);
            pstmt.setString(2, country);
            pstmt.setString(3, timezone);
            pstmt.executeUpdate();
            pstmt.close();
            return true;
        } catch (Exception ignore) {
            return false;
        }

    }

    public boolean populateAirline(String airlineName,
                                   String airlineAlias,
                                   String airlineCountry) {
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement(sqlAirline);
            pstmt.setString(1, airlineName);
            pstmt.setString(2, airlineAlias);
            pstmt.setString(3, airlineCountry);
            pstmt.executeUpdate();
            pstmt.close();
            return true;
        } catch (Exception ignore) {
            return false;
        }

    }

    public boolean populateAirport(String airportName,
                                   String airportAlias,
                                   String airportCity,
                                   String airportCountry) {
        PreparedStatement pstmt = null;

        try {
            pstmt = connection.prepareStatement(sqlAirport);
            pstmt.setString(1, airportName);
            pstmt.setString(2, airportAlias);
            pstmt.setString(3, airportCity);
            pstmt.setString(4, airportCountry);
            pstmt.executeUpdate();
            pstmt.close();
            return true;
        } catch (Exception ignore) {
            return false;
        }

    }

    public boolean populateFlight(String departureAirport,
                                  String destinationAirport,
                                  String airline, Timestamp departureTime,
                                  Timestamp destinationTime) {
        PreparedStatement pstmt = null;

        try {
            pstmt = connection.prepareStatement(sqlFlight);
            pstmt.setString(1, departureAirport);
            pstmt.setString(2, destinationAirport);
            pstmt.setString(3, airline);
            pstmt.setTimestamp(4, departureTime);
            pstmt.setTimestamp(5, destinationTime);
            pstmt.executeUpdate();
            pstmt.close();
            return true;
        } catch (Exception ignore) {
            return false;
        }
    }


    public static void main(String[] args) throws SQLException {

        int NUMBER_OF_FLIGHS_FROM_SINGLE_AIRPORT = 20;


        DbManager db = new DbManager();
        Airport departureAirport = TestDataGenerator.getRandomAirport();
        db.populateCountry(departureAirport.getCountry());
        db.populateCity(departureAirport.getCountry(), departureAirport.getCity(), departureAirport.getUtcOffset());
        db.populateAirport(departureAirport.getName(), departureAirport.getAlias(), departureAirport.getCity(), departureAirport.getCountry());

        int i = 0;
        while (i < NUMBER_OF_FLIGHS_FROM_SINGLE_AIRPORT) {

            try {
                Airline airline = TestDataGenerator.getRandomAirline();
                Airport destinationAirport = TestDataGenerator.getRandomAirport();
                OffsetDateTime departureDate = TestDataGenerator.getRandomDate(2019, 4, 10, departureAirport.getUtcOffset());
                OffsetDateTime destinationDate = departureDate.plusMinutes(TestDataGenerator.getRandomInRange(60, 300));

                Flight f = new Flight(departureAirport, destinationAirport, airline, departureDate, destinationDate);

                db.populateCountry(airline.getCountry());
                db.populateCountry(destinationAirport.getCountry());

                db.populateCity(destinationAirport.getCountry(), destinationAirport.getCity(), destinationAirport.getUtcOffset());
                db.populateAirport(destinationAirport.getName(), destinationAirport.getAlias(), destinationAirport.getCity(), destinationAirport.getCountry());
                db.populateAirline(airline.getName(), airline.getAlias(), airline.getCountry());

                //Timestamp can't keep Timezone --> convert to UTC
                Timestamp departure = Timestamp.from(departureDate.toInstant());
                Timestamp destination = Timestamp.from(destinationDate.toInstant());

                db.populateFlight(departureAirport.getAlias(), destinationAirport.getAlias(), airline.getAlias(), departure, destination);
                i += 1;

            } catch (Exception ignore) {
            }
        }
        db.disconnect();

    }

}


/*
 * db.populateCountry("Austria");
 * db.populateCountry("Germany");
 * db.populateCountry("Switzerland");
 * db.populateCountry("Italy");
 * db.populateCountry("Sweden");
 * <p>
 * db.populateCity("Austria", "Vienna", "UTC+2");
 * db.populateCity("Germany", "Frankfurt", "UTC+2");
 * db.populateCity("Switzerland", "Bern", "UTC+2");
 * db.populateCity("Italy", "Rome", "UTC+2");
 * db.populateCity("Sweden", "Stockholm", "UTC+2");
 * <p>
 * <p>
 * db.populateAirport("Flughafen Wien-Schwechat", "VIE", "Vienna", "Austria");
 * db.populateAirport("Flughafen Frankfurt am Main", "FRA", "Frankfurt", "Germany");
 * <p>
 * <p>
 * db.populateAirline("Austrian Airlines", "OS", "Austria");
 * db.populateAirline("German Wings", "PW", "Germany");
 *
 * @Ignore Timestamp departure = new Timestamp(2019, 4, 25, 13, 45, 10, 0);
 * Timestamp destination = new Timestamp(2019, 4, 25, 14, 25, 10, 0);
 * db.populateFlight("VIE", "FRA", "OS", departure, destination);
 * db.populateFlight("VIE", "FRA", "OS", departure, destination);
 **/



