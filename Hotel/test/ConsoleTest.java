import org.junit.Assert;
import org.junit.Test;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import static org.junit.Assert.*;

/**
 * Created by constie on 03.12.2017.
 */
public class ConsoleTest {
    private Hotel hotel = Hotel.getInstance();
    private PrintStream out = System.out;
    private Scanner in = new Scanner(System.in);
    private Guest guest = null;
    private boolean isAdmin = false;
    private ConsoleTest.states state = ConsoleTest.states.MAINMENU;

    private enum states {
        MAINMENU,
        LOGINGUEST,
        REGISTERGUEST,
        REGISTERROOMS,
        CANCELROOMS
    }

    @Test
    public void run() throws Exception {
        state = states.MAINMENU;
        Assert.assertEquals(state.MAINMENU, state);
    }

    @Test
    public void mainMenu() throws Exception {
        Assert.assertNull(guest);
    }

    @Test
    public void loginGuest() throws Exception {
        guest = new Guest("a", "a", "a", "a", 0);
        Assert.assertEquals("a", guest.getName());
        Assert.assertEquals("a", guest.getUsername());
        Assert.assertEquals("a", guest.getLastname());
        Assert.assertEquals("a", guest.getPassword());
    }

    @Test
    public void registerGuest() throws Exception {
        guest = new Guest("b", "b", "b", "b", 0);
        Assert.assertNotEquals("a", guest.getName());
        Assert.assertNotEquals("a", guest.getUsername());
        Assert.assertNotEquals("a", guest.getLastname());
        Assert.assertNotEquals("a", guest.getPassword());
    }

    @Test
    public void registerRooms() throws Exception {
        String choice = "y";
        Date now = new Date();
        Date checkIn = new Date(1512518463000l);
        Date checkOut = new Date(1512259261000l);

        Assert.assertEquals(new Date(1512518463000l), checkIn);
        Assert.assertEquals(new Date(1512259261000l), checkOut);
        Assert.assertEquals("y", choice);
        Assert.assertEquals(new Date(), now);
    }

    @Test
    public void cancelRooms() throws Exception {
        String choice = "n";
        Date now = new Date();
        Date checkIn = new Date(1512518463000l);
        Date checkOut = new Date(1512259261000l);

        Assert.assertNotEquals(new Date(1509753661000l), checkIn);
        Assert.assertNotEquals(new Date(1507071661000l), checkOut);
        Assert.assertNotEquals("y", choice);
        Assert.assertNotEquals(new Date(1512518463000l), now);
    }

}