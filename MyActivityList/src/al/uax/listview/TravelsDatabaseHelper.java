package al.uax.listview;


import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TravelsDatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "dbName";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_NAME = "viajes";

	public TravelsDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_NAME + "(" +
				"ID INTEGER PRIMARY KEY AUTOINCREMENT," + 
				"CIUDAD TEXT NOT NULL," +
				"PAIS TEXT NOT NULL," + 
				"ANYO INTEGER NOT NULL," +
				"ANOTACION TEXT );"
				);
		//initialData(db);
	}
/*
	private void initialData(SQLiteDatabase db){
		insertTravel(db, "Londres", "UK", 2012, "¡Juegos Olimpicos!");
		insertTravel(db, "Paris", "Francia", 2007, null);
		insertTravel(db, "Gotham City", "EEUU", 2011, "¡¡Batman!!");
		insertTravel(db, "Hamburgo", "Alemania", 2009, null);
		insertTravel(db, "Pekin", "China", 2011, "");
	}
*/	
	public void insertTravel(SQLiteDatabase db, String ciudad, String pais, int anyo, String anotacion) {
		ContentValues values = new ContentValues();
		values.put("CIUDAD", ciudad);
		values.put("PAIS", pais);
		values.put("ANYO", anyo);
		values.put("ANOTACION", anotacion);
		
		db.insert(TABLE_NAME, null, values);
	}

	public void updateTravel(SQLiteDatabase db, String ciudad, String pais, int anyo, String anotacion, int id){
		ContentValues values = new ContentValues();
		values.put("CIUDAD", ciudad);
		values.put("PAIS", pais);
		values.put("ANYO", anyo);
		values.put("ANOTACION", anotacion);
		
		db.update(TABLE_NAME, values, "ID=" + id, null);
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
			int ciudadIndex = c.getColumnIndex("CIUDAD");
			int paisIndex = c.getColumnIndex("PAIS");
			int anyoIndex = c.getColumnIndex("ANYO");
			int anotacionIndex = c.getColumnIndex("ANOTACION");
			
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
		if(db.delete(TABLE_NAME, "ID=" + id, null) >= 0)
			Log.d("TAG", "Borrado OK");
		else
			Log.d("TAG", "Error en el Borrado");
	}
}
