package net.cloud95.android.lession.data01;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityInsert extends Activity {

   private EditText et_insert_latitude;
   private EditText et_insert_longitude;
   private EditText et_insert_accuracy;
   private EditText et_insert_note;
   private PlaceDAO placeDAO;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_insert);

      buildViews();
      placeDAO = new PlaceDAO(this); // 取得資料庫物件
   }

   private void buildViews() {
      et_insert_latitude = (EditText) findViewById(R.id.latitude_edit);
      et_insert_longitude = (EditText) findViewById(R.id.longitude_edit);
      et_insert_accuracy = (EditText) findViewById(R.id.accuracy_edit);
      et_insert_note = (EditText) findViewById(R.id.note_edit);
   }

   public void clickOk(View view) { // 讀取使用者輸入的資料
      double latitude = Double.parseDouble(et_insert_latitude.getText().toString());
      double longitude = Double.parseDouble(et_insert_longitude.getText().toString());
      double accuracy = Double.parseDouble(et_insert_accuracy.getText().toString());
      String note = et_insert_note.getText().toString();

      Place place = new Place(); // 建立準備新增資料的物件 → Place型別物件沒有id值
      // 把讀取的資料設定給物件
      place.setLatitude(latitude);
      place.setLongitude(longitude);
      place.setAccuracy(accuracy);
      place.setDatetime(System.currentTimeMillis()); // ===
      place.setNote(note);

      place = placeDAO.insert(place); // → Place型別物件已有id值
      Toast.makeText(this, "資料新增成功", Toast.LENGTH_SHORT).show();
      Intent intent = getIntent();
      setResult(Activity.RESULT_OK, intent);
      finish();
   }

   public void clickCancel(View view) {
      finish();
   }

}