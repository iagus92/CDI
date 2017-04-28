import java.io.IOException;
import java.net.*;
import java.io.*;
import java.util.concurrent.CountDownLatch;
import java.awt.image.*;
import javax.imageio.*;

/**
 * Created by IagoFG on 25/04/2017.
 */
public class BinarizedSocket {

    // Main del servidor.
    public static void main (String args[]) throws InterruptedException {

        System.out.println("Starting...");
        Socket socket;
        ObjectInputStream input;
        ObjectOutputStream output;
        CountDownLatch startSignal = new CountDownLatch(1);
        int nThreads = 10;
        Thread[] threads = new Thread[nThreads];
        CountDownLatch doneSignal = new CountDownLatch(nThreads);
        BufferedImage img;
        WritableRaster raster_out;
        int[][] data;
        int width, height, umbr=20000;

        try {
            File file_in=new File("nanotube.png");
            img = ImageIO.read(file_in);
            raster_out=img.getRaster();

            width=img.getWidth();
            height=img.getHeight();
            data=new int[width][height];
            Raster raster_in=img.getData();

            // Se copia la imagen al array.
            for(int i=0; i<width; i++) {
                for(int j=0; j<height; j++) {
                    final int d=raster_in.getSample(i, j, 0);
                    data[i][j]=d;
                }
            }

            ServerSocket server = new ServerSocket(4444); // Crea socket.

            // Crea clientes. Una vez conectados al socket crea hilos.
            for(int i=0; i<nThreads; i++) {
                Client c = new Client(startSignal,doneSignal,width,height,umbr,i+1, nThreads);
                c.connect();
                threads[i] = new Thread(c);
                threads[i].start();
            }
            startSignal.countDown(); // Resta 1 y libera los clientes en espera.

            int a=0,b=0;
            for(int i=0; i<nThreads; i++) {
                socket = server.accept(); // Comienza a escuchar peticiones.
                output = new ObjectOutputStream(socket.getOutputStream()); // Crea OutputStream para socket.
                output.writeObject(data); // Escribe en el OutputStream.
                input = new ObjectInputStream(socket.getInputStream()); // Recibe InputStream de socket.
                int[][] dataFrag = (int[][]) input.readObject(); // Castea InputStream a array.
                raster_out.setSample(a,b,0,dataFrag[a][b]);
                input.close();
                output.close();
                socket.close();
            }
            doneSignal.await(); // Hilo principal espera a que finalicen los clientes.

            img.setData(raster_out);
            File file_out = new File("binarizada.png");
            ImageIO.write(img, "png", file_out);

        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally{
            System.out.println("Exiting...");
        }
    }

}

// Clase cliente.
class Client implements Runnable {
    private InetAddress host = null;
    private Socket socket = null;
    private ObjectOutputStream output = null;
    private ObjectInputStream input = null;
    private final CountDownLatch startSignal;
    private final CountDownLatch doneSignal;
    private int current, width, height, umbr, hilos;
    private int [][] data;

    public Client(CountDownLatch startSignal, CountDownLatch doneSignal, int width, int height, int umbr, int current, int hilos){
        this.startSignal = startSignal;
        this.doneSignal = doneSignal;
        this.height = height;
        this.current = current;
        this.width = width;
        this.umbr = umbr;
        this.hilos = hilos;
        data = new int[width][height];
    }

    public void run() {
        try {
            startSignal.await(); // Espera.
            input = new ObjectInputStream(socket.getInputStream()); // Recibe InputStream de socket.
            data = (int[][]) input.readObject(); // Castea InputStream a array.
            data =  binarize(data);
            output = new ObjectOutputStream(socket.getOutputStream()); // Crea OutputStream para socket.
            output.writeObject(data); // Escribe en el OutputStream.
            input.close();
            output.close();
            socket.close();
            doneSignal.countDown(); // Cada hilo resta 1 cuando acaba.
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void connect() throws IOException {
        host = InetAddress.getLocalHost(); // Obtiene nombre del servidor.
        socket = new Socket(host.getHostName(), 4444); // Se conecta al socket del servidor.
        while(!socket.isConnected()); // Asegura la conexión.
        System.out.println("Connection: "+socket.isConnected());
    }

    public int[][] binarize(int[][] data){
        int end;
        int start = data.length/hilos*(current-1);
        if(current < hilos)
            end = data.length/hilos*current;
        else end = data.length;

        for(int i=start; i < end; i++) {
            for(int j=0; j<data[0].length; j++) {
                if (data[i][j]/2 >= umbr)
                    data[i][j] = Integer.MAX_VALUE;
                else
                    data[i][j] = 0;
            }
        }
        return data;
    }
}