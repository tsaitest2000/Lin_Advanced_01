package net.cloud95.android.lession.data01;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {

   public static final String DATABASE_NAME = "myData.db";
   public static final int DATABASE_VERSION = 1; // 資料庫版本，資料結構改變時更改此數字，通常加一
   private static SQLiteDatabase mDb;


   public MyDBHelper(Context context, String name, CursorFactory factory, int version) {
      super(context, name, factory, version);
   }

   public static SQLiteDatabase getDatabase(Context context) {
      if (mDb == null || !mDb.isOpen()) {
         mDb = new MyDBHelper(context, DATABASE_NAME, null, DATABASE_VERSION).getWritableDatabase();
      }
      return mDb;
   }

   // ★★ onCreate與onUpgrade是兩個獨立的方法，兩者之間並沒有先後的順序問題 ★★
   @Override
   public void onCreate(SQLiteDatabase db) {
      db.execSQL(PlaceDAO.CREATE_TABLE);
   }

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      db.execSQL(PlaceDAO.DROP_TABLE);
      onCreate(db); // 呼叫onCreate建立新版的表格
   }

}

// ★ 繼承SQLiteOpenHelper的MyDBHelper是真正能接觸到SqLite3這支程式者
// ★ 下達SQL指令時是對SqlLte3下達指令 → 由SqLite3對資料庫進行操作
// ★ SqLite是輕量級資料庫，功能只有SQL Server的三分之一，無法支援所有的SQL(92.95)語法
// ★ 建議：CMD視窗直接下達SQL指令，若無法執行者就代表SqLite沒有支援該SQL語法