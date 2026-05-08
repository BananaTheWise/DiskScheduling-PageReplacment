
package algorithms.DiskScheduling;

public class SSTF {
   public static int calculateSeek(int[] cylinders, int head, String direction) {

        int totalSeek = 0;
        boolean[] visited = new boolean[cylinders.length];

        System.out.println("Head Movements:");

        for (int i = 0; i < cylinders.length; i++) {

            int minDistance = Integer.MAX_VALUE;

            // Step 1: find minimum distance
            for (int j = 0; j < cylinders.length; j++) {
                if (!visited[j]) {
                    int distance = Math.abs(head - cylinders[j]);
                    if (distance < minDistance) {
                        minDistance = distance;
                    }
                }
            }

            // Step 2: pick cylinder (handle tie here)
            int index = -1;

            for (int j = 0; j < cylinders.length; j++) {
                if (!visited[j] && Math.abs(head - cylinders[j]) == minDistance) {

                    if (index == -1) {
                        index = j;
                    } else {
                        // tie-breaking
                        if (direction.equalsIgnoreCase("large") && cylinders[j] > cylinders[index]) {
                            index = j;
                        } else if (direction.equalsIgnoreCase("small") && cylinders[j] < cylinders[index]) {
                            index = j;
                        }
                    }
                }
            }

            // Move head
            visited[index] = true;

            System.out.println("Move from " + head + " to " + cylinders[index] +
                               " = " + Math.abs(head - cylinders[index]));

            totalSeek += Math.abs(head - cylinders[index]);
            head = cylinders[index];
        }

        System.out.println("Total Seek Distance: " + totalSeek);
        return totalSeek;
    }
} 

