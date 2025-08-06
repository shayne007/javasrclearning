package com.feng.jdk.collection;

import java.util.HashMap;

/**
 * TODO
 *
 * @since 2025/7/30
 */

public class FlowControlDemo {

	public static void main(String[] args) {
		HashMap<Integer, Integer> map = new HashMap<>();
		Integer key = 5;
		Integer value = key;
		map.put(key++, value);
		map.put(300, 300);
		int count = 0;
		for (Integer i : map.keySet()) {
			for (Integer j : map.values()) {
				count++;
				if (i == map.get(i)) {
					break;
				}
				else {
					continue;
				}
			}

		}
		System.out.println(count);
	}

}
