package al.uax.servicios;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class ServicioEnlazadoAritmeticOps extends Service{
	private static final String TAG = "OperationService";

	//Suma realizada como servicio NO enlazado
	public static final int MSG_RESTA = 1;
	public static final int MSG_MULTIPLICACION = 2;
	public static final int MSG_DIVISION = 3;
	public static final int MSG_FACTORIAL = 4;

	private static Float val1, val2;

//	private OperationsHandler mHandler;
	private Messenger mMessenger;
	
	/**
	 * Clase que se encarga de devolver el servicio a quien se enlaza con el
	 */
	public class OperationBinder extends Binder{
		ServicioEnlazadoAritmeticOps getService(){
			return ServicioEnlazadoAritmeticOps.this;
		}
	}
	private OperationBinder mOperationBinder = new OperationBinder();
	
	private static class IncomingHandler extends Handler {
	    @Override
	    public void handleMessage(Message msg) {
	        switch (msg.what) {
	            case MSG_RESTA:
					val1 = msg.getData().getFloat("val1");
					val2 = msg.getData().getFloat("val2");
	            	break;
	                
//	            case MSG_SET_VALUE:
//	                mValue = msg.arg1;
//	                for (int i=mClients.size()-1; i>=0; i--) {
//	                    try {
//	                        mClients.get(i).send(Message.obtain(null,
//	                                MSG_SET_VALUE, mValue, 0));
//	                    } catch (RemoteException e) {
//	                        // The client is dead.  Remove it from the list;
//	                        // we are going through the list from back to front
//	                        // so this is safe to do inside the loop.
//	                        mClients.remove(i);
//	                    }
//	                }
//	                break;
	            default:
	                super.handleMessage(msg);
	        }
	    }
	}
	private IncomingHandler mHandler = new IncomingHandler();
	
	/**
	 * Thread que hará el trabajo en segundo plano
	 */
	private class OperationThread extends Thread{

		@Override
		public void run(){
			try {
				Log.d(TAG, "VAL1: " + val1);
				Log.d(TAG, "VAL2: " + val2);
				
				mMessenger.send(Message.obtain(null, MSG_RESTA, val1 - val2));
				
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			stopSelf();
		}
	}
	private OperationThread mOperationThread;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		Log.d(TAG, "Service - onStartCommand");
		
		//Lanza el thread
		mOperationThread = new OperationThread();
		mOperationThread.start();
		
		//El servicio no debe seguir funcionando una vez parado, por eso devolvemos START_NOT_STICKY
		return START_NOT_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return mOperationBinder;
	}
	
	/**
	 * Función pública para pasar el objeto Messenger
	 * @param messenger
	 */
	public void setMessenger(Messenger messenger) {
		mMessenger = messenger;
	}

	public void onDestroy(){
		super.onDestroy();
		mOperationThread = null;
	}
	
}
