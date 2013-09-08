package al.uax.myactivitylist;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class StickyIntent extends Activity{

	private Intent batteryStatus;
	private BroadcastReceiver receiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
		
			// Are we charging / charged?
			int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
			boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || 
					status == BatteryManager.BATTERY_STATUS_FULL;

			// How are we charging?
			int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
			boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
			boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
			
			int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
			int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

			float currBattery = 100 * level / (float)scale;

			if(isCharging){
				if(usbCharge)
					Toast.makeText(context, "Conexión USB: " + currBattery + "%", Toast.LENGTH_LONG).show();
				
				if(acCharge)
					Toast.makeText(context, "Conexión AC: " + currBattery + "%", Toast.LENGTH_LONG).show();
			}
			else{
				Toast.makeText(context, "No esta cargando", Toast.LENGTH_LONG).show();
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		RelativeLayout layout = new RelativeLayout(this);
		TextView text = new TextView(this);
		text.setTextAppearance(this, android.R.style.TextAppearance_Large);
		text.setText("Estando en este intent, probar las siguientes cosas:\n"
				+ "Conectar el cable de carga a un USB\n"
				+ "Conectar el cable de carga a un enchufe AC\n"
				+ "Desconectarlo");
		layout.addView(text);
		setContentView(layout);
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		batteryStatus = this.registerReceiver(null, filter);
		registerReceiver(receiver, filter);
	}

	@Override
	protected void onStop() {
		super.onStop();
		unregisterReceiver(receiver);
	}
}