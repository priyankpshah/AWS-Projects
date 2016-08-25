
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author priyankpshah
 */
public class udpclient extends Thread {

    static int threadno;
    static int size;
    static int iter = 100;
    static double starttime = 0, endtime = 0;
    static int port = 7678;
    int portNumber = 0;
    String Hostname;

    public udpclient(int i,String host) {
        this.portNumber = i;
        this.Hostname = host;

    }
    //run method for thread class ; socket creation and data send and receive operations.
    
    public void run() {
        try {
            DatagramSocket client = new DatagramSocket();
            byte[] send = new byte[size];
            byte[] receive = new byte[size];

            for (int i = 0; i < size; i++) {
                send[i] = 1;
            }

            InetAddress ipaddr = InetAddress.getByName(Hostname);
            DatagramPacket sender = new DatagramPacket(send, send.length, ipaddr, this.portNumber);

            for (int i = 0; i < iter; i++) {
                client.send(sender);
            }
           
            DatagramPacket receiver = new DatagramPacket(send, send.length);
            for (int i = 0; i < iter; i++) {
                receive = receiver.getData();
            }
           

            client.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {

        size = Integer.parseInt(args[0]);
        threadno = Integer.parseInt(args[1]);
       String host = args[2]; 
        System.out.println("Byte Length is:" + size);
        starttime = System.currentTimeMillis();

        udpclient th[] = new udpclient[threadno];
        starttime = System.currentTimeMillis();
        for (int i = 0; i < threadno; i++) {
            th[i] = new udpclient(port + i,host);

            th[i].start();
            System.out.println("thread started: t" + i);
        }
        for (int i = 0; i < threadno; i++) {
            try {
                th[i].join();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
        endtime = System.currentTimeMillis();
        
        //calculation of latency and throguhput for given paraemeters by user.

        double total_time = endtime - starttime;
        System.out.println("total time:" + total_time);
        double latency = total_time / iter;
        System.out.println("Latency: " + latency + " ms");

        double throughput = (2 * size * iter * 8) / (total_time * 1000);
        System.out.println("Throghput: " + throughput + "Mbits/sec");

    }
}
