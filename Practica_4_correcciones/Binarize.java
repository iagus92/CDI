// Ejercicio 2.1.2
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.*;

/**
 * Clase que binariza una imagen en tonos de gris en funcion de un umbral indicado por el usuario.
 * Todos los pixeles que superen el umbral se pondran a blanco, mientras que los que se queden por
 * debajo se pondran a negro.
 * <p>
 * Recibe como parametro el umbral.
 * @author Iago Fernandez Gonzalez
 * @version 1.0
 */
public class Binarize {

    public static void main(String[] args) {

        System.out.println("starting...");
        int umbr;

        // Asegura que el número de argumentos pasados por parámetro es uno.
        if (args.length != 1) {
            throw new IllegalArgumentException("Se requieren dos argumentos numérico.");
        }

        // Lanza una excepción si el argumento no es numérico.
        try {
            umbr = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Arg1: umbral");
        }

        try {
            // Lee una imagen de 16-bit en escala de grises.
            File file_in = new File("nanotube.png");
            BufferedImage img = ImageIO.read(file_in);
            WritableRaster raster_out = img.getRaster();

            // Se crea un array bidimensional y un raster para acceder a la imagen.
            int width = img.getWidth();
            int height = img.getHeight();
            int data[][] = new int[width][height];
            Raster raster_in = img.getData();

            // Se copia la imagen al array.
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    final int d = raster_in.getSample(i, j, 0);
                    data[i][j] = d;
                }
            }

            // Binariza los pixeles del array.
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    if (data[i][j] / 2 >= umbr)
                        data[i][j] = Integer.MAX_VALUE;
                    else
                        data[i][j] = 0;
                    // Almacena los pixeles binarizados en el raster.
                    raster_out.setSample(i, j, 0, data[i][j]);
                }
            }

            // Se almacena la imagen en un fichero de salida.
            img.setData(raster_out);
            File file_out = new File("binarizada.png");
            ImageIO.write(img, "png", file_out);

        // Captura la excepción lanzada por ImageIO.write si el fichero no es una imagen.
        }catch (IOException e) {
            System.out.println("Error de E/S: " + e.getMessage());
        }

        System.out.println("exiting...");
    }
}