// Ejercicio 2.1.3
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.*;

/**
 * Clase que utiliza hilos para binarizar una imagen en tonos de gris en funcion de un umbral indicado por el usuario.
 * Todos los pixeles que superen el umbral se pondran a blanco, mientras que los que se queden por
 * debajo se pondran a negro.
 * <p>
 * Recibe como parametros el numero de hilos y el umbral.
 * @author Iago Fernandez Gonzalez
 * @version 1.0
 */
public class BinarizeThreads {

    static BufferedImage img;
    static int[][] data;
    static int umbr, hilos, width, height;

  public static void main(String[] args) {

      System.out.println("starting...");
      int current=0;

      // Asegura que el número de argumentos pasados por parámetro es dos.
      if (args.length != 2){
          throw new IllegalArgumentException("Se requieren dos argumentos numérico.");
      }

      // Lanza una excepción si los argumentos no son numéricos.
      try{
          hilos = Integer.parseInt(args[1]);
          umbr = Integer.parseInt(args[0]);
      }catch(NumberFormatException e){
          throw new IllegalArgumentException("Arg1: umbral, arg2: hilos");
      }


      try {
          // Lee una imagen de 16-bit en escala de grises.
          File file_in=new File("nanotube.png");
          img = ImageIO.read(file_in);
          WritableRaster raster_out=img.getRaster();

          // Se crea un array bidimensional y un raster para acceder a la imagen.
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
            // Se almacenan los hilos en un ArrayList y se llama a start.
            ArrayList<Thread> ts = new ArrayList<>();
            for (int i = 0; i < hilos; i++) {
                ts.add(new Thread(new MyThread(hilos, ++current, raster_out)));
                ts.get(i).start();
            }

            // Se hace join de los hilos para asegurar que finalizan antes de continuar.
            for (int i = 0; i < hilos; i++) {
                ts.get(i).join();
            }

            // Se almacena la imagen en un fichero de salida.
            img.setData(raster_out);
            File file_out = new File("binarizada.png");
            ImageIO.write(img, "png", file_out);

      // Captura la excepción lanzada por el join si es interrumpido.
      }catch (InterruptedException e) {
          System.out.println("Error de interupción: "+e.getMessage());
      } // Captura la excepción lanzada por ImageIO.write si el fichero no es una imagen.
      catch (IOException e) {
          System.out.println("Error de E/S: "+e.getMessage());
      }

        System.out.println("exiting...");
  }

    // Clase que implementa la interfaz Runnable y contiene el método run de los hilos.
    static class MyThread implements Runnable{

        private int current, hilos;
        WritableRaster raster_out;

        // Constructor que recibe como parámetros el número total de hilos,
        // el número del hilo actual y el buffer donde se almacenará la imagen final.
        public MyThread(int hilos, int current, WritableRaster raster_out){

            this.current = current;
            this.hilos = hilos;
            this.raster_out = raster_out;
        }

        // Recorre el array por bloques de filas binarizando la imagen.
        public void run(){

            System.out.println("Hilo "+Thread.currentThread().getName());
            int end;
            int start = width/hilos*(current-1);
            if(current < hilos)
                end = width/hilos*current;
            else end = width;

            for(int i=start; i < end; i++) {
                for(int j=0; j<height; j++) {
                    if (data[i][j]/2 >= umbr)
                        data[i][j] = Integer.MAX_VALUE;
                    else
                        data[i][j] = 0;
                    // Almacena los pixeles binarizados en el raster.
                    raster_out.setSample(i,j,0,data[i][j]);
                }
            }
        }
    }
}