package db;


import application.*;
import com.github.appreciated.apexcharts.helper.Series;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;
import utils.Utility;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is for playing around with DB stuff
 * Do not use it for something else
 */
public class Playground {
    public static void main(String[] args) {

        FlightServiceProvider flsp = new FlightServiceProvider();
        CostFunctionServiceProvider cfsp = new CostFunctionServiceProvider();
        AirlineServiceProvider alsp = new AirlineServiceProvider();
        AirportServiceProvider apsp = new AirportServiceProvider();


        String a  ="Test";


        /*
        Airport departureAirport = apsp.fetch("AKA");
        Airport destinationAirport = apsp.fetch("ACC");
        Airline airline = alsp.fetch("000");
        OffsetDateTime departureTime = OffsetDateTime.parse("2018-12-12T13:30:30+05:00");
        OffsetDateTime destinationTime = departureTime.plus(100, ChronoUnit.MINUTES);


        Flight flight = new Flight(
                departureAirport,
                destinationAirport,
                airline,
                departureTime,
                destinationTime);

        flight.setFlightID(flsp.getFlightID(flight));
        System.out.println(flight);

        CostFunction cf = new CostFunction();
        cf.setName("Very Cool");
        cf.setOwner("000");

        var flights = flsp.fetch(0,5);
        System.out.println(flights);



        CostFunction cf = new CostFunction();
        Proposal p1 = new Proposal();
        p1.setPrice(50);
        p1.setDelay(-15);
        p1.setBid(true);

        Proposal p2 = new Proposal();
        p2.setPrice(100);
        p2.setDelay(50);
        p2.setBid(false);

        cf.setName("Harald2CF");
        cf.setOwner("AMG");
        cf.addProposal(p1);
        cf.addProposal(p2);

        //cfsp.post(cf);

        var ap = apsp.fetch("ABB");
        System.out.println(ap.toString());
        var al = alsp.fetch("ACC");
        System.out.println(al.toString());
*/
    }
}
