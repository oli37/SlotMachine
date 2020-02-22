package db;

import java.util.List;

public interface ServiceProvider<E> {


    List<E> fetch(int offset, int limit);

    List<E> fetch();

    int getCount();

    boolean post(E element);


}
