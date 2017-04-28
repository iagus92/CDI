import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import javax.imageio.*;

/**
 * Clase que cuenta el numero de pixeles negros de una imagen binarizada y suavizada.
 * @author Iago Fernandez Gonzalez
 * @version 1.0
 */
public class BlackPixels {

    static BufferedImage img;
    static int[][] data;
    static int filter, hilos, width, height, umbral;
    static int cont=0;

  public static void main(String[] args) {

      System.out.println("starting...");
			long inicio = System.nanoTime();
      int current=0;

      // Asegura que el número de argumentos pasados por parámetro es tres.
      if (args.length != 3){
          throw new IllegalArgumentException("Se requieren tres argumentos numéricos.");
      }

      // Lanza una excepción si los argumentos no son numéricos.
      try{
          hilos = Integer.parseInt(args[2]);
          filter = Integer.parseInt(args[0]);
          umbral = Integer.parseInt(args[1]);
      }catch(NumberFormatException e){
          throw new IllegalArgumentException("Arg1: filtro, arg2: umbral, arg3: hilos");
      }

    try {
      // Lee una imagen de 16-bit en escala de grises.
      File file_in=new File("nanotube.png");
      img=ImageIO.read(file_in);
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
        ArrayList<MyThread> ts = new ArrayList<>();
        for (int i = 0; i < hilos; i++) {
            ts.add(new MyThread(hilos, ++current, raster_out));
            ts.get(i).start();
        }

        // Se hace join de los hilos para asegurar que finalizan antes de continuar.
        // Se acumula en cont el contador de pixeles negros de cada hilo.
        for (int i = 0; i < hilos; i++) {
            ts.get(i).join();
            cont += ts.get(i).getCont();
        }

        // Se almacena la imagen en un fichero de salida.
        img.setData(raster_out);
        File file_out = new File("binarizadaSuavizada.png");
        ImageIO.write(img, "png", file_out);

    // Captura la excepción lanzada por el join si es interrumpido.
    }catch (InterruptedException e) {
        System.out.println("Error de interupción: "+e.getMessage());
    } // Captura la excepción lanzada por ImageIO.write si el fichero no es una imagen.
    catch (IOException e) {
        System.out.println("Error de E/S: "+e.getMessage());
    }

    // Se muestra por consola el número de pixeles negros.
    System.out.println("Número de pixeles negros: "+cont);
		long fin = System.nanoTime()-inicio;
    System.out.println("Tiempo de ejecucion: " + fin/1000000 + " ms");
    System.out.println("exiting...");
  }

    // Clase que implementa la interfaz Runnable y contiene el método run de los hilos.
    static class MyThread extends Thread{

        int current, hilos, cont=0;
        WritableRaster raster_out;

        // Constructor que recibe como parámetros el número total de hilos,
        // el número del hilo actual y el buffer donde se almacenará la imagen final.
        public MyThread(int hilos, int current, WritableRaster raster_out){

            this.current = current;
            this.hilos = hilos;
            this.raster_out = raster_out;
        }

        // Devuelve el total de pixeles negros del fragmento del array tratado por el hilo.
        public int getCont(){
            return cont;
        }

        // Recorre el array por bloques de filas suavizando y binarizando la imagen.
        public void run(){

            Thread.currentThread().setName("Hilo "+current);
            double  sumatorio = 0;
            int end, aux;

            int start = width/hilos*(current-1);
            if(current < hilos)
                end = width/hilos*current;
            else end = width;

            // We build an output raster and fill-in with some modified data.
            double  suavizado = 1/Math.pow(2* filter +1,2);
            for(int i=start; i < end; i++) {
                for(int j=0; j<height; j++) {
                    sumatorio = 0;
                    for(int k = i- filter; k<= filter +i; k++ ){
                        for(int k2 = j- filter; k2<= filter +j; k2++ ) {
                            if(k > 0 && k < width && k2 > 0 && k2 < height) {
                                sumatorio += data[k][k2];
                            }
                        }
                    }
                    if (suavizado*sumatorio/2 >= umbral) {
                        aux = Integer.MAX_VALUE;
                    }else {
                        aux = 0;
                        cont++;
                    }
                    // Almacena los pixeles suavizados en el raster.
                    raster_out.setSample(i,j,0,aux);
                }
            }
        }
    }
}
