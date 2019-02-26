package com.simorgh.calendarutil.persiancalendar;

/**
 * 
 * @author Morteza contact: <a
 *         href="mailto:Mortezaadi@gmail.com">Mortezaadi@gmail.com</a>
 * @version 1.0
 */
public class PersianCalendarConstants {

	// 00:00:00 UTC (Gregorian) Julian day 0,
	// 0 milliseconds since 1970-01-01
	public static final long MILLIS_JULIAN_EPOCH = -210866803200000L;
	// Milliseconds of a day calculated by 24L(hours) * 60L(minutes) *
	// 60L(seconds) * 1000L(mili);
	public static final long MILLIS_OF_A_DAY = 86400000L;

	/**
	 * The JDN of 1 Farvardin 1; Equivalent to March 19, 622 A.D.
	 */
	public static final long PERSIAN_EPOCH = 1948321;

	public static final String[] persianMonthNames = { "فروردین", // Farvardin
			"اردیبهشت", // Ordibehesht
			"خرداد", // Khordad
			"تیر", // Tir
			"مرداد", // Mordad
			"شهریور", // Shahrivar
			"مهر", // Mehr
			"آبان", // Aban
			"آذر", // Azar
			"دی", // Dey
			"بهمن", // Bahman
			"اسفند" // Esfand
	};

	public static final String[] persianWeekDays = { "\u0634\u0646\u0628\u0647", // Shanbeh
			"\u06cc\u06a9\u200c\u0634\u0646\u0628\u0647", // Yekshanbeh
			"\u062f\u0648\u0634\u0646\u0628\u0647", // Doshanbeh
			"\u0633\u0647\u200c\u0634\u0646\u0628\u0647", // Sehshanbeh
			"\u0686\u0647\u0627\u0631\u0634\u0646\u0628\u0647", // Chaharshanbeh
			"\u067e\u0646\u062c\u200c\u0634\u0646\u0628\u0647", // Panjshanbeh
			"\u062c\u0645\u0639\u0647" // jome
	};

}
