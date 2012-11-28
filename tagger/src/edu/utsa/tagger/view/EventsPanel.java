package edu.utsa.tagger.view;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.utsa.xgui.Anchor;
import edu.utsa.xgui.ListLayout;
import edu.utsa.xgui.Margins;
import edu.utsa.xgui.XLayout;
import edu.utsa.xgui.XScrollPane;

@SuppressWarnings("serial")
public class EventsPanel extends JComponent implements ViewListener
{
	JLabel events_panel_header;
	JLabel add_event;
	JComponent events_list;
	XScrollPane events_scrollpane;
	
	public EventsPanel()
	{
		events_panel_header = new JLabel("  Events");
		events_panel_header.setFont(FontsAndColors.segoe_ui_light.deriveFont(36f));
		events_panel_header.setForeground(FontsAndColors.BLUE_DARK);
		
		events_list = new JPanel();
		events_list.setOpaque(false);
		events_list.setLayout(new ListLayout(0));
		
		events_scrollpane = new XScrollPane(events_list);
		
		add_event = new Button("  Add Event  ");
		add_event.setFont(FontsAndColors.segoe_ui);
		
		setOpaque(false);
		setLayout(new XLayout());
		setPreferredSize(new Dimension(300, 0));
		add(add_event, new Anchor(Anchor.TR, Anchor.NOSTRETCH, new Margins(10, 0, 0, 10)));
		add(events_panel_header, new Anchor(Anchor.TL, Anchor.NOSTRETCH, new Margins(10, 0, 0, 10)));
		add(events_scrollpane, new Anchor(events_panel_header, Anchor.BOTTOM, Anchor.STRETCHALL, new Margins(10, 0, 0, 0)));
	}
	
	public JComponent getEventsList()
	{
		return events_list;
	}
	
	public JLabel getAddEventButton()
	{
		return add_event;
	}
	
	@Override public void fontSizeChanged(ViewEvent e)
	{
		for (Component c : this.getComponents())
		{
			ToolBox.changeFontSize(c, e.getFontSizeDelta());
		}
	}

	@Override public void propertiesChanged(ViewEvent e) {}

	@Override public void dataUpdated(ViewEvent e) {}
}
