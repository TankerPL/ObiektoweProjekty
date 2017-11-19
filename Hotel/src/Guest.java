public class Guest {
    private String username = null;
    private String name = null;
    private String lastname = null;
    private float discount = 0;

    Guest(String username, String name, String lastname) {
        this.name = name;
        this.lastname = lastname;
        this.username = username;
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

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount > 0 ? discount : 0;
    }
}
