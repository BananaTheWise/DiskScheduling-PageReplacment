
package algorithms.PageReplacement;

public class secondChance {
    public static void simulate(int[] pages, int frameCount) {

        int[] frames = new int[frameCount];
        int[] refBits = new int[frameCount];

        // Initialize frames as empty
        for (int i = 0; i < frameCount; i++) {
            frames[i] = -1;
            refBits[i] = 0;
        }

        int pointer = 0;
        int pageFaults = 0;

        for (int page : pages) {

            boolean hit = false;

            // Check if page already exists
            for (int i = 0; i < frameCount; i++) {
                if (frames[i] == page) {
                    refBits[i] = 1; // Give second chance
                    hit = true;
                    break;
                }
            }

            if (!hit) {
                pageFaults++;

                while (true) {
                    // If reference bit is 0 → replace
                    if (refBits[pointer] == 0) {
                        frames[pointer] = page;
                        refBits[pointer] = 1;

                        pointer = (pointer + 1) % frameCount;
                        break;
                    } else {
                        // Give second chance
                        refBits[pointer] = 0;
                        pointer = (pointer + 1) % frameCount;
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
        System.out.println("]");    }
}