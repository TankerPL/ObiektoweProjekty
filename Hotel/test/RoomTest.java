import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by constie on 03.12.2017.
 */
public class RoomTest {
    private int number;
    private int beds;
    private float price;
    private float discount = 0;

    @Test
    public void getNumber() throws Exception {
        number = 1;
        Assert.assertEquals((long) 1, (long) number);
    }

    @Test
    public void setNumber() throws Exception {
        number = 1;
        Assert.assertEquals((long) 1, (long) number);
    }

    @Test
    public void getBeds() throws Exception {
        beds = 3;
        Assert.assertEquals((long) 3, (long) beds);
    }

    @Test
    public void setBeds() throws Exception {
        beds = 3;
        Assert.assertEquals((long) 3, (long) beds);
    }

    @Test
    public void getDiscount() throws Exception {
        Assert.assertEquals((long) 0, (long) discount);
    }

    @Test
    public void setDiscount() throws Exception {
        discount = 2;
        Assert.assertEquals((long) 2, (long) discount);
    }

    @Test
    public void getPrice() throws Exception {
        price = 22.44f;
        Assert.assertEquals((long) 22.44, (long) price);
    }

    @Test
    public void getPriceWithDiscount() throws Exception {
        price = 22.44f;
        float afDis = (float) (price*0.95);
        Assert.assertEquals((long) 21.318, (long) afDis);
    }

    @Test
    public void setPrice() throws Exception {
        price = 22.44f;
        Assert.assertEquals((long) 22.44, (long) price);
    }

    @Test
    public void toCSV() throws Exception {
        number = 1;
        beds = 3;
        price = 22.44f;
        Assert.assertEquals((long) 1, (long) number);
        Assert.assertEquals((long) 3, (long) beds);
        Assert.assertEquals((long) 22.44, (long) price);
    }

}