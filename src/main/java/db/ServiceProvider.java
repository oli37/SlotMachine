package db;

import java.util.List;

public interface ServiceProvider {

    //List<?> fetchSpecificAirline(String airlineAlias, int offset, int limit);

    List<?> fetch(int offset, int limit);

    List<?> fetchAll();

    int getCount();


}
