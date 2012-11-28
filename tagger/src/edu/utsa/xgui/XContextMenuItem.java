package edu.utsa.xgui;

import java.awt.Component;

import javax.swing.JLabel;

@SuppressWarnings("serial")
public class XContextMenuItem extends XComponent
{
	XContextMenu xcontextmenu;
	JLabel label;
	boolean title;
	
	public XContextMenuItem(XContextMenu xcontextmenu, String text, boolean title)
	{
		this.xcontextmenu = xcontextmenu;
		this.title = title;
		this.label = new JLabel(text);
		add(label, new Anchor(Anchor.TL, Anchor.NOSTRETCH, new Margins(2, 5, 2, 10)));
	}
	
	public JLabel getLabel()
	{
		return label;
	}
	
	public boolean isTitle()
	{
		return title;
	}
	
	@Override public void clicked(Component c)
	{
		xcontextmenu.handleClick(label.getText());
	}
}