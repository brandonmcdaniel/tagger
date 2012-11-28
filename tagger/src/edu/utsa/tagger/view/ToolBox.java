package edu.utsa.tagger.view;

import java.awt.Component;

public abstract class ToolBox
{
	// prevent instantiation
	private ToolBox() {}
	
	public static void changeFontSize(Component c, float font_size_delta)
	{
		c.setFont(c.getFont().deriveFont(c.getFont().getSize() + font_size_delta));
	}
}
