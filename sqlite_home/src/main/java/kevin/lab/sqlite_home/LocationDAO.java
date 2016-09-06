package kevin.lab.sqlite_home;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class LocationDAO {

   public static final String KEY_ID = "id";
   public static final String TABLE_NAME = "location";
   public static final String COL_LATITUDE = "latitude";
   public static final String COL_LONGITUDE = "longitude";
   public static final String COL_ACCURACY = "accuracy";
   public static final String COL_DATETIME = "dateTime";
   public static final String COL_NOTE = "note";

   public static final String[] COLUMNS = {KEY_ID, COL_LATITUDE, COL_LONGITUDE, COL_ACCURACY, COL_DATETIME, COL_NOTE};
   public static final String[] COLUMNS_SHOW = {KEY_ID, COL_DATETIME, COL_NOTE};

   public static final String ORDER_CREATE_TABLE =
      "create table if not exists " + TABLE_NAME + " ( " +
         KEY_ID + " integer primary key autoincrement, " +
         COL_LATITUDE + " real not null, " +
         COL_LONGITUDE + " real not null, " +
         COL_ACCURACY + " real not null, " +
         COL_DATETIME + " real not null, " +
         COL_NOTE + " real not null )";

   public static final String ORDER_DROP_TALBE =
      "drop table " + TABLE_NAME + " purge";

   private SQLiteDatabase database;

   public LocationDAO(Context context) {
      database = MyDBHelper.getDatabase(context);
   }

   public void closeDatabase(){
      database.close();
   }

   public Location insert(Location location) {
      ContentValues cv = new ContentValues();
      cv.put(COL_LATITUDE, location.getLatitude());
      cv.put(COL_LONGITUDE, location.getLongitude());
      cv.put(COL_ACCURACY, location.getAccuracy());
      cv.put(COL_DATETIME, location.getDateTime());
      cv.put(COL_NOTE, location.getNote());
      long id = database.insert(TABLE_NAME, null, cv);
      location.setId(id);
      return location;
   }



}
