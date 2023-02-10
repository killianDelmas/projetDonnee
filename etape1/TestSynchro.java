import java.util.Random;

public class TestSynchro {

    static int compteurLocal;

	public static void main(String argv[]) {

		// initialize the system
		Client.init();
		
		// look up the TestSynchro object in the name server
		// if not found, create it, and register it in the name server
		SharedObject sho = Client.lookup("TestSynchro");
		if (sho == null) {
			sho = Client.create(new Compteur());
			Client.register("TestSynchro", sho);
		}
		
        Random rand = new Random();
        compteurLocal = 0;
        
		for(int i = 0; i<10; i++){

            int choix = rand.nextInt(2);

            if (choix != 0) {
                sho.lock_read();
                ((Compteur) sho.obj).read();
                sho.unlock();
            }
            else {
                sho.lock_write();
                ((Compteur) sho.obj).compter(1);
                sho.unlock();
                compteurLocal ++;
            }
        }
        System.out.println(compteurLocal);
	}
}
