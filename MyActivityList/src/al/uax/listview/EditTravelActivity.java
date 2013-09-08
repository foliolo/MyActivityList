package al.uax.listview;

import java.util.ArrayList;

import al.uax.myactivitylist.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class EditTravelActivity extends Activity {
	private ArrayList<TravelInfo> travels;
	private TextView ciudad;
	private TextView pais;
	private TextView anyo;
	private TextView anotacion;
	private int item = -1; //Posición del item

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_travel);
		
		//Obtenemos la referencia de los objetos gráficos
		ciudad = (TextView) findViewById(R.id.ciudad);
		pais = (TextView) findViewById(R.id.pais);
		anyo = (TextView) findViewById(R.id.anyo);
		anotacion = (TextView) findViewById(R.id.anotacion);
		
		//Obtenemos los datos de la actividad principal
		Bundle datos = getIntent().getExtras();
		if(datos != null){
			item = datos.getInt("posicion");
			travels = (ArrayList<TravelInfo>) datos.getSerializable("lista_viajes");
			
			//Mostramos los datos a modificar
			if(item >= 0){
				ciudad.setText(travels.get(item).getCity());
				pais.setText(travels.get(item).getCountry());
				anyo.setText("" + travels.get(item).getYear());
				anotacion.setText(travels.get(item).getAnotacion());
			}
		}
		
		Button boton = (Button) findViewById(R.id.boton);
		boton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Modificamos la lista con la nueva información
				if(item >= 0){
					travels.get(item).setCity(ciudad.getText().toString());
					travels.get(item).setCountry(pais.getText().toString());
					travels.get(item).setYear(Integer.parseInt(anyo.getText().toString()));
					travels.get(item).setAnotacion(anotacion.getText().toString());
				}
				//Añadimos el nuevo elemento
				else{
					travels.add(new TravelInfo(
							ciudad.getText().toString(), 
							pais.getText().toString(), 
							Integer.parseInt(anyo.getText().toString()), 
							anotacion.getText().toString())
					);
				}
				
				//Empaquetamos el nuevo vector y lo mandamos a la actividad principal
				Bundle data = new Bundle();
				data.putSerializable("lista_viajes", travels);
				getIntent().putExtras(data);
		    	
				setResult(RESULT_OK, getIntent());
		    	finish();
			}
		});
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
