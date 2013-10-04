package al.uax.listview;

import al.uax.myactivitylist.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class EditTravelActivity extends Activity {
//	private ArrayList<TravelInfo> travels;
	private TextView ciudad;
	private TextView pais;
	private TextView anyo;
	private TextView anotacion;
	private int posicion = -1; //Posición del item

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
		Bundle extras = getIntent().getExtras();
		posicion = extras.getInt("posicion", -1);
		
		if (extras != null && posicion != -1)
		{
	    	ciudad.setText( ((TravelInfo) extras.getSerializable("modif_viaje") ).getCity());
	    	pais.setText( ((TravelInfo) extras.getSerializable("modif_viaje") ).getCountry());
	    	anyo.setText("" + ((TravelInfo) extras.getSerializable("modif_viaje") ).getYear());
	    	anotacion.setText( ((TravelInfo) extras.getSerializable("modif_viaje") ).getAnotacion());
		}
/*		
		//Realizamos la funcionalidad del botón. Se devuelven los datos actualizados a la activity principal.
		Button boton = (Button) findViewById(R.id.boton);
		boton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Modificamos la lista con la nueva información
				if(posicion != -1){
					int fecha = anyo.getText().toString().equals("") ? 0 : Integer.parseInt(anyo.getText().toString());
					
					Bundle data = new Bundle();
					data.putSerializable("modif_viaje", (Serializable) new TravelInfo(ciudad.getText().toString(), 
							pais.getText().toString(), 
							fecha, 
							anotacion.getText().toString()));
					getIntent().putExtras(data);
					
					setResult(RESULT_OK, getIntent());
					finish();
				}
				else{
					//Creamos el Intent que devolverá el nuevo objeto a añadir(controlando que la fecha no sea vacío)
					int fecha = anyo.getText().toString().equals("") ? 0 : Integer.parseInt(anyo.getText().toString());
					
					getIntent().putExtra("nuevo_viaje", (Serializable) new TravelInfo(ciudad.getText().toString(), 
							pais.getText().toString(), 
							fecha, 
							anotacion.getText().toString()));
					
					setResult(RESULT_OK, getIntent());
					finish();
				}
			}
		});
*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.travel, menu);
		return true;
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
