/**
 * Created by IagoFG on 27/03/2017.
 */
public class Library_2 {
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
                System.out.println("The arguments must be numbers...");
                System.exit(1);
            }

            Book2 libro = new Book2(p, t);
            Thread[] threads = new Thread[n];

				// Creamos y lanzamos los thhreads pasándole el objeto Book por parámetro.
            for (int i = 0; i < threads.length; ++i) {
                threads[i] = new Thread(new Reader2(i, t, libro));
                threads[i].start();
            }
				
				// Join de los hilos para asegurar que el principal sea el último. 
            for (int i = 0; i < threads.length; ++i) {
                try {
                    threads[i].join();
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                    System.exit(1);
                }
            }
        }finally{
            long fin = System.nanoTime()-inicio;
            System.out.println("Tiempo de ejecucion: " + fin/1000000 + " ms");
            System.out.println("Exiting...");
        }
    }

    static class Reader2 implements Runnable{

        private int hilo;
        private int wait;
        private int cont=0;
        private Book2 b;

        public Reader2(int hilo, int wait, Book2 b) {
            this.hilo = hilo;
            this.wait = wait;
            this.b = b;
        }

		  // Sincroniza el objeto Book y cada hilo llama a wait si no es su turno.
		  // Lee y llama a notify si es su turno.
        public void run(){
            try {
                while (b.stillPages()) {
                    synchronized(b){
                        if (b.myTurn(hilo)) {
                            b.read(hilo);
                            cont++;
                            b.notifyAll();
                        } else {
                            b.wait();
                        }
                        //if (wait > 0) Thread.sleep(wait / (2 * n));
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally{
                System.out.println("--- The reader number "+(hilo+1)+" had read "+cont+" pages.");
            }
        }
    }

    static class Book2{

        private volatile int pages;
        private int wait;
        private int num = 0;

        public Book2(int pages, int wait) {
            this.pages = pages;
            this.wait = wait;
        }

			// El hilo lee una página si todavía quedan páginas. 
        public void read(int hilo){
            if(stillPages()) {
                try {
                    System.out.println("The reader number " + (hilo + 1) + " is reading page " + pages);
                    pages--;
                    //if (wait > 0) Thread.sleep(wait);
                } catch (Exception e) {
                    e.printStackTrace();
                }finally{
                    System.out.println("The reader number " + (hilo + 1) + " finished reading page " + (pages+1));
                }
            }
        }

			// Controla si es su turno mediante una variable booleana.
        public boolean myTurn(int hilo){
            if(hilo==num){
                num++;
                if(num==n) num = 0;
                return true;
            }else {
                return false;
            }
        }

			// Controla si quedan páginas por leer mediante una variable booleana.
        public synchronized boolean stillPages(){
            if(pages > 0){
                return true;
            }else{
                return false;
            }
        }
    }
}
