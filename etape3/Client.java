import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.lang.reflect.Constructor;
import java.net.*;

public class Client extends UnicastRemoteObject implements Client_itf {

	static HashMap<Integer, SharedObject_itf> shos = new HashMap<Integer, SharedObject_itf>();

	static Server_itf serv;

	static Client client;

	public Client() throws RemoteException {
		super();
	}

///////////////////////////////////////////////////
//         Interface to be used by applications
///////////////////////////////////////////////////

	// initialization of the client layer
	public static void init() {
		try {
			client = new Client();
			serv = (Server_itf) Naming.lookup("//localhost:1397/my_server");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}

	}
	
	// lookup in the name server
	public static SharedObject lookup(String name) {
		int id;
		try {
			id = serv.lookup(name);
			if ( id != -1 ) {
				Object o = lock_read(id); 
				SharedObject so = create_stub(o,id);
				shos.put(id, so);
				so.unlock();
				return so;
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
		
	}		
	
	// binding in the name server
	public static void register(String name, SharedObject_itf so) {
		try {
			serv.register(name, ((SharedObject) so).id);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	// creation of a shared object
	public static SharedObject create(Object o) {
		int id;
		try {
			id = serv.create(o);
			SharedObject so = create_stub(o, id);
			shos.put(id, so);
			return so;
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
/////////////////////////////////////////////////////////////
//    Interface to be used by the consistency protocol
////////////////////////////////////////////////////////////

	// request a read lock from the server
	public static Object lock_read(int id) {
		try {
			return serv.lock_read(id, client);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}

	// request a write lock from the server
	public static Object lock_write (int id) {
		try {
			return serv.lock_write(id, client);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}

	// receive a lock reduction request from the server
	public Object reduce_lock(int id) throws java.rmi.RemoteException {
		SharedObject sho = (SharedObject) shos.get(id);
		return sho.reduce_lock();
	}


	// receive a reader invalidation request from the server
	public void invalidate_reader(int id) throws java.rmi.RemoteException {
		SharedObject sho = (SharedObject) shos.get(id);
		if (sho != null) {
			sho.invalidate_reader();
		}
	}


	// receive a writer invalidation request from the server
	public Object invalidate_writer(int id) throws java.rmi.RemoteException {
		SharedObject sho = (SharedObject) shos.get(id);
		return sho.invalidate_writer();
	}

	// create stubs
	public static SharedObject create_stub(Object o, int id) {
		try {
			Class<?> classe = o.getClass();
			String nomStub = classe.getName() + "_stub";
			Class<?> classeStub = Class.forName(nomStub);
			Constructor<?> constructeurStub = classeStub.getConstructors()[0];
			return (SharedObject) constructeurStub.newInstance(o,id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

}
