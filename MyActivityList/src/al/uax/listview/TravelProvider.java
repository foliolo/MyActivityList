package al.uax.listview;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

public class TravelProvider extends ContentProvider {
	
	private static final String AUTHORITY = "al.uax.listview";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/travels");
	
	private static final int URI_TRAVELS = 1;
	private static final int URI_TRAVELS_ITEM = 2;
	
	private static final UriMatcher mUriMatcher;
	private static SQLiteOpenHelper mDbHelper = null;
	
	static{
		mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		mUriMatcher.addURI(AUTHORITY, "travels", URI_TRAVELS);
		mUriMatcher.addURI(AUTHORITY, "travels/#", URI_TRAVELS_ITEM);
	}
	
	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		//Abrimos la base de datos para la eliminación
		mDbHelper = new TravelsDatabaseHelper(getContext());
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		
		int result = db.delete(TravelsDatabaseHelper.TABLE_NAME, where, whereArgs);
		
		//Notificamos el cambio
		getContext().getContentResolver().notifyChange(uri, null);
		
		//Cerramos la base de datos
		mDbHelper.close();
		return result;
	}

	@Override
	public String getType(Uri uri) {
		int match = mUriMatcher.match(uri);
		
		switch(match){
			case URI_TRAVELS:
				return "vnd.android.cursor.dir/vnd.al.uax.listview.travels";
				
			case URI_TRAVELS_ITEM:
				return "vnd.android.cursor.item/vnd.al.uax.listview.travels";
			
			default:
				Log.d("TAG", "Uri didn't match: " + uri);
				throw new IllegalArgumentException("Unknown Uri: " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		mDbHelper = new TravelsDatabaseHelper(getContext());
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		
		long id = db.insert(TravelsDatabaseHelper.TABLE_NAME, null, values);
		
		Uri result = null; 
		
		if(id >= 0){
			result = ContentUris.withAppendedId(CONTENT_URI, id);
			//Notificamos el cambio
			getContext().getContentResolver().notifyChange(uri, null);
		}
		
		//Cerramos la base de datos
		mDbHelper.close();
		
		return result;
	}

	@Override
	public boolean onCreate() {
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

		mDbHelper = new TravelsDatabaseHelper(getContext());
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		
		int match = mUriMatcher.match(uri);
		
		SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();
		qBuilder.setTables(TravelsDatabaseHelper.TABLE_NAME);
		
		switch(match){
			case URI_TRAVELS:
				//NADA
				break;
				
			case URI_TRAVELS_ITEM:
				String id = uri.getPathSegments().get(1);
				qBuilder.appendWhere(Travels._ID + "=" + id);
				break;
				
			default:
				Log.d("TAG", "Uri didn't match: " + uri);
		}
		
		Cursor c = qBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where, String[] selectionArgs) {
		int result = 0;
		
		//Abrimos la base de datos para su actualización
		mDbHelper = new TravelsDatabaseHelper(getContext());
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
	
		//Actualizamos la tabla
		result = db.update(TravelsDatabaseHelper.TABLE_NAME, values, where, selectionArgs);
		
		//Notificamos el cambio
		getContext().getContentResolver().notifyChange(uri, null);
		
		//Cerramos la base de datos
		mDbHelper.close();
		
		return result;
	}
	
	
	public class Travels implements BaseColumns{
		public static final String _ID = "_id";
		public static final String CITY = "ciudad";
		public static final String COUNTRY = "pais";
		public static final String YEAR = "anyo";
		public static final String NOTE = "anotacion";
	}
}
