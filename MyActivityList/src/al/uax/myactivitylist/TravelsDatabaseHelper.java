package al.uax.myactivitylist;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TravelsDatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "dbName";
	private static final int DATABASE_VERSION = 0;
	private static final String TABLE_NAME = "viajes";

	public TravelsDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE" + TABLE_NAME + "(" +
				"ID INTERGER PRIMARY KEY AUTOINCREMENT," + 
				"CIUDAD TEXT NOT NULL," +
				"PAIS TEXT NOT NULL," + 
				"ANYO INTEGER NOT NULL," +
				"ANOTACION TEXT );"
				);
		initialData(db);
	}
	
	private void initialData(SQLiteDatabase db){
		insertTravel(db, "Londres", "UK", 2012, "¡Juegos Olimpicos!");
		insertTravel(db, "Paris", "Francia", 2007, null);
		insertTravel(db, "Gotham City", "EEUU", 2011, "¡¡Batman!!");
		insertTravel(db, "Hamburgo", "Alemania", 2009, null);
		insertTravel(db, "Pekin", "China", 2011, null);
	}
	
	private void insertTravel(SQLiteDatabase db, String ciudad, String pais, int anyo, String anotacion) {
		ContentValues values = new ContentValues();
		values.put("CIUDAD", ciudad);
		values.put("PAIS", pais);
		values.put("ANYO", anyo);
		values.put("ANOTACION", anotacion);
		
		db.insert(TABLE_NAME, null, values);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(oldVersion < newVersion){
			db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME + ";");
			onCreate(db);
		}
	}
}
