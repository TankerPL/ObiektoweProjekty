import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by constie on 03.12.2017.
 */
public class HotelTest {
    private Hotel hotel = new Hotel();
    private HashMap<String, Guest> guests = new HashMap<>();
    private Guest guest = new Guest("a", "a", "a", "a", 0);
    private HashMap<Integer, Room> rooms = new HashMap<>();
    private Room room = new Room(1, 3, 22.44f);
    private HashMap<Integer, ArrayList<Reservation>> reservationsPerRoom = new HashMap<>();
    private HashMap<String, ArrayList<Reservation>> reservationsPerGuest = new HashMap<>();
    Reservation reservation = new Reservation(1, "a", new Date(1512259261000l), new Date(1512518463000l));



    @Test
    public void getInstance() throws Exception {
        Assert.assertNotNull(hotel);
    }

    @Test
    public void createGuest() throws Exception {
        guests.put("a", guest);
        assertFalse(guests.isEmpty());
    }

    @Test
    public void guestExists() throws Exception {
        Assert.assertFalse(guests.containsKey("notguest"));
    }

    @Test
    public void getGuest() throws Exception {
        guests.put("a", guest);
        Assert.assertEquals(guest, guests.get("a"));
    }

    @Test
    public void getReservationPerGuest() throws Exception {
//        System.out.println("reservationsPerGuest: " + reservationsPerGuest);
        reservationsPerGuest.put("a", new ArrayList<>());
//        ArrayList<Reservation> res = reservationsPerGuest.putIfAbsent("a", new ArrayList<>());
//        res.add(reservation);
        reservationsPerGuest.get("a").add(reservation);
//        System.out.println("reservationsPerGuest: " + reservationsPerGuest);
        Assert.assertEquals(reservation, reservationsPerGuest.get("a").get(0));
    }

    @Test
    public void getRoom() throws Exception {
        rooms.put(1, room);
        Assert.assertEquals(room, rooms.get(1));
    }

    @Test
    public void getFreeRooms() throws Exception {
        Map<Integer, ArrayList<Integer>> freeRooms = new HashMap<>();
        freeRooms.put(1, new ArrayList<>());
        freeRooms.get(1).add(1);
        System.out.println("freeRooms.get(0): " + freeRooms.get(1));
        ArrayList<Integer> n = new ArrayList<>();
        n.add(1);

        Assert.assertEquals(n, freeRooms.get(1));
    }

    @Test
    public void makeReservation() throws Exception {
        guests.put("a", guest);

        reservationsPerGuest.put("a", new ArrayList<>());
        reservationsPerGuest.get("a").add(reservation);
        Assert.assertEquals(reservation, reservationsPerGuest.get("a").get(0));
    }

    @Test
    public void cancelReservation() throws Exception {
        reservationsPerRoom.put(1, new ArrayList<>());
        reservationsPerRoom.get(1).add(reservation);
        reservationsPerRoom.get(1).remove(reservation);
        Assert.assertTrue(reservationsPerRoom.get(1).isEmpty());
    }

    @Test
    public void updateGuestCSV() throws Exception {
        guests.put("a", guest);
        Assert.assertNotNull(guests);
    }

    @Test
    public void updateRoomsCSV() throws Exception {
        rooms.put(1, room);
        Assert.assertNotNull(rooms);
    }

    @Test
    public void updateReservationCSV() throws Exception {
        reservationsPerGuest.put("a", new ArrayList<>());
        reservationsPerGuest.get("a").add(reservation);
        Assert.assertFalse(reservationsPerGuest.get("a").isEmpty());
    }


}