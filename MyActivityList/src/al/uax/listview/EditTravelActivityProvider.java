package al.uax.listview;

import al.uax.listview.TravelProvider.Travels;
import al.uax.myactivitylist.R;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class EditTravelActivityProvider extends Activity {
//	private ArrayList<TravelInfo> travels;
	private TextView ciudad;
	private TextView pais;
	private TextView anyo;
	private TextView anotacion;
	private int posicion = -1; //Posición del item
	private ContentResolver cr;
	private Cursor c;
	private static final String[] PROJECTION = {Travels._ID, Travels.CITY, Travels.COUNTRY, Travels.YEAR, Travels.NOTE};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_travel);
		
		//Obtenemos la referencia de los objetos gráficos
		ciudad = (TextView) findViewById(R.id.ciudad);
		pais = (TextView) findViewById(R.id.pais);
		anyo = (TextView) findViewById(R.id.anyo);
		anotacion = (TextView) findViewById(R.id.anotacion);
		
		//Rellenamos los elementos con los datos que nos manda la actividad principal. (Siempre que no sea un elemento nuevo)		
//		Bundle extras = getIntent().getExtras();
//		posicion = extras.getInt("posicion", -1);
		
		posicion = getIntent().getIntExtra("posicion", -1);
		
		cr = getContentResolver();
		c = cr.query(TravelProvider.CONTENT_URI, PROJECTION, null, null, null);
		if (posicion != -1)
		{
			c.moveToPosition(posicion);
	    	ciudad.setText( c.getString(c.getColumnIndex(Travels.CITY)));
	    	pais.setText( c.getString(c.getColumnIndex(Travels.COUNTRY)));
	    	anyo.setText( c.getString(c.getColumnIndex(Travels.YEAR)));
	    	anotacion.setText( c.getString(c.getColumnIndex(Travels.NOTE)));
		}
/*		
		//Realizamos la funcionalidad del botón. Se devuelven los datos actualizados a la activity principal.
		Button boton = (Button) findViewById(R.id.boton);
		boton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Obtención de los datos del formulario
				ContentValues values = new ContentValues();
				values.put(Travels.CITY, ciudad.getText().toString());
				values.put(Travels.COUNTRY, pais.getText().toString());
				values.put(Travels.YEAR, anyo.getText().toString());
				values.put(Travels.NOTE, anotacion.getText().toString());
				
				//Modificamos la lista con la nueva información
				if(posicion != -1){
					//Actualización de la base de datos mediante el provider
					String id = c.getString(c.getColumnIndex(Travels._ID));
					String where = Travels._ID + "=" + id;

					cr.update(TravelProvider.CONTENT_URI, values, where, null);
					
				}
				else{
					//Añadimos el nuevo elemento a la base de datos
					cr.insert(TravelProvider.CONTENT_URI, values);
				}
				
				//Llamada a la Actividad principal 
				getIntent().setClass(EditTravelActivityProvider.this, MyListViewProvider.class);
				startActivity(getIntent());
				
				//Finalización de la activity
				finish();
			}
		});
*/		
		//ActionBar
		ActionBar bar = getActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_list, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		PackageManager pm = getPackageManager();
		
		switch(item.getItemId()){	

		case android.R.id.home:
			intent = new Intent(this, MyListViewProvider.class);
			if(pm.resolveActivity(intent, 0) != null){
				startActivity(intent);
				finish();
			}
			else
				Log.d("TAG", "No hay ninguna Activity capaz de reolver el Intent");
			break;
			
		case R.id.action_settings:
			Toast.makeText(EditTravelActivityProvider.this, "Configuración", Toast.LENGTH_SHORT).show();
			break;
			
		case R.id.guardar:
			//Obtención de los datos del formulario
			ContentValues values = new ContentValues();
			values.put(Travels.CITY, ciudad.getText().toString());
			values.put(Travels.COUNTRY, pais.getText().toString());
			values.put(Travels.YEAR, anyo.getText().toString());
			values.put(Travels.NOTE, anotacion.getText().toString());
			
			//Modificamos la lista con la nueva información
			if(posicion != -1){
				//Actualización de la base de datos mediante el provider
				String id = c.getString(c.getColumnIndex(Travels._ID));
				String where = Travels._ID + "=" + id;

				cr.update(TravelProvider.CONTENT_URI, values, where, null);
				
			}
			else{
				//Añadimos el nuevo elemento a la base de datos
				cr.insert(TravelProvider.CONTENT_URI, values);
			}
			
			//Llamada a la Actividad principal 
			getIntent().setClass(EditTravelActivityProvider.this, MyListViewProvider.class);
			startActivity(getIntent());
			
			//Finalización de la activity
			finish();
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState){
		Log.e("TAG", "Estado salvado");
		
		//Localizamos los elementos de pantalla que vamos a guardar
		anotacion = (TextView) findViewById(R.id.anotacion);

		String miCiudad = ciudad.getText().toString();
		String miPais = pais.getText().toString();
		String miAnyo= anyo.getText().toString();
		String miAnotacion = anotacion.getText().toString();
		
		outState.putString("ciudad",miCiudad);
		outState.putString("pais", miPais);
		outState.putString("anyo", miAnyo);
		outState.putString("anotacion", miAnotacion);
		
		super.onSaveInstanceState(outState);
	}
	
	//@Override
	protected void onRestoreInstaceState(Bundle savedInstanceState){
		Log.e("TAG", "Estado restaurado");
		
		if(savedInstanceState != null){
			String miCiudad = savedInstanceState.getString("ciudad");
			String miPais = savedInstanceState.getString("pais");
			String miAnyo= savedInstanceState.getString("anyo");
			String miAnotacion = savedInstanceState.getString("anotacion");
			
			ciudad.setText(miCiudad);
			pais.setText(miPais);
			anyo.setText(miAnyo);
			anotacion.setText(miAnotacion);
		}
		
		super.onRestoreInstanceState(savedInstanceState);
	}
}
