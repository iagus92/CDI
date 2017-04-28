// Ejercicio 1.4.1
import java.util.ArrayList;

/**
 * Clase que mide el tiempo total de ejecucion (en ms) del numero de hilos pasado como parametro.
 * @author Iago Fernandez Gonzalez
 * @version 1.0
 */
public class ThreadTime {
    
    public static void main (String[] args){
        
        System.out.println("Starting...");
        long inicio = System.nanoTime();
        int hilos;
        ArrayList<Thread> ts = new ArrayList<>();

        // Si el número de argumentos pasados por parámetro no es 2 se lanza una excepción.
        if (args.length != 1) {
            throw new IllegalArgumentException("Se requiere un argumento numérico");
        }

        // Si los argumentos no son numéricos lanza una excepción.
        try {
            hilos = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El argumento debe ser numérico");
        }

        // Se instancian los hilos y se ponen en estado ejecutable tras el start.
        for (int i = 0; i<hilos;i++){
            MyRunnable mr =  new MyRunnable();
            ts.add(new Thread(mr));
            ts.get(i).start();
        }

        // Se usa join para que el main espere a que finalicen todos los hilos creados antes de continuar.
        for (int i = 0; i<hilos;i++){
            try {
                ts.get(i).join();
            } catch (InterruptedException ex) {
                System.out.println("ERROR: interrumpido hilo con join");
            }
        }

        long fin = System.nanoTime()-inicio;
        System.out.println("Tiempo de ejecucion: " + fin/1000000 + " ms");
        System.out.println("Exiting...");
    }

    static class MyRunnable implements Runnable{

        @Override
        public void run (){

            System.out.println("Inicio del hilo " +Thread.currentThread().getName());
            int aux=1;
            for (int i=0;i<1000000;i++){
                aux += aux*aux*i;
            }
            System.out.println("Fin del hilo " +Thread.currentThread().getName());
        }
    }

}
