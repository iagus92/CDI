// Ejercicio 1.1.2

/**
 * Clase de ejemplo que extiende de Thread.
 * @see java.lang.Thread
 * @author Iago Fernandez
 * @version 1.0
 */
public class MyThread extends Thread {

    /**
     * Muestra un mensaje de hola mundo.
     */
    public void run() {
         
        System.out.println("Hello World, I'm a Java Thread");
    }

    public static void main(String[] args) {

        System.out.println("Starting...");
        new MyThread().start();
        System.out.println("Exiting...");
    }   
}