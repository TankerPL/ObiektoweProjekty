import org.junit.Test;
import org.testng.Assert;

import static org.junit.Assert.*;

/**
 * Created by constie on 03.12.2017.
 */
public class GuestTest {
    private String username = null;
    private String name = null;
    private String lastname = null;
    private String password = null;
    private float discount = 0;

    @Test
    public void getName() throws Exception {
        name = "a";
        Assert.assertEquals("a", name);
    }

    @Test
    public void getLastname() throws Exception {
        lastname = "a";
        Assert.assertEquals("a", lastname);
    }

    @Test
    public void getUsername() throws Exception {
        username = "a";
        Assert.assertEquals("a", username);
    }

    @Test
    public void getPassword() throws Exception {
        password = "a";
        Assert.assertEquals("a", password);
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
    public void toCSV() throws Exception {
        name = "a";
        lastname = "a";
        username = "a";
        password = "a";
        discount = 2;
        Assert.assertEquals((long) 2, (long) discount);
        Assert.assertEquals("a", password);
        Assert.assertEquals("a", username);
        Assert.assertEquals("a", lastname);
        Assert.assertEquals("a", name);
    }

}