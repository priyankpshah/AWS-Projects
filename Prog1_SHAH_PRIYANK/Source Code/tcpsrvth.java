
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author priyankpshah
 */
public class tcpsrvth extends Thread {

    static int block = 0;
    static int iteration = 1000;
    static byte a[];
    static int port = 5454;
    int portnumber = 0;

    public tcpsrvth(int i) {
        this.portnumber = i;
    }
    //Run method for thread class ; all the major operations are performed here for socket connection and data send receive
    public void run() {
        try {
            
           
            ServerSocket receiver = new ServerSocket(this.portnumber);
            Socket s = receiver.accept();
            InputStream input = s.getInputStream();
            OutputStream toclient = new DataOutputStream(s.getOutputStream());

            for (int i = 0; i < iteration; i++) {
                input.read(a);
            }
           

            for (int i = 0; i < iteration; i++) {
                toclient.write(a);
            }
            

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    //taking argument from user as in no of thread to run the program and packet size.
    public static void main(String args[]) throws Exception { 


       int threadno = Integer.parseInt(args[0]);
        block = Integer.parseInt(args[1]);
        a = new byte[block];

        tcpsrvth th[] = new tcpsrvth[threadno];
        
        System.out.println("Initializing....Waiting for Client!!!!!");
        for (int i = 0; i < threadno; i++) {
            th[i] = new tcpsrvth(port + i);
            th[i].start();
        }
        
        for (int i = 0; i < threadno; i++) {
            try {
                th[i].join();
            } 
            catch (InterruptedException ex) {
            }
        }

    }
}
