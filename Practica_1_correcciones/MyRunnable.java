// Ejercicio 1.1.2

/**
 * Clase de ejemplo que implementa la interfaz Runnable.
 * @author Iago Fernandez
 * @version 1.0
 */
public class MyRunnable implements Runnable {

    /**
     * Muestra un mensaje de hola mundo.
     */
    public void run() {

          System.out.println("Hello World, I'm a Java Thread Runnable");
    }

    public static void main(String[] args) {

        System.out.println("Starting...");
        (new Thread(new MyRunnable())).start();
        System.out.println("Exiting...");
    }   
}