package al.uax.listview;

import al.uax.listview.TravelProvider.Travels;
import al.uax.myactivitylist.R;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

//Clase principal de TravelInfo
public class MyListViewProvider extends ListActivity{
	
	private ListView list;
	protected static final int REQUEST_MODIF = 10;
	protected static final int REQUEST_ADD = 11;
	private TravelsCursorAdapter mAdapter;
	private static final String[] PROJECTION = {Travels._ID, Travels.CITY, Travels.COUNTRY, Travels.YEAR, Travels.NOTE};
	private Cursor c;
	ContentResolver cr;
	private int posicion;

	@Override
	public void onCreate(Bundle savedInstanceState){
		Log.d("TAG", "CREATE");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_view);

		list = (ListView) findViewById(android.R.id.list);
		
		cr = getContentResolver();
		
//		Uri uri = Uri.parse("content://al.uax.listview/travels");
//		c = cr.query(uri, PROJECTION, null, null, null);
		c = cr.query(TravelProvider.CONTENT_URI, PROJECTION, null, null, null);

		//Creamos el adapter y lo asociamos a la actividad
		mAdapter = new TravelsCursorAdapter(this, c);
		list.setAdapter(mAdapter);
		
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(view != null){
					c.moveToPosition(position);
					
					String city = c.getString(c.getColumnIndex(Travels.CITY));
					String country = c.getString(c.getColumnIndex(Travels.COUNTRY));
					String year = c.getString(c.getColumnIndex(Travels.YEAR));
					String note = c.getString(c.getColumnIndex(Travels.NOTE));
					
					String mensaje = "Visita: " + city + "(" + country + "), año: " + year + "\nAnotación: " + note;
					
					Toast.makeText(MyListViewProvider.this, mensaje, Toast.LENGTH_LONG).show();
				}
			}
		});
		
		//Registramos el menú que saldrá al pulsar sobre los item		
		registerForContextMenu(list);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Log.d("TAG", "PAUSE");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d("TAG", "RESUME");
	}	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_view, menu);
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
				intent = new Intent(this, EditTravelActivityProvider.class);
				
				//Envío de la posición modificada
				intent.putExtra("posicion", posicion);
				startActivity(intent);
			
				break;
			
			case R.id.delete:
				//Actualización de la base de datos
				String where = "_id=" + ++posicion;
				cr.delete(TravelProvider.CONTENT_URI, where, null);
				
				intent = new Intent(this, MyListViewProvider.class);
				
				//Envío de la posición modificada
				intent.putExtra("posicion", posicion);
				startActivity(intent);
				finish();
				break;
/*				
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
*/
		}
		
		return super.onContextItemSelected(item);
	}
	
	public boolean onMenuItemSelected(int featureId, MenuItem item){
		Intent intent;
		PackageManager pm = getPackageManager();
		switch(item.getItemId()){	
			case R.id.action_settings:
				Toast.makeText(MyListViewProvider.this, "Configuración", Toast.LENGTH_SHORT).show();
				break;
				
			case R.id.nuevo_viaje:
				intent = new Intent(this, EditTravelActivityProvider.class);
				if(pm.resolveActivity(intent, 0) != null){
					intent.putExtra("posicion", -1);
					startActivity(intent);
				}
				else
					Log.d("TAG", "No hay ninguna Activity capaz de reolver el Intent");
				break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
/*	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
//		Bundle extras;
		if (resultCode == RESULT_OK) {
			switch(requestCode){
				case REQUEST_MODIF:
					
					c = cr.query(TravelProvider.CONTENT_URI, PROJECTION, null, null, null);

					mAdapter = new TravelsCursorAdapter(this, c);
					mAdapter.notifyDataSetChanged();
					list.setAdapter(mAdapter);
					
//					extras = data.getExtras();
//					if(extras != null){
//						travels.remove(posicion);
//						TravelInfo viaje = (TravelInfo) extras.getSerializable("modif_viaje");
//						travels.add(posicion, viaje);
//					
//						//Actualización de la base de datos
//						dbHelper.updateTravel(dbHelper.getWritableDatabase(), 
//							viaje.getCity(),
//							viaje.getCountry(),
//							viaje.getYear(),
//							viaje.getAnotacion(),
//							posicion+1);
//					}
//					
//					adapter.notifyDataSetChanged();
		    	  	break;
		    	  	
				case REQUEST_ADD:
//					extras = data.getExtras();
//					if(extras != null){
//						travels.add((TravelInfo) extras.getSerializable("nuevo_viaje"));
//						
//						dbHelper.insertTravel(dbHelper.getWritableDatabase(), 
//							travels.get(travels.size()-1).getCity(),
//							travels.get(travels.size()-1).getCountry(),
//							travels.get(travels.size()-1).getYear(), 
//							travels.get(travels.size()-1).getAnotacion());
//					}
//					
//					adapter.notifyDataSetChanged();
		    	  	break;
		    	  	
				default:
					throw new IllegalStateException("Invalid request code.");
	      	}
			
			//Actualizamos el listView
//			adapter.notifyDataSetChanged();
//			getContentResolver().notifyChange(TravelProvider.CONTENT_URI, null);
			mAdapter.notifyDataSetChanged();
			
		}
	}
 */	

	final class TravelsCursorAdapter extends ResourceCursorAdapter{
		private LayoutInflater mInflater;

		public TravelsCursorAdapter(Context context, Cursor c){
			super(context, android.R.layout.simple_list_item_2, c, 0);

			mInflater = LayoutInflater.from(context);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent){
			View view = mInflater.inflate(android.R.layout.simple_list_item_2, parent, false);

			ViewHolder holder = new ViewHolder();
			TextView text1 = (TextView) view.findViewById(android.R.id.text1);
			TextView text2 = (TextView) view.findViewById(android.R.id.text2);
			holder.text1 = text1;
			holder.text2 = text2;

			view.setTag(holder);

			return view;
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			ViewHolder holder = (ViewHolder) view.getTag();

			String city = cursor.getString(cursor.getColumnIndex(Travels.CITY));
			String country = cursor.getString(cursor.getColumnIndex(Travels.COUNTRY));

			holder.text1.setText(city);
			holder.text2.setText(country);
		}

		private class ViewHolder{
			private TextView text1;
			private TextView text2;
		}
	}
}

