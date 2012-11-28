package edu.utsa.tagger.view;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import edu.utsa.xgui.Anchor;
import edu.utsa.xgui.ListLayout;
import edu.utsa.xgui.Margins;
import edu.utsa.xgui.XLayout;
import edu.utsa.xgui.XScrollPane;

@SuppressWarnings("serial")
public class TagsPanel extends JLayeredPane implements ViewListener
{
	private JComponent tags_list;
	private RichTextField search_box;
	private JPanel search_results;
	private XScrollPane tags_scrollpane;
	
	public TagsPanel()
	{
		tags_list = new JComponent(){};
		tags_list.setLayout(new ListLayout(0));
		
		tags_scrollpane = new XScrollPane(tags_list);
		
		search_box = new RichTextField();
		
		search_results = new JPanel();
		search_results.setOpaque(false);
		search_results.setBorder(new DropShadowBorder(Color.BLACK, 0, 8, .2f, 16, true, true, true, true));
		search_results.setLayout(new ListLayout(0));
		search_results.setVisible(false);
		
		setBackground(Color.WHITE);
		setLayout(new XLayout());
		setLayer(search_box, 2);
		setLayer(search_results, 1);
		add(search_box, new Anchor(2, Anchor.TL, Anchor.HORIZONTAL, new Margins(10, 10, 0, 10)));
		add(tags_scrollpane, new Anchor(search_box, 0, Anchor.BOTTOM, Anchor.STRETCHALL, new Margins(5, -10, 0, 0)));
		add(search_results, new Anchor(search_box, 1, Anchor.BOTTOM, Anchor.HORIZONTAL, new Margins(-8, -8, 0, 0)));
	}

	public RichTextField getSearchTextField()
	{
		return search_box;
	}
	
	public JComponent getTagsList()
	{
		return tags_list;
	}
	
	public JPanel getSearchResults()
	{
		return search_results;
	}
	
	public XScrollPane getScrollPane()
	{
		return tags_scrollpane;
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
