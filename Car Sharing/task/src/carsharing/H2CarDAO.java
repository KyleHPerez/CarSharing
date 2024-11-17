package carsharing;

import java.util.List;
import java.util.Optional;

public class H2CarDAO implements DAO<Car> {

    public static final String CREATE_TABLE = """
                CREATE TABLE IF NOT EXISTS cars (
                    ID INT PRIMARY KEY AUTO_INCREMENT,
                    NAME VARCHAR_IGNORECASE(255) NOT NULL,
                    COMPANY_ID INT NOT NULL,
                    foreign key (COMPANY_ID) REFERENCES COMPANY(ID)
                );""";
    public static final String INSERT = "INSERT INTO CARS (NAME, COMPANY_ID) VALUES ('%s', '%s');";
    public static final String SELECT_ALL = "SELECT * FROM CARS;";
    public static final String SELECT_BY_ID = "SELECT * FROM CARS WHERE ID = '%d'";
    public static final String SELECT_AVAILABLE_BY_COMPANY_ID = """
            SELECT *
            FROM CARS LEFT JOIN CUSTOMERS ON CARS.ID = CUSTOMERS.RENTED_CAR_ID
            WHERE CARS.COMPANY_ID = '%s' AND CUSTOMERS.ID = NULL;""";
    public static final String SELECT_BY_COMPANY_ID = "SELECT * FROM CARS WHERE COMPANY_ID = '%s';";
    public static final String DELETE_BY_ID = "DELETE FROM CARS WHERE ID = '%d';";

    private final DbClient dbClient;

    public H2CarDAO(String[] args) throws ClassNotFoundException {
        dbClient = new DbClient(args);
    }

    @Override
    public void createTable() {
        dbClient.run(CREATE_TABLE);
    }

    @Override
    public void insert(String... values) {
        String name = values[0];
        String companyId = values[1];
        dbClient.run(String.format(INSERT, name, companyId));
    }

    @Override
    public Optional<List<Car>> selectAll() {
        return Optional.ofNullable(dbClient.selectForCarList(SELECT_ALL));
    }

    @Override
    public Optional<Car> selectById(int id) {
        return dbClient.selectCar(String.format(SELECT_BY_ID, id));
    }

    public Optional<List<Car>> selectByCompanyId(int id) {
        return Optional.ofNullable(dbClient.selectForCarList(String.format(SELECT_BY_COMPANY_ID, id)));
    }

    public Optional<List<Car>> selectAvailableByCompanyId(int id) {
        return Optional.ofNullable(dbClient.selectForCarList(String.format(SELECT_BY_COMPANY_ID, id)));
    }

    @Override
    public void update(Car car) {

    }

    @Override
    public void deleteById(int id) {
        dbClient.run(String.format(DELETE_BY_ID, id));
    }
}
