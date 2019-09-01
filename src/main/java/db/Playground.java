package db;


import application.Proposal;

import java.time.LocalDateTime;

/**
 * This class is for playing around with DB stuff
 * Do not use it for something else
 */
public class Playground {
    public static void main(String[] args) {


        ProposalServiceProvider asp = new ProposalServiceProvider();
        var inittime = LocalDateTime.now();
        var destime = inittime.plusMinutes(120);
        Proposal a = new Proposal(121, 500, true, inittime, destime);

        asp.post(a);
/*
        var series = new Series(100, 300, 600, -700);

        var neu =  Utility.changeSeries(series, 1, 1);

        System.out.println(neu.getData()[1]);

        DbManager dbm = new DbManager();
        var airports = TestDataGenerator.getAllAirports();
        var airlines = TestDataGenerator.getAllAirlines();


        airports.forEach(dbm::addAirport);
        airlines.forEach(dbm::addAirline);

        dbm.populateDB(15);
        dbm.disconnect();


        var start = System.nanoTime();
        FlightServceProvider flsp = new FlightServceProvider();
        int limit = flsp.getCount();
        System.out.println("Limit: " + limit);
        var a = flsp.fetchAll();
        a.forEach(System.out::println);
        System.out.println(a.size());

        var end = System.nanoTime();

        System.out.println((end - start) / 1000000 + " ms");

        //List<Airline> al = dbm.fetchAirlines(50, 100);

        //al.forEach(System.out::println);
        //System.out.println(al.size());

        //List<Airport> ap = dbm.fetchAirports(0, 0);
        //ap.forEach(System.out::println);
        //System.out.println(ap.size());

*/


    }
}
