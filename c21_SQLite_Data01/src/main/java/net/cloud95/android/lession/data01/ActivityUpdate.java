package net.cloud95.android.lession.data01;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityUpdate extends Activity {

   private EditText et_update_id;
   private EditText et_update_latitude;
   private EditText et_update_longitude;
   private EditText et_update_accuracy;
   private EditText et_update_datetime;
   private EditText et_update_note;

   private Place place;
   private PlaceDAO placeDAO;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_update);

      placeDAO = new PlaceDAO(this);

      Intent intent = getIntent();
      long id = intent.getLongExtra("id", -1); // 讀取修改資料的編號
      place = placeDAO.query_id(id); // 取得指定編號的物件
      buildViews();
   }

   private void buildViews() {
      et_update_id = (EditText) findViewById(R.id.et_update_id);
      et_update_latitude = (EditText) findViewById(R.id.et_update_latitude);
      et_update_longitude = (EditText) findViewById(R.id.et_update_longitude);
      et_update_accuracy = (EditText) findViewById(R.id.et_update_accuracy);
      et_update_datetime = (EditText) findViewById(R.id.et_update_datetime);
      et_update_note = (EditText) findViewById(R.id.et_update_note);

      et_update_id.setText(Long.toString(place.getId()));
      et_update_latitude.setText(Double.toString(place.getLatitude()));
      et_update_longitude.setText(Double.toString(place.getLongitude()));
      et_update_accuracy.setText(Double.toString(place.getAccuracy()));
      et_update_datetime.setText(place.getDatetime());
      et_update_note.setText(place.getNote());
   }

   public void onClick(View view) {
      switch (view.getId()) {
         case R.id.btn_update_cancel:
            finish();
            break;
         case R.id.btn_update_ok:
            place.setLatitude(Double.parseDouble(et_update_latitude.getText().toString()));
            place.setLongitude(Double.parseDouble(et_update_longitude.getText().toString()));
            place.setAccuracy(Double.parseDouble(et_update_accuracy.getText().toString()));
            place.setDatetime(et_update_datetime.getText().toString());
            place.setNote(et_update_note.getText().toString());

            placeDAO.update(place);
            Toast.makeText(this, "資料更新完成", Toast.LENGTH_SHORT).show();
            Intent intent = getIntent();
            setResult(Activity.RESULT_OK, intent);
            finish();
            break;
      }
   }

}
// 介面佈局檔中的Focusable="false"：無法取得滑鼠聚焦也無法編輯; Editable：可以取得滑鼠聚焦，但無法編輯
