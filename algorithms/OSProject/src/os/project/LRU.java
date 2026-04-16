package os.project;
import java.util.*;
public class LRU {
    public static void simulate(int[] pages, int frameCount) {
        int[] frames = new int[frameCount];
        List<Integer> lruOrder = new ArrayList<>();
        int pageFaults = 0;

        // Initialize frames as empty
        for (int i = 0; i < frameCount; i++) {
            frames[i] = -1;
        }

        for (int page : pages) {
            if (containsPage(frames, page)) { 
                // Update LRU order - move to end (most recently used)
                lruOrder.remove((Integer) page);
                lruOrder.add(page);
            } else {
                pageFaults++;
                
                // Remove least recently used page if frames full
                if (countFrames(frames) == frameCount) {
                    int lruPage = lruOrder.remove(0);
                    for (int i = 0; i < frameCount; i++) {
                        if (frames[i] == lruPage) {
                            frames[i] = -1;
                            break;
                        }
                    }
                }
                
                // Add new page
                for (int i = 0; i < frameCount; i++) {
                    if (frames[i] == -1) {
                        frames[i] = page;
                        break;
                    }
                }
                lruOrder.add(page);
            }
            
            // Print frames with fixed positions
            printFrames(frames);
        }
        
        System.out.println("Total Page Faults = " + pageFaults);
    }

    private static boolean containsPage(int[] frames, int page) {
        for (int f : frames) {
            if (f == page) return true;
        }
        return false;
    }

    private static int countFrames(int[] frames) {
        int count = 0;
        for (int f : frames) {
            if (f != -1) count++;
        }
        return count;
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

