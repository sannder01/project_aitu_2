/**
 * Worker entity
 * SOLID: Single Responsibility - represents only worker data
 */
public class Worker {
    private int id;
    private String name;
    private String position;
    private String email;

    public Worker(int id, String name, String position, String email) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.email = email;
    }

    public Worker(String name, String position, String email) {
        this.name = name;
        this.position = position;
        this.email = email;
    }

    @Override
    public String toString() {
        return id + ": " + name + " (" + position + ") - " + email;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
