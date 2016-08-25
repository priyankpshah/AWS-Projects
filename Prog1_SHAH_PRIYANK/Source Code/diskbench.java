
/**
 *
 * @author priyankpshah
 */
import java.awt.PointerInfo;
import java.io.*;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Random;
import java.util.Scanner;

public class diskbench extends Thread {

    static long start_time = 0;
    static long end_time = 0;
    static FileOutputStream fs = null;
    static RandomAccessFile raf = null;
    static diskbench db = null;
    static FileChannel fc = null;
    static ByteBuffer bf = null;
    static int pointer = 0;
    static byte block[] = null;
    static int s = 0; //size for block ; user input
    static int size;
    static int threadno = 0; //threadno ; user input
    static String mode;
    static String type;
    static final int iter = 100; //no of iterration to overhead initialize timing
    static float swd, rwd, srd, rrd = 0; //total duration stored for operation 

    public diskbench() {
        try {

            String file = "diskfile";
            fs = new FileOutputStream(new File(file));
            raf = new RandomAccessFile(file, "rw");
            fc = raf.getChannel();
            bf = ByteBuffer.allocate(block.length);


        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static void main(String[] args) {

        s = Integer.parseInt(args[0]);
        threadno = Integer.parseInt(args[1]);
        block = new byte[s];
        bf.wrap(block);

        //thread initialization 
        diskbench th[] = new diskbench[threadno];
        for (int i = 0; i < threadno; i++) {
            th[i] = new diskbench();
            th[i].start();
        }

        for (int i = 0; i < threadno; i++) {
            try {
                th[i].join();
            } catch (InterruptedException ie) {
            }
        }

        //Sequantial Write
        double swdurationinmili = swd / 1000000;
        double swduration = swd / 1000000000;
        double swlatency = swdurationinmili / iter;
        double swthroughtput = (s * (iter / swduration) / 1024 * 1024);

        System.out.println("Output:\n\n");
        System.out.println("-----------------------------------------------------------\n");
        System.out.println("Sequantial Write:");
        System.out.println("\nTotal Time: " + swdurationinmili + "ms \nTotal Bytes: " + s + "");
        System.out.println("Latency: " + swlatency + " ms" + "\nThroughput: " + swthroughtput + " MBPS\n");
        System.out.println("-----------------------------------------------------------");
        //Random Write
        double rwdurationinmili = rwd / 1000000;
        double rwduration = rwd / 1000000000;
        double rwlatency = rwdurationinmili / iter;
        double rwthroughtput = (s * iter) / (rwduration) / 1024 * 1024;

        System.out.println("-----------------------------------------------------------");
        System.out.println("Random Write:");
        System.out.println("\nTotal Time: " + rwdurationinmili + "ms \nTotal Bytes: " + s);
        System.out.println("Latency: " + rwlatency + " ms" + "\nThroughput: " + rwthroughtput + " MBPS\n");
        System.out.println("-----------------------------------------------------------");

        //Sequnatial Read
        double srdurationinmili = srd / 1000000;
        double srduration = srd / 1000000000;
        double srlatency = srdurationinmili / iter;
        double srthroughtput = (s * (iter / srduration)) * 1024 * 1024;

        System.out.println("-----------------------------------------------------------");
        System.out.println("Sequantial Read:");
        System.out.println("\nTotal Time: " + srdurationinmili + "ms \nTotal Bytes: " + s);
        System.out.println("Latency: " + srlatency + " ms" + "\nThroughput: " + srthroughtput + " MBPS\n");
        System.out.println("-----------------------------------------------------------");

        //Random Read
        double rrdurationinmili = rrd / 1000000;
        double rrduration = rrd / 1000000000;
        double rrlatency = rrdurationinmili / iter;
        double rrthroughtput = (s * (iter / rrduration)) * 1024 * 1024;

        System.out.println("-----------------------------------------------------------");
        System.out.println("Random Read:");
        System.out.println("\nTotal Time: " + rrdurationinmili + "ms \nTotal Bytes: " + s);
        System.out.println("Latency: " + rrlatency + " ms" + "\nThroughput: " + rrthroughtput + " MBPS\n");
        System.out.println("-----------------------------------------------------------");

    }
    //function to generate random number for seek position

    public int random(int i) {
        Random rand = new Random();
        pointer = rand.nextInt(i);
        return pointer;
    }
    //random read function

    public float randomRead() {
        try {
            start_time = System.nanoTime();
            for (int i = 0; i < iter; i++) {
                int r = db.random(s);
                fc.position(r);
                while (bf.hasRemaining()) {
                    fc.read(bf);

                }

            }
            fc.close();
            raf.close();
            end_time = System.nanoTime() - start_time;
        } catch (IOException ie) {
        }
        return (end_time);
    }
    //random write function

    public float randomWrite() {
        try {
            start_time = System.nanoTime();
            for (int i = 0; i < iter; i++) {
                int r = db.random(s);
                fc.position(r);
                while (bf.hasRemaining()) {
                    fc.write(bf);
                }

            }
            fc.close();
            raf.close();
            end_time = System.nanoTime() - start_time;
        } catch (IOException ie) {
        }
        return end_time;
    }
    //sequantial read function

    public float sequantialRead() {
        try {
            start_time = System.nanoTime();
            raf.seek(0);
            for (int i = 0; i < iter; i++) {

                raf.read(block, 0, s - 1);
            }
            raf.close();
            end_time = System.nanoTime() - start_time;
        } catch (IOException ie) {
        }

        return end_time;
    }
    //sequantial write function

    public float sequantialWrite() {
        try {
            start_time = System.nanoTime();
            raf.seek(0);
            for (int i = 0; i < iter; i++) {

                raf.write(block, 0, s - 1);
            }
            raf.close();
            end_time = System.nanoTime() - start_time;
        } catch (IOException ie) {
        }
        return end_time;
    }
    //thread run method

    public void run() {
        db = new diskbench();
        swd = db.sequantialWrite();
        srd = db.sequantialRead();

        rwd = db.randomWrite();
        rrd = db.randomRead();



    }
}
