package edu.utsa.tagger.model;

import java.util.Stack;

public final class ErrorStack
{
	private static Stack<Err> error_stack = new Stack<Err>();
	
	private ErrorStack() {}
	
	public static void push(int code, String message)
	{
		Err err = new Err(code, message);
		error_stack.push(err);
		/*if (View.dialog_open)
		{
			View.getNotificationError().newNotification(message);
		}*/
		System.out.println(message);
	}
	
	public static Err pop()
	{
		return error_stack.pop();
	}
	
	public static Err peek()
	{
		return error_stack.peek();
	}
	
	public static boolean isEmpty()
	{
		return error_stack.isEmpty();
	}
	
	public static class Err
	{
		public final int code;
		public final String message;
		
		public Err(int code, String message)
		{
			this.code = code;
			this.message = message;
		}
	}
}
