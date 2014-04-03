package lambda;

public class Author {

    private String name;
    private Integer year;

    public Author() {}
    public Author(String name, Integer year) {
        this.name = name;
        this.year = year;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getYear() {
        return this.year;
    }

    public String getName() {
        return this.name;
    }
}
