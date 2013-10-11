package al.uax.servicios;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ServicioAritmeticOps extends Service{
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Float val1 = intent.getFloatExtra("val1", 0);
		Float val2 = intent.getFloatExtra("val2", 0);

		Float sol = val1 + val2;
//		try{
//			Thread.sleep(5000);
//		}catch(Exception e){
//			
//		}
		OperacionesAritmeticas.solOperacion.setText(sol.toString());
		
		return START_NOT_STICKY;
	}
	
/*	
	private OperationsHandler mHandler;
	private Messenger mOperationMessenger;
	
	/**
	 * Clase que se encarga de devolver el servicio a quien se enlaza con el
	 * /
	public class OperationBinder extends Binder{
		OperationBinder getService(){
			return OperationBinder.this;
		}
	}
	private OperationBinder mOperationBider = new OperationBinder();
	
	/**
	 * Thread que hace el trabajo en background
	 * /
	private class OperationThread extends Thread{
		private Handler mHandler;
		
		@Override
		public void run(){
			try{
				//Finalmente envia el mensaje a la interfaz de usuario para actualizar la barra de progreso
				Message msg = Message.obtain(null, MSG_SUMA);
				int a = msg.arg1;
				int b = msg.arg2;
				msg.arg1 = a+b;
				mOperationMessenger.send(msg);
			}
			catch(RemoteException e){
				e.printStackTrace();
			}
			stopSelf();
		}
	}
	
	@Override
	public int onStartCommand(Intent intent, int flag, int startId){
		Toast.makeText(ServicioAritmeticOps.this, "onStartCommand", Toast.LENGTH_SHORT).show();
		return START_NOT_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return mOperationBider;
	}
	

	private static class OperationsHandler extends Handler{
		public void handlemessage(Message msg){
			switch(msg.what){
			case MSG_SUMA:
				break;
				
			case MSG_RESTA:
				break;
				
			case MSG_MULTIPLICACION:
				break;
				
			case MSG_DIVISION:
				break;
				
			case MSG_FACTORIAL:
				break;
			}
		}
	}

	@Override
	public void onDestroy(){
	 super.onDestroy();
	 Toast.makeText(ServicioAritmeticOps.this, "Servicio destruido", Toast.LENGTH_SHORT).show();
	 mHandler = null;
	}
*/

}