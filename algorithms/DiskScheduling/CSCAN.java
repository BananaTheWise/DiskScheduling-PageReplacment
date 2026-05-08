package algorithms.DiskScheduling;

import java.util.Arrays;

public class CSCAN {

    public static int calculateSeek(int[] cylinders, int head, String direction, int diskSize) {

        int totalSeek = 0;

        // Sort the requests
        Arrays.sort(cylinders);

        System.out.println("Head Movements:");

        // Find split point
        int index = 0;
        while (index < cylinders.length && cylinders[index] < head) {
            index++;
        }

        if (direction.equalsIgnoreCase("large")) {

            // Move right
            for (int i = index; i < cylinders.length; i++) {
                System.out.println("Move from " + head + " to " + cylinders[i] +
                                   " = " + Math.abs(head - cylinders[i]));
                totalSeek += Math.abs(head - cylinders[i]);
                head = cylinders[i];
            }

            // Go to end
            System.out.println("Move from " + head + " to " + (diskSize - 1) +
                               " = " + Math.abs(head - (diskSize - 1)));
            totalSeek += Math.abs(head - (diskSize - 1));
            head = diskSize - 1;

            // Jump to beginning (IMPORTANT difference)
            System.out.println("Jump from " + head + " to 0 = " + (diskSize - 1));
            totalSeek += (diskSize - 1);
            head = 0;

            // Continue moving right
            for (int i = 0; i < index; i++) {
                System.out.println("Move from " + head + " to " + cylinders[i] +
                                   " = " + Math.abs(head - cylinders[i]));
                totalSeek += Math.abs(head - cylinders[i]);
                head = cylinders[i];
            }

        } else { // direction = small

            // Move left
            for (int i = index - 1; i >= 0; i--) {
                System.out.println("Move from " + head + " to " + cylinders[i] +
                                   " = " + Math.abs(head - cylinders[i]));
                totalSeek += Math.abs(head - cylinders[i]);
                head = cylinders[i];
            }

            // Go to 0
            System.out.println("Move from " + head + " to 0 = " + Math.abs(head));
            totalSeek += Math.abs(head);
            head = 0;

            // Jump to end
            System.out.println("Jump from 0 to " + (diskSize - 1) +
                               " = " + (diskSize - 1));
            totalSeek += (diskSize - 1);
            head = diskSize - 1;

            // Continue moving left
            for (int i = cylinders.length - 1; i >= index; i--) {
                System.out.println("Move from " + head + " to " + cylinders[i] +
                                   " = " + Math.abs(head - cylinders[i]));
                totalSeek += Math.abs(head - cylinders[i]);
                head = cylinders[i];
            }
        }

        System.out.println("Total Seek Distance: " + totalSeek);
        return totalSeek;
    }
}