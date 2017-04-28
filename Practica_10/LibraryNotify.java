/**
 * Created by IagoFG on 27/03/2017.
 */
public class LibraryNotify {
    public static int n=0;

    public static void main(String[] args){

        System.out.println("Starting...");
        long inicio = System.nanoTime();

        try {
            int p=0,t=0;

            if (args.length != 3) {
                System.out.println("provide 3 arguments...");
                System.exit(1);
            }
            try {
                n = Integer.parseInt(args[0]);
                p = Integer.parseInt(args[1]);
                t = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                System.out.println("The arguments must be number...");
                System.exit(1);
            }

            Book2 libro = new Book2(p, t);
            Thread[] threads = new Thread[n];
            Reader2[] readers = new Reader2[n];

            // Creamos los arrays de lectores y de hilos.
            for (int i = 0; i < n; ++i) {
                readers[i] = new Reader2(i, t, libro, readers);
                threads[i] = new Thread(readers[i]);
            }

            // Hacemos start de los hilos y nos aseguramos de que todos estén en estado waiting.
            for (int i = 0; i < threads.length; ++i) {
                threads[i].start();
                while(readers[i].getStatus());
            }

            // Notificamos al primer lector para que empiece a leer.
            synchronized (readers[0]) {
                readers[0].notify();
                readers[0].setStatus(true);
            }

            // Join de los hilos para asegurar que todos han finalizado.
            for (int i = 0; i < threads.length; ++i) {
                try {
                    threads[i].join();
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            // Calculamos el tiempo de ejecución y finalizamos.
            long fin = System.nanoTime()-inicio;
            System.out.println("Tiempo de ejecucion: " + fin/1000000 + " ms");
            System.out.println("Exiting...");
        }
    }

    static class Reader2 implements Runnable{

        private int hilo, next, wait, cont=0;
        private boolean status;
        private Book2 book;
        private Reader2[] readers;

        // Constructor. Asigna a next la posición del siguiente lector en el array.
        public Reader2(int hilo, int wait, Book2 book, Reader2[] r) {
            this.hilo = hilo;
            this.wait = wait;
            this.book = book;
            status = true;
            readers = r;
            if(hilo<n-1) next = hilo+1;
            else next = 0;
        }

        // Primero pone a esperar a todos los hilos. Utiliza flags de control (despierto a true y dormido a false).
        // Cada hilo lee una página al llegar su turno, después notifica al siguiente y se pone a dormir.
        // Continúa en bucle mientras queden páginas por leer. Al finalizar despierta a todos los hilos.
        public void run(){
            synchronized(readers[hilo]) {
                try {
                    readers[hilo].setStatus(false);
                    readers[hilo].wait();
                    while (book.stillPages()) {
                        book.read(hilo);
                        cont++;
                        if(n>1) { // Wait/notify si hay más de 1 hilo.
                            synchronized (readers[next]) {
                                while(readers[next].getStatus()); // Innecesario.
                                readers[next].notify();
                                readers[next].setStatus(true);
                            }
                            readers[hilo].setStatus(false);
                            readers[hilo].wait();
                        }
                        //if (wait > 0) Thread.sleep(wait / (2*n));
                    }
                    synchronized (readers[next]) {
                        readers[next].notify();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //System.out.println("--- The reader number " + (hilo) + " had read " + cont + " pages.");
                }
            }
        }

        public synchronized boolean getStatus(){
            return status;
        }

        public void setStatus(boolean status){
            this.status = status;
        }
    }

    static class Book2{

        private volatile int pages;
        private int wait;
        private int num = 0;

        // Se crea el libro con el número de páginas indicado.
        public Book2(int pages, int wait) {
            this.pages = pages;
            this.wait = wait;
        }

        // Lee una página (resta 1 al total).
        public synchronized void read(int hilo){
            if(stillPages()) {
                try {
                    //System.out.println("The reader number " + (hilo) + " is reading page " + pages);
                    pages--;
                    //if (wait > 0) Thread.sleep(wait);
                } catch (Exception e) {
                    e.printStackTrace();
                }finally{
                    //System.out.println("The reader number " + (hilo + 1) + " finished reading page " + (pages+1));
                }
            }
        }

        // Devuelve true si quedan páginas por leer.
        public synchronized boolean  stillPages(){
            if(pages > 0){
                return true;
            }else{
                return false;
            }
        }
    }
}
