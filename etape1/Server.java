import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class Server extends UnicastRemoteObject implements Server_itf {

	public HashMap<Integer, ServerObject_itf> sos = new HashMap<Integer, ServerObject_itf>();

	public HashMap<String, Integer> nameToId = new HashMap<String, Integer>();

	protected Server() throws RemoteException {
		super();
	}

	public static void main(String args[]) {

		int port;

		String URL;

		try {
			port = 1397;
			Registry registry = LocateRegistry.createRegistry(port);
			Server_itf serv = new Server();
			URL = "//localhost:" + port + "/my_server";
			Naming.rebind(URL, serv);
		} catch (RemoteException | MalformedURLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int lookup(String name) throws RemoteException {
		if (nameToId.get(name) == null){
			return -1;
		} else {
			return nameToId.get(name);
		}
	}

	@Override
	public void register(String name, int id) throws RemoteException {
		this.nameToId.put(name, id);
	}

	@Override
	public int create(Object o) throws RemoteException {
		int id = this.sos.size() + 1;
		ServerObject so = new ServerObject(o, id);
		this.sos.put(id, so);
		return id;
	}

	@Override
	public Object lock_read(int id, Client_itf client) throws RemoteException {
		ServerObject_itf so = sos.get(id);
		return so.lock_read(client);
	}

	@Override
	public Object lock_write(int id, Client_itf client) throws RemoteException {
		ServerObject_itf so = sos.get(id);
		return so.lock_write(client);
	}

}
