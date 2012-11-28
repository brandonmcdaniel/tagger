package edu.utsa.tagger.view;

import java.util.EventObject;

@SuppressWarnings("serial")
public class ViewEvent extends EventObject
{
	private float fontsize_delta;
	private Object pinneddata;
	
	public ViewEvent(Object source, float fontsize_delta)
	{
		super(source);
		this.fontsize_delta = fontsize_delta;
	}
	
	public ViewEvent(Object source, Object pinneddata)
	{
		super(source);
		this.pinneddata = pinneddata;
	}
	
	public float getFontSizeDelta()
	{
		return fontsize_delta;
	}
	
	public Object getPinnedData()
	{
		return pinneddata;
	}
}
