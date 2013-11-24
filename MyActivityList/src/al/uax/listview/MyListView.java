package al.uax.listview;

import java.io.Serializable;
import java.util.ArrayList;

import al.uax.myactivitylist.R;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//Clase principal de TravelInfo
public class MyListView extends ListActivity{
	private ArrayList<TravelInfo> travels = new ArrayList<TravelInfo>();
	private ArrayAdapter<TravelInfo> adapter;
	private ListView list;
	protected static final int REQUEST_MODIF = 10;
	protected static final int REQUEST_ADD = 11;
	private static HorarioDatabaseHelper dbHelper;
	private int posicion;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_view);
		
		//Generamos los datos
//		travels.add(new TravelInfo("Londres", "UK", 2012, "Anotación 1"));
//		travels.add(new TravelInfo("Paris", "Francia", 2007, "Anotación 2"));
//		travels.add(new TravelInfo("Gotham", "City", 2011, "Anotación 3"));
//		travels.add(new TravelInfo("Hamburgo", "Alemania", 2009, "Anotación 4"));
//		travels.add(new TravelInfo("Pekin", "China", 2011, "Anotación 5"));
		
		//Recuperamos los datos de la base de datos
		dbHelper = new HorarioDatabaseHelper(this);
		travels = dbHelper.getTravelsList();
		
		list = (ListView) findViewById(android.R.id.list);
		
		//Creamos el adapter y lo asociamos a la actividad
		adapter = new TravelAdapter(this, travels);
		setListAdapter(adapter);
		
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(view != null){
					TravelInfo travel = travels.get(position);
					
					String mensaje = "Visita: " + travel.getCity() + "(" + travel.getCountry() + "), año: " + travel.getYear() + 
							"\nAnotación: " + travel.getAnotacion();
					
					Toast.makeText(MyListView.this, mensaje, Toast.LENGTH_LONG).show();
				}
			}
		});
		
		//Registramos el menú que saldrá al pulsar sobre los item		
		registerForContextMenu(list);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		dbHelper.close();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	
	
	
	
/*
	//Método que detecta cuando se pulsa un item de la lista.
	protected void onListItemClick(android.widget.ListView l, View v, int position, long id){
//		TravelInfo info = adapter.getItem(position);
		
		Intent intent = new Intent(this, EditTravelActivity.class);
		Bundle data = new Bundle();
		data.putSerializable("lista_viajes", travels);
		data.putInt("posicion", position);
		posicion = position;
		intent.putExtras(data);

		startActivityForResult(intent,REQUEST_MODIF);
		
		super.onListItemClick(l, v, position, id);
	}
*/
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.menu_item, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		Intent intent;
		
		posicion = info.position;
		
		switch(item.getItemId()){
			case R.id.modif:
				//Creamos el intent que modificara el item seleccionado
				intent = new Intent(this, EditTravelActivity.class);
				
				Bundle data = new Bundle();
				data.putInt("posicion", posicion);
				data.putSerializable("modif_viaje", (Serializable) travels.get(posicion));
				intent.putExtras(data);
				
				startActivityForResult(intent, REQUEST_MODIF);
				break;
			
			case R.id.delete:
				//Borramos el item seleccionado
				travels.remove(info.position);
				
				//Actualización de la base de datos
				dbHelper.remove(dbHelper.getWritableDatabase(), posicion+1);
				
				adapter.notifyDataSetChanged();
				break;
				
			case R.id.correo:
				//Creamos el intent que mandará la información al correo.
				intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				
				//Creamos el texto a mandar
				posicion = info.position;
				String textSend = "Viaje realizado:\n"
						+ "Ciudad: " + travels.get(posicion).getCity() + "\n"
						+ "País: " + travels.get(posicion).getCountry() + "\n"
						+ "Año" + travels.get(posicion).getYear() + "\n"
						+ "Anotación: " + travels.get(posicion).getAnotacion();
				
				intent.putExtra(Intent.EXTRA_TEXT, textSend);
				startActivity(Intent.createChooser(intent, getResources().getString(R.string.menu_item_send)));
				break;
		}
		
		return super.onContextItemSelected(item);
	}
	
	public boolean onMenuItemSelected(int featureId, MenuItem item){
		Intent intent;
		PackageManager pm = getPackageManager();
		switch(item.getItemId()){	
			case R.id.action_settings:
				Toast.makeText(MyListView.this, "Configuración", Toast.LENGTH_SHORT).show();
				break;
				
			case R.id.nuevo_viaje:
				intent = new Intent(this, EditTravelActivity.class);
				if(pm.resolveActivity(intent, 0) != null){
					Bundle data = new Bundle();
					data.putSerializable("lista_viajes", travels);
					data.putInt("posicion", -1);
					intent.putExtras(data);
					startActivityForResult(intent,REQUEST_ADD);
				}
				else
					Log.d("TAG", "No hay ninguna Activity capaz de reolver el Intent");
				break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		Bundle extras;
		if (resultCode == RESULT_OK) {
			switch(requestCode){
				case REQUEST_MODIF:
					extras = data.getExtras();
					if(extras != null){
						travels.remove(posicion);
						TravelInfo viaje = (TravelInfo) extras.getSerializable("modif_viaje");
						travels.add(posicion, viaje);
					
						//Actualización de la base de datos
						dbHelper.updateTravel(dbHelper.getWritableDatabase(), 
							viaje.getCity(),
							viaje.getCountry(),
							viaje.getYear(),
							viaje.getAnotacion(),
							posicion+1);
					}
					
					adapter.notifyDataSetChanged();
		    	  	break;
		    	  	
				case REQUEST_ADD:
					extras = data.getExtras();
					if(extras != null){
						travels.add((TravelInfo) extras.getSerializable("nuevo_viaje"));
						
						dbHelper.insertTravel(dbHelper.getWritableDatabase(), 
							travels.get(travels.size()-1).getCity(),
							travels.get(travels.size()-1).getCountry(),
							travels.get(travels.size()-1).getYear(), 
							travels.get(travels.size()-1).getAnotacion());
					}
					
					adapter.notifyDataSetChanged();
		    	  	break;
		    	  	
				default:
					throw new IllegalStateException("Invalid request code.");
	      	}
			
			//Actualizamos el listView
			adapter.notifyDataSetChanged();
		}
	}
	
	
	//Inner class para el Adaptador que manejara los datos de la clase que hemos creado	
	private class TravelAdapter extends ArrayAdapter<TravelInfo>{
		private Context context;
		private ArrayList<TravelInfo> travels;
		
		public TravelAdapter(Context context, ArrayList<TravelInfo> travels) {
			super(context, android.R.layout.simple_list_item_2, travels);
			
			this.context = context;
			this.travels = travels;
			
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout view;
			ViewHolder holder;
			
			if(convertView == null){
				view = new LinearLayout(context);
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				inflater.inflate(android.R.layout.simple_list_item_2, view, true);
				
				holder = new ViewHolder();
				holder.text1 = (TextView) view.findViewById(android.R.id.text1);
				holder.text2 = (TextView) view.findViewById(android.R.id.text2);
				
				view.setTag(holder);
			}
			else{
				view = (LinearLayout) convertView;
				holder = (ViewHolder) view.getTag();
			}
			
			//Rellenamos la vista con los datos
			TravelInfo info = travels.get(position);
			holder.text1.setText(info.getCity() + "(" + info.getCountry() + ")");
			holder.text2.setText("Año: " + info.getYear() + "     \t" + info.getAnotacion());
			
			return view;
		}
	}
	
	//Otra inner class. Se utiliza como asistente para almacenar las referencias de los objetos de la interfaz gráfica
	static class ViewHolder{
		TextView text1;
		TextView text2;
	}
}


/*
Utilización de los comandos del ADB:
Ruta: D:\Programas\PROGRAMACION\ELCIPSE Kepler\platform-tools>

Comandos:
adb.exe shell command rm /data/data/al.aux.myactivitylist/databases/*
adb.exe shell command chmod -R 777 /data/data/al.uax.myactivityist/databases
adb.exe uninstall /data/app/al.uax.myactivitylist-2.apk
*/