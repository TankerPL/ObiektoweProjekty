public class Guest implements CSVInterface {
    private String username = null;
    private String name = null;
    private String lastname = null;
    private String password = null;
    private float discount = 0;

    Guest(String username, String name, String lastname, String password, float discount) {
        this.name = name;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        setDiscount(discount);
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount > 0 ? discount : 0;
    }

    @Override
    public String toCSV() {
        return String.format("%s,%s,%s,%s,%s", username, name, lastname, password, discount);
    }
}
