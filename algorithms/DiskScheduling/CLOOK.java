package algorithms.DiskScheduling;

import java.util.Arrays;

public class CLOOK {

    public static int calculateSeek(int[] cylinders, int head, String direction) {

        int totalSeek = 0;

        Arrays.sort(cylinders);

        System.out.println("Head Movements:");

        int index = 0;
        while (index < cylinders.length && cylinders[index] < head) {
            index++;
        }

        if (direction.equalsIgnoreCase("large")) {

            // Move right (like LOOK)
            for (int i = index; i < cylinders.length; i++) {
                System.out.println("Move from " + head + " to " + cylinders[i] +
                        " = " + Math.abs(head - cylinders[i]));
                totalSeek += Math.abs(head - cylinders[i]);
                head = cylinders[i];
            }

            // Jump to the smallest request (IMPORTANT difference)
            if (index > 0) {
                System.out.println("Jump from " + head + " to " + cylinders[0] +
                        " = " + Math.abs(head - cylinders[0]));
                totalSeek += Math.abs(head - cylinders[0]);
                head = cylinders[0];
            }

            // Continue moving right
            for (int i = 1; i < index; i++) {
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

            // Jump to largest request
            if (index < cylinders.length) {
                System.out.println("Jump from " + head + " to " + cylinders[cylinders.length - 1] +
                        " = " + Math.abs(head - cylinders[cylinders.length - 1]));
                totalSeek += Math.abs(head - cylinders[cylinders.length - 1]);
                head = cylinders[cylinders.length - 1];
            }

            // Continue moving left
            for (int i = cylinders.length - 2; i >= index; i--) {
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