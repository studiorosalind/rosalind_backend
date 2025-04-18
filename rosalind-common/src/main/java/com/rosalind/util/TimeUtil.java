package com.rosalind.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public final class TimeUtil {

  private final static TimeZone timeZoneUtc = TimeZone.getTimeZone("UTC");
  private final static TimeZone timeZoneSeoul = TimeZone.getTimeZone("Asia/Seoul");

  public static long getCurrentTimeMillisUtc() {
    Calendar calendar = Calendar.getInstance(timeZoneUtc);
    long currentTimeMillisUtc = System.currentTimeMillis();

    calendar.setTimeInMillis(currentTimeMillisUtc);
    return calendar.getTimeInMillis();
  }

  public static long convertToEpochTime(String dobString) {
    // Parse the input string using the specified format (ddMMyy)
    SimpleDateFormat format = new SimpleDateFormat("yyMMdd");
    Date dob = null;
    try {
      dob = format.parse(dobString);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }

    // Set the time zone to UTC
    format.setTimeZone(TimeZone.getTimeZone("UTC"));

    // Convert the date to UTC-0 epoch time in milliseconds
    return dob.getTime();
  }

  public static String convertEpochToDateString(long epochMillis) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    sdf.setTimeZone(timeZoneSeoul);
    return sdf.format(new Date(epochMillis));
  }

  public static int calculateAge(long dobEpochMillis) {
    Instant dobInstant = Instant.ofEpochMilli(dobEpochMillis);
    LocalDate dobDate = dobInstant.atZone(ZoneId.systemDefault()).toLocalDate();
    LocalDate currentDate = LocalDate.now();

    int age = currentDate.getYear() - dobDate.getYear();

    if (currentDate.getMonthValue() < dobDate.getMonthValue()
      || (currentDate.getMonthValue() == dobDate.getMonthValue() && currentDate.getDayOfMonth() < dobDate.getDayOfMonth())) {
      age--;
    }

    return age;
  }

  public static Date toStartOfDaySeoul(Date date) {
    Calendar calendar = Calendar.getInstance(timeZoneSeoul);
    calendar.setTime(date);
    calendar.set(Calendar.HOUR_OF_DAY,0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar.getTime();
  }

  public static Date toStartOfNextDaySeoul(Date date) {
    Calendar calendar = Calendar.getInstance(timeZoneSeoul);
    calendar.setTime(date);
    calendar.add(Calendar.DATE, 1);
    calendar.set(Calendar.HOUR_OF_DAY,0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    return calendar.getTime();
  }

}