package net.cloud95.android.lession.data01;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.Date;

// 資料功能類別
// SQLite的資料格式：文字Text, 數字Integer, 浮點數Real, 二進位Binary. 它不接受日期 故只能用字串來紀錄日期
// ★★ Sqlite3沒有支援所有的SQL語法，請特別留意 ............................................
public class PlaceDAO {

   public static final String TABLE_NAME = "place";

   public static final String KEY_ID = "_id"; // 編號表格欄位名稱，固定不變
   public static final String COL_LATITUDE = "latitude";
   public static final String COL_LONGITUDE = "longitude";
   public static final String COL_ACCURACY = "accuracy";
   public static final String COL_DATETIME = "datetime";
   public static final String COL_NOTE = "note";

   // 所有欄位名稱陣列，把所有表格欄位名稱變數湊起來建立一個字串陣列
   public static final String[] COLUMNS = {KEY_ID, COL_LATITUDE, COL_LONGITUDE, COL_ACCURACY, COL_DATETIME, COL_NOTE};
   // 顯示用欄位名稱陣列，在資料查詢畫面上希望顯示位置表格的編號、日期時間和說明
   public static final String[] COLUMNS_SHOW = {KEY_ID, COL_DATETIME, COL_NOTE};

   public static final String CREATE_TABLE = // 使用上面宣告的變數建立表格的SQL指令
      "CREATE TABLE " + TABLE_NAME + " ( " +
         KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_LATITUDE + " REAL NOT NULL, " +
         COL_LONGITUDE + " REAL NOT NULL, " + COL_ACCURACY + " REAL NOT NULL, " +
         COL_DATETIME + " TEXT NOT NULL, " + COL_NOTE + " TEXT NOT NULL)";

   private SQLiteDatabase database;

   public PlaceDAO(Context context) { // 建構子，一般的應用都不需要修改
      database = MyDBHelper.getDatabase(context);
   }

   // ★★★ SQLite只針對單一用戶 → 多支程式要處理同個資料庫 → 先連線者取得鎖定 → 使用完關閉讓其它程式連入
   public void closeDatabase() {
      database.close();
   }

   public Place insert(Place place) { // 新增參數指定的物件 → 建立準備新增資料的ContentValues物件
      ContentValues cv = new ContentValues(); // ContentValues物件包裝新增資料 (欄位名稱, 欄位資料)
      cv.put(COL_LATITUDE, place.getLatitude());
      cv.put(COL_LONGITUDE, place.getLongitude());
      cv.put(COL_ACCURACY, place.getAccuracy());
      cv.put(COL_DATETIME, place.getDatetime());
      cv.put(COL_NOTE, place.getNote());
      long id = database.insert(TABLE_NAME, null, cv); // 新增一筆資料並取得編號 (表格名, 欄位預設值, ContentValues物件)
      place.setId(id); // 設定編號
      return place;
   }

   public boolean update(Place place) { // 修改參數指定的物件 → 建立準備修改資料的ContentValues物件
      ContentValues cv = new ContentValues(); // ContentValues物件包裝修改資料，參數一:欄位名稱 參數二:欄位資料
      cv.put(COL_LATITUDE, place.getLatitude());
      cv.put(COL_LONGITUDE, place.getLongitude());
      cv.put(COL_ACCURACY, place.getAccuracy());
      cv.put(COL_DATETIME, place.getDatetime());
      cv.put(COL_NOTE, place.getNote());
      String where = KEY_ID + "=" + place.getId(); // 設定修改資料的條件為編號，格式為「欄位名稱＝資料」
      return database.update(TABLE_NAME, cv, where, null) > 0; // 執行修改資料並回傳修改的資料數量是否成功
   }

   public boolean delete(long id) { // 刪除參數指定編號的資料
      String where = KEY_ID + "=" + id; // 設定條件為編號，格式為「欄位名稱=資料」
      return database.delete(TABLE_NAME, where, null) > 0; // 刪除指定編號資料並回傳刪除是否成功
   }

   // 取得所有資料的Cursor物件 → 給ListView的資料內容。按下畫面中"Search"圖示時所要執行的程式區塊
   public Cursor getCursor_All() {
      Cursor result = database.query(TABLE_NAME, COLUMNS_SHOW, null, null, null, null, null);
      return result;
   }

   // 取得參數指定日期資料的Cursor物件 ★ date例如：2016-08-31 選定日期後按下確定按鈕時所要執行的程式區塊
   /* ★★ 在SQL中第一個索引值是一，而不是零 ★★ */
   public Cursor getCursor_query(String date) {
      // 設定條件為查詢日期時間欄位的前十個字元(日期部份) 格式為「substr(欄位名稱,開始,個數)='資料'」
      String where = "substr(datetime, 1, 10)='" + date + "'";
      Cursor result = database.query(TABLE_NAME, COLUMNS_SHOW, where, null, null, null, null); // 查詢指定日期條件的資料
      return result;
   } /* 參數：selectionArgs是用來支援selection的 如 where name=林俊志,李俊志,王俊志 */

   public Place get(long id) { // 取得指定編號的資料物件
      Place place = null;
      String where = KEY_ID + "=" + id; // 使用編號為查詢條件
      Cursor resultCursor = database.query(TABLE_NAME, COLUMNS, where, null, null, null, null, null);
      if (resultCursor.moveToFirst()) { // 如果有查詢結果
         place = getRecord(resultCursor); // 讀取包裝一筆資料的物件
      } /*查詢結束時會是一個類似表格的東西，此時Cursor是指在第一筆資料之前 */
      resultCursor.close(); // 關閉Cursor物件，釋放記憶體
      return place;
   }

   // 把Cursor目前的資料包裝為物件
   public Place getRecord(Cursor cursor) {
      // 準備回傳結果用的物件
      Place result = new Place();
      result.setId(cursor.getLong(0));
      result.setLatitude(cursor.getDouble(1));
      result.setLongitude(cursor.getDouble(2));
      result.setAccuracy(cursor.getDouble(3));
      result.setDatetime(cursor.getString(4));
      result.setNote(cursor.getString(5));
      return result;
   }

   // 首次執行應用程式時會新增的範例資料 → 透過SharedPeference物件紀錄是否為首次執行此應用程式
   public void sampleData(Context context) {
      SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
      boolean firstTime = sp.getBoolean("FIRST_TIME", true);
      if (firstTime) {
         Place P01 = new Place(0, 25.04719, 121.516981, 10.0, "2011-12-31 08:30", "Hello!");
         Place P02 = new Place(0, 24.143033, 121.271982, 25.0, "2011-01-01 06:12", "Hi!");
         Place P03 = new Place(0, 25.200854, 121.646714, 55.0, "2012-02-12 16:50", "Awesome!");
         this.insert(P01);
         this.insert(P02);
         this.insert(P03);
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
