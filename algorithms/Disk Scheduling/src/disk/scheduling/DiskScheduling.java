package disk.scheduling;

import java.util.Scanner;

public class DiskScheduling {

    public static void main(String[] args) {
      System.out.println("Enter cylinders visited");
        Scanner sc=new Scanner(System.in);
        String reference= sc.nextLine();
        String[] parts = reference.split("[,\\s]+");
         int[] cylinders = new int[parts.length];
          for (int i = 0; i < parts.length; i++) {
            cylinders[i] = Integer.parseInt(parts[i]);
        }
        System.out.println("Enter large if your disk goes to larger cylinder or small if disk goes to small cylinder");
        
        String direction=sc.next();
        System.out.println("Enter starting head");
        int start=sc.nextInt();
        System.out.println("Enter number of cylinders");
        int size=sc.nextInt();
        System.out.println("FCFS");
        FCFS.calculateSeek(cylinders, start);
        System.out.println("=============================================");
        System.out.println("SSTF");
        SSTF.calculateSeek(cylinders, start, direction);
        System.out.println("=============================================");
        System.out.println("SCAN");
        SCAN.calculateSeek(cylinders, start, direction, size);
        System.out.println("=============================================");
        System.out.println("LOOK");
        Look.calculateSeek(cylinders, start, direction);
    }
    
}
