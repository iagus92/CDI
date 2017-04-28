// Ejercicio 1.1.3

/**
 * Clase que espera un segundo antes de ejecutar el metodo run() del hilo.
 * <p>
 * Extiende de Thread.
 * @see java.lang.Thread
 * @author Iago Fernandez
 * @version 1.0
 */
public class Segundo extends Thread {

    /**
     * Muestra un mensaje de hola mundo tras estar en sleep un segundo.
     * <p>
     * Si se interrumpe el hilo durante el sleep se muestra un mensaje de error.
     */
    public void run() {

        try{
            sleep(1000);
        }
        catch (InterruptedException e){
            System.out.println("ERROR: hilo en sleep interrumpido.");
        }
        System.out.println("Hello World, I'm a Java Thread");
    }

    public static void main(String[] args) {

        System.out.println("Starting...");
        new Segundo().start();
        System.out.println("Exiting...");
    }    
}