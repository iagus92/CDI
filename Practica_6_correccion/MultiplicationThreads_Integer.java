// Ejercicio 4.3
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Clase que realiza una operacion de multiplicacion mediante la acumulacion de sumas de varios hilos usando AtomicInteger.
 * Recibe como parametros dos operandos y el numero de hilos.
 * @author Iago Fernandez Gonzalez
 * @version 1.0
 */
public class MultiplicationThreads_Integer {
    static AtomicInteger r;
    static int p,q;

    public static void main(String[] args) {
        long inicio = System.nanoTime();
        int n=0;
        try {
            if (args.length != 3) {
                System.out.println("provide 3 arguments...");
            }
            try {
                p = Integer.parseInt(args[0]);
                q = Integer.parseInt(args[1]);
                n = Integer.parseInt(args[2]);
            }catch(NumberFormatException e){
                System.out.println("The arguments must be numbers...");
            }
            r = new AtomicInteger();

            if(p !=0 && q != 0) {
                // Si el número de hilos es mayor que el segundo operando, toma su valor para evitar crear hilos innecesarios.
                if (n > q) {
                    n = q;
                }

                // Se determina en número de sumas que determina cada hilo en función de la paridad del módulo entre el
                // número de hilos y el segundo operando.
                int num, mod = q % n;
                if (mod != 0) num = q / n + 1;
                else num = q / n;

                // Crea los hilos pasando como parámetro el número de veces que se suma el segundo operando al resultado.
                Thread[] threads = new Thread[n];
                for (int i = 0; i < mod; ++i) {
                    threads[i] = new Thread(new Multiplication(i, num));
                }
                for (int i = mod; i < threads.length; ++i) {
                    threads[i] = new Thread(new Multiplication(i, q/n));
                }

                for (int i = 0; i < threads.length; ++i) {
                    threads[i].start();
                }
                for (int i = 0; i < threads.length; ++i) {
                    try {
                        threads[i].join();
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
            System.out.println(args[0]+"*"+args[1]+" = "+r);
        }finally {
            long fin = System.nanoTime()-inicio;
            System.out.println("Tiempo de ejecucion: " + fin/1000000 + " ms");
            System.out.println("exiting...");
        }
    }

    static class Multiplication implements Runnable {
        int id;
        int num;

        Multiplication(int id, int num) {
            this.id=id;
            this.num = num;
        }

        public void run() {
            try {
                //System.out.println("starting worker... "+id);
                // Añade al resultado el segundo operando tantas veces como indique num.
                while(num > 0) {
                    r.getAndAdd(p);
                    num--;
                }
            } finally {
                //System.out.println("exiting... "+id);
            }
        }
    }
}
