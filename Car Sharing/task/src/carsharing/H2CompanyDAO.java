package carsharing;

import java.util.List;
import java.util.Optional;

public class H2CompanyDAO implements DAO<Company> {

    private static final String CREATE_DATABASE = "CREATE DATABASE IF NOT EXISTS carsharing";
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS COMPANY (ID INT PRIMARY KEY AUTO_INCREMENT, NAME VARCHAR_IGNORECASE(255) UNIQUE NOT NULL);";

    private static final String INSERT = "INSERT INTO COMPANY (NAME) VALUES '%s')";
    private static final String SELECT_ALL = "SELECT * FROM COMPANY";
    private static final String SELECT_BY_ID = "SELECT * FROM COMPANY WHERE ID = '%s'";
    private static final String UPDATE = "UPDATE COMPANY SET NAME = ? WHERE ID = '%s'";
    private static final String DELETE_BY_ID = "DELETE FROM COMPANY WHERE ID = '%s'";

    private final DbClient dbClient;

    public H2CompanyDAO(String[] args) throws ClassNotFoundException {
        dbClient = new DbClient(args);
        createTable();
    }

    @Override
    public void createTable() {
        dbClient.run(CREATE_TABLE);
    }

    @Override
    public void insert(String... companyName) {
        dbClient.run(String.format(INSERT, companyName[0]));
    }

    @Override
    public Optional<List<Company>> selectAll() {
        return Optional.ofNullable(dbClient.selectForCompanyList(SELECT_ALL));
    }

    @Override
    public Optional<Company> selectById(int id) {
        return dbClient.selectCompany(String.format(SELECT_BY_ID, id));
    }

    @Override
    public void update(Company company) {
        int id = company.getId();
        String name = company.getName();
        dbClient.run(String.format(UPDATE, name, id));
    }

    @Override
    public void deleteById(int id) {
        dbClient.run(String.format(DELETE_BY_ID, id));
    }
}
