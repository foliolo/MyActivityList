package al.uax.localizacion;

import al.uax.myactivitylist.R;
import android.app.Activity;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Este ejemplo muestra como utilizar el LocationManager para encontrar la localizacion del dispositivo.
 * Se muestran dos formas de tomar la localizacion:
 * 
 * 1. getLastLocation(): devuelve la ultima localizacion conocida.
 * 2. requestSingleLocationUpdate(): solicita una actualizacion de la localizacion.
 * 
 * Para establecer los parametros de la busqueda de la localizacion se deja en manos del LocationManager
 * encontrar el mejor provider (GPS, WIFI...) en funcion de unos criterios definidos en la clase
 * Criteria. En este caso se pide un gasto medio de bateria.
 * 
 * Finalmente se muestra la localizacion en Google Maps usando un Intent.
 * 
 */
public class LocationActivity extends Activity {
	
	private static final String TAG = "LocationActivity";
	
	private LocationManager mLocationManager;
	private Location mLocation;
	private Criteria mCriteria;

	private Button mLastLocationButton;
	private Button mGetLocationButton;
	private Button mMapButton;
	private TextView mLocationText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        mLastLocationButton = (Button) findViewById(R.id.last_location_button);
        mGetLocationButton = (Button) findViewById(R.id.get_last_location);
        mMapButton = (Button) findViewById(R.id.map_button);
        mLocationText = (TextView) findViewById(R.id.location_text);
        
        //Instanciación del LocationManager
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        
        //Establecemos los criterios para elegir el provider
        mCriteria = new Criteria();
        mCriteria.setPowerRequirement(Criteria.POWER_MEDIUM);
        
        mLastLocationButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				//Tomar la ultima localización conocida
				String provider = mLocationManager.getBestProvider(mCriteria, true);
				Location location = mLocationManager.getLastKnownLocation(provider);
				
				showLocation(location);
			}
		});
        
    	mGetLocationButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				//Solicitar una actualización de la localización
				mLocationManager.requestSingleUpdate(mCriteria, new LocationListener() {
					
					public void onStatusChanged(String provider, int status, Bundle extras) {
						Log.i(TAG, "Status changed: " + provider + " " + status);
					}
					
					public void onProviderEnabled(String provider) {
						Log.i(TAG, "Provider enabled: " + provider);
					}
					
					public void onProviderDisabled(String provider) {
						Log.i(TAG, "Provider enabled: " + provider);
						
					}
					
					public void onLocationChanged(Location location) {
						Toast.makeText(LocationActivity.this, "Location found", Toast.LENGTH_SHORT).show();
						showLocation(location);
					}
				}, getMainLooper());
				Toast.makeText(LocationActivity.this, "Requeting location", Toast.LENGTH_SHORT).show();
			}
		});
        
        mMapButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				if (mLocation == null){
					Toast.makeText(LocationActivity.this, "Location data is null", Toast.LENGTH_SHORT).show();
				} else {
					
					//Lanza un Intent para mostrar un mapa con la localización actual
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse("geo:" + mLocation.getLatitude() + "," + mLocation.getLongitude()));
					startActivity(intent);
				}
			}
		});
    }
	
	private void showLocation(Location location){
		
		//Muestra la localización en pantalla en forma textual
		mLocation = location;
		if (mLocation != null)
			mLocationText.setText("Location: \nLatitude: " + mLocation.getLatitude() + 
					"\nLongitude : " + mLocation.getLongitude());
	}

}
