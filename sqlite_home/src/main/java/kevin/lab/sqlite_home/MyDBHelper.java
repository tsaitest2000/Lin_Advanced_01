package kevin.lab.sqlite_home;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {

   public static final String DATABASE_NAME = "Location.db";
   public static final int DATABASE_VERSION = 1;
   private static SQLiteDatabase database;

   public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
      super(context, name, factory, version);
   }

   public static SQLiteDatabase getDatabase(Context context) {
      if (database == null || !database.isOpen()) {
         database = new MyDBHelper(context, DATABASE_NAME, null, DATABASE_VERSION).getWritableDatabase();
      }
      return database;
   }

   @Override
   public void onCreate(SQLiteDatabase sqLiteDatabase) {
      sqLiteDatabase.execSQL("");
   }

   @Override
   public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
      sqLiteDatabase.execSQL("");
   }

}
