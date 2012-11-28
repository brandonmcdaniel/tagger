package edu.utsa.xgui;

@SuppressWarnings("serial")
public abstract class XContextMenu extends XComponent
{
	XComponent xcomponent = null;
	
	public XContextMenu(XComponent xcomponent)
	{
		this.xcomponent = xcomponent;
		setLayout(new ListLayout(0));
	}
	
	public XComponent getXComponent()
	{
		return xcomponent;
	}
	
	public abstract void handleClick(String item_text);
}
