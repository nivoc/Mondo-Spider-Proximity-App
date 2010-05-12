package com.mondospider.android.lib;

import java.util.Date;
import java.sql.*;

public class ReadableDates {
	Date dtNow = new Date();
	double dbl=0.0;
	String value="";
	public String ReadbleDate(Date dt)
	{
		Timestamp tsPosted = new Timestamp(dt.getTime());
		Timestamp tsNow = new Timestamp(dtNow.getTime());
		
		long lg_Seconds =  (tsNow.getTime() - tsPosted.getTime())/1000;
	
		value=TimeString(lg_Seconds);
		return value;
	}
	private String TimeString(long lg_Seconds)
	{
		if(lg_Seconds<=59)//For 1st min.
		{
			value="Right now";
		}
		else if(lg_Seconds>=60 && lg_Seconds<=3599)//For 1min to 59 mins
		{
			if((lg_Seconds/60)<=1)
			{
				value ="1 minute ago";
			}
			else
			{
				dbl = lg_Seconds/60;
				value=(int)(dbl+1)+" minutes ago";
			}
		}
		else if(lg_Seconds >= 3600 && lg_Seconds <=86399) //1 hour to 23 hours
		{
			if((lg_Seconds/3600)<=1)
			{
				value ="1 hour ago";
			}
			else
			{
				dbl = lg_Seconds/3600;
				value=(int)(dbl+1)+" hours ago";
			}
		}
		else if(lg_Seconds >= 86400 && lg_Seconds <= 604799)//1 day to 6 days
		{
			if((lg_Seconds/86400)<=1)
			{
				value ="1 day ago";
			}
			else
			{
				dbl = lg_Seconds/86400;
				value=(int)(dbl+1)+" days ago";
			}
		}
		else if(lg_Seconds >= 604800 && lg_Seconds <= 2419199)// 1 week to 3 weeks
		{
			if((lg_Seconds/604800)<=1)
			{
				value ="1 week ago";
			}
			else
			{
				dbl = lg_Seconds/604800;
				value=(int)(dbl+1)+" weeks ago";
			}
		}
		else if(lg_Seconds >= 2419200 && lg_Seconds <= 31579200)// 1 month to 1 year
		{
			if((lg_Seconds/2419200)<=1)
			{
				value ="1 month ago";
			}
			else
			{
				dbl = lg_Seconds/2419200;
				value=(int)(dbl+1)+" months ago";
			}
		}
		else if(lg_Seconds >= 31579200)//1 year to many years
		{
			if((lg_Seconds/31579200)<=1)
			{
				value ="1 year ago";
			}
			else
			{
				dbl = lg_Seconds/31579200;
				value=(int)(dbl+1)+" years ago";
			}
		}
		return value;
    }
}
