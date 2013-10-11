package al.uax.servicios;

import al.uax.myactivitylist.R;
import al.uax.servicios.ServicioEnlazadoAritmeticOps.OperationBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class OperacionesAritmeticas extends Activity{
	private static final String TAG = "OperationService";
	
	private EditText valor1;
	private EditText valor2;
	private Button btnSuma;
	private Button btnResta;
	private Button btnMult;
	private Button btnDiv;
	private EditText valorFactorial;
	private Button btnFactorial;
	public static EditText solOperacion;
	public static EditText solFactorial;

	private Float val1, val2;
	private ServicioEnlazadoAritmeticOps mService;
	
	
	private static class OperationHandler extends Handler{
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
				case ServicioEnlazadoAritmeticOps.MSG_RESTA:
					Float solucion = msg.getData().getFloat("solucion");
					solOperacion.setText(solucion.toString());
					break;
					
				default:
					super.handleMessage(msg);
			}
		}
	}
	private OperationHandler mHandler;
	private Messenger mOperationMessenger;
	
	/**
	 * Conexión con el servicio.
	 */
	private ServiceConnection mServiceConnection = new ServiceConnection() {
		
		public void onServiceDisconnected(ComponentName name) {
			Log.d(TAG, "Service disconnected");
			Toast.makeText(OperacionesAritmeticas.this, "Servicio desconectado", Toast.LENGTH_SHORT).show();
			mService = null;
		}
		
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.d(TAG, "Service connected");
			Toast.makeText(OperacionesAritmeticas.this, "Servicio conectado", Toast.LENGTH_SHORT).show();
			OperationBinder binder = (OperationBinder) service;
			mService = binder.getService();
//
			mHandler = new OperationHandler();
//			mOperationMessenger = new Messenger(mHandler);
//			mService.setMessenger(mOperationMessenger);
			
			
			// This is called when the connection with the service has been
	        // established, giving us the service object we can use to
	        // interact with the service.  We are communicating with our
	        // service through an IDL interface, so get a client-side
	        // representation of that from the raw service object.
	        mOperationMessenger = new Messenger(mHandler);

	        // We want to monitor the service for as long as we are
	        // connected to it.
	        try {
	            Message msg = Message.obtain(null, ServicioEnlazadoAritmeticOps.MSG_RESTA);
	            msg.replyTo = mOperationMessenger;
	            mOperationMessenger.send(msg);

	            // Give it some value as an example.
//	            msg = Message.obtain(null, ServicioEnlazadoAritmeticOps.MSG_RESTA, this.hashCode(), 0);
//	            mOperationMessenger.send(msg);
	        } catch (RemoteException e) {
	            // In this case the service has crashed before we could even
	            // do anything with it; we can count on soon being
	            // disconnected (and then reconnected if it can be restarted)
	            // so there is no need to do anything here.
	        }

	        // As part of the sample, tell the user what happened.
//	        Toast.makeText(OperacionesAritmeticas.this, "Servicio conectado", Toast.LENGTH_SHORT).show();
		}
	};
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_service);
		
		valor1 = (EditText) findViewById(R.id.value1);
		valor2 = (EditText) findViewById(R.id.value2);
		btnSuma = (Button) findViewById(R.id.btnSuma);
		btnResta = (Button) findViewById(R.id.btnResta);
		btnMult = (Button) findViewById(R.id.btnMultiplicacion);
		btnDiv = (Button) findViewById(R.id.btnDivision);
		valorFactorial = (EditText) findViewById(R.id.valorFactorial);
		btnFactorial = (Button) findViewById(R.id.btnFactorial);
		solOperacion = (EditText) findViewById(R.id.solucion);
		solFactorial = (EditText) findViewById(R.id.solucionFactorial);
		

		/**
		 * Llamada a un servicio normal
		 */
		btnSuma.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				val1 = !valor1.getText().toString().equals("") ? Float.parseFloat(valor1.getText().toString()) : 0;
				val2 = !valor2.getText().toString().equals("") ? Float.parseFloat(valor2.getText().toString()) : 0;
				
				Intent intent = new Intent(OperacionesAritmeticas.this, ServicioAritmeticOps.class);
				intent.putExtra("val1", val1);
				intent.putExtra("val2", val2);
				startService(intent);
			}
		});
		
		btnResta.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				val1 = !valor1.getText().toString().equals("") ? Float.parseFloat(valor1.getText().toString()) : 0;
				val2 = !valor2.getText().toString().equals("") ? Float.parseFloat(valor2.getText().toString()) : 0;

				try {
					Message msg = Message.obtain(null, ServicioEnlazadoAritmeticOps.MSG_RESTA);
					Bundle bundle = new Bundle();
					bundle.putFloat("val1", val1);
					bundle.putFloat("val2", val2);
					msg.setData(bundle);
				
					mOperationMessenger.send(msg);
					
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				
				mService.setMessenger(mOperationMessenger);
				Intent intent = new Intent(OperacionesAritmeticas.this, ServicioEnlazadoAritmeticOps.class);
				startService(intent);
			}
		});
		
		btnMult.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		
		btnDiv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		
		btnFactorial.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Float sol = calcularFactorial(Float.parseFloat(valorFactorial.getText().toString()));
				solFactorial.setText(sol.toString());
			}

		});
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

	@Override
	protected void onStart() {
		super.onStart();

		Log.d(TAG, "Binding to service...");
		Toast.makeText(OperacionesAritmeticas.this, "Bindinding service", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(OperacionesAritmeticas.this, ServicioEnlazadoAritmeticOps.class);
		bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
	}

	@Override
	protected void onStop() {
		super.onStop();

		Log.d(TAG, "Unbinding service...");
		Toast.makeText(OperacionesAritmeticas.this, "Unbindinding service", Toast.LENGTH_SHORT).show();
		unbindService(mServiceConnection);
	}
}