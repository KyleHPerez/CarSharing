package carsharing;

public class Company implements Comparable<Company> {

    private int id;
    private String name;

    public Company(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Company o) {
        return this.id - o.id;
    }
}
