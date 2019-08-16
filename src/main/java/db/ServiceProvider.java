package db;

import java.util.List;

public interface ServiceProvider {

    List<?> fetch(int offset, int limit);

    List<?> fetchAll();
    int getCount();

}
