package al.uax.myactivitylist;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MiPendingIntent extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pending);
		
		Button boton = (Button) findViewById(R.id.botonpending);

		boton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//It's a joke!!! ;)
				Toast.makeText(MiPendingIntent.this, "¿Por qué tocas?",  Toast.LENGTH_LONG).show();
				
				//Indicamos la demora en segundos (para la alarma)
				int segundos = 5; 
				
				//Creamos el intent que nos llevara a la pag web
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("http://www.google.es"));
				
				//Creamos el pending intent con el flag FLAG_ONE_SHOT, que indica que solo se lanzará una vez
				PendingIntent pending = PendingIntent.getActivity(MiPendingIntent.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
				
				//Instanciamos el AlarmManager y registramos la alarma en sistema RTC
				AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
				am.set(AlarmManager.RTC,System.currentTimeMillis() + segundos*1000, pending);
			}
		});
	}
}