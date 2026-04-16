
package os.project;
import java.util.*;
public class Optimal {
    public static void simulate(int[] pages, int frameCount) {
        int[] frames = new int[frameCount];
        int pageFaults = 0;

        // Initialize frames as empty
        for (int i = 0; i < frameCount; i++) {
            frames[i] = -1;
        }

        for (int i = 0; i < pages.length; i++) {
            int page = pages[i];
            
            if (containsPage(frames, page)) {
                printFrames(frames);
                continue;
            }

            // PAGE FAULT
            pageFaults++;

            // If space available → just add
            if (countFrames(frames) < frameCount) {
                for (int j = 0; j < frameCount; j++) {
                    if (frames[j] == -1) {
                        frames[j] = page;
                        break;
                    }
                }
            } else {
                // Find page to replace - the one used farthest in future
                int indexToRemove = -1;
                int farthest = i + 1;

                for (int j = 0; j < frameCount; j++) {
                    int currentPage = frames[j];

                    int k;
                    for (k = i + 1; k < pages.length; k++) {
                        if (pages[k] == currentPage) {
                            break;
                        }
                    }

                    // If page not found in future
                    if (k == pages.length) {
                        indexToRemove = j;
                        break;
                    }

                    // Choose farthest usage
                    if (k > farthest) {
                        farthest = k;
                        indexToRemove = j;
                    }
                }

                // Replace page
                frames[indexToRemove] = page;
            }

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
    }}