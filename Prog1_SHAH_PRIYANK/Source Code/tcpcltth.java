
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;
import java.net.*;

/**
 *
 * @author priyankpshah
 */
public class tcpcltth extends Thread {

    static int iteration = 1000;
    static double start_time = 0, end_time = 0;
    static byte[] a;
    static int block;
    static int port = 5454;
    int portnumber = 0;
    String hostname = null;
    //taking argument from user and passing to local variable
    public tcpcltth(int port, String host) {
        this.portnumber = port;
        this.hostname = host;
    }
    //run method for tcp client ; major operation performs here
    public void run() {
        try {
            Socket clientsocket = new Socket(hostname, this.portnumber);
            DataOutputStream toserver = new DataOutputStream(clientsocket.getOutputStream());
            InputStream fromserver = clientsocket.getInputStream();
           
            start_time = System.currentTimeMillis();
           
            for (int i = 0; i < iteration; i++) {
                toserver.write(a);
            }
           
            for (int i = 0; i < iteration; i++) {
                fromserver.read(a);
            }
           
            end_time = System.currentTimeMillis();
            
            clientsocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    //main method ; taking input from user for block size, port number and host address
    public static void main(String[] args) throws Exception {
        int threadno = Integer.parseInt(args[0]);
        block = Integer.parseInt(args[1]);
        a = new byte[block];
        String hn = args[2];

        for (int i = 0; i < block; i++) {
            a[i] = 1;
        }
        tcpcltth th[] = new tcpcltth[threadno];
        //thread initialization
        for (int i = 0; i < threadno; i++) {
            th[i] = new tcpcltth(port + i, hn);

            th[i].start();
        }
        for (int i = 0; i < threadno; i++) {
            try {
                th[i].join();
            } catch (InterruptedException ex) {
            }
        }
        System.out.println("Connected!!");
        
      //Calculation for throughput and latency
        double total_time = end_time - start_time;
        System.out.println("Total Time:" + total_time);

        double latency = (total_time) / iteration;

        double throughput = (2 * block * iteration * 8) / (total_time * 1000);

        System.out.println("Latency: " + latency + " ms");
        System.out.println("Throughput: " + throughput + " Mbit/sec");
        System.out.println("Connection Closed!!");

    }
}
