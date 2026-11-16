
import java.util.Scanner;

public class BullyAlgorithm {

    static int n;
    static int[] processIds;
    static boolean[] isAlive;

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter total number of processes: ");
        n = sc.nextInt();

        if (n < 2) {
            System.out.println("At least 2 processes are required.");
            sc.close();
            return;
        }

        processIds = new int[n];
        isAlive = new boolean[n];

        // Accept process IDs
        for (int i = 0; i < n; i++) {

            System.out.print("Enter Process ID for index " + i + ": ");

            processIds[i] = sc.nextInt();
            isAlive[i] = true;
        }

        // Find initial coordinator
        int currentCoordinatorIndex = getMaxAliveIndex();

        System.out.println("\nInitial Coordinator is Process with ID: "
                + processIds[currentCoordinatorIndex]);

        // Simulate coordinator failure
        System.out.println("Process "
                + processIds[currentCoordinatorIndex]
                + " has crashed.");

        isAlive[currentCoordinatorIndex] = false;

        // Accept only a valid and alive initiator
        int initiatorIndex;

        while (true) {

            System.out.print("\nEnter the index of the process that initiates the election (0 to "
                    + (n - 1) + "): ");

            initiatorIndex = sc.nextInt();

            if (initiatorIndex < 0 || initiatorIndex >= n) {

                System.out.println("Invalid index. Try again.");

            }
            else if (!isAlive[initiatorIndex]) {

                System.out.println("Process "
                        + processIds[initiatorIndex]
                        + " is crashed. Cannot initiate election.");

            }
            else {

                break;
            }
        }

        runBullyElection(initiatorIndex);

        sc.close();
    }

    private static void runBullyElection(int initiatorIndex) {

        System.out.println("\n--- Starting Bully Election ---");

        System.out.println("Process "
                + processIds[initiatorIndex]
                + " detected coordinator failure and initiated an election.");

        boolean higherNodeResponded = false;

        // Send election messages to all higher-ID processes
        for (int i = 0; i < n; i++) {

            if (processIds[i] > processIds[initiatorIndex]) {

                System.out.println("Election message sent from "
                        + processIds[initiatorIndex]
                        + " to " + processIds[i]);

                if (isAlive[i]) {

                    System.out.println("Process " + processIds[i]
                            + " sent OK message back to "
                            + processIds[initiatorIndex]);

                    higherNodeResponded = true;
                }
            }
        }

        // Select new coordinator
        if (higherNodeResponded) {

            int newCoordinatorIndex = getMaxAliveIndex();

            System.out.println("\nHigher process responded. "
                    + "Electing highest alive process...");

            announceCoordinator(newCoordinatorIndex);

        } else {

            announceCoordinator(initiatorIndex);
        }
    }

    private static int getMaxAliveIndex() {

        int maxId = Integer.MIN_VALUE;
        int maxIndex = -1;

        for (int i = 0; i < n; i++) {

            if (isAlive[i] && processIds[i] > maxId) {

                maxId = processIds[i];
                maxIndex = i;
            }
        }

        return maxIndex;
    }

    private static void announceCoordinator(int coordIndex) {

        System.out.println("\n>>> New Coordinator is Process "
                + processIds[coordIndex] + " <<<");

        for (int i = 0; i < n; i++) {

            if (isAlive[i] && i != coordIndex) {

                System.out.println("Coordinator announcement sent to Process "
                        + processIds[i]);
            }
        }
    }
}
