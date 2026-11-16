
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RingAlgorithm {

    static int n;
    static int[] processIds;
    static boolean[] isAlive;

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter total number of processes in the ring: ");
        n = sc.nextInt();

        if (n < 2) {
            System.out.println("At least 2 processes are required.");
            sc.close();
            return;
        }

        processIds = new int[n];
        isAlive = new boolean[n];

        for (int i = 0; i < n; i++) {

            System.out.print("Enter Process ID for ring position " + i + ": ");
            processIds[i] = sc.nextInt();

            isAlive[i] = true;
        }

        // Find initial coordinator
        int initialCoordinatorIndex = getMaxAliveIndex();

        System.out.println("\nInitial Coordinator is Process: "
                + processIds[initialCoordinatorIndex]);

        // Simulate coordinator crash
        System.out.println("Process "
                + processIds[initialCoordinatorIndex]
                + " has crashed.");

        isAlive[initialCoordinatorIndex] = false;

        // Accept only an alive initiator
        int initiator;

        while (true) {

            System.out.print("\nEnter ring index of process initiating election (0 to "
                    + (n - 1) + "): ");

            initiator = sc.nextInt();

            if (initiator < 0 || initiator >= n) {
                System.out.println("Invalid index. Try again.");
            }
            else if (!isAlive[initiator]) {
                System.out.println("Process "
                        + processIds[initiator]
                        + " is crashed. Cannot initiate election.");
            }
            else {
                break;
            }
        }

        runRingElection(initiator);

        sc.close();
    }

    private static void runRingElection(int initiator) {

        System.out.println("\n--- Starting Ring Election ---");

        List<Integer> activeList = new ArrayList<>();

        // Add initiator only if alive
        activeList.add(processIds[initiator]);

        int current = (initiator + 1) % n;

        // Circulate election message around the ring
        while (current != initiator) {

            if (isAlive[current]) {

                activeList.add(processIds[current]);

                System.out.println("Process " + processIds[current]
                        + " received election message, added its ID to list: "
                        + activeList);

            } else {

                System.out.println("Process " + processIds[current]
                        + " is down. Skipping...");
            }

            current = (current + 1) % n;
        }

        System.out.println("\nElection message returned to initiator (Process "
                + processIds[initiator] + ")");

        System.out.println("Final Active List: " + activeList);

        // Find highest active process ID
        int newCoordinatorId =
                activeList.stream()
                        .max(Integer::compare)
                        .orElse(-1);

        System.out.println("\n>>> New Coordinator is Process ID: "
                + newCoordinatorId + " <<<");

        // Circulate coordinator announcement
        System.out.println("\nCirculating Coordinator Message around the ring...");

        current = (initiator + 1) % n;

        while (current != initiator) {

            if (isAlive[current]) {

                System.out.println("Process " + processIds[current]
                        + " notified: Coordinator is "
                        + newCoordinatorId);
            }

            current = (current + 1) % n;
        }

        System.out.println("Process " + processIds[initiator]
                + " notified: Coordinator is " + newCoordinatorId);
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
}
