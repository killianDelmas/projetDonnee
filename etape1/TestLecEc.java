import java.util.*;


public class TestLecEc {

    public SharedObject sentence;
    public String myName;

	public static void main(String argv[]) {
		
		String myName = argv[0];
	
		// initialize the system
		Client.init();
		
		// look up the TestSansSynchro object in the name server
		// if not found, create it, and register it in the name server
		SharedObject s = Client.lookup("Fichier");
		if (s == null) {
			s = Client.create(new LecEc());
			Client.register("Fichier", s);
		}
		// instanciate the test
		new TestLecEc(s,myName);
	}

	public TestLecEc(SharedObject s,String name) {
        myName = name;
		sentence = s;
        Boolean b = true;
        Scanner scan = new Scanner(System.in);
        while (b) {
            System.out.println("Que voulez vous faire " + myName + " ? (e : écriture, l : lecture, q : quitter)");
            String rep = scan.nextLine();
            if (rep.equals("e")){
                sentence.lock_write();
                ((LecEc)(sentence.obj)).ecriture();
                sentence.unlock();
            } else if (rep.equals("l")){
                sentence.lock_read();
                ((LecEc)(sentence.obj)).lecture();
                sentence.unlock();
            } else if (rep.equals("q")){
                b = false;
            } else {
                System.out.println("Mauvaise commande");
                System.out.println("Exemple de commande correcte : java TestLecEc <name>");
            }
            

        }
        System.exit(0);
	}
}




