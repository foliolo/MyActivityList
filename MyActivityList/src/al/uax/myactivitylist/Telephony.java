package al.uax.myactivitylist;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Este ejemplo muestra el uso de la clase TelephonyManager
 * 
 * La lógica está situada en el método onCreate, por tanto el oyente de eventos quedara
 * activado al lanzar la Activity.
 * 
 * El objeto PhoneStateListener se crea como una clase anónima, sobrescribiendo el método onCallStateChanged().
 * Es importante mantener la referencia al objeto para poder desregistrarlo mas adelante, por eso lo guardamos
 * como atributo de la clase.
 * 
 * Finalmente el TelephonyManager elimina el TelephonyManagerExample en el metodo onDestroy.
 *
 */
public class Telephony extends Activity {
	
	public static final String TAG = "TelephonyManagerExample";
	
	TelephonyManager mManager;
	PhoneStateListener mListener = new PhoneStateListener(){
    	@Override
    	public void onCallStateChanged(int state, String incomingNumber) {
    		super.onCallStateChanged(state, incomingNumber);

    		//Sacamos por el log los cambios de estado recibidos
    		Log.d(TAG, "Cambio de estado: " + state);
    		
    		//Si el estado indica que el teléfono esta sonando mostramos una notificación Toast por pantalla
    		if (state == TelephonyManager.CALL_STATE_RINGING){
    			Toast.makeText(Telephony.this, 
    					"Llamada entrante: " + incomingNumber, Toast.LENGTH_LONG).show();
    		}
    	}
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.telephony);
        
        //Obtenemos la instancia del TelephonyManager
        mManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        
        //Registramos el oyente para escuchar los eventos de llamadas
        mManager.listen(mListener, PhoneStateListener.LISTEN_CALL_STATE);
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        
        //Desregistramos el oyente usando el parámetro LISTEN_NONE
        mManager.listen(mListener, PhoneStateListener.LISTEN_NONE);
    }

}
