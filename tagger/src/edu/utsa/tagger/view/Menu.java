package edu.utsa.tagger.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import edu.utsa.xgui.Anchor;
import edu.utsa.xgui.Margins;
import edu.utsa.xgui.XComponent;

@SuppressWarnings("serial")
public class Menu extends XComponent
{
	private JLabel zoomin;
	private JLabel zoomout;
	private JLabel zoom_label;
	private JLabel pinproperties_check;
	private JLabel pinproperties_label;
	
	public Menu()
	{
		zoomin = new JLabel("+");
		zoomin.setFont(FontsAndColors.segoe_ui_light.deriveFont(24f));
		zoomin.setForeground(FontsAndColors.GREY_LIGHT);
		
		zoomout = new JLabel("-");
		zoomout.setFont(FontsAndColors.segoe_ui_light.deriveFont(24f));
		zoomout.setForeground(FontsAndColors.GREY_LIGHT);
		
		zoom_label = new JLabel("Zoom");
		zoom_label.setFont(FontsAndColors.segoe_ui_light.deriveFont(24f));
		zoom_label.setForeground(FontsAndColors.GREY_LIGHT);
		
		pinproperties_check = new JLabel("\u2610"); // box w/o check (\u2611 box w/ check)
		pinproperties_check.setFont(FontsAndColors.segoe_ui_symbol.deriveFont(24f));
		pinproperties_check.setForeground(FontsAndColors.GREY_LIGHT);
		
		pinproperties_label = new JLabel("Pin Properties");
		pinproperties_label.setFont(FontsAndColors.segoe_ui_light.deriveFont(24f));
		pinproperties_label.setForeground(FontsAndColors.GREY_LIGHT);
		
		add(zoomout, new Anchor(Anchor.TR, Anchor.NOSTRETCH, new Margins(5, 0, 5, 10)));
		add(zoomin, new Anchor(zoomout, Anchor.LEFT, Anchor.NOSTRETCH, new Margins(0, 0, 0, 10)));
		add(zoom_label, new Anchor(zoomin, Anchor.LEFT, Anchor.NOSTRETCH, new Margins(0, 0, 0, 10)));
		add(pinproperties_check, new Anchor(zoom_label, Anchor.LEFT, Anchor.NOSTRETCH, new Margins(0, 0, 0, 30)));
		add(pinproperties_label, new Anchor(pinproperties_check, Anchor.LEFT, Anchor.NOSTRETCH, new Margins(0, 0, 0, 10)));
		
		setPreferredSize(new Dimension(0, 0));
		setBackground(new Color(0, 0, 0, 160));
		setVisible(false);
	}
	
	public JLabel getPinPropertiesCheckbox()
	{
		return pinproperties_check;
	}
	
	@Override public void draw(Graphics2D g)
	{
		g.setColor(getBackground());
		g.fill(SwingUtilities.calculateInnerArea(this, null));
	}
	
	public JLabel getZoomIn(){return zoomin;}
	public JLabel getZoomOut(){return zoomout;}
}
