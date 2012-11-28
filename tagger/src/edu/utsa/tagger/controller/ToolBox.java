package edu.utsa.tagger.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public abstract class ToolBox
{
	// prevent instantiation
	private ToolBox(){}
	
	public static String TEST_EVENTS = 
			"1,some event 1,some event 1's description,/Time-Locked Event/Stimulus/Visual/Shape/Rotated,/Time-Locked Event/Stimulus/Visual/Shape/Star;"+
			"2,some event 2,some event 2's description;"+
			"3,some event 3,some event 3's description;"+
			"4,some event 4,some event 4's description;"+
			"5,some event 5,some event 5's description";
	
	public static String fileToString(File file, Charset charset) throws IOException
	{
		int len = (int) file.length();
		if (len <= 0 || len >= Integer.MAX_VALUE)
		{
			return null;
		}
		byte[] b = new byte[len];
		InputStream in = new FileInputStream(file);
		int total = 0;
		while (total < len)
		{
			int result = in.read(b, total, len - total);
			if (result == -1)
			{
				break;
			}
			total += result;
		}
		if (in != null)
		{
			in.close();
		}
		if (total != b.length)
		{
			return null;
		}
		return new String(b, charset);
	}
}
