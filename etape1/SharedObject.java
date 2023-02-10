import java.io.*;

public class SharedObject implements Serializable, SharedObject_itf {
	
	public enum Lock {NL, RLC, WLC, RLT, WLT, RLT_WLC};
	
	public Lock lock;
	
	// obj pointe vers une copie de l'Objet associé au SharedObject
	public Object obj;
	
	// id unique par rapport au serveur
	public int id;


	public SharedObject(Object o, int id) {
		this.lock = Lock.NL;
		this.obj = o;
		this.id = id;
	}
	
	// invoked by the user program on the client node
	public void lock_read() {
		System.out.println("Avant " + lock);
		switch(lock) {
			case NL:
				obj = Client.lock_read(id); 
				lock = Lock.RLT;
				break;
			case RLC:
				lock = Lock.RLT;
				break;
			case WLC:
				lock = Lock.RLT_WLC;
				break;
			default:
				break;
		}
		System.out.println("Apres " + lock);
	}

	// invoked by the user program on the client node
	public void lock_write() {
		System.out.println("Avant " + lock);
		switch(lock) {
			case NL:
			case RLC:
				obj = Client.lock_write(id);
				lock = Lock.WLT;
				break;
			case WLC:
				lock = Lock.WLT;
				break;
			default:
				break;
		}
		System.out.println("Apres " + lock);
	}

	// invoked by the user program on the client node
	public synchronized void unlock() {
		System.out.println("Avant " + lock);
		switch(lock) {
			case RLT:
				lock = Lock.RLC;
				break;
			case WLT:
			case RLT_WLC:
				lock = Lock.WLC;
				break;
			default:
				break;
		}
		System.out.println("Apres " + lock);
		notify();
	}

	// callback invoked remotely by the server
	public synchronized Object reduce_lock() {
		System.out.println("Avant " + lock);
		switch(lock) {
			case WLT:
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			case WLC:
				lock = Lock.RLC;
				break;
			case RLT_WLC:
				lock = Lock.RLT;
				break;
			default:
				break;
		}
		System.out.println("Apres " + lock);
		return obj;
	}

	// callback invoked remotely by the server
	public synchronized void invalidate_reader() {
		System.out.println("Avant " + lock);
		switch(lock) {
			case RLT:
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			case RLC:
				lock = Lock.NL;
				break;
			default:
				break;
		}
		System.out.println("Apres " + lock);
	}

	public synchronized Object invalidate_writer() {
		System.out.println("Avant " + lock);
		switch(lock) {
			case WLT:
			case RLT_WLC:
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			case WLC:
				lock = Lock.NL;
			default:
				break;
		}
		System.out.println("Apres " + lock);
		return obj;
	}
}
