/**
 * Created by IagoFG on 22/03/2017.
 */
public class Library {

    public static int n=0;

    public static void main(String[] args){

        System.out.println("Starting...");
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

            Book libro = new Book(p, t);
            Thread[] threads = new Thread[n];

				// Creamos y lanzamos los thhreads pasándole el objeto Book por parámetro.
            for (int i = 0; i < threads.length; ++i) {
                threads[i] = new Thread(new Reader(i, t, libro));
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
            System.out.println("Exiting...");
        }
    }

    static class Reader implements Runnable{

        private int hilo;
        private int wait;
        private int cont=0;
        private Book book;

        public Reader(int hilo, int wait, Book book) {
            this.hilo = hilo;
            this.wait = wait;
            this.book = book;
        }

			// Sincroniza el objeto Book y cada hilo lee una página mientras queden páginas por leer.
        public void run(){
            try {
                while (book.stillPages()) {
                    synchronized(book) {
                         cont++;
                        book.read(hilo);
                        if (wait > 0) Thread.sleep(wait / (2*n));
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally{
                System.out.println("--- The reader number "+(hilo+1)+" had read "+cont+" pages.");
            }
        }
    }

    static class Book{

        private volatile int pages;
        private int wait;

        public Book(int pages, int wait) {
            this.pages = pages;
            this.wait = wait;
        }

			// El hilo lee una página si todavía quedan páginas. 
        public void read(int hilo){
				if(stillPages()) {            
					System.out.println("The reader number " + (hilo + 1) + " is reading page " + pages);
		         pages--;
		         try {
		             if(wait > 0) Thread.sleep(wait);
		         } catch (InterruptedException e) {
		             e.printStackTrace();
		         }
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
