package application;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class Flight {

    private int flightID;
    private Airport departureAirport;
    private Airport destinationAirport;
    private Airline airline;
    private OffsetDateTime departureTime;
    private OffsetDateTime destinationTime;


    public Flight(int flightID,
                  Airport departureAirport,
                  Airport destinationAirport,
                  Airline airline,
                  OffsetDateTime departureTime,
                  OffsetDateTime destinationTime) {
        this.flightID = flightID;
        this.departureAirport = departureAirport;
        this.destinationAirport = destinationAirport;
        this.airline = airline;
        this.departureTime = departureTime;
        this.destinationTime = destinationTime;
    }

    public Flight(Airport departureAirport,
                  Airport destinationAirport,
                  Airline airline,
                  OffsetDateTime departureTime,
                  OffsetDateTime destinationTime) {
        this.flightID = this.hashCode();
        this.departureAirport = departureAirport;
        this.destinationAirport = destinationAirport;
        this.airline = airline;
        this.departureTime = departureTime;
        this.destinationTime = destinationTime;
    }

    public int getFlightID() {
        return flightID;
    }

    public Airport getDepartureAirport() {
        return departureAirport;
    }

    public Airport getDestinationAirport() {
        return destinationAirport;
    }

    public Airline getAirline() {
        return airline;
    }

    public OffsetDateTime getDepartureTime() {
        return departureTime;
    }

    public OffsetDateTime getDestinationTime() {
        return destinationTime;
    }

    /**
     * @return flight time in milliseconds
     * @see TimeUnit#convert(long, TimeUnit)
     */
    public long getFlightTime() {
        ChronoUnit unit = ChronoUnit.MINUTES;
        return unit.between(departureTime, destinationTime);

    }

    @Override
    public String toString() {
        return "Flight{" +
                ", departureAirport=" + departureAirport.toString() +
                ", destinationAirport=" + destinationAirport.toString() +
                ", airline=" + airline.toString() +
                ", departureTime=" + departureTime.toString() +
                ", destinationTime=" + destinationTime.toString() +
                '}';
    }
}
