// Ejercicio 1.4.4
import java.util.ArrayList;

/**
 * Clase con dos modos de ejecucion sobre el numero de hilos pasado como parametro.
 * <p>
 * Modo 0: simplemente ejecuta los hilos.
 * <p>
 * Modo 1: ejecuta los hilos y ademas muestra los tiempos de ejecucion de cada hilo, de la sincronizacion y de ejecucion total.
 * @author Iago Fernandez Gonzalez
 * @version 1.0
 */
public class ExecutionModes {
    
    public static void main (String[] args){
        
        System.out.println("Starting...");
        long inicio = System.nanoTime();
        int modo, hilos;
        ArrayList<Thread> ts = new ArrayList<>();

        // Asegura que el número de argumentos pasado por parámetro es dos.
        if (args.length != 2){
            throw new IllegalArgumentException("Se requieren dos argumentos numéricos.");
        }

        // Lanza excepción si los argumentos no son numéricos.
        try{
            hilos = Integer.parseInt(args[0]);
            modo = Integer.parseInt(args[1]); // valores: 0 ó 1
            if (modo <0 || modo >1){
                throw new IllegalArgumentException("\nArgumento 2: "
                        + "modo (valores admitidos: 0 o 1)");
            }
        }catch(NumberFormatException e){
            throw new IllegalArgumentException("\nArgumento 1: nº de hilos\n"
                    + "Argumento 2: modo (valores admitidos: 0 o 1)");
        }

        // Si el modo de ejecución es 1 muestra datos sobre el tiempo de ejecución por consola.
        if (modo == 1){
            System.out.println("-------------------------------------------------------\n"+
                                "   HILO\t\t   Duracion del Hilo\tInicio\t  Fin\n"+
                                "-------------------------------------------------------");
        }
        
        //Instanciar los Threads
        for (int i = 0; i<hilos;i++){
            MyRunnable2 mr =  new MyRunnable2(inicio, modo);
            ts.add(new Thread(mr));
            ts.get(i).start();
        }
        
        long creacion = System.nanoTime()-inicio;
        
        for (int i = 0; i<hilos;i++){
            try {
                ts.get(i).join();
            } catch (InterruptedException ex) {
                System.out.println("ERROR: interrumpido hilo con join");
            }
        }
        
        long sincro = System.nanoTime()-inicio;

        // Si el modo de ejecución es 1 muestra datos sobre el tiempo de ejecución por consola.
        if (modo==1){
            System.out.println("\n  -- Tiempo de creación: " + creacion/1000000 + " ms\n"+
                                "  -- Tiempo de sincronización: " + sincro/1000000 + " ms");
        }
        
        System.out.println("\nExiting...");
    }

    // Clase que implementa la interfaz Runnable y contiene el método run de los hilos.
    static class MyRunnable2 implements Runnable{

        private long ti = 0;
        private int modo;

        public MyRunnable2(long ti, int modo){
            this.ti = ti;
            this.modo = modo;
        }

        // Realiza una serie de operaciones y muestra el tiempo de ejecución del hilo.
        @Override
        public void run (){
            long inicio_hilo = System.nanoTime();
            long ts = inicio_hilo-ti;
            int opera=1;

            // Realiza operaciones matemáticas para evitar que el hilo acabe demasiado rápido.
            for (int i=0;i<10000000;i++){
                opera += opera*Math.sqrt(opera)*i;
            }

            long fin_hilo = (System.nanoTime()-inicio_hilo)/1000000;
            long tf = System.nanoTime()-ti;

            // Si el modo de ejecución es 1 muestra datos sobre el tiempo de ejecución del hilo por consola.
            if (modo ==1)
                System.out.println("[" +Thread.currentThread().getName()+ "] \t\t " + fin_hilo +
                                    " ms\t" + ts/1000000 + " ms\t" + tf/1000000 + " ms");
        }
    }

}