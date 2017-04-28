// Ejercicio 4.1
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Clase que realiza una operacion de multiplicacion mediante un algoritmo concurrente usando AtomicInteger.
 * No funciona correctamente.
 * Recibe como parametros dos operandos y el numero de hilos.
 * @author Iago Fernandez Gonzalez
 * @version 1.0
 */
public class MultiplicationThreads_Fail {
    static AtomicInteger q,r;
    static int p;

    public static void main(String[] args) {
        long inicio = System.nanoTime();
        int n=0;
        try {
            if (args.length != 3) {
                System.out.println("provide 3 arguments...");
                System.exit(1);
            }
            try {
                p = Integer.parseInt(args[0]);
                q = new AtomicInteger(Integer.parseInt(args[1]));
                n = Integer.parseInt(args[2]);
            }catch(NumberFormatException e){
                System.out.println("The arguments must be numbers...");
                System.exit(1);
            }
            r = new AtomicInteger();

            Thread[] threads = new Thread[n];

            for(int i=0; i<threads.length; ++i) {
                threads[i] = new Thread(new Multiplication(i));
            }

            for(int i=0; i<threads.length; ++i) {
                threads[i].start();
            }

            for(int i=0; i<threads.length; ++i) {
                try {
                    threads[i].join();
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                    System.exit(1);
                }
            }
            System.out.println(args[0]+"*"+args[1]+"= "+r);
        }finally {
            long fin = System.nanoTime()-inicio;
            System.out.println("Tiempo de ejecucion: " + fin/1000000 + " ms");
            System.out.println("exiting...");
        }
    }

    static class Multiplication implements Runnable {
        int id;

        Multiplication(int id) {
            this.id=id;
        }

        public void run() {
            try {
                System.out.println("starting worker... "+id);
                while(q.get() >= 0) {
                    r.getAndAdd(p);
                    q.getAndDecrement();
                }
            }
            catch(Exception E) {
                System.out.println("??? "+id);
            }
            finally {
                System.out.println("exiting... "+id);
            }
        }
    }

}
