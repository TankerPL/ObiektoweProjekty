import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Hotel {
    private static Hotel instance = new Hotel();
    private HashMap<String, Guest> guests = new HashMap<>();
    private HashMap<Integer, ArrayList<Integer>> beds = new HashMap<>();
    private HashMap<Integer, Room> rooms = new HashMap<>();
    private ArrayList<Reservation> reservations = new ArrayList<>();
    private HashMap<Integer, ArrayList<Reservation>> reservationsPerRoom = new HashMap<>();
    private HashMap<String, ArrayList<Reservation>> reservationsPerGuest = new HashMap<>();

    Hotel() {
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
        Guest g = guests.put(userName, new Guest(userName, firstName, lastName, password, 0.f));
        updateGuestCSV();
        return g;
    }

    public Guest updateGuest(String userName, String firstName, String lastName, String password){
        if (!guests.containsKey(userName)) {
            return null;
        }
        Guest g = guests.get(userName);
        g.setName(firstName);
        g.setLastName(lastName);
        g.setPassword(password);
        updateGuestCSV();
        return g;
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

    public Room getRoom(int roomNumber) {
        return rooms.get(roomNumber);
    }

    public Map<Integer, ArrayList<Integer>> getFreeRooms(Date checkIn, Date checkOut) {
        Map<Integer, ArrayList<Integer>> freeRooms = new HashMap<>();
        for (int roomNumber : rooms.keySet()) {
            if (isRoomFree(roomNumber, checkIn, checkOut)) {
                int beds = rooms.get(roomNumber).getBeds();
                freeRooms.putIfAbsent(beds, new ArrayList<>());
                freeRooms.get(beds).add(roomNumber);
            }
        }
        return freeRooms;
    }

    public boolean makeReservation(String username, Map<Integer, Integer> requiredRooms, Date checkIn, Date checkOut) {
        if (!guests.containsKey(username)) {
            return false;
        }
        Map<Integer, ArrayList<Integer>> freeRooms = getFreeRooms(checkIn, checkOut);
        ArrayList<Integer> bookedRooms = new ArrayList<>();
        for (int beds : requiredRooms.keySet()) {
            if (freeRooms.get(beds).size() < requiredRooms.get(beds)) {
                return false;
            }
            bookedRooms.addAll(freeRooms.get(beds).subList(0, requiredRooms.get(beds)));
        }
        Reservation r = new Reservation(bookedRooms, username, checkIn, checkOut);
        reservations.add(r);
        reservationsPerGuest.putIfAbsent(username, new ArrayList<>());
        reservationsPerGuest.get(username).add(r);
        for (Integer i : bookedRooms) {
            reservationsPerRoom.putIfAbsent(i, new ArrayList<>());
            reservationsPerRoom.get(i).add(r);
        }
        updateReservationCSV();
        return true;
    }

    public void cancelReservation(Reservation reservation) {
        reservations.remove(reservation);
        reservationsPerGuest.remove(reservation.getGuestUsername());
        for (Integer room : reservation.getRoomNumbers()) {
            reservationsPerRoom.get(room).remove(reservation);
        }
        updateReservationCSV();
    }

    public boolean hasRoomReservations(int roomNumber) {
        return reservationsPerRoom.get(roomNumber) != null && !reservationsPerRoom.get(roomNumber).isEmpty();
    }

    public void addRoom(int number, int beds, float price, float discount) {
        Room room = new Room(number, beds, price, discount);
        this.rooms.put(number, room);
        this.beds.putIfAbsent(beds, new ArrayList<>());
        this.beds.get(beds).add(number);
        updateRoomsCSV();
    }

    public boolean removeRoom(int number) {
        if (reservationsPerRoom.get(number) == null || reservationsPerRoom.get(number).isEmpty()) {
            rooms.remove(number);
            reservationsPerRoom.remove(number);
            updateRoomsCSV();
            return true;
        }
        return false;
    }

    public void editRoom(int oldNumber, int newNumber, int beds, float price, float discount) {
        Room room = getRoom(oldNumber);
        room.setRoomNumber(newNumber).setBeds(beds).setPrice(price).setDiscount(discount);
        rooms.put(newNumber, room);
        rooms.remove(oldNumber);
        updateRoomsCSV();
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

    private void setDiscountForRoom(float discount, int roomId) {
        rooms.get(roomId).setDiscount(discount);
        updateRoomsCSV();
    }

    private void setDiscountForRoom(float discount, List<Integer> roomIds) {
        for (Integer roomId : roomIds) {
            setDiscountForRoom(discount, roomId);
        }
        updateRoomsCSV();
    }

    private boolean isRoomFree(int roomNumber, Date checkIn, Date checkOut) {
        ArrayList<Reservation> reservations = reservationsPerRoom.get(roomNumber);
        if (reservations == null || reservations.isEmpty()) return true;
        for (Reservation r : reservations) {
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

    private void setDiscountForAll(float discount) {
        setDiscountForRoom(discount, new ArrayList<Integer>() {{
            addAll(rooms.keySet());
        }});
        updateRoomsCSV();
    }

    /**
     * HELPERS
     */

    private void loadGuests() {
        List<String[]> file = loadCSV("csv/guests.csv");
        for (String[] line : file) {
            guests.put(line[0], new Guest(
                            line[0],
                            line[1],
                            line[2],
                            line[3],
                            Float.valueOf(line[4])
                    )
            );
        }
    }

    private void saveGuests() {
        String[] data = guests.values().stream().map(Guest::toCSV).toArray(String[]::new);
        saveCSV("csv/guests.csv", data);
    }

    private void loadRooms() {
        List<String[]> file = loadCSV("csv/rooms.csv");
        for (String[] line : file) {
            rooms.put(Integer.valueOf(line[0]), new Room(Integer.valueOf(line[0]), Integer.valueOf(line[1]), Float.valueOf(line[2])));
            beds.putIfAbsent(Integer.valueOf(line[1]), new ArrayList<>());
            beds.get(Integer.valueOf(line[1])).add(Integer.valueOf(line[0]));
        }
    }

    private void saveRooms() {
        String[] data = rooms.values().stream().map(Room::toCSV).toArray(String[]::new);
        saveCSV("csv/rooms.csv", data);
    }

    private void loadReservations() {
        List<String[]> file = loadCSV("csv/reservations.csv");
        for (String[] line : file) {
            if (new Date(Long.valueOf(line[3])).after(new Date())) {
                continue;
            }
            List<Integer> rooms =
                    Arrays.stream(Arrays.copyOfRange(line, 3, line.length))
                            .map(Integer::valueOf)
                            .collect(Collectors.toCollection(ArrayList::new));
            String username = line[0];
            Reservation reservation = new Reservation(rooms, username, new Date(Long.valueOf(line[2])), new Date(Long.valueOf(line[3])));
            reservations.add(reservation);
            reservationsPerGuest.putIfAbsent(username, new ArrayList<>());
            reservationsPerGuest.get(username).add(reservation);
            for (Integer roomNumber : rooms) {
                reservationsPerRoom.putIfAbsent(roomNumber, new ArrayList<>());
                reservationsPerRoom.get(roomNumber).add(reservation);
            }
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
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    private List<String[]> loadCSV(String file) {
        Scanner scanner = null;
        ArrayList<String[]> data = new ArrayList<>();

        try {
            scanner = new Scanner(new FileReader(file));
            while (scanner.hasNext()) {
                data.add(scanner.next().split(","));
            }

        } catch (IOException e) {
            e.printStackTrace();
//            System.exit(1);
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        return data;
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

