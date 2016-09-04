package net.cloud95.android.lession.data01;

import java.text.SimpleDateFormat;
import java.util.Date;

// DAO設計模式中的"紀錄層"。Place為位置類別，一個Place物件就代表一個位置資料
public class Place {

   private long id;
   private double latitude;
   private double longitude;
   private double accuracy;
   private String datetime;
   private String note;

   public Place() {

   }

   public Place(long id, double latitude, double longitude, double accuracy, String datetime, String note) {
      this.id = id;
      this.latitude = latitude;
      this.longitude = longitude;
      this.accuracy = accuracy;
      this.datetime = datetime;
      this.note = note;
   }

   public long getId() {
      return id;
   }

   public void setId(long id) {
      this.id = id;
   }

   public double getLatitude() {
      return latitude;
   }

   public void setLatitude(double latitude) {
      this.latitude = latitude;
   }

   public double getLongitude() {
      return longitude;
   }

   public void setLongitude(double longitude) {
      this.longitude = longitude;
   }

   public double getAccuracy() {
      return accuracy;
   }

   public void setAccuracy(double accuracy) {
      this.accuracy = accuracy;
   }

   public String getDatetime() {
      return datetime;
   }

   // 額外加入的程式碼 三個 M：月份的縮寫、四個 M：月份的全名。HH：24小時制、hh：12小時制
   public void setDatetime(long now) {
      Date date = new Date(now);
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
      this.datetime = simpleDateFormat.format(date);
   }

   public void setDatetime(String datetime) {
      this.datetime = datetime;
   }

   public String getNote() {
      return note;
   }

   public void setNote(String note) {
      this.note = note;
   }

}
