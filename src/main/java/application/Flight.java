package application;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Flight {

    private int flightID;
    private Airport departureAirport;
    private Airport destinationAirport;
    private Airline airline;
    private OffsetDateTime departureTime;
    private OffsetDateTime destinationTime; //not used
    private List<CostFunction> costFunctionList = new ArrayList<>();
    private int timeslot; // one slot for every 15min = 24*4=96 slots
    //00:00 - 00:14 slot 0
    //00:15 - 00:29 slot 1
    //...

    public Flight(Airport departureAirport,
                  Airport destinationAirport,
                  Airline airline,
                  OffsetDateTime departureTime,
                  OffsetDateTime destinationTime) {
        this.departureAirport = departureAirport;
        this.destinationAirport = destinationAirport;
        this.airline = airline;
        this.departureTime = departureTime;
        this.destinationTime = destinationTime;
        this.timeslot = calculateTimeslot();
    }

    public Flight(int flightID,
                  Airport departureAirport,
                  Airport destinationAirport,
                  Airline airline,
                  OffsetDateTime departureTime,
                  OffsetDateTime destinationTime) {

        this(departureAirport, destinationAirport, airline, departureTime, destinationTime);
        this.flightID = flightID;
    }


    public int getFlightID() {
        return flightID;
    }

    public void setFlightID(int flightID) {
        this.flightID = flightID;
    }

    public Airport getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(Airport departureAirport) {
        this.departureAirport = departureAirport;
    }

    public Airport getDestinationAirport() {
        return destinationAirport;
    }

    public void setDestinationAirport(Airport destinationAirport) {
        this.destinationAirport = destinationAirport;
    }

    public Airline getAirline() {
        return airline;
    }

    public void setAirline(Airline airline) {
        this.airline = airline;
    }

    public OffsetDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(OffsetDateTime departureTime) {
        this.departureTime = departureTime;
        this.timeslot = calculateTimeslot();

    }

    public OffsetDateTime getDestinationTime() {
        return destinationTime;
    }

    public void setDestinationTime(OffsetDateTime destinationTime) {
        this.destinationTime = destinationTime;
    }

    public List<CostFunction> getCostFunctionList() {
        return costFunctionList;
    }

    public void setCostFunctionList(List<CostFunction> costFunctionList) {
        this.costFunctionList = costFunctionList;
    }

    public void addCF(CostFunction cf) {
        costFunctionList.add(cf);
    }

    public int getTimeslot() {
        return timeslot;
    }

    private int calculateTimeslot() {
        int h = departureTime.getHour();
        int m = departureTime.getMinute();

        return (int) (h * 4 + (m / 15) + 0.5);
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
                "flightID=" + flightID +
                ", timeslot=" + timeslot +
                ", departureAirport=" + departureAirport +
                ", destinationAirport=" + destinationAirport +
                ", airline=" + airline +
                ", departureTime=" + departureTime +
                ", destinationTime=" + destinationTime +
                ", costFunctionList=" + costFunctionList +
                '}';
    }
}
