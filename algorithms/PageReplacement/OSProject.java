package algorithms.PageReplacement;

import java.util.Scanner;

 class OSProject {

    public static void main(String[] args) {
        System.out.println("Enter page reference string");
        Scanner sc=new Scanner(System.in);
        String reference= sc.nextLine();
        String[] parts = reference.split("[,\\s]+");
        System.out.println("Enter number of Frames");
        int Frame_no=sc.nextInt();
        int[] pages = new int[parts.length];
         for (int i = 0; i < parts.length; i++) {
            pages[i] = Integer.parseInt(parts[i]);
        }
         System.out.println("FIFO page Replacement");
         FIFO.simulate(pages,Frame_no);
         System.out.println("LRU page replacement");
         LRU.simulate(pages,Frame_no);
         System.out.println("Optimal page replacement");
         Optimal.simulate(pages, Frame_no);
        System.out.println("Second Chance page replacement ");
        secondChance.simulate(pages, Frame_no);
    }
    
}
