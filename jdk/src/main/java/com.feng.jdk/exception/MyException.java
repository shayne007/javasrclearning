package com.feng.jdk.exception;

/**
 * TODO
 *
 * @since 2025/7/30
 */
public class MyException extends Exception {
	MyException(String reason){
	}
	MyException(String reason,String cause){
		super();
	}
	public String getReason() throws Exception {
		throw new Exception("MyException");
	}

}
