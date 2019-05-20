package application;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class Flight {

    private Airport departureAirport;
    private Airport destinationAirport;
    private Airline airline;
    private OffsetDateTime departureTime;
    private OffsetDateTime destinationTime;


    public Flight(
            Airport departure_airport,
            Airport destination_airport,
            Airline airline,
            OffsetDateTime departure_time,
            OffsetDateTime destination_time) {
        this.departureAirport = departure_airport;
        this.destinationAirport = destination_airport;
        this.airline = airline;
        this.departureTime = departure_time;
        this.destinationTime = destination_time;
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
