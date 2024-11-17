package carsharing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DbClient {

    private static String dbFilePath = "jdbc:h2:./src/carsharing/db/%s";
    private static final String DRIVER_CLASS_NAME = "org.h2.Driver";

    private final String dbUrl;

    Connection conn;

    public DbClient(String[] args) throws ClassNotFoundException {
        Optional<String> dbFileName = Optional.ofNullable(args[1]);
        dbUrl = String.format(dbFilePath, dbFileName.orElse("carsharing"));
        Class.forName(DRIVER_CLASS_NAME);
    }

    public void run(String request) {
        try (Connection conn = DriverManager.getConnection(this.dbUrl);
             Statement statement = conn.createStatement()
             ) {
                 statement.executeUpdate(request);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<Company> selectCompany(String query) {
        List<Company> results = selectForCompanyList(query);
        if (results.size() > 1) {
            throw new IllegalStateException("Query returned more than one result!");
        }
        return Optional.ofNullable(results.getFirst());
    }

    public Optional<Car> selectCar(String query) {
        List<Car> results = selectForCarList(query);
        if (results.size() > 1) {
            throw new IllegalStateException("Query returned more than one result!");
        }
        return Optional.ofNullable(results.getFirst());
    }

    public Optional<Customer> selectCustomer(String query) {
        List<Customer> results = selectForCustomerList(query);
        if (results.size() > 1) {
            throw new IllegalStateException("Query returned more than one result!");
        }
        return Optional.ofNullable(results.getFirst());
    }

    public List<Company> selectForCompanyList(String query) {
        List<Company> results = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(dbUrl);
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            ) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String name = resultSet.getString("NAME");
                    results.add(new Company(id, name));
                }
                return results;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public List<Car> selectForCarList(String query) {
        List<Car> results = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(dbUrl);
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(query)
        ) {
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                int companyId = resultSet.getInt("COMPANY_ID");
                results.add(new Car(id, name, companyId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public List<Customer> selectForCustomerList(String query) {
        List<Customer> results = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(dbUrl);
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query)
        ) {
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                int rentedCarId = resultSet.getInt("RENTED_CAR_ID");
                results.add(new Customer(id, name, rentedCarId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }
}
