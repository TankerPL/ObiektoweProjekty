import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Hotel {
    private Hotel instance = new Hotel();
    private HashMap<String, Guest> guests = new HashMap<>();
    private HashMap<Integer, Room> rooms = new HashMap<>();
    private HashMap<Integer, Float> discounts = new HashMap<>();
    private HashMap<Integer, ArrayList<Reservation>> reservations = new HashMap<>();

    private Hotel() {
        loadRooms();
        loadUsers();
        loadReservations();
    }

    public Hotel getInstance() {
        return instance;
    }

    public Guest createGuest(String userName, String firstName, String lastName) {
        if (guests.containsKey(userName)) {
            return null;
        }
        return guests.put(userName, new Guest(userName, firstName, lastName));
    }

    public boolean makeReservation(String username) {

    }

    /**
     * HELPERS
     */


    private void setDiscountForAll(float discount) {
        setDiscountForRoom(discount, (Integer[]) rooms.keySet().toArray());
    }

    private void setDiscountForRoom(float discount, int roomId) {
        discounts.put(roomId, discount);
    }

    private void setDiscountForRoom(float discount, Integer[] roomIds) {
        for (Integer roomId : roomIds) {
            discounts.put(roomId, discount);
        }
    }

    private boolean isRoomFree(int roomNumber, Date checkIn, Date checkOut) {
        for (Reservation r : reservations.get(roomNumber)) {
            if (r.getCheckIn().before(checkOut) || r.getCheckOut().after(checkIn)) {
                return false;
            }
        }
        return true;
    }

    private List<Integer> checkReservations(Date checkIn, Date checkOut) {
        ArrayList<Integer> freeRooms = new ArrayList<>();
        for (int roomNumber : rooms.keySet()) {
            if (isRoomFree(roomNumber, checkIn, checkOut)) {
                freeRooms.add(roomNumber);
            }
        }
        return freeRooms;
    }

    private void loadUsers() {
        List<Object[]> file = loadCSV("csv/guests.csv");
        for (Object[] line : file) {
            guests.put(line[0].toString(), new Guest(line[0].toString(), line[1].toString(), line[2].toString()));
        }
    }

    private void saveUsers() {

    }

    private void loadRooms() {
        List<Object[]> file = loadCSV("csv/rooms.csv");
        for (Object[] line : file) {
            rooms.put((int) line[0], new Room((int) line[0], (int) line[1], (int) line[2]));
            discounts.put((int) line[0], (float) line[3]);
        }
    }

    private void saveRooms() {
    }

    private void loadReservations() {
        List<Object[]> file = loadCSV("csv/reservations.csv");
        for (Object[] line : file) {
            int roomNumber = (int) line[0];
            reservations.putIfAbsent(roomNumber, new ArrayList<>()).add(
                    new Reservation(roomNumber, line[1].toString(), new Date((long) line[2]), new Date((long) line[3])));
        }
    }

    private void saveReservations() {
    }

    private void saveCSV(String file, Object[] data) {

    }

    private List<Object[]> loadCSV(String file) {
        Scanner scanner = null;
        ArrayList<Object[]> rooms = new ArrayList<>();

        try {
            scanner = new Scanner(new FileReader(file));
            while (scanner.hasNext()) {
                rooms.add(scanner.next().split(","));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        return rooms;
    }

    /**
     * Main
     *
     * @param args
     */

    public static void main(String[] args) {
        Console console = new Console();
        console.run();
    }
}

