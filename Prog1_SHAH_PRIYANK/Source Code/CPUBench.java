/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.Random;
import java.util.Scanner;

public class CPUBench extends Thread {

    int y = 23;
    int x = 18;
    int z = 25;
    static long iteration = 100000000;
    static int threadno; //noofthreads ; user input
    static String ch; // choice of operation ; user input
    float a = 24.7f;
    float b = 2.9f;
    float c = 11.5f;

    public void run() {
	//integer operation operation	
        if (ch.equalsIgnoreCase("i")) {
            for (int i = 0; i < iteration / threadno; i++) {
                y += x;
                x += z;
                z += y;
                y = z - x;
                y = y - x;
                z = z * z;
                x = x * y;
                y = y * x;
                y = z + x;
            }
	//floating point operations
        } else if (ch.equalsIgnoreCase("f")) {
            for (int i = 0; i < iteration / threadno; i++) {
                b += a;
                a += c;
                c = c * c;
                a = a * b;
                b = b * a;
            }

        }
    }

    public static void main(String args[]) throws InterruptedException {
        long endtime = 0;
        long starttime = 0;
        
	ch = args[1];
	threadno = Integer.parseInt(args[0]);
        CPUBench tm[] = new CPUBench[threadno];
     
       //thread Run
        starttime = System.currentTimeMillis();
        for (int i = 0; i < threadno; i++) {
            tm[i] = new CPUBench();
            tm[i].start();
        }

        for (int i = 0; i < threadno; i++) {
            try {
                tm[i].join();
            } catch (InterruptedException ex) {
            }
        }
        endtime = System.currentTimeMillis();

        long duration = endtime - starttime;
        double flops = ((iteration * 5 / duration) * 1000 * 12);
        double Gflops = (flops / 1000000000);
	//calculation
        if (ch.equalsIgnoreCase("f")) {
            System.out.println("Total time for thread:" + (duration));
            System.out.println("Flops:" + flops);
            System.out.println("GFlops:" + Gflops);
        } else if (ch.equalsIgnoreCase("i")) {
            System.out.println("Total time for thread:" + (duration));
            System.out.println("Iops:" + flops);
            System.out.println("GIops:" + Gflops);

        }
    }
}
