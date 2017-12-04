import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by constie on 03.12.2017.
 */
public class ReservationTest {
    private Date checkIn = null;
    private Date checkOut = null;
    private int days = 3;
    private ArrayList<Integer> roomNumbers = new ArrayList<>();
    private String guestUsername = null;

    @Test
    public void getCheckIn() throws Exception {
        checkIn = new Date(1512259261000l);
        Assert.assertNotNull(checkIn);
    }

    @Test
    public void setCheckIn() throws Exception {
        checkIn = new Date(1512518463000l);
        Assert.assertNotNull(checkIn);
    }

    @Test
    public void setCheckOut() throws Exception {
        checkOut = new Date(1512259261000l);
        Assert.assertNotNull(checkOut);
    }

    @Test
    public void getCheckOut() throws Exception {
        checkOut = new Date(1512259261000l);
        Assert.assertNotNull(checkOut);
    }

    @Test
    public void getGuestUsername() throws Exception {
        guestUsername = "a";
        Assert.assertNotNull(guestUsername);
    }

    @Test
    public void setGuest() throws Exception {
        guestUsername = "a";
        Assert.assertNotNull(guestUsername);
    }

    @Test
    public void getRoomNumbers() throws Exception {
        roomNumbers.add(1);
        roomNumbers.add(2);
        roomNumbers.add(3);
        Assert.assertEquals((Integer) 1, roomNumbers.get(0));
    }


    @Test
    public void setRoomNumbers() throws Exception {
        roomNumbers.add(1);
        roomNumbers.add(2);
        roomNumbers.add(3);
        Assert.assertFalse(roomNumbers.isEmpty());
    }

    @Test
    public void getDays() throws Exception {
        checkIn = new Date(1512518463000l);
        checkOut = new Date(1512259261000l);
        long days = Math.abs((int) ((checkIn.getTime() - checkOut.getTime()) / (1000 * 60 * 60 * 24)));
        Assert.assertEquals((long) 3, days);
    }

    @Test
    public void toCSV() throws Exception {
        guestUsername = "a";
        checkIn = new Date(1512518463000l);
        checkOut = new Date(1512259261000l);
        Assert.assertNotNull(guestUsername);
        Assert.assertNotNull(checkIn);
        Assert.assertNotNull(checkOut);
    }

}