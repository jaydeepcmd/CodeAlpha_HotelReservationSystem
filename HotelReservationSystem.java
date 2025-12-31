import java.io.*;
import java.util.*;

class Room {
    int roomNumber;
    String category;
    boolean isBooked;

    Room(int roomNumber, String category) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.isBooked = false;
    }
}

class Reservation {
    String customerName;
    int roomNumber;
    String category;
    double price;

    Reservation(String customerName, int roomNumber, String category, double price) {
        this.customerName = customerName;
        this.roomNumber = roomNumber;
        this.category = category;
        this.price = price;
    }
}

public class HotelReservationSystem {

    static ArrayList<Room> rooms = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    static final String FILE_NAME = "bookings.txt";

    public static void main(String[] args) {
        initializeRooms();
        int choice;

        do {
            System.out.println("\n===== HOTEL RESERVATION SYSTEM =====");
            System.out.println("1. View Available Rooms");
            System.out.println("2. Book Room");
            System.out.println("3. Cancel Reservation");
            System.out.println("4. View Bookings");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1 -> viewRooms();
                case 2 -> bookRoom();
                case 3 -> cancelBooking();
                case 4 -> viewBookings();
                case 5 -> System.out.println("Thank you for using the system!");
                default -> System.out.println("Invalid choice!");
            }
        } while (choice != 5);
    }

    static void initializeRooms() {
        rooms.add(new Room(101, "Standard"));
        rooms.add(new Room(102, "Standard"));
        rooms.add(new Room(201, "Deluxe"));
        rooms.add(new Room(202, "Deluxe"));
        rooms.add(new Room(301, "Suite"));
    }

    static void viewRooms() {
        System.out.println("\nAvailable Rooms:");
        for (Room r : rooms) {
            if (!r.isBooked) {
                System.out.println("Room " + r.roomNumber + " - " + r.category);
            }
        }
    }

    static void bookRoom() {
        sc.nextLine();
        System.out.print("Enter your name: ");
        String name = sc.nextLine();

        System.out.print("Enter room number: ");
        int roomNo = sc.nextInt();

        for (Room r : rooms) {
            if (r.roomNumber == roomNo && !r.isBooked) {
                double price = switch (r.category) {
                    case "Standard" -> 2000;
                    case "Deluxe" -> 3500;
                    default -> 5000;
                };

                System.out.println("Payment Amount: ₹" + price);
                System.out.print("Confirm payment (yes/no): ");
                sc.nextLine();
                String confirm = sc.nextLine();

                if (confirm.equalsIgnoreCase("yes")) {
                    r.isBooked = true;
                    saveBooking(new Reservation(name, roomNo, r.category, price));
                    System.out.println("Booking confirmed!");
                }
                return;
            }
        }
        System.out.println("Room not available.");
    }

    static void cancelBooking() {
        System.out.print("Enter room number to cancel: ");
        int roomNo = sc.nextInt();

        for (Room r : rooms) {
            if (r.roomNumber == roomNo && r.isBooked) {
                r.isBooked = false;
                removeBooking(roomNo);
                System.out.println("Reservation cancelled.");
                return;
            }
        }
        System.out.println("Booking not found.");
    }

    static void saveBooking(Reservation res) {
        try (FileWriter fw = new FileWriter(FILE_NAME, true)) {
            fw.write(res.customerName + "," + res.roomNumber + "," +
                     res.category + "," + res.price + "\n");
        } catch (IOException e) {
            System.out.println("Error saving booking.");
        }
    }

    static void viewBookings() {
        System.out.println("\nBooking Details:");
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                System.out.println("Name: " + data[0] +
                        " | Room: " + data[1] +
                        " | Category: " + data[2] +
                        " | Price: ₹" + data[3]);
            }
        } catch (IOException e) {
            System.out.println("No bookings found.");
        }
    }

    static void removeBooking(int roomNo) {
        File inputFile = new File(FILE_NAME);
        File tempFile = new File("temp.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             FileWriter fw = new FileWriter(tempFile)) {

            String line;
            while ((line = br.readLine()) != null) {
                if (!line.contains("," + roomNo + ",")) {
                    fw.write(line + "\n");
                }
            }
            inputFile.delete();
            tempFile.renameTo(inputFile);

        } catch (IOException e) {
            System.out.println("Error cancelling booking.");
        }
    }
}
