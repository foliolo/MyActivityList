package al.uax.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import al.uax.myactivitylist.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class XmlWeather extends Activity {
	
	private static final String WEATHER_URL = "http://weather.yahooapis.com/forecastrss?w=";
	private static final String MADRID_CODE = "766273";
	
	public WeatherInfo info;
	public String buffer = "";
	
    public ImageView imageView;
    public Bitmap imagen;
    public TextView informacion;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xml_main);
		
		imageView = (ImageView) findViewById(R.id.imagen_tiempo);
		informacion = (TextView) findViewById(R.id.label_xml);
		
		//lanzamos la tarea asíncrona para obtener los datos (si tenemos conexión)
		if(verificaConexion(this)){
			try {
				new Weather().execute(MADRID_CODE);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				Toast.makeText(XmlWeather.this, e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		}
		else{
			Toast.makeText(XmlWeather.this, "Comprueba tu conexión a Internet. Saliendo ... ", Toast.LENGTH_SHORT).show();
		    this.finish();
		}
	}
	
	/**
	 * Módulo de comprobación de conectividad
	 */
	public static boolean verificaConexion(Context context) {
	    ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    
	    // No sólo wifi, también GPRS
	    NetworkInfo[] redes = connec.getAllNetworkInfo();
	    
	    // este bucle debería no ser tan ñapa
	    for (int i = 0; i < 2; i++) {
	        // ¿Tenemos conexión? ponemos a true
	        if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
	            return true;
	        }
	    }
	    return false;
	}
	
	/**
	 * Clase que realiza la conexión asincrona con el servicio del Yahoo Weather
	 */
	private class Weather extends AsyncTask<String, Void, WeatherInfo>{

		private ProgressDialog progressDialog;
		
		@Override
		protected void onPreExecute(){
	        progressDialog = new ProgressDialog(XmlWeather.this);
			progressDialog.setTitle("Procesando XML");
			progressDialog.setMessage("Obteniendo datos...");
         	progressDialog.show();
		}
		
		@Override
		protected WeatherInfo doInBackground(String... params) {
			String code = params[0];
			
			if(TextUtils.isEmpty(code))
				throw new IllegalArgumentException("Code cannot be empty");
			
			URL url = null;
			HttpURLConnection connection = null;
			
			try{
				//Construimos la URL y realizamos la conexión
				url = new URL(WEATHER_URL + code + "&u=c");
				connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				
				InputStream is = connection.getInputStream();
				
				//Optimización ---> No se por qué...
				if (is == null)
					return null;
				
				//Parseamos la respuesta
				info = new WeatherInfo();
						
				XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
				XmlPullParser parser = factory.newPullParser();
				parser.setInput(new InputStreamReader(is));
				
				//Realizamos el bucle que recorrerá el fichero XML
				int eventType = parser.getEventType();
				while(eventType != XmlPullParser.END_DOCUMENT){
					switch(eventType){
					case XmlPullParser.START_TAG:
//						Log.i("TAG", "starTag: " + parser.getName());
						
						//Obtención de la LOCALIZACIÓN y las UNIDADES
						if(parser.getName().equals("yweather:location")){
						    info.ciudad = parser.getAttributeValue(null, "city");
						    info.pais = parser.getAttributeValue(null, "country");
						    buffer += info.ciudad + " - " + info.pais + "\n";
						}
						if(parser.getName().equals("yweather:units")){
						    info.unitsTemp = parser.getAttributeValue(null, "temperature");
						}
						
						//Obtención del TIEMPO y la TEMPERATURA del día
						if(parser.getName().equals("yweather:condition")){
						    info.tiempo = parser.getAttributeValue(null, "text");
						    info.temp = Integer.parseInt(parser.getAttributeValue(null, "temp"));
						    buffer += "Tiempo: " + info.tiempo + "\n";
						    buffer += "Temperatura actual: " + info.temp + "º" + info.unitsTemp + "\n";
						}

						//Obtención de la IMAGEN
						if(parser.getName().equals("description")){
							parser.next();
						    String aux = parser.getText();
						    try{
							    int inicio = aux.indexOf(new String("\""));
							    int fin = aux.indexOf(new String("\""), inicio+1); 
					
							    info.imagen = aux.substring(inicio+1, fin);
						    }catch(StringIndexOutOfBoundsException e){
						    	Log.d("PRUEBA", "Esta no es la cadena tronco...");
						    }
						    Log.d("PRUEBA", "Imagen: " + info.imagen);

					    	//Descargamos la imagen y la ponemos en la actividad
						    if(info.imagen != null)
							    downloadImagen(info.imagen);
						}
						break;
						
					case XmlPullParser.END_TAG:
//						Log.i("TAG", "endTag: " + parser.getName());
						break;
						
					case XmlPullParser.TEXT:
//						Log.i("TAG", "Text: " + parser.getText());
						break;
					}
					eventType = parser.next();
				}
			}
			catch (IOException e){
				e.printStackTrace();
				Toast.makeText(XmlWeather.this, e.getMessage(), Toast.LENGTH_SHORT).show();
			}
			catch (XmlPullParserException e){
				e.printStackTrace();
			}
			finally{
				if(connection != null)
					connection.disconnect();
			}
			
			return info;
		}
		
		@Override
		protected void onPostExecute(WeatherInfo result){
			super.onPostExecute(result);
			progressDialog.dismiss();

	        imageView.setImageBitmap(imagen);
	        informacion.setText(buffer);

//			Toast.makeText(XmlWeather.this, "Task finalizada", Toast.LENGTH_SHORT).show();
		}
		
		void downloadImagen(String imageHttpAddress) {
	        URL imageUrl = null;
	        try {
	            imageUrl = new URL(imageHttpAddress);
	            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
	            conn.connect();
	            imagen = BitmapFactory.decodeStream(conn.getInputStream());
	        } catch (IOException e) {
	            Log.d("PRUEBA", "Error cargando la imagen: " + e.getMessage());
	            e.printStackTrace();
	        }
	    }
	}
	
	/**
	 * Clase para almacenar la información del tiempo
	 */
	@SuppressWarnings("unused")
	private class WeatherInfo{
		//Ciudad
		private String ciudad;
		private String pais;
		private String unitsTemp;
		private String tiempo;
		private int temp;
		private String imagen;
	
		//Tiempo de Mañana
		private String tiempoMnn;
		private int maxTemMnn;
		private int minTempMnn;
	}
}


/*
 * // Processes summary tags in the feed.
private String readSummary(XmlPullParser parser) throws IOException, XmlPullParserException {
    parser.require(XmlPullParser.START_TAG, ns, "summary");
    String summary = readText(parser);
    parser.require(XmlPullParser.END_TAG, ns, "summary");
    return summary;
}

// For the tags title and summary, extracts their text values.
private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
    String result = "";
    if (parser.next() == XmlPullParser.TEXT) {
        result = parser.getText();
        parser.nextTag();
    }
    return result;
}
*/
