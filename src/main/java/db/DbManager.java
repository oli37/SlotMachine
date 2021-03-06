package db;

import application.Airline;
import application.Airport;
import application.Flight;
import utils.TestDataGenerator;

import java.sql.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class DbManager {

    private final String url = "jdbc:postgresql://localhost/postgres";
    private final String user = "postgres";
    private final String password = "postgres";
    private static Connection connection = null;
    private static final Logger LOGGER = Logger.getLogger("SlotMachine");


    public DbManager() {

        while (connection == null) {
            try {
                connection = connection = DriverManager.getConnection(url, user, password);
                System.out.println(connection != null ? "Connected" : "Failed to Connect");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public Connection getConnection() {
        return connection;
    }

    public boolean disconnect() {
        try {
            connection.close();
            System.out.println("Disconnected");
            return true;
        } catch (SQLException ignore) {
        }
        return false;

    }

    public void addAirline(Airline airline) {
        assert airline != null;
        postAirline(airline.getName(), airline.getAlias(), airline.getCountry());
    }

    public void addAirport(Airport airport) {
        assert airport != null;
        postCountry(airport.getCountry());
        postCity(airport.getCountry(), airport.getCity(), airport.getUtcOffset());
        postAirport(airport.getName(), airport.getAlias(), airport.getCity(), airport.getCountry());

    }

    public void addFlight(Flight flight) {
        assert flight != null;
        Airline airline = flight.getAirline();
        Airport departureAirport = flight.getDepartureAirport();
        Airport destinationAirport = flight.getDestinationAirport();
        OffsetDateTime departureDate = flight.getDepartureTime();
        OffsetDateTime destinationDate = flight.getDestinationTime();

        postCountry(airline.getCountry());
        postCountry(destinationAirport.getCountry());

        postCity(destinationAirport.getCountry(), destinationAirport.getCity(), destinationAirport.getUtcOffset());
        postAirport(destinationAirport.getName(), destinationAirport.getAlias(), destinationAirport.getCity(), destinationAirport.getCountry());
        postAirline(airline.getName(), airline.getAlias(), airline.getCountry());

        //Timestamp can't keep Timezone --> convert to UTC
        Timestamp departure = Timestamp.from(departureDate.toInstant());
        Timestamp destination = Timestamp.from(destinationDate.toInstant());

        postFlight(departureAirport.getAlias(), destinationAirport.getAlias(), airline.getAlias(), departure, destination);

    }


    /**
     * Populates the Database with random flights
     *
     * @param nrFlightsFromSingleAirport number of flights from a random departure airport
     */
    public void populateDB(int nrFlightsFromSingleAirport) {
        int i = 0;
        while (i < nrFlightsFromSingleAirport) {
            try {
                System.out.println("Added Item " + i);

                Airport departureAirport = TestDataGenerator.getRandomAirport();
                Airline airline = TestDataGenerator.getRandomAirline();
                Airport destinationAirport = TestDataGenerator.getRandomAirport();
                OffsetDateTime departureDate = TestDataGenerator.getRandomDate(2019, 4, 10, departureAirport.getUtcOffset());
                OffsetDateTime destinationDate = departureDate.plusMinutes(TestDataGenerator.getRandomInRange(60, 300));

                Flight f = new Flight(departureAirport, destinationAirport, airline, departureDate, destinationDate);

                addFlight(f);
                i += 1;

            } catch (Exception ignore) {
                i -= 1;
            }
        }

    }

    /**
     * Returns a list from all Flights stored in the database
     *
     * @return List of {@link Flight}
     */
    public List<Flight> getAllFlights() {

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
            pstmt = connection.prepareStatement(sqlFlight);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Flight f = getFlight(rs);
                flightList.add(f);
            }

        } catch (Exception ignore) {
        }

        return flightList;
    }


    public Flight getFlight(ResultSet rs) throws SQLException {

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


    /**
     * Due to foreign key constraints, several tables might be written to add one Object eg. Airport
     */


    private boolean postCountry(String country) {
        PreparedStatement pstmt = null;

        try {
            String sqlCountry = "insert into slotmachine.country (country_name) values (?);";
            pstmt = connection.prepareStatement(sqlCountry);
            pstmt.setString(1, country);
            pstmt.executeUpdate();
            pstmt.close();
            return true;
        } catch (Exception ignore) {
            return false;
        }

    }


    private boolean postCity(String country,
                             String city,
                             String utcOffset) {
        PreparedStatement pstmt = null;
        try {
            String sqlCity = "insert into slotmachine.city (city_name, country, utc_offset) values (?, ?, ?);";
            pstmt = connection.prepareStatement(sqlCity);
            pstmt.setString(1, city);
            pstmt.setString(2, country);
            pstmt.setString(3, utcOffset);
            pstmt.executeUpdate();
            pstmt.close();
            return true;
        } catch (Exception ignore) {
            return false;
        }

    }

    private boolean postAirline(String airlineName,
                                String airlineAlias,
                                String airlineCountry) {
        PreparedStatement pstmt = null;
        try {
            String sqlAirline = "insert into slotmachine.airline (airline_name, airline_alias, airline_country) values (?, ?, ?);";
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


    private boolean postAirport(String airportName,
                                String airportAlias,
                                String airportCity,
                                String airportCountry) {
        PreparedStatement pstmt = null;

        try {
            String sqlAirport = "insert into slotmachine.airport (airport_name, airport_alias, airport_city, airport_country) values (?, ?, ?, ?);";
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


    private boolean postFlight(String departureAirport,
                               String destinationAirport,
                               String airline, Timestamp departureTime,
                               Timestamp destinationTime) {
        PreparedStatement pstmt = null;

        try {
            String sqlFlight = "insert into slotmachine.flight (departureAirport, destinationAirport, airline, departureTime, destinationTime) values (?,?,?,?,?);";
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

}



