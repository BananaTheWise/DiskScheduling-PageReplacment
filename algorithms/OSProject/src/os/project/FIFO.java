package os.project;
import java.util.*;
public class FIFO {
    
    public static void simulate(int[] pages, int frameCount) {

        int[] frames = new int[frameCount];
        Queue<Integer> queue = new LinkedList<>();
        int pageFaults = 0;

        // Initialize frames as empty
        for (int i = 0; i < frameCount; i++) {
            frames[i] = -1;
        }

        for (int page : pages) {

            // condition to see if page doesn't exist
            if (!queue.contains(page)) {

                pageFaults++;

                if (queue.size() == frameCount) {
                    int removed = queue.poll();
                    // Remove from frames array
                    for (int i = 0; i < frameCount; i++) {
                        if (frames[i] == removed) {
                            frames[i] = -1;
                            break;
                        }
                    }
                }

                // Add new page to queue and frames
                queue.offer(page);
                for (int i = 0; i < frameCount; i++) {
                    if (frames[i] == -1) {
                        frames[i] = page;
                        break;
                    }
                }
            }

            // Print frames with fixed positions
            printFrames(frames);
        }

        System.out.println("Total Page Faults = " + pageFaults);
    }

    private static void printFrames(int[] frames) {
        System.out.print("Frames [");
        for (int i = 0; i < frames.length; i++) {
            if (frames[i] == -1) {
                System.out.print("OUT");
            } else {
                System.out.print(frames[i]);
            }
            if (i < frames.length - 1) System.out.print("|");
        }
        System.out.println("]");
    }
}

