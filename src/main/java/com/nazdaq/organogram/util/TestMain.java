package com.nazdaq.organogram.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

public class TestMain {
	public static void main(String[] args) throws ParseException {
		// TODO Auto-generated method stub
		/*Date now = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.DAY_OF_MONTH, -7);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date remDate = cal.getTime();
		System.out.println(df.format(remDate));*/
//		Set<String> zones = ZoneId.getAvailableZoneIds();
//		System.out.println("Default Zone:"+ZoneId.systemDefault());
//		System.out.println("Number of available time zones is: " + zones.size());
//		zones.forEach(System.out::println);
		
       /* ZoneId singaporeZone = ZoneId.of("Europe/London");
        ZonedDateTime dateTimeInSingapore = ZonedDateTime.of(
        LocalDateTime.of(2016, Month.JANUARY, 1, 6, 0), singaporeZone);
        ZoneId aucklandZone = ZoneId.of("Asia/Dhaka");
        ZonedDateTime sameDateTimeInAuckland =
                    dateTimeInSingapore.withZoneSameInstant(aucklandZone);
        Duration timeDifference = Duration.between(
                                dateTimeInSingapore.toLocalTime(),
                                sameDateTimeInAuckland.toLocalTime());
        System.out.printf("Time difference between %s and %s zones is %d hours",
                    singaporeZone, aucklandZone, timeDifference.toHours());*/
		
		TestMain tm = new TestMain();
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		Date startDate = df.parse("02-07-1987");
		Date endDate = new Date();
		
		System.out.println(tm.ageCalculator(startDate, endDate));
		
	}
	
	
	public String ageCalculator(Date startDate, Date endDate){
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);

          int monthsBetween = 0;
            int dateDiff = end.get(Calendar.DAY_OF_MONTH)-start.get(Calendar.DAY_OF_MONTH);      
            if(dateDiff<0) {
                int borrrow = end.getActualMaximum(Calendar.DAY_OF_MONTH);           
                 dateDiff = (end.get(Calendar.DAY_OF_MONTH)+borrrow)-start.get(Calendar.DAY_OF_MONTH);
                 monthsBetween--;
                 if(dateDiff>0) {
                     monthsBetween++;
                 }
            }
            else {
                monthsBetween++;
            }      
            monthsBetween += end.get(Calendar.MONTH)-start.get(Calendar.MONTH);      
            monthsBetween  += (end.get(Calendar.YEAR)-start.get(Calendar.YEAR))*12;      
            int res =  monthsBetween-1;
            
            
    		return res/12+" year " + res%12 + " month";
     }
	
	
	
	

}
