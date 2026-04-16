/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package disk.scheduling;

import java.util.Arrays;

public class Look {
 public static int calculateSeek(int[] cylinders, int head, String direction) {

        int totalSeek = 0;

        Arrays.sort(cylinders);

        System.out.println("Head Movements:");

        int index = 0;
        while (index < cylinders.length && cylinders[index] < head) {
            index++;
        }

        if (direction.equalsIgnoreCase("large")) {

            // Move right (only up to last request)
            for (int i = index; i < cylinders.length; i++) {
                System.out.println("Move from " + head + " to " + cylinders[i] +
                        " = " + Math.abs(head - cylinders[i]));
                totalSeek += Math.abs(head - cylinders[i]);
                head = cylinders[i];
            }

            // Reverse direction (no going to disk end!)

            for (int i = index - 1; i >= 0; i--) {
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

            // Reverse direction
            for (int i = index; i < cylinders.length; i++) {
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