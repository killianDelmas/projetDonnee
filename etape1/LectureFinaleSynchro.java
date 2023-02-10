public class LectureFinaleSynchro {

    public static void main(String argv[]) {
		
		// initialize the system
		Client.init();
		
		// look up the TestSyncho object in the name server
		// if not found, create it, and register it in the name server
		SharedObject sho = Client.lookup("TestSynchro");
		if (sho == null) {
			sho = Client.create(new Compteur());
			Client.register("TestSynchro", sho);
		}
        sho.lock_read();
        int compteurFinal = ((Compteur) sho.obj).read();
        sho.unlock();

        System.out.println(compteurFinal);
        System.exit(0);
    }
    
}
