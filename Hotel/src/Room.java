public class Room implements CSVInterface {
    private int number;
    private int beds;
    private float price;
    private float discount = 0;

    Room(int number, int beds, float price) {
        this.number = number;
        this.beds = beds;
        this.price = price;
    }

    Room(int number, int beds, float price, float discount) {
        this(number, beds, price);
        setDiscount(discount);
    }

    public int getRoomNumber() {
        return this.number;
    }

    public Room setRoomNumber(int number) {
        this.number = number;
        return this;
    }

    public int getBeds() {
        return this.beds;
    }

    public Room setBeds(int beds) {
        this.beds = beds;
        return this;
    }

    public float getDiscount() {
        return discount;
    }

    public Room setDiscount(float discount) {
        this.discount = discount > 0 ? discount : 0;
        return this;
    }

    public float getPrice() {
        return this.price;
    }

    public float getPriceWithDiscount() {
        if (discount == 0) {
            return this.price;
        } else if (discount <= 1) {
            return this.price - this.price * discount;
        }
        return this.price - discount;
    }

    public Room setPrice(float price) {
        this.price = price;
        return this;
    }

    @Override
    public String toCSV() {
        return String.format("%s,%s,%s,%s", number, beds, price, discount);
    }
}
