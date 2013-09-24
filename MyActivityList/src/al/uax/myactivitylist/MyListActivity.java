package al.uax.myactivitylist;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MyListActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_activity);
		
		//Definimos nuestra colección de datos (lista de actividades)
		//ArrayList<String> listaActividades = new ArrayList<String>(R.array.actividades);
		String[] listaActividades = getResources().getStringArray(R.array.actividades);
		
		
		//Creamos el adaptador para acceder a los datos de nuestro listado. 
		//Cada item se mostrará en un view definido por Android
		ListAdapter adapter = new ArrayAdapter<String>(MyListActivity.this, android.R.layout.simple_list_item_1, listaActividades);
		
		//Enlazamos el adapter con nuestra vista
		ListView view = (ListView) findViewById(android.R.id.list);
		view.setAdapter(adapter);
		
		view.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				Intent intent;
				PackageManager pm;
				switch(pos) {
		            case 0: 
		            	 intent = new Intent(MyListActivity.this, PrimeraInterfaz.class);
		            	
		            	 pm = getPackageManager();
		            	if(pm != null)
		            		startActivity(intent);
		            	else
		            		Log.d("TAG", "No hay ningun Activity para el intent");
		            	break;
		            	
		            case 1:
            			intent = new Intent(MyListActivity.this, ListasAdapters.class);
		            	
		            	pm = getPackageManager();
		            	if(pm != null)
		            		startActivity(intent);
		            	else
		            		Log.d("TAG", "No hay ningun Activity para el intent");
		            	break;
		            	
		            case 2:
            			//intent = new Intent(MyListActivity.this, al.uax.listview.MyListView.class);
		            	intent = new Intent(MyListActivity.this, al.uax.listview.MyListViewProvider.class);
		            	
		            	pm = getPackageManager();
		            	if(pm != null)
		            		startActivity(intent);
		            	else
		            		Log.d("TAG", "No hay ningun Activity para el intent");
		            	break;	
		            	
		            case 3:
            			intent = new Intent(MyListActivity.this, al.uax.myactivitylist.Receiver.class);
		            	
		            	pm = getPackageManager();
		            	if(pm != null)
		            		startActivity(intent);
		            	else
		            		Log.d("TAG", "No hay ningun Activity para el intent");
		            	break;
		            	
		            case 4:
		            	intent = new Intent(MyListActivity.this, al.uax.myactivitylist.StickyIntent.class);
		            	
		            	pm = getPackageManager();
		            	if(pm != null)
		            		startActivity(intent);
		            	else
		            		Log.d("TAG", "No hay ningun Activity para el intent");
		            	break;
		            	
		            case 5:
		            	intent = new Intent(MyListActivity.this, al.uax.myactivitylist.MiPendingIntent.class);
		            	
		            	pm = getPackageManager();
		            	if(pm != null)
		            		startActivity(intent);
		            	else
		            		Log.d("TAG", "No hay ningun Activity para el intent");
		            	break;
		            	
		            case 6:
		            	intent = new Intent(MyListActivity.this, al.uax.myactivitylist.Telephony.class);
		            	
		            	pm = getPackageManager();
		            	if(pm != null)
		            		startActivity(intent);
		            	else
		            		Log.d("TAG", "No hay ningun Activity para el intent");
		            	break;
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_list, menu);
		return true;
	}
}