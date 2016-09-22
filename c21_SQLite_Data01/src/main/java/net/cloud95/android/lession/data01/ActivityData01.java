package net.cloud95.android.lession.data01;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Toast;

//  撰寫順序： 1.Place 2.MyDBHelper 3.PlaceDAO 4.ActivityInsert 5.ActivityUpdate 6.ActivityQuery 7.ActivityMain
public class ActivityData01 extends Activity {

   // ListView顯示資料用的畫面元件陣列
   private static final int[] IDS = {R.id.lv_place_id, R.id.lv_place_date_time, R.id.lv_place_note}; // SimpleCursorAdapter之用
   private static final int REQUEST_CODE_INSERT = 0;
   private static final int REQUEST_CODE_UPDATE = 1;
   private static final int REQUEST_CODE_QUERY = 2;

   private ListView lv_activity_data01;
   private PlaceDAO placeDAO;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_data01);

      lv_activity_data01 = (ListView) findViewById(R.id.lv_activity_data01);
      this.registerForContextMenu(lv_activity_data01); // 按下item才會跳出活動的畫面
      lv_activity_data01.setOnItemClickListener(new LvOnClickLnr());

      placeDAO = new PlaceDAO(this);
      placeDAO.sampleData(this);
      ActivityData01.this.refresh(); // 讀取與顯示資料
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) { // 重新整理、加入資料、搜尋 三顆按鈕
      MenuInflater menuInflater = this.getMenuInflater();
      menuInflater.inflate(R.menu.menu_data01, menu); // 呼叫inflate方法載入指定的選單資源，參數二是此方法的Menu物件
      return true; // 回傳true選單才會顯示
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
         case R.id.menu_refresh: // 讀取全部資料與顯示
            refresh();
            break;
         case R.id.menu_insert:
            Intent intentInsert = new Intent(this, ActivityInsert.class);
            this.startActivityForResult(intentInsert, REQUEST_CODE_INSERT);
            break;
         case R.id.menu_query:
            Intent intentQuery = new Intent(this, ActivityQuery.class);
            this.startActivityForResult(intentQuery, REQUEST_CODE_QUERY);
            break;
      }
      return super.onOptionsItemSelected(item);
   }

   private void refresh() { // 重新讀取與顯示 → 查詢全部資料
      Cursor cursor = placeDAO.query_all();
      SimpleCursorAdapter sca = new SimpleCursorAdapter(
         this, R.layout.listview_place, cursor, PlaceDAO.COLUMNS_SHOW, IDS, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
      lv_activity_data01.setAdapter(sca);
   }

   @Override
   public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
      // 取得 "讀取選項資訊的AdapterContextMenuInfo" 物件
      AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
      Place placePicked = placeDAO.query_id(info.id); // 使用AdapterContextMenuInfo物件的編號取得資料
      menu.setHeaderTitle(placePicked.getNote()); // 設定選單標題
      if (view == lv_activity_data01) { // 若為ListView元件
         MenuInflater menuInflater = getMenuInflater();
         menuInflater.inflate(R.menu.menu_data01_context, menu);
      } /* ★★ 第二層選單透過"AdapterContextMenuInfo"得知第一層選單被選擇的item的id值 ★★ */
   }

   @Override
   public boolean onContextItemSelected(MenuItem item) {
      AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
      final Place placePicked = placeDAO.query_id(info.id);
      switch (item.getItemId()) {
         case R.id.menu_update:
            Intent intentUpdate = new Intent(this, ActivityUpdate.class);
            intentUpdate.putExtra("id", placePicked.getId()); // 設定資料編號
            startActivityForResult(intentUpdate, REQUEST_CODE_UPDATE);
            break;
         case R.id.menu_delete:
            new AlertDialog.Builder(this)
               .setTitle("確認要刪除?")
               .setMessage("刪除 " + placePicked.getNote() + " 嗎 ?")
               .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogInterface, int i) {
                     placeDAO.delete(placePicked.getId());
                     refresh();
                  }
               })
               .setNegativeButton(getString(android.R.string.cancel), null)
               .show();
            break;
      }
      return true;
   }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (resultCode == Activity.RESULT_OK) {
         switch (requestCode) {
            case REQUEST_CODE_QUERY:
               String dateValue = data.getStringExtra("dateValue");  // 取得搜尋日期
               Cursor cursor = placeDAO.query_date(dateValue);
               SimpleCursorAdapter sca = new SimpleCursorAdapter(
                  this, R.layout.listview_place, cursor, PlaceDAO.COLUMNS_SHOW, IDS, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
               ); // ★★ 參數六：CursorAdapter觀察資料庫動態 若有變動就跟著變動 ★★
               lv_activity_data01.setAdapter(sca);
               break;
            case REQUEST_CODE_INSERT:
            case REQUEST_CODE_UPDATE:
               refresh();
               break;
         }
      }
   }

   private class LvOnClickLnr implements OnItemClickListener {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
         Place place = placeDAO.query_id(id);
         Toast.makeText(ActivityData01.this, place.getNote(), Toast.LENGTH_SHORT).show();
      }
   } /* ★ 我：position是位於LiveView物件中的位置值，id是其身分證 (position會變, id不會變) */

   @Override
   protected void onDestroy() {
      placeDAO.closeDatabase();
      super.onDestroy();
   }

}
