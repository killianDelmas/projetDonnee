import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ServerObject implements ServerObject_itf {

    public List<Client_itf> lecteurs = new ArrayList<Client_itf>();

    public Client_itf redacteur;

    public enum Etat {NL, RL, WL};
	
	public Etat etat;

    public Object obj;

    public int id;

    public ServerObject(Object o, int id) {
        this.redacteur = null;
        this.etat = Etat.NL;
        this.obj = o;
        this.id = id;
    }

  
    public Object lock_read(Client_itf c) throws RemoteException {
        
        switch (etat) {
            case NL:
                synchronized (this) {
                    lecteurs.add(c);
                }
                etat = Etat.RL;
                break;
            case RL:
                synchronized (this) {
                    lecteurs.add(c);
                }
                break;
            case WL:
                obj = redacteur.reduce_lock(id);
                synchronized (this) {
                    lecteurs.add(c);
                    lecteurs.add(redacteur);
                }
                etat = Etat.RL;
                break;
        }
        return obj;
    }


    public Object lock_write(Client_itf c) throws RemoteException {
        
        switch(etat) {
            case NL:
                break;
            case RL:
                synchronized (this) {
                    for (Client_itf cl : lecteurs) {
                        cl.invalidate_reader(id);
                    }
                    lecteurs.clear();
                }
                break;
            case WL:
                obj = redacteur.invalidate_writer(id);
                break;
        }

        redacteur = c;
        etat = Etat.WL;
        
        return obj;
    }
    
}
