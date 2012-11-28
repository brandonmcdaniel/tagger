package edu.utsa.tagger.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.BorderFactory;

import edu.utsa.xgui.XComponent;
import edu.utsa.xgui.XContextMenu;
import edu.utsa.xgui.XContextMenuItem;

@SuppressWarnings("serial")
public abstract class ContextMenu extends XContextMenu
{
	public ContextMenu(XComponent xcomponent)
	{
		super(xcomponent);
		setBorder(BorderFactory.createCompoundBorder(
				new DropShadowBorder(new Color(175, 175, 175), 0, 4, .1f, 8, true, true, true, true),
				BorderFactory.createLineBorder(new Color(175, 175, 175))));
	}
	
	public ContextMenu(XComponent xcomponent, String title_text, String ... item_labels)
	{
		this(xcomponent);
		addItem(title_text, true);
		for (int i = 0; i < item_labels.length; i++)
		{
			addItem(item_labels[i]);
		}
	}
	
	public void addItem(String item_text)
	{
		addItem(item_text, false);
	}
	
	public void addItem(final String item_text, final boolean title)
	{
		XContextMenuItem item = new XContextMenuItem(this, item_text, false)
		{
			@Override public void draw(Graphics2D g)
			{
				g.setColor(title ? new Color(235, 235, 235) : Color.white);
				g.fill(new Rectangle(0, 0, getWidth(), getHeight()));
				if (!title && isMouseover())
				{
					g.setColor(new Color(225, 225, 255));
					g.fill(new Rectangle(1, 1, getWidth()-2, getHeight()-2));
				}
			}
		};
		item.getLabel().setFont(FontsAndColors.segoe_ui);
		if (title)
		{
			item.getLabel().setForeground(new Color(112, 112, 112));
		}
		add(item);
	}
}
