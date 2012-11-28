package edu.utsa.tagger.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import edu.utsa.tagger.model.TagData;
import edu.utsa.xgui.Anchor;
import edu.utsa.xgui.Margins;
import edu.utsa.xgui.XComponent;

@SuppressWarnings("serial")
public class SearchResult extends XComponent implements ViewListener
{
	private TagData tagdata;
	
	JLabel label = new JLabel();
	Button select = new Button("  Select  ");

	public SearchResult(TagData tagdata)
	{
		this.tagdata = tagdata;
		
		label.setText(tagdata.getFullPath());
		select.setVisible(false);
		add(select, new Anchor(Anchor.TR, Anchor.NOSTRETCH, new Margins(0, 0, 0, 5)));
		add(label, new Anchor(Anchor.TL, Anchor.HORIZONTAL, new Margins(0, 5, 0, 0)));
	}
	
	@Override public void setFont(Font font)
	{
		super.setFont(font);
		label.setFont(font);
		select.setFont(font.deriveFont(font.getSize() - 2f));
	}
	
	public Button getSelectButton()
	{
		return select;
	}
	
	public TagData getTagData()
	{
		return tagdata;
	}
	
	@Override public void draw(Graphics2D g)
	{
		g.setColor(isMouseover() ? FontsAndColors.VERY_LIGHT_GREY : Color.white);
		g.fill(SwingUtilities.calculateInnerArea(this, null));
	}
	
	@Override public void entered()
	{
		select.setVisible(true);
	}
	
	@Override public void exited()
	{
		select.setVisible(false);
	}
	
	@Override public void fontSizeChanged(ViewEvent e)
	{
		label.setFont(label.getFont().deriveFont(label.getFont().getSize() + e.getFontSizeDelta()));
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
