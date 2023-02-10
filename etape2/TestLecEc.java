import java.util.*;


public class TestLecEc {

    public LecEc_itf lecEc;
    public String myName;

	public static void main(String argv[]) {
		
		String myName = argv[0];
	
		// initialize the system
		Client.init();
		
		// look up the IRC object in the name server
		// if not found, create it, and register it in the name server
		LecEc_itf l = (LecEc_itf) Client.lookup("Fichier");
		if (l == null) {
			l = (LecEc_itf) Client.create(new LecEc());
			Client.register("Fichier", l);
		}
		// create the graphical part
		new TestLecEc(l,myName);
	}

	public TestLecEc(LecEc_itf l,String name) {
        myName = name;
		lecEc = l;
        Boolean b = true;
        Scanner scan = new Scanner(System.in);
        while (b) {
            System.out.println("Que voulez vous faire" + myName + " ? (e : écriture, l : lecture, q : quitter)");
            String rep = scan.nextLine();
            if (rep.equals("e")){
                lecEc.lock_write();
                lecEc.ecriture();
                lecEc.unlock();
            } else if (rep.equals("l")){
                lecEc.lock_read();
                lecEc.lecture();
                lecEc.unlock();
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




