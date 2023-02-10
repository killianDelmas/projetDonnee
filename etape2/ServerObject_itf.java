import java.rmi.RemoteException;

public interface ServerObject_itf {
    
    public Object lock_read(Client_itf c) throws RemoteException;

    public Object lock_write(Client_itf c)throws RemoteException;

}
