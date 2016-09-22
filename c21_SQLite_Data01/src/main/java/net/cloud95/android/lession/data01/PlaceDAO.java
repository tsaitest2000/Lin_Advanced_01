package net.cloud95.android.lession.data01;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.Date;

// SQLite的資料格式：文字Text, 數字Integer, 浮點數Real, 二進位Binary. 它不接受日期 故只能用字串來紀錄日期
// ★★ Sqlite3沒有支援所有的SQL語法，請特別留意 ............................................

public class PlaceDAO {

   public static final String TABLE_NAME = "place";

   public static final String KEY_ID = "_id";
   public static final String LATITUDE = "latitude";
   public static final String LONGITUDE = "longitude";
   public static final String ACCURACY = "accuracy";
   public static final String DATETIME = "datetime";
   public static final String NOTE = "note";

   public static final String[] COLUMNS = {KEY_ID, LATITUDE, LONGITUDE, ACCURACY, DATETIME, NOTE};
   public static final String[] COLUMNS_SHOW = {KEY_ID, DATETIME, NOTE};

   public static final String CREATE_TABLE =
      "CREATE TABLE " + TABLE_NAME + " ( " +
         KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + LATITUDE + " REAL NOT NULL, " +
         LONGITUDE + " REAL NOT NULL, " + ACCURACY + " REAL NOT NULL, " +
         DATETIME + " TEXT NOT NULL, " + NOTE + " TEXT NOT NULL)";

   public static final String DROP_TABLE = "drop table if exists " + TABLE_NAME;

   private SQLiteDatabase mDb;

   public PlaceDAO(Context context) {
      mDb = MyDBHelper.getDatabase(context);
   }

   // ★★★ SQLite只針對單一用戶 → 多支程式要處理同個資料庫 → 先連線者取得鎖定 → 使用完關閉讓其它程式連入
   public void closeDatabase() {
      mDb.close();
   }

   public Place insert(Place place) {
      ContentValues cv = new ContentValues();
      cv.put(LATITUDE, place.getLatitude());
      cv.put(LONGITUDE, place.getLongitude());
      cv.put(ACCURACY, place.getAccuracy());
      cv.put(DATETIME, place.getDatetime());
      cv.put(NOTE, place.getNote());
      long id = mDb.insert(TABLE_NAME, null, cv); // 新增一筆資料並取得編號
      place.setId(id); // 設定編號
      return place;
   }

   public boolean delete(long id) {
      String where = KEY_ID + "=" + id;
      return mDb.delete(TABLE_NAME, where, null) > 0;
   }

   public boolean update(Place place) {
      ContentValues cv = new ContentValues();
      cv.put(LATITUDE, place.getLatitude());
      cv.put(LONGITUDE, place.getLongitude());
      cv.put(ACCURACY, place.getAccuracy());
      cv.put(DATETIME, place.getDatetime());
      cv.put(NOTE, place.getNote());
      String where = KEY_ID + "=" + place.getId();
      return mDb.update(TABLE_NAME, cv, where, null) > 0;
   }

   // 取得所有資料的Cursor物件 → 給ListView的資料內容
   public Cursor query_all() {
      Cursor cursorResult = mDb.query(TABLE_NAME, COLUMNS_SHOW, null, null, null, null, null);
      return cursorResult;
   }

   // 取得參數指定日期資料的Cursor物件 ★ date例如：2016-08-31 選定日期後按下確定按鈕時所要執行的程式區塊
   /* ★★ 在SQL中第一個索引值是一，而不是零 ★★ */
   public Cursor query_date(String date) {
      // 設定條件為查詢日期時間欄位的前十個字元(日期部份) 格式為「substr(欄位名稱,開始,個數)='資料'」
      String where = "substr(datetime, 1, 10)='" + date + "'";
      Cursor cursorResult = mDb.query(TABLE_NAME, COLUMNS_SHOW, where, null, null, null, null);
      return cursorResult;
   } /* 參數：selectionArgs是用來支援selection的 如 where name=林俊志,李俊志,王俊志 */

   public Place query_id(long id) { // 取得指定編號的資料物件
      Place place = null;
      String where = KEY_ID + "=" + id;
      Cursor cursorResult = mDb.query(TABLE_NAME, COLUMNS, where, null, null, null, null, null);
      if (cursorResult.moveToFirst()) { // 如果有查詢結果
         place = new Place();
         place.setId(cursorResult.getLong(0));
         place.setLatitude(cursorResult.getDouble(1));
         place.setLongitude(cursorResult.getDouble(2));
         place.setAccuracy(cursorResult.getDouble(3));
         place.setDatetime(cursorResult.getString(4));
         place.setNote(cursorResult.getString(5));
      } /*查詢結束時會是一個類似表格的東西，此時Cursor指在第一筆資料之前 */
      cursorResult.close(); // 關閉Cursor物件，釋放記憶體
      return place;
   }

   // 首次執行應用程式時會新增的範例資料 → 透過SharedPeference物件紀錄是否為首次執行此應用程式
   public void sampleData(Context context) {
      SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
      boolean firstTime = sp.getBoolean("FIRST_TIME", true);
      if (firstTime) {
         Place P01 = new Place(0, 25.047196, 121.516981, 10.0, "2013-12-10 08:30", "happy");
         Place P02 = new Place(0, 24.143033, 121.271982, 25.0, "2014-12-10 06:12", "power");
         Place P03 = new Place(0, 25.200854, 121.646714, 55.0, "2015-12-10 16:50", "Peace");
         PlaceDAO.this.insert(P01);
         PlaceDAO.this.insert(P02);
         PlaceDAO.this.insert(P03);
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
         for (int i = 0; i < 10; i++) { // 新增接下來十天的紀錄(假資料)
            Date date = new Date(System.currentTimeMillis() + (i * 1000 * 60 * 60 * 24));
            Place place = new Place(0, 25.04719 + i, 121.516981 + i, i * 10, sdf.format(date), "Place:" + i);
            insert(place);
         }
         SharedPreferences.Editor editor = sp.edit();
         editor.putBoolean("FIRST_TIME", false);
         editor.commit();
      }
   }

}
