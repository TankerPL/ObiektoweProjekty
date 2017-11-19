public class Room {
    private int number;
    private int beds;
    private float price;
    private float discount = 0;

    Room(int number, int beds, float price) {
        this.number = number;
        this.beds = beds;
        this.price = price;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getBeds() {
        return this.beds;
    }

    public void setBeds(int beds) {
        this.beds = beds;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount > 0 ? discount : 0;
    }

    public float getPrice() {
        return this.price;
    }

    public float getPriceWithDiscount() {
        if (discount == 0) {
            return this.price;
        }
        if (discount <= 1) {
            return this.price - this.price * discount;
        }
        return this.price - discount;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}

/*class OneBed extends Room {
    public OneBed(int number, float price) {
        super(number, 1, price);
    }
}

class TwoBeds extends Room {
    public TwoBeds(int number, float price) {
        super(number, 2, price);
    }
}

class ThreeBeds extends Room {
    public ThreeBeds(int number, float price) {
        super(number, 3, price);
    }
}

class FourBeds extends Room {
    public FourBeds(int number, float price) {
        super(number, 4, price);
    }
}*/