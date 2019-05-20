package application;


public class Airport {

    private String name;
    private String city;
    private String alias;
    private String country;
    private String utcOffset;


    public Airport(String name, String city, String alias, String country, String utcOffset) {
        this.name = name;
        this.city = city;
        this.country = country;
        this.alias = alias;

        utcOffset= utcOffset.replace(".5", ":30");
        if (!utcOffset.startsWith("-") ) {
            StringBuilder sb = new StringBuilder();
            sb.append("+");
            sb.append(utcOffset);
            utcOffset = sb.toString();
        }
        this.utcOffset = utcOffset;
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
}




