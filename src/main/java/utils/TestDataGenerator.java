package utils;

import application.Airline;
import application.Airport;

import java.time.*;

public class TestDataGenerator {

    /**
     * Returns a random Airport
     *
     * @return random Airport as {@link Airport}
     */
    public static Airport getRandomAirport() {

        String PATH = "src/main/resources/airports.csv";
        String[] res = XMLReader.RandomRowFromXML(PATH);
        if (res != null) {

            // Names are formatted: "Campbell River application.Airport"
            String name = res[1].split("application")[0];
            String city = res[2];
            String country = res[3];
            String alias = res[4];
            String utcOffset = res[9];

            return new Airport(name, city, alias, country, utcOffset);

        }
        return null;

    }

    /**
     * Returns a random Airline
     *
     * @return random Airline as {@link Airline}
     */
    public static Airline getRandomAirline() {

        String PATH = "src/main/resources/airlines.csv";
        String[] res = XMLReader.RandomRowFromXML(PATH);
        if (res != null) {
            String name = res[1];
            String alias = res[4];
            String country = res[6];
            return new Airline(name, alias, country);
        }
        return null;
    }


    /**
     * Returns a Random date given a year, month and timezone
     *
     * @param year      year
     * @param month     month
     * @param day       day
     * @param utcOffset zoneId as {@link java.time.ZoneOffset}
     * @return Random ZonedDateTime
     */
    public static OffsetDateTime getRandomDate(int year, int month, int day, String utcOffset) {
        LocalDateTime ldt = LocalDateTime.of(year,
                month,
                day,
                getRandomInRange(0, 23),
                getRandomInRange(0, 59));
        ZoneOffset offset = ZoneOffset.of(utcOffset);
        return ldt.atOffset(offset);
    }

    /**
     * Creates an Integer value in range (min, max)
     *
     * @param min minimum value
     * @param max maximum value
     * @return random Integer in range
     */
    public static int getRandomInRange(int min, int max) {
        int range = Math.abs(max - min) + 1;
        return (int) (Math.random() * range) + (min <= max ? min : max);

    }


}


