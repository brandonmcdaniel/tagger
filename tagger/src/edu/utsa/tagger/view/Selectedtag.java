package edu.utsa.tagger.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

import edu.utsa.tagger.model.EventData;
import edu.utsa.tagger.model.TagData;
import edu.utsa.xgui.Anchor;
import edu.utsa.xgui.Margins;
import edu.utsa.xgui.XComponent;

@SuppressWarnings("serial")
public class Selectedtag extends XComponent implements ViewListener
{
	private EventData eventdata;
	private TagData tagdata;
	private JLabel label;
	private JLabel unselect;
	
	public Selectedtag(EventData eventdata, TagData tagdata)
	{
		this.eventdata = eventdata;
		this.tagdata = tagdata;
		
		label = new JLabel(tagdata.getFullPath());
		label.setFont(FontsAndColors.segoe_ui);
		label.setForeground(FontsAndColors.GREY_DARK);
		
		unselect = new JLabel("\u2716");
		unselect.setFont(FontsAndColors.segoe_ui_symbol);
		unselect.setForeground(FontsAndColors.GREY_DARK);
		unselect.setVisible(false);
		unselect.addMouseListener(new MouseAdapter()
		{
			@Override public void mouseEntered(MouseEvent e)
			{
				unselect.setForeground(Color.red);
			}

			@Override public void mouseExited(MouseEvent e)
			{
				unselect.setForeground(FontsAndColors.GREY_DARK);
			}
			
		});
		
		setDraggable(true);
		add(unselect);
		add(label, new Anchor(unselect, Anchor.RIGHT, Anchor.HORIZONTAL, new Margins(0, 5, 0, 0)));
	}
	
	public EventData getEventData()
	{
		return eventdata;
	}
	
	public TagData getTagData()
	{
		return tagdata;
	}
	
	public JLabel getUnselectButton()
	{
		return unselect;
	}
	
	////////////////////////
	// XCOMPONENT METHODS //
	////////////////////////
	
	@Override public String getDragText()
	{
		return tagdata.getPath();
	}
	
	@Override public Object dragExport()
	{
		return tagdata;
	}
	
	@Override public void entered()
	{
		label.setForeground(Color.BLACK);
		unselect.setVisible(true);
	}
	
	@Override public void exited()
	{
		label.setForeground(FontsAndColors.GREY_DARK);
		unselect.setVisible(false);
	}
	
	///////////////////////////
	// VIEW LISTENER METHODS //
	///////////////////////////
	
	@Override public void fontSizeChanged(ViewEvent e)
	{
		for (Component c : this.getComponents())
		{
			ToolBox.changeFontSize(c, e.getFontSizeDelta());
		}
	}

	@Override public void propertiesChanged(ViewEvent e){}

	@Override public void dataUpdated(ViewEvent e)
	{
		if (e.getPinnedData() == tagdata)
		{
			label.setText(tagdata.getFullPath());
		}
	}

}
