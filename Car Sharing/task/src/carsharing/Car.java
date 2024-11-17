package carsharing;

public class Car implements Comparable<Car>{
    private final int id;
    private String name;
    private int companyId;

    public Car(int id, String name, int companyId) {
        this.id = id;
        this.name = name;
        this.companyId = companyId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCompanyId() {
        return companyId;
    }

    @Override
    public int compareTo(Car o) {
        return this.id - o.id;
    }
}
