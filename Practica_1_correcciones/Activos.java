// Ejercicio 1.1.4

/**
 * Clase de ejemplo que cuenta el numero de hilos activos.
 * <p>
 * Extiende de Thread.
 * @see java.lang.Thread
 * @author Iago Fernandez
 * @version 1.0
 */
public class Activos extends Thread{

    /**
     * Muestra un mensaje de hola mundo y hace un sleep de un segundo.
     */
    public void run(){
         
        System.out.println("Hello World, I'm a Java Thread");
        try{
            sleep(1000);
        }
        catch (InterruptedException e){
            System.out.println("ERROR: hilo en sleep interrumpido.");
        }
    }

    public static void main(String[] args) {
			
        System.out.println("Starting...");
        Activos s = new Activos();

        // Muestra el número de hilos activos antes y después del start.
        System.out.println("> nº de hilos al inicio: " + java.lang.Thread.activeCount());
        s.start();
        System.out.println("> nº de hilos tras start: " + java.lang.Thread.activeCount());

        // Asegura que el hilo ha finalizado antes de continuar.
        while (s.isAlive());
        System.out.println("> nº de hilos al final: " + java.lang.Thread.activeCount());

        System.out.println("Exiting...");
    }     
}