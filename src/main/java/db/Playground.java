package db;


import application.CostFunction;

/**
 * This class is for playing around with DB stuff
 * Do not use it for something else
 */
public class Playground {
    public static void main(String[] args) {


        CostFunctionServiceProvider cfsp = new CostFunctionServiceProvider();
        cfsp.post(new CostFunction("3362s136", -100, 0, 300.5, 44, 700, 1200));

        System.out.println(cfsp.getCount());


        var list = cfsp.fetch(1, 50);
        list.stream().map(x -> x.getName()).forEach(System.out::println);
/*
        ProposalServiceProvider asp = new ProposalServiceProvider();
        var inittime = LocalDateTime.now();
        var destime = inittime.plusMinutes(120);
        Proposal a = new Proposal(121, 500, true, inittime, destime);

        asp.post(a);

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
