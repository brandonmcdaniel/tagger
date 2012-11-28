package edu.utsa.tagger.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JComponent;
import javax.swing.JLabel;

import edu.utsa.tagger.model.EventData;
import edu.utsa.tagger.model.TagData;
import edu.utsa.xgui.Anchor;
import edu.utsa.xgui.ListLayout;
import edu.utsa.xgui.Margins;
import edu.utsa.xgui.XComponent;

@SuppressWarnings("serial")
public class Event extends XComponent implements ViewListener
{
	private EventData eventdata;
	private boolean selected = false;
	private JLabel label;
	private JComponent tags_list;
	private JLabel options;
	
	public Event(EventData eventdata)
	{
		this.eventdata = eventdata;
		
		// options
		options = new JLabel("\u25bc"); // or \ue0a1?
		options.setFont(FontsAndColors.segoe_ui_symbol);
		options.setForeground(FontsAndColors.GREY_DARK);
		options.setVisible(false);
		
		label = new JLabel(eventdata.getLabel());
		label.setFont(FontsAndColors.segoe_ui);
		label.setForeground(FontsAndColors.GREY_DARK);
		
		tags_list = new JComponent(){};
		tags_list.setLayout(new ListLayout(0));
		tags_list.setOpaque(false);
		
		setDroppable(true);
		add(options, new Anchor(Anchor.TL, Anchor.NOSTRETCH, new Margins(2, 5, 2, 0)));
		add(label, new Anchor(options, Anchor.RIGHT, Anchor.HORIZONTAL, new Margins(0, 5, 0, 0)));
		add(tags_list, new Anchor(options, Anchor.BOTTOM, Anchor.HORIZONTAL, new Margins(3, 0, 2, 10)));
	}
	
	@Override public void setForeground(Color fg)
	{
		super.setForeground(fg);
		for (Component c : this.getComponents())
		{
			c.setForeground(fg);
		}
	}
	
	public JLabel getOptionsButton()
	{
		return options;
	}
	
	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}
	
	public boolean isSelected()
	{
		return selected;
	}
	
	public JLabel getNameLabel()
	{
		return label;
	}
	
	public JComponent getTagsList()
	{
		return tags_list;
	}

	public EventData getEventData()
	{
		return eventdata;
	}
	
	////////////////////////
	// XCOMPONENT METHODS //
	////////////////////////
	
	@Override public void entered()
	{
		options.setVisible(true);
	}
	
	@Override public void exited()
	{
		options.setVisible(false);
	}
	
	@Override public String getDropActionText(XComponent being_dragged)
	{
		if (being_dragged instanceof Selectedtag)
		{
			return "Copy to";
		}
		else if (being_dragged instanceof Tag)
		{
			return "Move to";
		}
		else
		{
			return "";
		}
	}
	
	@Override public String getDropText()
	{
		return eventdata.getLabel();
	}

	//////////////////////////
	// VIEWLISTENER METHODS //
	//////////////////////////
	
	@Override public void fontSizeChanged(ViewEvent e)
	{
		for (Component c : this.getComponents())
		{
			ToolBox.changeFontSize(c, e.getFontSizeDelta());
		}
	}

	@Override public void propertiesChanged(ViewEvent e)	{}

	@Override public void dataUpdated(ViewEvent e)
	{
		if (e.getPinnedData() == eventdata)
		{
			label.setText("     " + eventdata.getLabel());
		}
	}
}
