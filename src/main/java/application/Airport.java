package application;

import java.time.ZoneOffset;

public class Airport implements Cloneable {

    private String name;
    private String city;
    private String alias;
    private String country;
    private String utcOffset;

    public Airport(String alias) {
        this.alias = alias;
    }

    public Airport(String name, String city, String alias, String country, String utcOffset) {
        this.name = name;
        this.city = city;
        this.country = country;
        this.alias = alias;
        this.utcOffset = format(utcOffset);
    }


    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getAlias() {
        return alias;
    }

    public String getCountry() {
        return country;
    }

    public String getUtcOffset() {
        return utcOffset;
    }

    @Override
    public String toString() {
        return "Airport{" +
                "name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", alias='" + alias + '\'' +
                ", country='" + country + '\'' +
                ", utcOffset='" + utcOffset + '\'' +
                '}';
    }

    /**
     * @param utcOffset according to csv-format
     * @return utcOffset according to {@link java.time.ZoneOffset}
     * @see java.time.ZoneOffset
     */
    private String format(String utcOffset) {

        //System.out.println("IN:  "+utcOffset);
        utcOffset = utcOffset.replace(".25", ":15");
        utcOffset = utcOffset.replace(".5", ":30");
        utcOffset = utcOffset.replace(".75", ":45");
        utcOffset = utcOffset.replace("N", "0");
        utcOffset = utcOffset.replace("+", "");
        if (utcOffset.isEmpty()) {
            utcOffset = "0";
        }

        if (!utcOffset.startsWith("-")) {
            StringBuilder sb = new StringBuilder();
            sb.append("+");

            if (utcOffset.contains(":") &&
                    (!utcOffset.contains("10") || !utcOffset.contains("11") || !utcOffset.contains("12"))) {
                sb.append("0");
            }

            sb.append(utcOffset);
            utcOffset = sb.toString();
        }

        return utcOffset;
    }

}




