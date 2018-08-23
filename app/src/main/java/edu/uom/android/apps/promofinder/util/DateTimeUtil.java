package edu.uom.android.apps.promofinder.util;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TimePicker;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import edu.uom.android.apps.promofinder.Constants;
import timber.log.Timber;

public class DateTimeUtil {

    public static String getISODateToJustdate(DateTime dateTime) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.JUST_DATE_FORMAT);
        return simpleDateFormat.format(dateTime.toDate());
    }

    public static String getISODateFromTime(String dateTime) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.ISO_DATE_TIME_TIMEZONE_FORMAT);

        final Calendar currentDate = Calendar.getInstance();

        currentDate.set(Calendar.HOUR_OF_DAY, Integer.parseInt(dateTime.split(":")[0]));
        currentDate.set(Calendar.MINUTE, Integer.parseInt(dateTime.split(":")[1]));

        return simpleDateFormat.format(currentDate.getTime());
    }

    public static String getTimeFromISODate(String dateTime) {
        DateTime dateTime1 = new DateTime(dateTime);
        SimpleDateFormat timeFormat = new SimpleDateFormat(Constants.TIME_FORMAT, Locale.ENGLISH);

        Timber.d("local datetime: - %s", dateTime1.toDate().toString());
        return timeFormat.format(dateTime1.toDate());
    }

    public static String getISODateTime(DateTime dateTime) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.ISO_DATE_TIME_TIMEZONE_FORMAT);
        return simpleDateFormat.format(dateTime.toDate());
    }

    public static String getTimeFromUTCFormat(String dateTime) {

        DateTime dateTime1 = new DateTime(dateTime);
        SimpleDateFormat timeFormat = new SimpleDateFormat(Constants.TIME_FORMAT, Locale.ENGLISH);

        Timber.d("local datetime: - %s", dateTime1.toDate().toString());
        return timeFormat.format(dateTime1.toDate());

    }

    public static String getDateFromUTCFormat(String dateTime) {

        DateTime dateTime1 = new DateTime(dateTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.JUST_DATE_FORMAT, Locale.ENGLISH);

        Timber.d("local datetime: - %s", dateTime1.toDate().toString());
        return dateFormat.format(dateTime1.toDate());

    }

}
