import java.io.*;
import java.util.*;

public class Hotel {
    private static Hotel instance = new Hotel();
    private HashMap<String, Guest> guests = new HashMap<>();
    private HashMap<Integer, ArrayList<Integer>> beds = new HashMap<>();
    private HashMap<Integer, Room> rooms = new HashMap<>();
    private HashMap<Integer, Float> discounts = new HashMap<>();
    private ArrayList<Reservation> reservations = new ArrayList<>();
    private HashMap<Integer, ArrayList<Reservation>> reservationsPerRoom = new HashMap<>();
    private HashMap<String, ArrayList<Reservation>> reservationsPerGuest = new HashMap<>();

    private Hotel() {
        loadRooms();
        loadGuests();
        loadReservations();
    }

    public static Hotel getInstance() {
        return instance;
    }

    public Guest createGuest(String userName, String firstName, String lastName, String password) {
        if (guests.containsKey(userName)) {
            return null;
        }
        return guests.put(userName, new Guest(userName, firstName, lastName, password, 0.f));
    }

    public boolean guestExists(String username) {
        return guests.containsKey(username);
    }

    public Guest getGuest(String username) {
        return guests.get(username);
    }

    public ArrayList<Reservation> getReservationPerGuest(String username) {
        return reservationsPerGuest.get(username);
    }

    public Map<Integer, ArrayList<Integer>> getFreeRooms(Date checkIn, Date checkOut) {
        Map<Integer, ArrayList<Integer>> freeRooms = new HashMap<>();
        for (int roomNumber : rooms.keySet()) {
            if (isRoomFree(roomNumber, checkIn, checkOut)) {
                freeRooms.putIfAbsent(roomNumber, new ArrayList<>()).add(roomNumber);
            }
        }
        return freeRooms;
    }

    public boolean makeReservation(String username, Map<Integer, Integer> requiredRooms, Date checkIn, Date checkOut) {
        if (!guests.containsKey(username)) {
            return false;
        }
        Map<Integer, ArrayList<Integer>> freeRooms = getFreeRooms(checkIn, checkOut);
        for (int beds : requiredRooms.keySet()) {
            if (requiredRooms.get(beds) == 0) {
                return false;
            }
        }
        return true;
    }

    public void cancelReservation(Reservation reservation) {
        reservations.remove(reservation);
        reservationsPerRoom.remove(reservation.getRoomNumber());
        reservationsPerGuest.remove(reservation.getGuestUsername());
    }

    public void updateGuestCSV() {
        saveGuests();
    }

    public void updateRoomsCSV() {
        saveRooms();
    }

    public void updateReservationCSV() {
        saveReservations();
    }

    public void updateAllCSV() {
        saveGuests();
        saveReservations();
        saveRooms();
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
        for (Reservation r : reservationsPerRoom.get(roomNumber)) {
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

    private void loadGuests() {
        List<Object[]> file = loadCSV("csv/guests.csv");
        for (Object[] line : file) {
            guests.put(line[0].toString(), new Guest(
                            line[0].toString(), line[1].toString(), line[2].toString(), line[3].toString(), (float) line[4]
                    )
            );
        }
    }

    private void saveGuests() {
        String[] data = guests.values().stream().map(Guest::toCSV).toArray(String[]::new);
        saveCSV("csv/guests.csv", data);
    }

    private void loadRooms() {
        List<Object[]> file = loadCSV("csv/rooms.csv");
        for (Object[] line : file) {
            rooms.put((int) line[0], new Room((int) line[0], (int) line[1], (int) line[2]));
            beds.putIfAbsent((int) line[1], new ArrayList<>()).add((int) line[0]);
            discounts.put((int) line[0], (float) line[3]);
        }
    }

    private void saveRooms() {
        String[] data = rooms.values().stream().map(Room::toCSV).toArray(String[]::new);
        saveCSV("csv/rooms.csv", data);
    }

    private void loadReservations() {
        List<Object[]> file = loadCSV("csv/reservations.csv");
        for (Object[] line : file) {
            int roomNumber = (int) line[0];
            String username = line[1].toString();
            Reservation reservation = new Reservation(
                    roomNumber, username, new Date((long) line[2]), new Date((long) line[3]));
            reservations.add(reservation);
            reservationsPerRoom.putIfAbsent(roomNumber, new ArrayList<>()).add(reservation);
            reservationsPerGuest.putIfAbsent(username, new ArrayList<>()).add(reservation);
        }
    }

    private void saveReservations() {
        String[] data = reservations.stream().map(Reservation::toCSV).toArray(String[]::new);
        saveCSV("csv/reservations.csv", data);
    }

    private void saveCSV(String file, String[] data) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(file, "UTF-8");
            for (Object line : data) {
                writer.println(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
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

