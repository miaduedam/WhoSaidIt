package app.daos;

import java.util.List;

public interface IDAO <T, I>{

    T create(T t);

    List<T> getAll();

    T getById(I id);

    T update(T t);

    Boolean delete(I id);
}
