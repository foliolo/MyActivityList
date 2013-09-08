package al.uax.myactivitylist;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ListasAdapters extends Activity{
	
	private Spinner mSpinner;
	
	@Override
	protected void onCreate(Bundle suvedInstanceState){
		super.onCreate(suvedInstanceState);
		setContentView(R.layout.lista);
		
		mSpinner = (Spinner) findViewById(R.id.spinner);
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.dias, android.R.layout.simple_spinner_dropdown_item);
		mSpinner.setAdapter(adapter);
		
		mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
@SuppressWarnings("unused")
				String dia = (String) parent.getItemAtPosition(pos);
				//Tratar la información
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				//No hacer nada
			}
		});
	}
}
