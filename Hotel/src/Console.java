import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class Console {
    private Hotel hotel = Hotel.getInstance();
    private PrintStream out = System.out;
    private Scanner in = new Scanner(System.in);
    private Guest guest = null;
    private boolean isAdmin = false;
    private states state = states.MAINMENU;

    private enum states {
        MAINMENU,
        LOGINGUEST,
        REGISTERGUEST,
        REGISTERROOMS,
        CANCELROOMS
    }

    public void run() {
        while (true) {
            out.println(state);
            switch (state) {
                case MAINMENU:
                    mainMenu();
                    break;
                case LOGINGUEST:
                    loginGuest();
                    break;
                case REGISTERGUEST:
                    registerGuest();
                    break;
                case REGISTERROOMS:
                    registerRooms();
                    break;
                case CANCELROOMS:
                    cancelRooms();
                    break;
                default:
            }
        }
    }

    private void mainMenu() {
        if (guest != null) {
            out.println("1 - Zarezerwuj pokoj");
            out.println("2 - Anuluj rezerwacje");
        }
        out.println();
        if (guest == null) {
            out.println("4 - Zaloguj sie");
            out.println("5 - Zarejestruj sie");
        } else {
            out.println("4 - Wyloguj sie");
        }
        out.println();

        String choice;
        while (true) {
            if (!in.hasNext()) continue;
            choice = in.next();
            if (choice.equals("quit")) break;
            if (choice.equals("admin")) {
                isAdmin = true;
                continue;
            }

            switch (choice) {
                case "1":
                    state = states.REGISTERROOMS;
                    return;
                case "2":
                    state = states.CANCELROOMS;
                    return;
                case "4":
                    if (guest == null) {
                        state = states.LOGINGUEST;
                    } else {
                        guest = null;
                    }
                    return;
                case "5":
                    if (guest != null) return;
                    state = states.REGISTERGUEST;
                    return;
                default:
                    clear();
                    out.println("Prosze wybrac odpowiednia opcje");
            }
        }
    }

    private void loginGuest() {
        String username;
        String password;
        Guest guest;

        while (true) {
            out.println("Podaj 0, aby powrocic do menu");
            out.print("Prosze podac nazwe uzytkownika: ");
            username = in.next();
            if (username.isEmpty()) continue;
            if (username.equals("0")) {
                state = states.MAINMENU;
                return;
            }
            guest = hotel.getGuest(username);
            if (guest == null) {
                out.println("Nie znaleziono uzytkownika " + username);
                continue;
            }
            out.print("Prosze podac haslo: ");
            password = in.next();
            if (password.isEmpty()) {
                out.println("Podano nieprawidlowe haslo");
                continue;
            }

            if (!password.equals(guest.getPassword())) {
                out.println("Podano nieprawidlowe haslo");
                continue;
            }
            this.guest = guest;
            state = states.MAINMENU;
            break;

        }
    }

    private void registerGuest() {
        String firstName;
        String lastName;
        String username;
        String password;
        String choice;

        while (true) {
            out.println("Podaj 0, aby powrocic do menu");
            do {
                out.print("Prosze podac imie: ");
                firstName = in.next();
            } while (firstName.isEmpty());
            do {
                out.print("Prosze podac nazwisko: ");
                lastName = in.next();
            } while (lastName.isEmpty());
            do {
                out.print("Prosze podac nazwe uzytkownika: ");
                username = in.next();
                if (hotel.guestExists(username)) {
                    out.println("Podana nazwa uzytkownika jest juz zajeta");
                    continue;
                }
            } while (username.isEmpty());
            do {
                out.print("Prosze podac haslo: ");
                password = in.next();
            } while (password.isEmpty());

            out.println("Imie: " + firstName);
            out.println("Nazwisko: " + lastName);
            out.println("Nazwa uzytkownika: " + username);
            out.println("Haslo: " + password);

            do {
                out.print("Czy dane sa poprawne? [y/n] ");
                choice = in.next();
                if (choice.equals("y")) {
                    guest = hotel.createGuest(username, firstName, lastName, password);
                    state = states.MAINMENU;
                    hotel.updateGuestCSV();
                    return;
                }
            } while (!choice.equals("n"));
        }
    }

    private void registerRooms() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date checkIn;
        Date checkOut;
        String choice;
        Date now = new Date();

        while (true) {
            out.println("Podaj datę zameldowania (YYYY-MM-DD): ");
            try {
                checkIn = df.parse(in.next());
                if (checkIn.compareTo(now) < 0) {
                    out.println("Data zameldowania nie moze byc przed " + now);
                    continue;
                }
                break;
            } catch (ParseException ignored) {
                out.println("Prosze podac date w formacie YYYY-MM-DD");
            }
        }
        while (true) {
            out.println("Podaj datę wymeldowania (YYYY-MM-DD): ");
            try {
                checkOut = df.parse(in.next());
                if (!checkOut.after(checkIn)) {
                    out.println("Data wymeldowania musi byc pozniej niż data zameldowania");
                    continue;
                }
                break;
            } catch (ParseException ignored) {
                out.println("Prosze podac date w formacie YYYY-MM-DD");
            }
        }

        Map<Integer, ArrayList<Integer>> freeRooms = hotel.getFreeRooms(checkIn, checkOut);
        if (freeRooms.isEmpty()) {
            out.println("Niestety nie mamy wolnych pokoi dla podanego terminu");
            do {
                out.print("Czy chcesz sprawdzic inny termin? [y/n]");
                choice = in.next();
                if (choice.equals("n")) state = states.MAINMENU;
            } while (!choice.equals("y") && !choice.equals("n"));
        }

        out.println("Dla podanego terminu mamy wolne pokoje:");
        for (Integer beds : freeRooms.keySet()) {
            out.printf("%s-osobowe: %s\n", beds, freeRooms.get(beds).size());
        }

        out.println("Prosze podac liczbe pokoi do zarezerwowania");
        HashMap<Integer, Integer> requiredRooms = new HashMap<>();
        int bed;

        for (Integer beds : freeRooms.keySet()) {
            out.printf("%s-osobowe (max %s): ", beds, freeRooms.get(beds).size());
            do {
                bed = in.nextInt();
                if (bed > freeRooms.get(beds).size()) {
                    out.println("Brak wystarczającej liczby pokoi");
                    out.printf("Dostepnych jest %s %s-osobowych pokoi\n", freeRooms.get(beds).size(), beds);
                    continue;
                }
                requiredRooms.put(beds, bed);
                break;
            } while (true);
        }
        out.println();
        Float price = 0.f;
        for (Integer i : requiredRooms.keySet()) {
            out.printf("%s-osobowy - %s\n", i + 1, requiredRooms.get(i));
            for (int j = 0; j < requiredRooms.get(i); j++) {
                price += hotel.getRoom(freeRooms.get(i).get(j)).getPriceWithDiscount();
            }
            price *= 1 - guest.getDiscount();
        }
        out.printf("Cena: %s PLN\n", price);
        String confirm;
        do {
            out.println("Czy potwierdzasz rezerwacje? [y/n]: ");
            confirm = in.next();
            if (confirm.equals("y")) {
                hotel.makeReservation(guest.getUsername(), requiredRooms, checkIn, checkOut);
                return;
            }
        } while (!confirm.equals("n"));

    }

    private void cancelRooms() {
        out.println("Zaplanowane rezerwacje");
        Reservation r;
        Integer choice;
        String confirm;
        ArrayList<Reservation> reservations = hotel.getReservationPerGuest(guest.getUsername());

        for (int i = 1; i <= reservations.size(); i++) {
            r = reservations.get(i - 1);
            out.printf("%s - %s %s %s", i, r.getCheckIn(), r.getCheckOut(), r.getRoomNumbers());
        }
        out.println("Prosze wybrac rezerwacje do anulowania: ");
        choice = in.nextInt();
        while (choice < 1 || choice >= reservations.size()) {
            out.println("Prosze podac poprawny numer rezerwacji");
            choice = in.nextInt();
        }
        r = reservations.get(choice - 1);
        do {
            out.print("Prosze potwierdzic anulowanie rezerwacji " + choice + " [y/n]: ");
            confirm = in.next();
            if (confirm.equals("y")) {
                hotel.cancelReservation(r);
                state = states.MAINMENU;
                return;
            }
        } while (!confirm.equals("n"));
    }

    private String getHeader() {
        return "--- HOTEL ---";
    }

    private void clear() {
        System.out.print("\n\033[2J\033[1;1H");
    }
}
