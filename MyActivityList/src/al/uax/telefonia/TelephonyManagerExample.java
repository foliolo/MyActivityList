package al.uax.telefonia;

import al.uax.myactivitylist.R;
import android.app.Activity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

/**
 * Este ejemplo utiliza el TelephonyManager para mostrar distinta informacion al usuario de dos formas
 * diferentes:
 * 
 * En primero lugar instancia el Manager y recoge datos a traves de los metodos get.
 * 
 * A continuacion registra un PhoneStateListener que monitoriza los eventos:
 * onCallStateChanged
 * onDataActivity
 * onDataConnectionChanged
 * onServiceStateChanged
 *
 */
public class TelephonyManagerExample extends Activity {

	public static final String TAG = "TelephonyManagerExample";

	TelephonyManager mManager;
	PhoneStateListener mListener = new PhoneStateListener(){

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);

			StringBuffer sb = new StringBuffer();
			sb.append("Estado de la llamada: ");

			switch (state){
			case TelephonyManager.CALL_STATE_OFFHOOK:
				sb.append("descolgado.");
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				sb.append("llamada entrante (");
				sb.append(incomingNumber);
				sb.append(").");
				break;
			case TelephonyManager.CALL_STATE_IDLE:
				sb.append("inactivo.");
				break;
			}

			show(sb.toString());
		}

		@Override
		public void onDataActivity(int direction) {
			super.onDataActivity(direction);

			StringBuffer sb = new StringBuffer();
			sb.append("Actividad de datos: ");

			switch (direction){
			case TelephonyManager.DATA_ACTIVITY_IN:
				sb.append("recibiendo datos.");
				break;
			case TelephonyManager.DATA_ACTIVITY_OUT:
				sb.append("enviando datos.");
				break;
			case TelephonyManager.DATA_ACTIVITY_INOUT:
				sb.append("enviando y recibiendo datos.");
				break;
			case TelephonyManager.DATA_ACTIVITY_DORMANT:
				sb.append("sin conexion.");
				break;
			case TelephonyManager.DATA_ACTIVITY_NONE:
				sb.append("sin trafico.");
				break;
			}

			show(sb.toString());
		}

		@Override
		public void onDataConnectionStateChanged(int state, int networkType) {
			super.onDataConnectionStateChanged(state, networkType);

			StringBuffer sb = new StringBuffer();
			sb.append("Cambio en la conexion de datos (");
			sb.append(getNetworkType(networkType));
			sb.append("): ");

			switch (state){
			case TelephonyManager.DATA_CONNECTED:
				sb.append("conectada.");
				break;
			case TelephonyManager.DATA_CONNECTING:
				sb.append("conectando.");
				break;
			case TelephonyManager.DATA_DISCONNECTED:
				sb.append("desconectada.");
				break;
			case TelephonyManager.DATA_SUSPENDED:
				sb.append("suspendida.");
				break;
			}

			show(sb.toString());
		}

		@Override
		public void onServiceStateChanged(ServiceState serviceState) {
			super.onServiceStateChanged(serviceState);

			StringBuffer sb = new StringBuffer();
			sb.append("Estado del servicio: ");

			switch (serviceState.getState()){
			case ServiceState.STATE_IN_SERVICE:
				sb.append("activado. Codigo operador: ");
				sb.append(serviceState.getOperatorNumeric());
				if (serviceState.getRoaming())
					sb.append(" [R]");
				sb.append(".");
				break;
			case ServiceState.STATE_OUT_OF_SERVICE:
				sb.append("sin servicio.");
				break;
			case ServiceState.STATE_EMERGENCY_ONLY:
				sb.append("solo emergencias.");
				break;
			case ServiceState.STATE_POWER_OFF:
				sb.append("apagado.");
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.telephony_main);

		//Obtenemos la instancia del TelephonyManager
		mManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		
		StringBuffer sb = new StringBuffer();
		sb.append("Informacion del telefono: " + mManager.getLine1Number());
		sb.append("\n");
		sb.append("N. serie: " + mManager.getSimSerialNumber());
		sb.append("\n");
		sb.append("Operador: " + mManager.getNetworkOperatorName());
		sb.append("\n");
		sb.append("Operador de la SIM: " + mManager.getSimOperatorName());
		sb.append("\n");
		
		show(sb.toString());
		
		TextView text = (TextView) findViewById(R.id.text);
		text.setText(sb.toString());
		
		//Registramos el oyente para escuchar distintos eventos
		mManager.listen(mListener, PhoneStateListener.LISTEN_CALL_STATE | 
				PhoneStateListener.LISTEN_DATA_ACTIVITY |
				PhoneStateListener.LISTEN_DATA_CONNECTION_STATE |
				PhoneStateListener.LISTEN_SERVICE_STATE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		//Deregistramos el oyente usando el parametro LISTEN_NONE
		mManager.listen(mListener, PhoneStateListener.LISTEN_NONE);
	}

	/**
	 * Muestra la informacion en el log y a traves de noticacion Toast
	 * 
	 * @param s
	 */
	private void show(String s){
		Log.d(TAG, s);
//		Toast.makeText(TelephonyManagerExample.this, s, Toast.LENGTH_LONG).show();
	}

	/**
	 * Transforma el entero network type a un valor textual
	 */
	private String getNetworkType(int networkType){
		switch (networkType){
		case TelephonyManager.NETWORK_TYPE_1xRTT:
			return "1xRTT";
		case TelephonyManager.NETWORK_TYPE_CDMA:
			return "CDMA";
		case TelephonyManager.NETWORK_TYPE_EDGE:
			return "EDGE";
		case TelephonyManager.NETWORK_TYPE_EHRPD:
			return "eHRPD";
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
			return "EVDO-0";
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
			return "EVDO-A";
		case TelephonyManager.NETWORK_TYPE_EVDO_B:
			return "EVDO-B";
		case TelephonyManager.NETWORK_TYPE_GPRS:
			return "GPRS";
		case TelephonyManager.NETWORK_TYPE_HSDPA:
			return "HSDPA";
		case TelephonyManager.NETWORK_TYPE_HSPA:
			return "HSPA";
		case TelephonyManager.NETWORK_TYPE_HSPAP:
			return "HSPA+";
		case TelephonyManager.NETWORK_TYPE_HSUPA:
			return "HSUPA";
		case TelephonyManager.NETWORK_TYPE_IDEN:
			return "iDen";
		case TelephonyManager.NETWORK_TYPE_LTE:
			return "LTE";
		case TelephonyManager.NETWORK_TYPE_UMTS:
			return "UMTS";
		case TelephonyManager.NETWORK_TYPE_UNKNOWN:
		default:
			return "desconocido";
		}
	}
}
