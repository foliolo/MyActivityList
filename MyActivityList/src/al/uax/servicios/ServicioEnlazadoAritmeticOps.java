package al.uax.servicios;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
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

	private Messenger mMessenger;
	
	private IBinder binder = new OperationBinder();
	
	/**
	 * Clase que se encarga de devolver el servicio a quien se enlaza con el
	 */
	public class OperationBinder extends Binder{
		ServicioEnlazadoAritmeticOps getService(){
			return ServicioEnlazadoAritmeticOps.this;
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		Log.d(TAG, "Service - onStartCommand");
		
		//El servicio no debe seguir funcionando una vez parado, por eso devolvemos START_NOT_STICKY
		return START_NOT_STICKY;
	}
	
	/**
	 * Calcular operaciones
	 */
	public void calcOperation (final Float x, final Float y, final int oper){
		Log.d("TAG", "Realizando operacion...");
		switch(oper){
		case MSG_RESTA:
			new Thread( new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(5000);
						sendMessage(x-y, OperacionesAritmeticas.SOL_OPERACION);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
			break;
			
		case MSG_MULTIPLICACION:
			new Thread( new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(5000);
						sendMessage(x*y, OperacionesAritmeticas.SOL_OPERACION);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
			break;
			
		case MSG_DIVISION:
			new Thread( new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(5000);
						sendMessage(x/y, OperacionesAritmeticas.SOL_OPERACION);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
			break;
		}
	}
	
	public void calcFactorial(final Float x){
		Log.d("TAG", "Realizando factorial...");
		new Thread( new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(5000);
					sendMessage(calcularFactorial(x), OperacionesAritmeticas.SOL_FACTORIAL);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private Float calcularFactorial(Float valor) {
		if(valor < 2)
			return (float) 1;
		else {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return valor * calcularFactorial(--valor);
		}
	}
	
	/**
	 * Función pública para pasar el objeto Messenger
	 * @param messenger
	 */
	public void setMessenger(Messenger messenger) {
		mMessenger = messenger;
	}
	
	private void sendMessage(Float res, int oper) {
		try {
			Message msg = Message.obtain(null, oper); //10 => oper; 11 => fact
			msg.obj = res;
			mMessenger.send(msg);
		} catch (RemoteException e) {
			Log.i(TAG, "RemoteException");
		}
	}
	
	public void onDestroy(){
		super.onDestroy();
	}
}
