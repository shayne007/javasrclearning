package com.feng.dataStructures.array;

import java.util.ArrayList;

/**
 * TODO
 *
 * @since 2025/6/11
 */

public class ArrayListDemo {

	public static void main(String[] args) {
		ArrayList<User> users = new ArrayList(10000);
		for (int i = 0; i < 10000; ++i) {
			users.add(new User());
		}
		System.out.println(users.size());
	}
}
class User{}
