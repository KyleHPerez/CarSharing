package carsharing;

import java.util.List;
import java.util.Optional;

public interface DAO<T> {

    void createTable();
    void insert(String... values);
    Optional<List<T>> selectAll();
    Optional<T> selectById(int id);
    void update(T t);
    void deleteById(int id);
}
