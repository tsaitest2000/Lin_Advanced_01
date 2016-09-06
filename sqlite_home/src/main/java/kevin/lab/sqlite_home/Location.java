package kevin.lab.sqlite_home;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Location {

   private long id;
   private double latitude;
   private double longitude;
   private double accuracy;
   private String dateTime;
   private String note;

   public Location() {

   }

   public Location(long id, double latitude, double longitude, double accuracy, String dateTime, String note) {
      this.id = id;
      this.latitude = latitude;
      this.longitude = longitude;
      this.accuracy = accuracy;
      this.dateTime = dateTime;
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

   public String getDateTime() {
      return dateTime;
   }

   public void setDateTime(String dateTime) {
      this.dateTime = dateTime;
   }

   public String getNote() {
      return note;
   }

   public void setNote(String note) {
      this.note = note;
   }

   public void setDateTime(long time) {
      Date date = new Date(time);
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
      this.setDateTime(simpleDateFormat.format(date));
   }

}
