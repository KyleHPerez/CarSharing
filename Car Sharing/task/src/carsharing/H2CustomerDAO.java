package carsharing;

import java.util.List;
import java.util.Optional;

public class H2CustomerDAO implements DAO<Customer> {

    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS CUSTOMER  (
            ID INT PRIMARY KEY AUTO_INCREMENT,
            NAME VARCHAR_IGNORECASE(255) NOT NULL,
            RENTED_CAR_ID INT,
            foreign key (RENTED_CAR_ID) REFERENCES CAR(ID)
            );""";
    private static final String INSERT = "INSERT INTO CUSTOMER (NAME) VALUES ('%s')";
    private static final String SELECT_ALL = "SELECT * FROM CUSTOMER";
    private static final String SELECT_BY_ID = "SELECT * FROM CUSTOMER WHERE ID = '%s'";
    private static final String SELECT_BY_NAME = "SELECT * FROM CUSTOMER WHERE NAME = '%s'";
    private static final String UPDATE = "UPDATE CUSTOMER SET RENTED_CAR_ID = '%s' WHERE ID = '%s'";
    private static final String DELETE = "DELETE FROM CUSTOMER WHERE ID = '%s'";

    private final DbClient dbClient;

    public H2CustomerDAO(String... args) throws ClassNotFoundException {
        dbClient = new DbClient(args);
    }


    @Override
    public void createTable() {
        dbClient.run(CREATE_TABLE);
    }

    @Override
    public void insert(String... values) {
        dbClient.run(String.format(INSERT, values));
    }

    @Override
    public Optional<List<Customer>> selectAll() {
        return Optional.ofNullable(dbClient.selectForCustomerList(SELECT_ALL));
    }

    @Override
    public Optional<Customer> selectById(int id) {
        return dbClient.selectCustomer(String.format(SELECT_BY_ID, "" + id));
    }

    public Optional<Customer> selectByName(String name) {
        return dbClient.selectCustomer(String.format(SELECT_BY_NAME, "" + name));
    }

    @Override
    public void update(Customer customer) {
        if (customer.getRentedCarId() == 0) {
            dbClient.run(String.format(UPDATE, "NULL", customer.getId()));
        } else {
            dbClient.run(String.format(UPDATE,customer.getRentedCarId(), customer.getId()));
        }
    }

    @Override
    public void deleteById(int id) {
        dbClient.run(String.format(DELETE,id));
    }
}
