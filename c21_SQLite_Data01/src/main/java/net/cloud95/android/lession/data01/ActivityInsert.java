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
      placeDAO = new PlaceDAO(this);
   }

   private void buildViews() {
      et_insert_latitude = (EditText) findViewById(R.id.et_insert_latitude);
      et_insert_longitude = (EditText) findViewById(R.id.et_insert_longitude);
      et_insert_accuracy = (EditText) findViewById(R.id.et_insert_accuracy);
      et_insert_note = (EditText) findViewById(R.id.et_insert_note);
   }

   public void onClick(View view) {
      switch (view.getId()) {
         case R.id.btn_insert_cancel:
            finish();
            break;
         case R.id.btn_insert_ok:
            Place place = new Place(); // 建立準備新增資料的物件 → Place型別物件沒有id值
            place.setLatitude(Double.parseDouble(et_insert_latitude.getText().toString()));
            place.setLongitude(Double.parseDouble(et_insert_longitude.getText().toString()));
            place.setAccuracy(Double.parseDouble(et_insert_accuracy.getText().toString()));
            place.setDatetime(System.currentTimeMillis());
            place.setNote(et_insert_note.getText().toString());

            place = placeDAO.insert(place); // → Place型別物件已有id值
            Toast.makeText(this, "資料新增成功", Toast.LENGTH_SHORT).show();
            Intent intent = getIntent();
            setResult(Activity.RESULT_OK, intent);
            finish();
            break;
      }
   }

}
