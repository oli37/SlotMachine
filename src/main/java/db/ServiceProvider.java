package db;

import java.util.List;

public interface ServiceProvider extends Cloneable {

    List<?> fetch(int offset, int limit);

    List<?> fetchAll();

    int getCount();

}
