package edu.utsa.tagger.view;

import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;

@SuppressWarnings("serial")
public class Button extends JLabel implements ViewListener
{
	private final Border MOUSEOVER_BORDER = BorderFactory.createLineBorder(FontsAndColors.GREY_DARK, 2);
	private final Border MOUSEOUT_BORDER = BorderFactory.createLineBorder(FontsAndColors.GREY_MEDIUM, 2);
	
	public Button(String text)
	{
		super(text);
		setOpaque(true);
		setBackground(FontsAndColors.GREY_MEDIUM);
		setBorder(MOUSEOUT_BORDER);
		addMouseListener(new MouseAdapter()
		{
			@Override public void mouseEntered(MouseEvent e){setBorder(MOUSEOVER_BORDER);}
			@Override public void mouseExited(MouseEvent e){setBorder(MOUSEOUT_BORDER);}
		});
	}
	
	@Override protected void paintComponent(Graphics g)
	{
		Insets i = getInsets();
		g.setClip(i.left, i.top, getWidth() - i.left - i.right, getHeight() - i.top - i.bottom);
		super.paintComponent(g);
		g.setClip(0, 0, getWidth(), getHeight());
	}
	
	@Override public void fontSizeChanged(ViewEvent e)
	{
		setFont(getFont().deriveFont(getFont().getSize() + e.getFontSizeDelta()));
	}
	
	@Override public void propertiesChanged(ViewEvent e){}

	@Override public void dataUpdated(ViewEvent e){}
}
