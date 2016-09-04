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

//  撰寫順序： 1.Place 2.MyDBHelper 3.PlaceDAO 4.ActivityInsert 5.ActivityQuery 6.ActivityUpdate 7.ActivityData01
public class ActivityData01 extends Activity {

   // ListView顯示資料用的畫面元件陣列
   private static final int[] IDS = {R.id.id_listview, R.id.datetime_listview, R.id.note_listview};
   private static final int REQUEST_CODE_INSERT = 0;
   private static final int REQUEST_CODE_UPDATE = 1;
   private static final int REQUEST_CODE_QUERY = 2;

   private ListView list_view;
   private PlaceDAO placeDAO;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_data01);

      list_view = (ListView) findViewById(R.id.list_view);
      registerForContextMenu(list_view); // 為ListView物件註冊Context Menu
      placeDAO = new PlaceDAO(this); // 建立資料存取物件
      placeDAO.sampleData(this); // 處理範例資料
      this.refresh(); // 讀取與顯示資料

      list_view.setOnItemClickListener(new LvOnClickLnr());
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      MenuInflater menuInflater = getMenuInflater();
      menuInflater.inflate(R.menu.menu_data01, menu); // 呼叫inflate方法載入指定的選單資源，參數二是此方法的Menu物件
      return true; // 回傳true選單才會顯示
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      int id = item.getItemId();
      switch (id) {
         case R.id.menu_refresh: // 讀取全部資料與顯示
            refresh();
            break;
         case R.id.menu_insert:
            Intent intentInsert = new Intent(this, ActivityInsert.class);
            startActivityForResult(intentInsert, REQUEST_CODE_INSERT);
            break;
         case R.id.menu_query:
            Intent intentSearch = new Intent(this, ActivityQuery.class);
            startActivityForResult(intentSearch, REQUEST_CODE_QUERY);
            break;
      }
      return super.onOptionsItemSelected(item);
   }

   @Override
   public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
      // 取得 "讀取選項資訊的AdapterContextMenuInfo" 物件
      AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
      Place selected = placeDAO.get(info.id); // 使用AdapterContextMenuInfo物件的編號取得資料
      menu.setHeaderTitle(selected.getNote()); // 設定選單標題
      if (view == list_view) { // 如果是ListView元件
         MenuInflater menuInflater = getMenuInflater(); // 取得載入選單用的MenuInflater物件
         menuInflater.inflate(R.menu.menu_data01_context, menu);// 呼叫inflate方法載入指定的選單資源，參數二是此方法的Menu物件
      } /* ★★ 第二層選單透過"AdapterContextMenuInfo"得知第一層選單被選擇的item的id值 ★★ */
   }

   @Override
   public boolean onContextItemSelected(MenuItem item) {
      AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
      final Place selected = placeDAO.get(info.id);
      int id = item.getItemId();
      switch (id) {
         case R.id.menu_update:
            Intent intentUpdate = new Intent(this, ActivityUpdate.class);
            intentUpdate.putExtra("id", selected.getId()); // 設定資料編號
            startActivityForResult(intentUpdate, REQUEST_CODE_UPDATE);
            break;
         case R.id.menu_delete:
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Delete?").setMessage("Delete " + selected.getNote() + "?");
            dialog.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialogInterface, int i) {
                  placeDAO.delete(selected.getId());
                  refresh();
               }
            });
            dialog.setNegativeButton(getString(android.R.string.cancel), null);
            dialog.show();
            break;
      }
      return true;
   }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (resultCode == Activity.RESULT_OK) {
         if (requestCode == REQUEST_CODE_QUERY) {
            String dateValue = data.getStringExtra("dateValue");  // 取得搜尋日期
            Cursor cursor = placeDAO.getCursor_query(dateValue);  // 查詢指定的日期
            // ★★★ 參數六：命令CursorAdapter觀察資料庫動態 它若有變動就要跟著變動
            SimpleCursorAdapter sca = new SimpleCursorAdapter(
               this, R.layout.listview_place, cursor,
               PlaceDAO.COLUMNS_SHOW, IDS, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
            );
            list_view.setAdapter(sca);
         } else { //REQUEST_CODE_INSERT 和 REQUEST_CODE_UPDATE
            refresh();
         }
      }
   }

   // 重新讀取與顯示 → 查詢全部資料
   private void refresh() {
      Cursor cursor = placeDAO.getCursor_All();
      SimpleCursorAdapter sca = new SimpleCursorAdapter(
         this, R.layout.listview_place, cursor,
         PlaceDAO.COLUMNS_SHOW, IDS, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
      list_view.setAdapter(sca);
   }

   private class LvOnClickLnr implements OnItemClickListener {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
         Place place = placeDAO.get(id);
         Toast.makeText(ActivityData01.this, place.getNote(), Toast.LENGTH_SHORT).show();
      }
   } /* ★ 我：position是位於LiveView物件中的位置值，id是其身分證 (position會變, id不會變) */

   @Override
   protected void onDestroy() {
      placeDAO.closeDatabase();
      super.onDestroy();
   }

}
