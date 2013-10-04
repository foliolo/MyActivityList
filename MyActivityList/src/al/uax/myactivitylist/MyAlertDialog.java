package al.uax.myactivitylist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;


public class MyAlertDialog extends Activity {
	private Button boton1;
	private Button boton2;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert_dialog);
		
		boton1 = (Button) findViewById(R.id.boton_alert_dialog1);
		boton2 = (Button) findViewById(R.id.boton_alert_dialog2);
		
/*		
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		
		Button boton = new Button(this);
		boton.setGravity(Gravity.CENTER_HORIZONTAL);
		boton.setText("Pulsar");
		
		layout.addView(boton);
		setContentView(layout);
*/		
		
/**
 * Ejemplo de dialogo simple
 */
		boton1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(MyAlertDialog.this);
				 
				   builder.setTitle("Información");
				   builder.setMessage("Esto es un mensaje de alerta.");
				   builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		                   public void onClick(DialogInterface dialog, int id) {
		                       dialog.cancel();
		                   }
		               });
		        
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		});
		

/**
 * Ejemplo de dialogo personalizado
 */
		boton2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				final Dialog dialog = new Dialog(MyAlertDialog.this);
				dialog.setTitle(R.string.dialog_titulo);
				dialog.setContentView(R.layout.dialog_personal);

				/**
				 * Botón de cancelar
				 */
				((Button) dialog.findViewById(R.id.dialog_cancel)).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						Toast.makeText(MyAlertDialog.this, "Cancel pulsado", Toast.LENGTH_SHORT).show();
					}
				});
				
				/**
				 * Botón OK
				 */
				((Button) dialog.findViewById(R.id.dialog_ok)).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						
						//Obtención del radio button elegido
						int rb = ((RadioGroup) dialog.findViewById(R.id.dialog_radiogroup)).getCheckedRadioButtonId();
						ImageView imagen = (ImageView) findViewById(R.id.image_alert_dialog);
						
						switch(rb){
						case R.id.dialog_rb_blue:
							imagen.setBackgroundColor(getResources().getColor(R.color.Blue));
							break;
						case R.id.dialog_rb_green:
							imagen.setBackgroundColor(getResources().getColor(R.color.Green));
							break;
						case R.id.dialog_rb_red:
							imagen.setBackgroundColor(getResources().getColor(R.color.Red));
							break;
						}
					}
				});
				
				/**
				 * Cambio de color del cuadro al cambiar el radio button
				 */
				((RadioGroup) dialog.findViewById(R.id.dialog_radiogroup)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						View colorBox = (View) dialog.findViewById(R.id.dialog_color_box);
						
						switch(checkedId){
						case R.id.dialog_rb_blue:
//							Toast.makeText(MyAlertDialog.this, "Blue", Toast.LENGTH_SHORT).show();
							colorBox.setBackgroundColor(getResources().getColor(R.color.Blue));
							break;
						case R.id.dialog_rb_green:
//							Toast.makeText(MyAlertDialog.this, "Green", Toast.LENGTH_SHORT).show();
							colorBox.setBackgroundColor(getResources().getColor(R.color.Green));
							break;
						case R.id.dialog_rb_red:
//							Toast.makeText(MyAlertDialog.this, "Red", Toast.LENGTH_SHORT).show();
							colorBox.setBackgroundColor(getResources().getColor(R.color.Red));
							break;
						}
						
					}
				});
				
				View colorBox = (View) dialog.findViewById(R.id.dialog_color_box);
				int checkedId = ((RadioGroup) dialog.findViewById(R.id.dialog_radiogroup)).getCheckedRadioButtonId();
				switch(checkedId){
				case R.id.dialog_rb_blue:
					colorBox.setBackgroundColor(getResources().getColor(R.color.Blue));
					break;
				case R.id.dialog_rb_green:
					colorBox.setBackgroundColor(getResources().getColor(R.color.Green));
					break;
				case R.id.dialog_rb_red:
					colorBox.setBackgroundColor(getResources().getColor(R.color.Red));
					break;
				}
				

				dialog.show();
				
				/*
				AlertDialog.Builder dbuilder = new Builder(MyAlertDialog.this);
				dbuilder.setIcon(R.drawable.ic_launcher);
				dbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(MyAlertDialog.this, "OK pulsado", Toast.LENGTH_SHORT).show();
					}
				});
				 */				
			}
		});
		
/**
 * Control de los botones del dialogo
 */
		
    }
}