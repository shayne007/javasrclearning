package com.feng.jdk.java8;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * TODO
 *
 * @since 2025/7/30
 */
public class MethodRef {

	public static void main(String[] args) {
		MethodRef methodRef = new MethodRef();
		populate(ArrayList::new,"RED","YELLOW","GREEN");
		//
//		populate2(ArrayList::new,"RED","YELLOW","GREEN");
	}

	private static void populate(Supplier<List> o, String... stuff) {
		List o1 = o.get();
		for (String s : stuff) {
			o1.add(s);
		}
		System.out.println(o1);
	}
	private static void populate2(Consumer<String> o, String... stuff) {
		for (String s : stuff) {
			o.accept(s);
		}
		System.out.println(o);
	}

}
