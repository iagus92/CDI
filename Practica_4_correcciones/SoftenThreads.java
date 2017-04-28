  // These are the packages we need for the example.
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.*;

/// \brief Main class to launch application.
/// \note We always print a final message before leaving.
public class SoftenThreads {

    static BufferedImage img;
    static int[][] data;
    static int filter, hilos, width, height;

  public static void main(String[] args) {

    System.out.println("starting...");

      int current=0;

      if (args.length != 2){
          throw new IllegalArgumentException("Se requieren dos argumentos numérico.");
      }

      try{
          hilos = Integer.parseInt(args[1]);
          filter = Integer.parseInt(args[0]);
      }catch(NumberFormatException e){
          throw new IllegalArgumentException("Arg1: filtro, arg2: hilos");
      }

    try {
        // Here, we just read a fixed file, the example is a 16-bit
        // gray scale image.
      File file_in=new File("nanotube.png");
      img=ImageIO.read(file_in);
      WritableRaster raster_out=img.getRaster();

      // We prepare our data array and a raster to access the image.
      width=img.getWidth();
      height=img.getHeight();
      data=new int[width][height];
      Raster raster_in=img.getData();

      // We copy the data from the image to our array.
      for(int i=0; i<width; i++) {
        for(int j=0; j<height; j++) {
          final int d=raster_in.getSample(i, j, 0);
          data[i][j]=d;
        }
      }
        // Here, we just read a fixed file, the example is a 16-bit
        // gray scale image.
        ArrayList<Thread> ts = new ArrayList<>();
        for (int i = 0; i < hilos; i++) {

            ts.add(new Thread(new myThread(hilos, ++current, raster_out)));
            ts.get(i).start();
        }
        for (int i = 0; i < hilos; i++) {

            ts.get(i).join();
        }

        img.setData(raster_out);
        File file_out = new File("suavizado.png");

        ImageIO.write(img, "png", file_out);

    // Captura la excepción lanzada por el join si es interrumpido.
    }catch (InterruptedException e) {
        System.out.println("Error de interupción: "+e.getMessage());
    } // Captura la excepción lanzada por ImageIO.write si el fichero no es una imagen.
    catch (IOException e) {
        System.out.println("Error de E/S: "+e.getMessage());
    }

      // As always: out last line.
    System.out.println("exiting...");
  }

    static class myThread implements Runnable{

        private int current, hilos;
        WritableRaster raster_out;

        public myThread(int hilos, int current, WritableRaster raster_out){

            this.current = current;
            this.hilos = hilos;
            this.raster_out = raster_out;
        }

        public void run(){
            System.out.println("Hilo "+Thread.currentThread().getName());
            double  sumatorio = 0;
            int end;

            int start = width/hilos*(current-1);
            if(current < hilos)
                end = width/hilos*current;
            else end = width;

            // We build an output raster and fill-in with some modified data.
            double  suavizado = 1/Math.pow(2*filter+1,2);
            for(int i=start; i < end; i++) {
                for(int j=0; j<height; j++) {
                    sumatorio = 0;
                    for(int k=i-filter; k<=filter+i; k++ ){
                        for(int k2=j-filter; k2<=filter+j; k2++ ) {
                            if(k > 0 && k < width && k2 > 0 && k2 < height) {
                                sumatorio += data[k][k2];
                            }
                        }
                    }
                    raster_out.setSample(i,j,0,suavizado*(sumatorio));
                }
            }
        }
    }
}