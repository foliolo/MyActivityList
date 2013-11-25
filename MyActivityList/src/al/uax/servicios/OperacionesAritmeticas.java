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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class OperacionesAritmeticas extends Activity{
	private static final String TAG = "OperationService";
	public static final int SOL_OPERACION = 10;
	public static final int SOL_FACTORIAL = 11;
	
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
	private static Float total;
	
	private ServicioEnlazadoAritmeticOps mService;
	private OperationHandler mHandler;
//	private Messenger mOperationMessenger;
	
	/**
	 * Conexión con el servicio.
	 */
	private ServiceConnection mServiceConnection = new ServiceConnection() {
		
		public void onServiceConnected(ComponentName name, IBinder binder) {
			Log.d(TAG, "Service connected");
			Toast.makeText(OperacionesAritmeticas.this, "Servicio conectado", Toast.LENGTH_SHORT).show();
			
			mService = ((OperationBinder) binder).getService();
			mHandler = new OperationHandler();
			mService.setMessenger(new Messenger(mHandler));
		}

		public void onServiceDisconnected(ComponentName name) {
			Log.d(TAG, "Service disconnected");
			Toast.makeText(OperacionesAritmeticas.this, "Servicio desconectado", Toast.LENGTH_SHORT).show();
			mService = null;
		}
	};
	
	static class OperationHandler extends Handler{
		@Override
		public void handleMessage(Message msg){
			Log.i(TAG, "IncomingHandler. SOLUCION");
			
			switch(msg.what){
				case SOL_OPERACION:
					total = (Float) msg.obj;
					solOperacion.setText(total.toString());
					break;
				
				case SOL_FACTORIAL:
					total = (Float) msg.obj;
					solFactorial.setText(String.format("%.0f", total));
					break;

				default:
					super.handleMessage(msg);
			}
		}
	}
	
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
				mService.calcOperation(val1, val2, 1);
			}
		});
		
		btnMult.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				val1 = !valor1.getText().toString().equals("") ? Float.parseFloat(valor1.getText().toString()) : 0;
				val2 = !valor2.getText().toString().equals("") ? Float.parseFloat(valor2.getText().toString()) : 0;
				mService.calcOperation(val1, val2, 2);
			}
		});
		
		btnDiv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				val1 = !valor1.getText().toString().equals("") ? Float.parseFloat(valor1.getText().toString()) : 0;
				val2 = !valor2.getText().toString().equals("") ? Float.parseFloat(valor2.getText().toString()) : 0;
				mService.calcOperation(val1, val2, 3);
			}
		});
		
		btnFactorial.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				val1 = !valorFactorial.getText().toString().equals("") ? Float.parseFloat(valorFactorial.getText().toString()) : 1;
				mService.calcFactorial(val1);
			}

		});
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
		Log.d(TAG, "(Stop) Unbinding service...");
		Toast.makeText(OperacionesAritmeticas.this, "Unbindinding service", Toast.LENGTH_SHORT).show();
		unbindService(mServiceConnection);
	}
}