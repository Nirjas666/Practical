
import java.util.Scanner;

public class TerminationDetection {

    static int messagesInTransit = 0;

    // Represents a process
    static class Process {

        int id;
        boolean active;

        Process(int id) {
            this.id = id;
            this.active = true;
        }

        void startWork() {
            active = true;
            System.out.println("Process P" + id + " is ACTIVE.");
        }

        void finishWork() {
            active = false;
            System.out.println("Process P" + id + " is PASSIVE.");
        }

        void sendMessage(Process receiver) {

            messagesInTransit++;

            System.out.println(
                "P" + id + " sent a message to P" + receiver.id
            );
        }

        void receiveMessage() {

            if (messagesInTransit > 0) {
                messagesInTransit--;
                active = true;

                System.out.println(
                    "P" + id + " received a message and became ACTIVE."
                );
            } else {
                System.out.println("No message available to receive.");
            }
        }
    }

    // Display current system status
    static void displayStatus(Process[] processes) {

        System.out.println("\n----- SYSTEM STATUS -----");

        for (Process p : processes) {

            System.out.println(
                "Process P" + p.id + ": " +
                (p.active ? "ACTIVE" : "PASSIVE")
            );
        }

        System.out.println(
            "Messages in transit: " + messagesInTransit
        );
    }

    // Check global termination
    static boolean isTerminated(Process[] processes) {

        for (Process p : processes) {

            if (p.active) {
                return false;
            }
        }

        return messagesInTransit == 0;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        if (n <= 0) {
            System.out.println("Invalid number of processes.");
            sc.close();
            return;
        }

        Process[] processes = new Process[n];

        // Create processes
        for (int i = 0; i < n; i++) {
            processes[i] = new Process(i + 1);
        }

        displayStatus(processes);

        int choice;

        do {

            System.out.println("\n===== MENU =====");
            System.out.println("1. Start process");
            System.out.println("2. Finish process");
            System.out.println("3. Send message");
            System.out.println("4. Receive message");
            System.out.println("5. Display status");
            System.out.println("6. Check termination");
            System.out.println("7. Exit");

            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            int id, receiver;

            switch (choice) {

                case 1:
                    System.out.print("Enter process ID: ");
                    id = sc.nextInt();

                    if (id >= 1 && id <= n) {
                        processes[id - 1].startWork();
                    } else {
                        System.out.println("Invalid process ID.");
                    }
                    break;

                case 2:
                    System.out.print("Enter process ID: ");
                    id = sc.nextInt();

                    if (id >= 1 && id <= n) {
                        processes[id - 1].finishWork();
                    } else {
                        System.out.println("Invalid process ID.");
                    }
                    break;

                case 3:
                    System.out.print("Enter sender process ID: ");
                    id = sc.nextInt();

                    System.out.print("Enter receiver process ID: ");
                    receiver = sc.nextInt();

                    if (id >= 1 && id <= n &&
                        receiver >= 1 && receiver <= n &&
                        id != receiver) {

                        processes[id - 1].sendMessage(
                            processes[receiver - 1]
                        );

                    } else {
                        System.out.println("Invalid process IDs.");
                    }
                    break;

                case 4:
                    System.out.print("Enter receiving process ID: ");
                    id = sc.nextInt();

                    if (id >= 1 && id <= n) {
                        processes[id - 1].receiveMessage();
                    } else {
                        System.out.println("Invalid process ID.");
                    }
                    break;

                case 5:
                    displayStatus(processes);
                    break;

                case 6:
                    displayStatus(processes);

                    if (isTerminated(processes)) {
                        System.out.println(
                            "SYSTEM HAS TERMINATED."
                        );
                    } else {
                        System.out.println(
                            "SYSTEM HAS NOT TERMINATED."
                        );
                    }
                    break;

                case 7:
                    System.out.println("Exiting program.");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 7);

        sc.close();
    }
}
