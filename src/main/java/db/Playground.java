package db;


import application.CostFunction;

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


        CostFunction cf = new CostFunction("TestCF", "AMG", 0, 120, 15, 50);

        System.out.println(cf);
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
