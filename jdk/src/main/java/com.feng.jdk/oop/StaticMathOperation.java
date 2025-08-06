package com.feng.jdk.oop;

import java.util.Formatter;
import java.util.Locale;

/**
 * TODO
 *
 * @since 2025/7/30
 */
public class StaticMathOperation {

	static int number2 = getValue();

	private static int getValue() {
		return number1;
	}

	static int number1 = 10;

	static int doSum() {
		return number1 + number2;
	}

	static int doMinus() {
		return number1 - number2;
	}

	public static void main(String[] args) {
		System.out.println(doSum());
		System.out.println(doMinus());
		Locale usa = new Locale("cn","US");
		System.out.println(Locale.US.getLanguage());
		System.out.println(Locale.US.getCountry());

		boolean b = false;
		int n =5;
		System.out.println(b || n==5);
		System.out.println(b=true || n==5);
		System.out.println(b);

		String strA = "A";
		String strB = "BBB";
		StringBuilder sb = new StringBuilder("CCC");
		Formatter formatter = new Formatter(sb);
//		formatter.format("%s%s",strA,strB);// %s表示字符串，%d表示整数，%f表示浮点数
		System.out.println(formatter);
		formatter.format("%-2s",strB);// -2s表示左对齐，宽度为2
		System.out.println(formatter);
		main();

		Locale locale = new Locale("en","US","");

	}

	public static void main() {
		System.out.println();
	}

}
