package application;

import utils.XMLReader;

import java.util.logging.Logger;

public class Airline {

    private String name;
    private String alias;
    private String country;

    private static final Logger LOGGER = Logger.getLogger("SlotMachine");

    public Airline(String name, String alias, String country) {
        this.name = name;
        this.alias = alias;
        this.country = country;
    }



    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return "Airline{" +
                "name='" + name + '\'' +
                ", alias='" + alias + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}