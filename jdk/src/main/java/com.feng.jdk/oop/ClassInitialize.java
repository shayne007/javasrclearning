package com.feng.jdk.oop;

/**
 * TODO
 *
 * @since 2025/7/30
 */
public class ClassInitialize extends ParentClass {
	public ClassInitialize(){
		count++;
	}
	public static void main(String[] args) {
		System.out.println(getCount());

		ClassInitialize p2 = new ClassInitialize();
		System.out.println(p2.getCount());
	}

}
 class ParentClass{
	protected static int count = 0;
	public ParentClass(){
		count++;
	}
	static int getCount(){
		return count;
	}

}
