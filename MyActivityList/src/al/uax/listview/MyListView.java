package al.uax.listview;

import java.util.ArrayList;

import al.uax.myactivitylist.R;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//Clase principal de TravelInfo
public class MyListView extends ListActivity{
	private ArrayList<TravelInfo> travels = new ArrayList<TravelInfo>();
	private ArrayAdapter<TravelInfo> adapter;
//	private ListView list;
	protected static final int REQUEST_MODIF = 10;
	protected static final int REQUEST_ADD = 11;
	private static TravelsDatabaseHelper dbHelper;
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
		dbHelper = new TravelsDatabaseHelper(this);
		travels = dbHelper.getTravelsList();
		
		//Creamos el adapter y lo asociamos a la actividad
		adapter = new TravelAdapter(this, travels);
		setListAdapter(adapter);
//		list = (ListView) findViewById(android.R.id.list);
//		list.setAdapter(adapter);
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_view, menu);
		return true;
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
/*				
			case R.id.compartir:
				intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("image/*");
				Intent chooserIntent = Intent.createChooser(intent,null);
				
				if(pm.resolveActivity(chooserIntent,0) != null)
					startActivityForResult(chooserIntent, REQUEST_ADD);
*/				
//				intent = new Intent();
//				intent.setAction(android.content.Intent.ACTION_SEND);
//				intent.setType("text/plain");
				
//				if(pm.resolveActivity(intent, 0) != null)
//					startActivity(Intent.createChooser(intent, "Elegir método"));
//				else
//					Log.d("TAG", "No hay ninguna Activity capaz de resolver el Intent");
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		//Obtenemos los datos de la subactivity
		if(data != null){
			travels.clear();
			travels.addAll((ArrayList<TravelInfo>) data.getSerializableExtra("lista_viajes"));
		}
		
		if (resultCode == RESULT_OK) {
			switch(requestCode){
				case REQUEST_MODIF:
					//Actualización de la base de datos
					dbHelper.updateTravel(dbHelper.getWritableDatabase(), 
							travels.get(posicion).getCity(),
							travels.get(posicion).getCountry(),
							travels.get(posicion).getYear(), 
							travels.get(posicion).getAnotacion(),
							posicion+1);
		    	  	break;
		    	  	
				case REQUEST_ADD:
					dbHelper.insertTravel(dbHelper.getWritableDatabase(), 
							travels.get(travels.size()-1).getCity(),
							travels.get(travels.size()-1).getCountry(),
							travels.get(travels.size()-1).getYear(), 
							travels.get(travels.size()-1).getAnotacion());
					
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