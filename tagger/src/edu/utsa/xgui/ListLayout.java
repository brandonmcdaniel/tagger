package edu.utsa.xgui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;

public class ListLayout implements LayoutManager2
{
	private int gap = 0;
	
	public ListLayout(int gap)
	{
		this.gap = gap;
	}

	@Override public void layoutContainer(Container target)
	{
		Insets insets = target.getInsets();
		int w, h = 0;
		int bottom = insets.top;
		for (Component comp : target.getComponents())
		{
			w = target.getWidth();
			h = comp.getPreferredSize().height;
			if (!comp.isVisible())
			{
				w = 0;
				h = 0;
			}
			comp.setBounds(insets.left, bottom, w - insets.left - insets.right, h);
			bottom += h + gap;
		}
	}

	@Override public void addLayoutComponent(String str, Component comp) {}
	@Override public void removeLayoutComponent(Component comp) {}
	@Override public Dimension minimumLayoutSize(Container target) { return null; }
	@Override public Dimension preferredLayoutSize(Container target)
	{
		Insets insets = target.getInsets();
		int x = 0, y = 0;
		for (Component comp : target.getComponents())
		{
			if (comp.isVisible())
			{
				if (comp.getPreferredSize().width > x)
					x = comp.getPreferredSize().width;
				y += comp.getPreferredSize().height;
			}
		}
		return new Dimension(insets.left + x + insets.right, insets.top + y + insets.bottom);
	}

	@Override public void addLayoutComponent(Component arg0, Object arg1) {}

	@Override public float getLayoutAlignmentX(Container arg0) { return 0.5f; }

	@Override public float getLayoutAlignmentY(Container arg0) { return 0.5f; }

	@Override public void invalidateLayout(Container arg0) {}

	@Override public Dimension maximumLayoutSize(Container arg0) { return null;}
}
