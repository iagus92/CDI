// Ejercicio 1.2.1

/**
 * Clase que ejecuta el numero de hilos pasado por parametro.
 * <p>
 * Extiende de Thread.
 * @see java.lang.Thread
 * @author Iago Fernandez
 * @version 1.0
 */
public class Hilos extends Thread {

    private int secs;

    /**
     * Crea un hilo.
     * @param secs segundos que dura el sleep del hilo.
     */
	public Hilos(int secs) {
		
		this.secs = secs;	
	}

    /**
     * Muestra un mensaje de saludo y, tras un segundo de sleep, muestra un mensaje de despedida.
     */
    public void run() {
         
        System.out.println("Hello, I'm a Thread number " + getName());
		  try{
			    sleep(secs);
			}
			catch (InterruptedException e){
                System.out.println("ERROR: hilo en sleep interrumpido.");
			}
		  System.out.println("Bye, this was Thread number " + getName());
    }

    public static void main(String[] args) {
		
		System.out.println("Starting...");
        int threads, secs;

		// Si el número de argumentos pasados por parámetro no es 2 se lanza una excepción.
		if (args.length != 2) {
			throw new IllegalArgumentException("Se requieren dos argumentos numéricos");
    	}

    	// Si los argumentos no son numéricos lanza una excepción.
		try {
   		    threads = Integer.parseInt(args[0]);
			secs = Integer.parseInt(args[1]); 
    	} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Arg 1: número de hilos\nArg2: duración del sleep");
    	}

    	// Crea los hilos
     	for (int i = 0; i < threads; i++) {
		    new Hilos(secs).start();
     	}
       
     	System.out.println("Exiting...");
    }    
}