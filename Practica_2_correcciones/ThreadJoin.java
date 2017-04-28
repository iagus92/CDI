// Ejercicio 1.3.1

import java.util.ArrayList;

/**
 * Clase que hace uso de join para sincronizar el numero de hilos pasado como parametro. Muestra mensajes al inicio y fin de cada llamada a run.
 * @author Iago Fernandez Gonzalez
 * @version 1.0
 */
public class ThreadJoin {
    
    public static void main (String[] args){
        
        System.out.println("Starting...");
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
        for (int i = 0; i<hilos; i++){
            ts.add(new ThreadJoin.MyThread());
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
        System.out.println("Exiting...");
    }

    static class MyThread extends Thread{

        // Muestra mensajes al inicio y fin de la ejecución de run.
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