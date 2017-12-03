public class Guest implements CSVInterface {
    private String username = null;
    private String name = null;
    private String lastName = null;
    private String password = null;
    private float discount = 0;

    Guest(String username, String name, String lastName, String password, float discount) {
        this.name = name;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        setDiscount(discount);
    }

    public String getName() {
        return name;
    }

    public Guest setName(String name) {
        this.name = name;
        return this;
    }

    public String getLastname() {
        return lastName;
    }

    public Guest setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public Guest setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Guest setPassword(String password) {
        this.password = password;
        return this;
    }

    public float getDiscount() {
        return discount;
    }

    public Guest setDiscount(float discount) {
        this.discount = discount > 0 ? discount : 0;
        return this;
    }

    @Override
    public String toCSV() {
        return String.format("%s,%s,%s,%s,%s", username, name, lastName, password, discount);
    }
}
