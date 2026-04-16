package disk.scheduling;

public class FCFS {

    public static int calculateSeek(int[] cylinders, int head) {
        int totalSeek = 0;

        System.out.println("Head Movements:");

        for (int i = 0; i < cylinders.length; i++) {
            int next = cylinders[i];
            int distance = Math.abs(head - next);

            System.out.println("Move from " + head + " to " + next + " = " + distance);

            totalSeek += distance;
            head = next;
        }

        System.out.println("Total Seek Distance: " + totalSeek);
        return totalSeek;
    }
}
