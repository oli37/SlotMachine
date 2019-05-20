package application;

import org.apache.commons.lang3.time.DateUtils;
import utils.TestDataGenerator;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Application {

    private static final Logger LOGGER = Logger.getLogger("SlotMachine");

    public static void main(String[] args) throws ParseException {
        loggerSetup();
        Airline airline = TestDataGenerator.getRandomAirline();
        Airport departureAirport = TestDataGenerator.getRandomAirport();
        Airport destinationAirport = TestDataGenerator.getRandomAirport();

        OffsetDateTime departureDate = TestDataGenerator.getRandomDate(2018, 7, 10, "+2");
        OffsetDateTime destinationDate = departureDate.plusMinutes(125);

        Flight f = new Flight(departureAirport, destinationAirport, airline, departureDate, destinationDate);

        System.out.println(departureDate.getHour() + "    " + departureDate.getMinute());

        SlotWindow sw = new SlotWindow(8, departureDate);
        System.out.println(sw.toString());


    }


    /**
     * Sets up logger for output in file
     */
    private static void loggerSetup() {

        String log_path = "logs/log";

        try {
            SimpleDateFormat df = new SimpleDateFormat("-yyyy-MM-dd-kk-mm-ss");
            FileHandler fh = new FileHandler(log_path + df.format(new Date()));
            LOGGER.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
