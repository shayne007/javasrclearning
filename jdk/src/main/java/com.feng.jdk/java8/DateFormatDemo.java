package com.feng.jdk.java8;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * TODO
 *
 * @since 2025/7/30
 */
public class DateFormatDemo {

	public static void main(String[] args) {
		LocalDate ld = LocalDate.of(2025, 7, 30);
		System.out.println(ld.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		System.out.println(ld.format(DateTimeFormatter.ofPattern("MMM, dd, yyyy")));
		System.out.println(ld.format(DateTimeFormatter.ofPattern("E, MMM, dd, yyyy")));

		ResourceBundle rb = ResourceBundle.getBundle("Messages", java.util.Locale.CHINA);
	}

}
