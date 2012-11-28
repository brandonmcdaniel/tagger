package edu.utsa.tagger.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;

import edu.utsa.tagger.model.EventData;
import edu.utsa.tagger.model.TagData;
import edu.utsa.xgui.Anchor;
import edu.utsa.xgui.Margins;
import edu.utsa.xgui.XComponent;
import edu.utsa.xgui.XLayout;
import edu.utsa.xgui.XScrollPane;

@SuppressWarnings("serial")
public class Properties extends XComponent implements ViewListener
{
	
	// pencil \u270e
	
	
	Object data = null;
	private boolean sticky = false;
	private boolean pinned = false;
	private JLabel title;
	private JLabel close;
	RichTextField code_label;
	RichTextField name_label;
	RichTextField description_label;
	RichTextField code;
	RichTextField name;
	RichTextField description;

	public Properties()
	{
		title = new JLabel("PROPERTIES");
		title.setFont(FontsAndColors.segoe_ui);
		title.setForeground(FontsAndColors.GREY_DARK);

		close = new JLabel("\u2716");
		close.setFont(FontsAndColors.segoe_ui_symbol);
		close.addMouseListener(new MouseAdapter(){
			@Override public void mouseClicked(MouseEvent e)
			{
				setSticky(false);
				if (!isPinned())
				{
					swipeOut();
				}
			}
			@Override public void mouseEntered(MouseEvent e){close.setForeground(Color.red);}
			@Override public void mouseExited(MouseEvent e){close.setForeground(FontsAndColors.GREY_DARK);}
		});

		code_label = new RichTextField("Code");
		code_label.setLock(true);

		name_label = new RichTextField("Name");
		name_label.setLock(true);

		description_label = new RichTextField("Description");
		description_label.setLock(true);

		code = new RichTextField(0, 20);

		name = new RichTextField(0, 20);

		description = new RichTextField();
		description.setLineWrap(true);

		JComponent c1 = new JComponent(){};
		c1.setLayout(new XLayout());
		c1.add(code_label);
		c1.add(name_label, new Anchor(code_label, Anchor.BOTTOM, Anchor.NOSTRETCH, new Margins(5, 0, 0, 0)));

		JComponent c2 = new JComponent(){};
		c2.setLayout(new XLayout());
		c2.add(code);
		c2.add(name, new Anchor(code, Anchor.BOTTOM, Anchor.NOSTRETCH, new Margins(5, 0, 0, 0)));

		JComponent c3 = new JComponent(){};
		c3.setLayout(new XLayout());
		c3.add(description_label);

		add(close, new Anchor(Anchor.TR, Anchor.NOSTRETCH, new Margins(5, 5, 5, 5)));
		add(title, new Anchor(Anchor.TL, Anchor.NOSTRETCH, new Margins(5, 5, 5, 5)));
		add(c1, new Anchor(title, Anchor.BOTTOM, Anchor.NOSTRETCH, new Margins(5, 5, 5, 5)));
		add(c2, new Anchor(c1, Anchor.RIGHT, Anchor.NOSTRETCH, new Margins(0, 5, 5, 5)));
		add(c3, new Anchor(c2, Anchor.RIGHT, Anchor.NOSTRETCH, new Margins(0, 5, 5, 5)));
		add(new XScrollPane(description), new Anchor(c3, Anchor.RIGHT, Anchor.STRETCHALL, new Margins(0, 5, 5, 5)));
		setBorder(BorderFactory.createLineBorder(FontsAndColors.GREY_MEDIUM));
		setPreferredSize(new Dimension(0, 0));
		setVisible(false);
	}

	public JLabel getTitleLabel()
	{
		return title;
	}
	public RichTextField getCodeTextField()
	{
		return code;
	}
	public RichTextField getNameTextField()
	{
		return name;
	}
	public RichTextField getDescriptionTextField()
	{
		return description;
	}
	public Object getData()
	{
		return data;
	}

	public boolean isSticky()
	{
		return sticky;
	}

	public void setSticky(boolean sticky)
	{
		this.sticky = sticky;
	}
	
	public boolean isPinned()
	{
		return pinned;
	}
	
	public void setPinned(boolean pinned)
	{
		this.pinned = pinned;
		if (pinned && !isVisible())
		{
			swipeIn();
		}
		if (!pinned && !sticky && isVisible())
		{
			swipeOut();
		}
	}

	public void setData(Object data)
	{
		if (sticky) return;

		this.data = data;

		if (data == null)
		{
			code.setText("");
			name.setText("");
			description.setText("");
			return;
		}
		
		if (data instanceof EventData)
		{
			EventData eventdata = (EventData) data;
			code.setText(eventdata.getCode());
			name.setText(eventdata.getLabel());
			description.setText(eventdata.getDescription());
		}
		else if (data instanceof TagData)
		{
			TagData tagdata = (TagData) data;
			code.setText("");
			name.setText(tagdata.getLabel());
			description.setText(tagdata.getDescription());
		}

		swipeIn();
	}

	////////////////////////
	// XCOMPONENT METHODS //
	////////////////////////

	@Override public void draw(Graphics2D g)
	{
		g.setColor(Color.white);
		g.fill(getBounds());
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

	@Override public void propertiesChanged(ViewEvent e)
	{
		setData(e.getPinnedData());
	}

	@Override public void dataUpdated(ViewEvent e){}
}
