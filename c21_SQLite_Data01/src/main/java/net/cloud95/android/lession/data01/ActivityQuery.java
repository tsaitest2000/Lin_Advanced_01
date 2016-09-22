package net.cloud95.android.lession.data01;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ActivityQuery extends Activity {

   private EditText edit_query_date;
   private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_query);

      buildViews();
   }

   private void buildViews() {
      edit_query_date = (EditText) findViewById(R.id.edit_query_date);

      Date date = new Date(); // 設定為今天的日期
      edit_query_date.setText(simpleDateFormat.format(date));
   }

   // edit_query_date元件被按下時執行的程式區塊
   public void clickDateSearch(View view) {
      try {
         String dateValue = edit_query_date.getText().toString();
         Date date = simpleDateFormat.parse(dateValue);
         Calendar calendar = Calendar.getInstance();
         calendar.setTime(date);

         DatePickerDialog dialog = new DatePickerDialog(
            this,
            new OnDateSetListener() {
               @Override
               public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                  Calendar cdr = Calendar.getInstance();
                  cdr.set(year, monthOfYear, dayOfMonth);
                  edit_query_date.setText(simpleDateFormat.format(cdr.getTime()));
               }
            },
            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
         );
         dialog.show();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void onClick(View view) {
      switch (view.getId()) {
         case R.id.btn_query_cancel:
            finish();
            break;
         case R.id.btn_query_ok:
            String dateValue = edit_query_date.getText().toString();
            Intent intent = getIntent();
            intent.putExtra("dateValue", dateValue); // 加入設定的日期資料
            setResult(Activity.RESULT_OK, intent);
            finish();
            break;
      }
   }

}