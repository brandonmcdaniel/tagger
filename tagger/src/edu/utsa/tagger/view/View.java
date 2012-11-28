package edu.utsa.tagger.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;

import edu.utsa.tagger.controller.Controller;
import edu.utsa.tagger.model.EventData;
import edu.utsa.tagger.model.TagData;
import edu.utsa.xgui.Anchor;
import edu.utsa.xgui.ListLayout;
import edu.utsa.xgui.Margins;
import edu.utsa.xgui.XContainer;
import edu.utsa.xgui.XContentPane;
import edu.utsa.xgui.XLayout;
import edu.utsa.xgui.XScrollPane;
import edu.utsa.xgui.XSplitPane;

@SuppressWarnings("serial")
public class View extends JFrame
{
	private Controller controller;

	public View(Controller controller)
	{
		this.controller = controller;
		try
		{
			SwingUtilities.invokeAndWait(new Runnable()
			{
				public void run()
				{
					createGUI();
				}
			});
		}
		catch (Exception e)
		{ 
			System.err.println("Failed to create GUI.");
			e.printStackTrace();
		}
	}
	
	private TagsPanel tags_panel;
	private Collection<Tag> tags;

	private EventsPanel events_panel;
	private Collection<Event> events;

	private Properties properties;

	private Menu menu;
	private JComponent menu_hitarea;

	private XContainer xcontainer;
	private XSplitPane xsplitpane;

	private void createGUI()
	{
		// *************************
		//    TAGS
		// *************************
		tags_panel = new TagsPanel();
		tags_panel.getSearchTextField().addKeyListener(new KeyAdapter()
		{
			@Override public void keyReleased(KeyEvent e)
			{
				searchTags(tags_panel.getSearchTextField().getText());
			}
		});
		tags = new ArrayList<Tag>();

		// *************************
		//    EVENTS
		// *************************
		events_panel = new EventsPanel();
		events_panel.getAddEventButton().addMouseListener(new MouseAdapter()
		{
			@Override public void mouseClicked(MouseEvent e)
			{
				EventData eventdata = controller.addEvent();
				addEvent(eventdata);
				refreshEvents();
			}
		});
		events = new ArrayList<Event>();

		// *************************
		//    PROPERTIES
		// *************************
		properties = new Properties();
		KeyAdapter keyadapter = new KeyAdapter()
		{
			@Override public void keyReleased(KeyEvent e)
			{
				Object pinneddata = properties.getData();
				if (properties.getData() != null)
				{
					if (pinneddata instanceof EventData)
					{
						controller.updateEventData(
								(EventData) pinneddata, 
								properties.getCodeTextField().getText(), 
								properties.getNameTextField().getText(), 
								properties.getDescriptionTextField().getText());
					}
					else if (pinneddata instanceof TagData)
					{
						controller.updateTagData(
								(TagData) pinneddata,  
								properties.getNameTextField().getText(), 
								properties.getDescriptionTextField().getText());
					}
				}
			}
		};
		properties.getCodeTextField().addKeyListener(keyadapter);
		properties.getNameTextField().addKeyListener(keyadapter);
		properties.getDescriptionTextField().addKeyListener(keyadapter);
		
		// *************************
		//    MENU
		// *************************
		menu_hitarea = new JComponent(){};
		menu_hitarea.addMouseListener(new MouseAdapter()
		{
			@Override public void mouseEntered(MouseEvent e)
			{
				menu.swipeIn();
			}
		});
		menu_hitarea.setPreferredSize(new Dimension(0, 10));
		menu = new Menu();
		menu.getZoomIn().addMouseListener(new MouseAdapter()
		{
			@Override public void mouseClicked(MouseEvent e){zoomIn();}
		});
		menu.getZoomOut().addMouseListener(new MouseAdapter()
		{
			@Override public void mouseClicked(MouseEvent e){zoomOut();}
		});
		menu.getPinPropertiesCheckbox().addMouseListener(new MouseAdapter()
		{
			@Override public void mouseClicked(MouseEvent e)
			{
				properties.setPinned(!properties.isPinned());
				menu.getPinPropertiesCheckbox().setText(properties.isPinned() ? "\u2611" : "\u2610");
			}
		});

		// *************************
		//    ALL TOGETHER NOW
		// *************************
		xcontainer = new XContainer();
		xsplitpane = new XSplitPane(XSplitPane.VERTICAL, 300)
		{
			@Override public void entered()
			{
				menu.swipeOut();
			}
		};
		xsplitpane.getA().setLayout(new XLayout());
		xsplitpane.getA().setBackground(FontsAndColors.VERY_LIGHT_GREY);
		xsplitpane.getB().setLayout(new XLayout());
		xsplitpane.getB().setBackground(Color.WHITE);
		xsplitpane.getA().add(events_panel, new Anchor(Anchor.TL, Anchor.STRETCHALL));
		xsplitpane.getB().add(tags_panel, new Anchor(Anchor.TL, Anchor.STRETCHALL));

		XContentPane content_pane = xcontainer.getContentPane();
		content_pane.setLayout(new XLayout());
		content_pane.setLayer(menu, 4);
		content_pane.setLayer(menu_hitarea, 3);
		content_pane.setLayer(properties, 0);
		content_pane.setLayer(xsplitpane, 0);
		content_pane.add(menu, new Anchor(4, Anchor.TL, Anchor.HORIZONTAL));
		content_pane.add(menu_hitarea, new Anchor(3, Anchor.TL, Anchor.HORIZONTAL));
		content_pane.add(properties, new Anchor(Anchor.BL, Anchor.HORIZONTAL));
		content_pane.add(xsplitpane, new Anchor(menu, Anchor.BOTTOM, Anchor.STRETCHALL));

		setLayout(new XLayout());
		add(xcontainer, new Anchor(Anchor.TL, Anchor.STRETCHALL));

		addViewListener(properties);
		addViewListener(events_panel);
		addViewListener(tags_panel);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(800, 600);
		setVisible(true);
	}

	public void toggleEditTag(TagData tagdata)
	{
		if (properties.getData() == tagdata)
		{
			properties.setSticky(false);
		}
		else
		{
			View.this.firePropertiesChanged(new ViewEvent(this, tagdata));
			properties.setSticky(true);
		}
	}
	
	public void toggleEditEvent(EventData eventdata)
	{
		if (properties.getData() == eventdata)
		{
			properties.setSticky(false);
		}
		else
		{
			View.this.firePropertiesChanged(new ViewEvent(this, eventdata));
			properties.setSticky(true);
		}
	}

	public void createAndShowContextMenu(Tag tag, Point p)
	{
		TagData tagdata = tag.getTagData();
		ContextMenu contextmenu = (ContextMenu) tag.getXContextMenu();
		contextmenu.removeAll();
		contextmenu.addItem(tagdata.getLabel(), true);
		if (!tagdata.isChildRequired() && controller.getActiveEvent() != null)
		{
			if (!tag.isSelected())
			{
				contextmenu.addItem("Select");
			}
			else
			{
				contextmenu.addItem("Unselect");
			}
		}
		if (tagdata.isExtensionAllowed() || tagdata.isChildRequired())
		{
			contextmenu.addItem("Add Child");
		}
		contextmenu.addItem("Edit");
		contextmenu.addItem("Delete");
		xcontainer.setContextMenu(contextmenu, p);
	}
	
	public void createAndShowContextMenu(Event event, Point p)
	{
		EventData eventdata = event.getEventData();
		ContextMenu contextmenu = (ContextMenu) event.getXContextMenu();
		contextmenu.removeAll();
		contextmenu.addItem(eventdata.getLabel(), true);
		if (controller.getActiveEvent() != eventdata)
		{
			contextmenu.addItem("Select");
		}
		contextmenu.addItem("Edit");
		contextmenu.addItem("Delete");
		xcontainer.setContextMenu(contextmenu, p);
	}

	public void deleteEvent(EventData eventdata)
	{
		firePropertiesChanged(new ViewEvent(this, null));
		controller.removeEvent(eventdata);
	}

	///////////////////
	//               //
	//  TAG METHODS  //
	//               //
	///////////////////

	public void loadTags(Collection<TagData> tagsdata)
	{
		tags.clear();
		addTags(tagsdata);
		refreshTags();
	}

	public void addTags(Collection<TagData> tagsdata)
	{
		for (TagData tagdata : tagsdata)
		{
			addTag(tagdata);
			addTags(tagdata.getChildTags());
		}
	}

	public void addTag(TagData tagdata)
	{
		// Make the tag.
		final Tag tag = new Tag(tagdata)
		{
			@Override public void draw(Graphics2D g)
			{
				super.draw(g);
				Color bg = null;
				Color fg = null;
				if (this.isSelected())
				{
					bg = FontsAndColors.TAG_BG_SELECTED;
					fg = FontsAndColors.TAG_FG_SELECTED;
				}
				else if (this.isMouseover() || xcontainer.getXContextMenu() == this.getXContextMenu())
				{
					bg = FontsAndColors.TAG_BG_HOVER;
					fg = FontsAndColors.TAG_FG_HOVER;
				}
				else
				{
					bg = FontsAndColors.TAG_BG_NORMAL;
					fg = FontsAndColors.TAG_FG_NORMAL;
				}
				g.setColor(bg);
				g.fill(SwingUtilities.calculateInnerArea(this, null));
				this.setForeground(fg);
			}
			
			@Override public void clicked(Component c)
			{
				super.clicked(c);
				if (c != this.getCollapser() && c != this.getOptionsButton() && !this.getTagData().isChildRequired())
				{
					controller.toggleTag(this.getTagData());
				}
			}

			@Override public void rightclicked(Component c, MouseEvent e)
			{
				super.rightclicked(c, e);
				View.this.createAndShowContextMenu(this, e.getPoint());
			}

			@Override public void entered()
			{
				super.entered();
				if (properties.isVisible()) properties.setData(this.getTagData());
			}

			@Override public void exited()
			{
				super.exited();
				if (properties.isVisible()) properties.setData(null);
			}
		};
		tag.setXContextMenu(new ContextMenu(tag, tag.getTagData().getLabel())
		{
			@Override public void handleClick(String item_text)
			{
				TagData tagdata = ((Tag) this.getXComponent()).getTagData();

				if (item_text.equals("Select")) controller.selectTag(tagdata);
				if (item_text.equals("Unselect")) controller.unselectTag(tagdata);
				if (item_text.equals("Add Child")) controller.addTag(tagdata);
				if (item_text.equals("Edit")) View.this.toggleEditTag(tagdata);
				if (item_text.equals("Delete")) controller.removeTag(tagdata);
			}
		});
		tag.getOptionsButton().addMouseListener(new MouseAdapter()
		{
			@Override public void mouseClicked(MouseEvent e)
			{
				View.this.createAndShowContextMenu(tag, SwingUtilities.convertPoint(tag, tag.getOptionsButton().getLocation(), xcontainer));
			}
		});
		addViewListener(tag);

		// Add tag in appropriate place.
		Tag parent = getTag(tags, tagdata.getParent());
		if (parent == null) tags.add(tag);
		else parent.getChildren().add(tag);
	}

	private static Tag getTag(Collection<Tag> tags, TagData tagdata)
	{
		for (Tag tag : tags)
		{
			if (tag.getTagData() == tagdata) return tag;

			Tag tmp = getTag(tag.getChildren(), tagdata);
			if (tmp != null) return tmp;
		}
		return null;
	}

	public void refreshTags()
	{
		tags_panel.getTagsList().removeAll();
		refreshTagsHelper(tags);
		invalidate();
		validate();
	}

	private void refreshTagsHelper(Collection<Tag> tags)
	{
		for (Tag tag : tags)
		{
			tags_panel.getTagsList().add(tag);
			tag.getCollapser().setVisible(tag.getTagData().getChildTags().size() > 0);
			refreshTagsHelper(tag.getChildren());
		}
	}

	private void searchTags(String search_string)
	{
		tags_panel.getSearchTextField().setText(search_string);
		if (search_string.equals(""))
		{
			invalidate();
			validate();
			tags_panel.getSearchResults().setVisible(false);
			return;
		}
		tags_panel.getSearchResults().removeAll();
		tags_panel.getSearchResults().setVisible(true);
		final ArrayList<TagData> results = (ArrayList<TagData>) controller.searchTagsData(search_string);
		for (int i = 0; i < results.size(); i++)
		{
			final TagData tagdata = results.get(i);
			SearchResult result = new SearchResult(tagdata)
			{
				@Override public void clicked(Component c)
				{
					scrollTo(tagdata);
					searchTags("");
					if (c == getSelectButton())
					{
						controller.toggleTag(getTagData());
					}
				}
			};
			result.setFont(FontsAndColors.segoe_ui);
			tags_panel.getSearchResults().add(result);
		}
		invalidate();
		validate();
	}

	public void scrollTo(TagData tagdata)
	{
		for (Component c : tags_panel.getTagsList().getComponents())
		{
			Tag tag = (Tag) c;
			if (tag.getTagData().equals(tagdata))
			{
				tags_panel.getScrollPane().scrollTo(tag.getY(), 0);
				return;
			}
		}
	}

	/////////////////////
	//                 //
	//  EVENT METHODS  //
	//                 //
	/////////////////////

	public void loadEvents(Collection<EventData> eventsdata)
	{
		events.clear();
		addEvents(eventsdata);
		refreshEvents();
	}

	public void addEvents(Collection<EventData> eventsdata)
	{
		for (EventData eventdata : eventsdata)
		{
			addEvent(eventdata);
		}
	}

	private void addEvent(EventData eventdata)
	{
		final Event event = new Event(eventdata)
		{
			@Override public void draw(Graphics2D g)
			{
				super.draw(g);
				Color bg = null;
				Color fg = null;
				if (this.isSelected())
				{
					bg = FontsAndColors.EVENT_BG_SELECTED;
					fg = FontsAndColors.EVENT_FG_SELECTED;
				}
				else if (this.isMouseover() || xcontainer.getXContextMenu() == this.getXContextMenu())
				{
					bg = FontsAndColors.EVENT_BG_HOVER;
					fg = FontsAndColors.EVENT_FG_HOVER;
				}
				else
				{
					bg = FontsAndColors.EVENT_BG_NORMAL;
					fg = FontsAndColors.EVENT_FG_NORMAL;
				}
				g.setColor(bg);
				g.fill(new Rectangle(0, 0, this.getWidth(), this.getNameLabel().getHeight() + 7));
				this.setForeground(fg);
			}
			
			@Override public void clicked(Component c)
			{
				super.clicked(c);
				if (c != this.getOptionsButton() && !(c instanceof Selectedtag) && !(c.getParent() instanceof Selectedtag))
				{
					controller.setActiveEvent(this.getEventData());
				}
			}

			@Override public void rightclicked(Component c, MouseEvent e)
			{
				super.rightclicked(c, e);
				View.this.createAndShowContextMenu(this, e.getPoint());
			}

			@Override public void entered()
			{
				super.entered();
				if (properties.isVisible()) properties.setData(this.getEventData());
			}

			@Override public void exited()
			{
				super.exited();
				if (properties.isVisible()) properties.setData(null);
			}

			@Override public void dropImport(Object o)
			{
				if (o instanceof TagData)
				{
					TagData tagdata = (TagData) o;
					if (!this.getEventData().containsTagData(tagdata))
					{
						controller.toggleTag(this.getEventData(), tagdata);
					}
				}
			}
		};
		event.setXContextMenu(new ContextMenu(event, event.getEventData().getLabel())
		{
			@Override public void handleClick(String item_text)
			{
				EventData eventdata = ((Event) this.getXComponent()).getEventData();

				if (item_text.equals("Select")) controller.setActiveEvent(eventdata);
				if (item_text.equals("Edit")) View.this.toggleEditEvent(eventdata);
				if (item_text.equals("Delete")) View.this.deleteEvent(eventdata);
			}
		});
		event.getOptionsButton().addMouseListener(new MouseAdapter()
		{
			@Override public void mouseClicked(MouseEvent e)
			{
				View.this.createAndShowContextMenu(event, SwingUtilities.convertPoint(event, event.getOptionsButton().getLocation(), xcontainer));
			}
		});
		refreshEvent(event);
		events.add(event);
		addViewListener(event);
	}

	public void refreshEvents()
	{
		events_panel.getEventsList().removeAll();
		for (Event event : events)
		{
			refreshEvent(event);

			// if selected
			EventData eventdata = controller.getActiveEvent();
			if (event.getEventData() == eventdata)
			{
				event.setSelected(true);
				for (Component c : tags_panel.getTagsList().getComponents())
				{
					Tag tag = (Tag) c;
					tag.setSelected(eventdata.containsTagData(tag.getTagData()));
				}
			}
			else
			{
				event.setSelected(false);
			}
			events_panel.getEventsList().add(event);
		}
		invalidate();
		validate();
	}
	
	private void refreshEvent(Event event)
	{
		event.getTagsList().removeAll();
		for (TagData tagdata : event.getEventData().getTags())
		{
			Selectedtag selectedtag = new Selectedtag(event.getEventData(), tagdata)
			{
				@Override public void clicked(Component c)
				{
					if (c == this.getUnselectButton())
					{
						controller.toggleTag(this.getEventData(), this.getTagData());
					}
					else
					{
						scrollTo(this.getTagData());
					}
				}
			};
			selectedtag.setFont(FontsAndColors.segoe_ui);
			event.getTagsList().add(selectedtag);
			addViewListener(selectedtag);
		}
	}

	private float base_font_size = 14;

	private void zoomIn()
	{
		if (base_font_size < 26)
		{
			base_font_size += 1;
			FontsAndColors.segoe_ui = FontsAndColors.segoe_ui.deriveFont(base_font_size);
			FontsAndColors.segoe_ui_light = FontsAndColors.segoe_ui_light.deriveFont(base_font_size);
			FontsAndColors.segoe_ui_symbol = FontsAndColors.segoe_ui_symbol.deriveFont(base_font_size);
			fireFontSizeChanged(new ViewEvent(this, 1));
		}
	}
	private void zoomOut()
	{
		if (base_font_size > 8)
		{
			base_font_size -= 1;
			FontsAndColors.segoe_ui = FontsAndColors.segoe_ui.deriveFont(base_font_size);
			FontsAndColors.segoe_ui_light = FontsAndColors.segoe_ui_light.deriveFont(base_font_size);
			FontsAndColors.segoe_ui_symbol = FontsAndColors.segoe_ui_symbol.deriveFont(base_font_size);
			fireFontSizeChanged(new ViewEvent(this, -1));
		}
	}

	////////////////////////
	//                    //
	//  LISTENER METHODS  //
	//                    //
	////////////////////////

	private EventListenerList listener_list = new EventListenerList();

	public void addViewListener(ViewListener listener)
	{
		listener_list.add(ViewListener.class, listener);
	}

	public void removeViewListener(ViewListener listener)
	{
		listener_list.remove(ViewListener.class, listener);
	}

	public void fireFontSizeChanged(ViewEvent e)
	{
		Object[] list = listener_list.getListenerList();
		for (int i = 0; i < list.length; i += 2)
		{
			if (list[i] == ViewListener.class)
			{
				((ViewListener) list[i+1]).fontSizeChanged(e);
			}
		}
	}

	public void firePropertiesChanged(ViewEvent e)
	{
		Object[] list = listener_list.getListenerList();
		for (int i = 0; i < list.length; i += 2)
		{
			if (list[i] == ViewListener.class)
			{
				((ViewListener) list[i+1]).propertiesChanged(e);
			}
		}
	}

	public void refreshData(Object data)
	{
		fireDataUpdated(new ViewEvent(this, data));
	}

	public void fireDataUpdated(ViewEvent e)
	{
		Object[] list = listener_list.getListenerList();
		for (int i = 0; i < list.length; i += 2)
		{
			if (list[i] == ViewListener.class)
			{
				((ViewListener) list[i+1]).dataUpdated(e);
			}
		}
	}
}