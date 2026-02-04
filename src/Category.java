
public class Category {
    private int id;
    private String name;
    private String description;
    private String color;

    public Category(int id, String name, String description, String color) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.color = color;
    }

    public Category(String name, String description, String color) {
        this.name = name;
        this.description = description;
        this.color = color;
    }

    @Override
    public String toString() {
        return id + ": " + name + " - " + description + " [" + color + "]";
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}
