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
		
		//Estamos cambiando el TextView de la actividad desde el servicio
		OperacionesAritmeticas.solOperacion.setText(sol.toString());
		
		return START_NOT_STICKY;
	}
}