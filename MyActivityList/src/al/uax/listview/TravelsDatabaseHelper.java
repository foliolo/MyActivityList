package al.uax.listview;


import java.util.ArrayList;

import al.uax.listview.TravelProvider.Travels;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TravelsDatabaseHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "dbName";
	private static final int DATABASE_VERSION = 1;
	public static final String TABLE_NAME = "viajes";

	public TravelsDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_NAME + "(" +
				Travels._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + 
				Travels.CITY + " TEXT NOT NULL," +
				Travels.COUNTRY + " TEXT NOT NULL," + 
				Travels.YEAR + " INTEGER NOT NULL," +
				Travels.NOTE + " TEXT );"
				);
		initialData(db);
	}

	private void initialData(SQLiteDatabase db){
		insertTravel(db, "Londres", "UK", 2012, "¡Juegos Olimpicos!");
		insertTravel(db, "Paris", "Francia", 2007, null);
		insertTravel(db, "Gotham City", "EEUU", 2011, "¡¡Batman!!");
		insertTravel(db, "Hamburgo", "Alemania", 2009, null);
		insertTravel(db, "Pekin", "China", 2011, "");
	}
	
	public void insertTravel(SQLiteDatabase db, String ciudad, String pais, int anyo, String anotacion) {
		ContentValues values = new ContentValues();
		values.put(Travels.CITY, ciudad);
		values.put(Travels.COUNTRY, pais);
		values.put(Travels.YEAR, anyo);
		values.put(Travels.NOTE, anotacion);
		
		db.insert(TABLE_NAME, null, values);
	}

	public void updateTravel(SQLiteDatabase db, String ciudad, String pais, int anyo, String anotacion, int id){
		ContentValues values = new ContentValues();
		values.put(Travels.CITY, ciudad);
		values.put(Travels.COUNTRY, pais);
		values.put(Travels.YEAR, anyo);
		values.put(Travels.NOTE, anotacion);
		
		db.update(TABLE_NAME, values, Travels._ID +"="+ id, null);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(oldVersion < newVersion){
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
			onCreate(db);
		}
	}
	
	public ArrayList<TravelInfo> getTravelsList(){
		ArrayList<TravelInfo> travels = new ArrayList<TravelInfo>();
		
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
		
		if(c.moveToFirst()){
			int ciudadIndex = c.getColumnIndex(Travels.CITY);
			int paisIndex = c.getColumnIndex(Travels.COUNTRY);
			int anyoIndex = c.getColumnIndex(Travels.YEAR);
			int anotacionIndex = c.getColumnIndex(Travels.NOTE);
			
			do{
				String ciudad = c.getString(ciudadIndex);
				String pais = c.getString(paisIndex);
				int anyo = c.getInt(anyoIndex);
				String anotacion = c.getString(anotacionIndex);
				
				TravelInfo travel = new TravelInfo(ciudad, pais, anyo, anotacion);
				travels.add(travel);
				
			}while(c.moveToNext());
		}
		
		c.close();
		return travels;
	}
	
	public void remove(SQLiteDatabase db, int id) {
		if(db.delete(TABLE_NAME, Travels._ID +"="+ id, null) >= 0)
			Log.d("TAG", "Borrado OK");
		else
			Log.d("TAG", "Error en el Borrado");
	}
}
