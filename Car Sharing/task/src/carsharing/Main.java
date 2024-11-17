package carsharing;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    static Scanner scanner;

    static H2CompanyDAO companyDAO;
    static H2CarDAO carDAO;
    static H2CustomerDAO customerDAO;

    static Optional<Company> companyBox = Optional.empty();
    static Optional<Customer> customerBox = Optional.empty();
    static Optional<Car> carBox = Optional.empty();

    public static void main(String[] args) throws ClassNotFoundException {
        companyDAO = new H2CompanyDAO(args);
        carDAO = new H2CarDAO(args);
        customerDAO = new H2CustomerDAO(args);
        scanner = new Scanner(System.in);
        while (true) {
            System.out.println("""
                1. Log in as a manager
                2. Log in as a customer
                3. Create a customer
                0. Exit
                """);
            switch (scanner.nextLine()) {
                case "1" : {managerMenu(); break;}
                case "2" : {listCustomers(); customerMenu(); break;}
                case "3" : {createCustomer(); break;}
                case "0" : {System.exit(0);}
                default: {System.out.println("Invalid selection"); break;}
            }
        }
    }

    public static void companyMenu() {
        if (companyBox.isEmpty()) {return;}
        while (true) {
            System.out.printf("""
                    '%s' company:
                    1. Car list
                    2. Create a car
                    0. Back
                    """, companyBox.get().getName());
            switch (scanner.nextLine()) {
                case "1": {listCars(companyBox.get()); break;}
                case "2": {createCar(companyBox.get()); break;}
                case "0": {
                    companyBox = Optional.empty(); return;}
                default: {System.out.println("Invalid selection");break;}
            }
        }
    }

    public static void customerMenu() {
        if (customerBox.isEmpty()) {return;}
        while (true) {
            System.out.println("""
                    1. Rent a car
                    2. Return a rented car
                    3. My rented car
                    0. Back
                    """);
            switch (scanner.nextLine()) {
                case "1" : {listCompanies(Main::listCarsAvailable); break;}
                case "2" : {returnCar(); break;}
                case "3" : {printCar(); break;}
                case "0" : {return;}
                default: {System.out.println("Invalid selection"); break;}
            }
        }
    }

    public static void createCar(Company company) {
        System.out.println("Enter the car name:");
        String name = scanner.nextLine();
        carDAO.insert(name, "" + company.getId());
    }

    public static void createCompany() {
        System.out.println("Enter the company name:");
        String name = scanner.nextLine();
        companyDAO.insert(name);
    }

    public static void createCustomer() {
        System.out.println("Enter the customer name:");
        String name = scanner.nextLine();
        customerDAO.insert(name);
        customerBox = customerDAO.selectByName(name);
    }

    public static void listCars(Company company) {
        System.out.printf("'%s' cars:%n", company.getName());
        try {
            List<Car> cars = carDAO.selectByCompanyId(company.getId()).orElseThrow(EmptyListException::new);

            cars.forEach(c -> System.out.printf("%d. %s%n", cars.indexOf(c) + 1, c.getName()));
            System.out.println();

        } catch (EmptyListException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void listCarsAvailable() {
        Company company = companyBox.orElseThrow();
        while (true) {
            try {
                List<Car> availableCars = carDAO.selectAvailableByCompanyId(company.getId()).orElseThrow(() ->
                        new EmptyListException(String.format(
                                "No available cars in the '%s' company.", company.getName())));
                System.out.println("Choose a car:");
                availableCars.stream().sorted().forEach(c ->
                        System.out.printf("%d. %s%n", availableCars.indexOf(c) + 1, c.getName()));
                System.out.println("0. Back\n");
                String choice = scanner.nextLine();
                if (choice.equals("0")) {return;}
                if (choice.matches("[0-9]+") && Integer.parseInt(choice) <= availableCars.size()) {
                    carBox = Optional.ofNullable(availableCars.get(Integer.parseInt(choice)));
                    rentCar(Integer.parseInt(choice));
                }
            } catch (EmptyListException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void listCompanies(Runnable runner) {
        while (true) {
            try {
                List<Company> companies = companyDAO.selectAll().orElseThrow(EmptyListException::new);

                System.out.println("Choose a company:");
                companies.stream().sorted().forEach(c -> {
                    System.out.printf("%d. %s%n", companies.indexOf(c) + 1, c.getName());
                });
                System.out.println("0. Back\n");

                String choice = scanner.nextLine();
                if (choice.equals("0")) {
                    return;
                }
                if (choice.matches("[0-9]+") && Integer.parseInt(choice) <= companies.size()) {
                    companyBox = Optional.ofNullable(companies.get(Integer.parseInt(choice) - 1));
                    runner.run();
                }
            } catch (EmptyListException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void listCustomers() {
        while (true) {
            System.out.println("Choose a customer:");
            try {
                List<Customer> customers = customerDAO.selectAll().orElseThrow(EmptyListException::new);
                customers.forEach(c -> System.out.printf("%d. %s%n", customers.indexOf(c) + 1, c.getName()));
                System.out.println("0. Back\n");

                String choice = scanner.nextLine();
                if (choice.equals("0")) {return;}
                if (choice.matches("[0-9]+") && Integer.parseInt(choice) <= customers.size()) {
                    customerBox = Optional.ofNullable(
                            customerDAO.selectById(Integer.parseInt(choice)).orElseThrow(EmptyListException::new));
                    return;
                }
            } catch (EmptyListException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void managerMenu() {
        while (true) {
            System.out.println("""
                1. Company list
                2. Create a company
                0. Back
                """);
            switch (scanner.nextLine()) {
                case "1" : {listCompanies(Main::companyMenu); break;}
                case "2" : {createCompany(); break;}
                case "0" : {return;}
                default: {System.out.println("Invalid selection"); break;}
            }
        }
    }

    public static void printCar() {
        try {
            Car car = carBox.orElseThrow(() -> new EmptyListException("You didn't rent a car!"));
            System.out.printf("""
                    Your rented car:
                    %s
                    Company:
                    %s%n
                    """,
                    car.getName(),
                    companyDAO.selectById(car.getCompanyId()).orElseThrow().getName());

        } catch (EmptyListException e) {
            System.out.println(e.getMessage());
        }

    }

    public static void rentCar(int carId) {
        Car car = carBox.orElseThrow();
        Customer customer = customerBox.orElseThrow();
        customer.setRentedCarId(carId);
        customerDAO.update(customerBox.get());

        System.out.printf("You rented '%s'%n%n", car.getName());
    }

    public static void returnCar() {
        Customer customer = customerBox.orElseThrow();
        if (customer.getRentedCarId() == 0) {
            System.out.println("You didn't rent a car!\n");
        } else {
            carBox = Optional.empty();
            customer.setRentedCarId(0);
            customerDAO.update(customer);
            System.out.println("You've returned a rented car!\n");
        }
    }
}