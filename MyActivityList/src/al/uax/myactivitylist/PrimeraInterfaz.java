package al.uax.myactivitylist;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
//Se debe poner el paquete de la aplicación principal del manifest

public class PrimeraInterfaz extends Activity {

	private EditText mDestinatario;
	private EditText mMensaje;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("TAG", "Create");
		setContentView(R.layout.primera_interfaz);

        mDestinatario = (EditText) findViewById(R.id.destinatario);
        mMensaje = (EditText) findViewById(R.id.mensaje);
		
		Button boton = (Button)findViewById(R.id.boton_enviar);
		boton.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Toast.makeText(getApplicationContext(), "Botón envíar pulsado", Toast.LENGTH_LONG).show();
			}
		});
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		Log.e("TAG", "Start");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.e("TAG", "Resume");
	}
	 
	@Override
	protected void onPause() {
		super.onPause();
		Log.e("TAG", "Pause");
		
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		Log.e("TAG", "Restart");
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		Log.e("TAG", "Stop");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.e("TAG", "Destroy");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_list, menu);
		return true;
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState){
		Log.e("TAG", "Estado salvado");

		String destinatario = mDestinatario.getText().toString();
		String mensaje = mMensaje.getText().toString();
		
		outState.putString("destinatario", destinatario);
		outState.putString("mensaje", mensaje);
		
		super.onSaveInstanceState(outState);
	}
	
	//No se por que no se puede sobreescribir
	//@Override
	protected void onRestoreInstaceState(Bundle savedInstanceState){
		Log.e("TAG", "Estado restaurado");
		
		if(savedInstanceState != null){
			String destinatario = savedInstanceState.getString("destinatario");
			String mensaje = savedInstanceState.getString("mensaje");
			
			mDestinatario.setText(destinatario);
			mMensaje.setText(mensaje);
		}
		
		super.onRestoreInstanceState(savedInstanceState);
	}
	
}
