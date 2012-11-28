package edu.utsa.xgui;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;


@SuppressWarnings("serial")
public class XContainer extends JLayeredPane
{
	private ArrayList<XComponent> new_list = new ArrayList<XComponent>();

	private XContentPane content_pane = new XContentPane();
	private XContextMenu xcontext_menu;
	private XDragPane drag_pane = new XDragPane();
	private XComponent potential_drag = null;
	private Point point = new Point();
	private XScrollPane deepestxscrollpane = null;

	public XContainer()
	{
		setLayout(new XLayout());
		setLayer(content_pane, 0);
		setLayer(drag_pane, 2);
		add(content_pane, new Anchor(Anchor.TL, Anchor.STRETCHALL));
		add(drag_pane, new Anchor(2, Anchor.TL, Anchor.STRETCHALL));

		EventQueue event_queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
		event_queue.push(new EventQueue()
		{
			@Override protected void dispatchEvent(AWTEvent e)
			{
				try
				{
					if (e instanceof MouseEvent)
					{
						MouseEvent mouse_event = (MouseEvent) e;
						mouse_event = SwingUtilities.convertMouseEvent(mouse_event.getComponent(), mouse_event, content_pane);
						handleMouseEvent(mouse_event);
					}
					if (e instanceof KeyEvent)
					{
						KeyEvent key_event = (KeyEvent) e;
						handleKeyEvent(key_event);
					}
					super.dispatchEvent(e);
				}
				catch (Throwable t)
				{
					t.printStackTrace();
				}
			}
		});
	}

	public void setContextMenu(XContextMenu xcontext_menu, Point location)
	{
		if (this.xcontext_menu != null)
		{
			remove(this.xcontext_menu);
			this.xcontext_menu = null;
		}
		this.xcontext_menu = xcontext_menu;
		setLayer(xcontext_menu, 1);
		add(xcontext_menu, new Anchor(1, Anchor.TL, Anchor.NOSTRETCH, new Margins(location.y, location.x, 0, 0)));
	}
	
	public XContextMenu getXContextMenu()
	{
		return xcontext_menu;
	}

	public XContentPane getContentPane()
	{
		return content_pane;
	}

	private void handleKeyEvent(KeyEvent e)
	{
		if (deepestxscrollpane != null && e.getID() == KeyEvent.KEY_PRESSED)
		{
			switch (e.getKeyCode())
			{
			case KeyEvent.VK_PAGE_UP:
				deepestxscrollpane.pageUp();
				break;
			case KeyEvent.VK_PAGE_DOWN:
				deepestxscrollpane.pageDown();
				break;
			}
		}	
	}
	
	private void handleMouseEvent(MouseEvent e)
	{
		Component deepest;
		XComponent deepestx;

		point.x = e.getX();
		point.y = e.getY();
		
		if (this.xcontext_menu == null)
		{
			deepest = SwingUtilities.getDeepestComponentAt(content_pane, point.x, point.y);
		}
		else
		{
			deepest = SwingUtilities.getDeepestComponentAt(this, point.x, point.y);
		}
		deepestx =  getDeepestXComponentAt(deepest);
		deepestxscrollpane = getDeepestXScrollPaneAt(deepestx);
		
		switch (e.getID())
		{
		
		case MouseEvent.MOUSE_WHEEL:
			
			if (this.xcontext_menu != null)
			{
				remove(this.xcontext_menu);
				this.xcontext_menu = null;
			}
			break;
			
		case MouseEvent.MOUSE_CLICKED:
			
			if (this.xcontext_menu != null)
			{
				remove(this.xcontext_menu);
				this.xcontext_menu = null;
			}
			for (XComponent xc : new_list)
			{
				if (e.getButton() == MouseEvent.BUTTON3)
				{
					if (xc == deepest) xc.rightclicked();
					xc.rightclicked(deepest, e);
				}
				else
				{
					if (xc == deepest) xc.clicked();
					xc.clicked(deepest);
				}
			}
			break;
			
		case MouseEvent.MOUSE_PRESSED:
			
			potential_drag = getDeepestDraggableXComponentAt(deepest);
			break;
			
		case MouseEvent.MOUSE_DRAGGED:
			
			if (potential_drag != null)
			{
				drag_pane.setDragged(potential_drag);
				potential_drag = null;
			}
			else
			{
				if (deepestx != drag_pane.getDragged())
				{
					drag_pane.setPendingDrop(getDeepestDroppableXComponentAt(deepest));
				}
				else
				{
					drag_pane.setPendingDrop(null);
				}
				drag_pane.repaint();
			}
			break;
			
		case MouseEvent.MOUSE_RELEASED:
			
			if (drag_pane.getDragged() != null)
			{
				drag_pane.release();
				content_pane.repaint();
			}
			break;
			
		}
		
		if (e.getID() == MouseEvent.MOUSE_MOVED || 
				e.getID() == MouseEvent.MOUSE_EXITED ||
				e.getID() == MouseEvent.MOUSE_RELEASED)
		{
			ArrayList<XComponent> old_list = new ArrayList<XComponent>(new_list);
			new_list = getNewList(deepestx);

			Queue<XComponent> exited_stack = new LinkedList<XComponent>();
			Stack<XComponent> entered_queue = new Stack<XComponent>();

			for (XComponent xc : old_list)
			{
				if (!new_list.contains(xc))
				{
					xc.setMouseover(false);
					xc.setMousedirectlyover(false);
					exited_stack.offer(xc);
					xc.repaint();
				}
			}

			for (XComponent xc : new_list)
			{
				xc.setMousedirectlyover(xc == deepest);
				if (!old_list.contains(xc))
				{
					xc.setMouseover(true);
					entered_queue.push(xc);
				}
				xc.repaint();
			}

			while (!exited_stack.isEmpty()) exited_stack.poll().exited();
			while (!entered_queue.isEmpty()) entered_queue.pop().entered();
		}
	}

	private XComponent getDeepestDraggableXComponentAt(Component c)
	{
		while (c != null && !(c instanceof XComponent && ((XComponent) c).isDraggable()))
		{
			c = c.getParent();
		}
		return (XComponent) c;
	}

	private XComponent getDeepestDroppableXComponentAt(Component c)
	{
		while (c != null && !(c instanceof XComponent && ((XComponent) c).isDroppable()))
		{
			c = c.getParent();
		}
		return (XComponent) c;
	}

	private XComponent getDeepestXComponentAt(Component c)
	{
		while (c != null && !(c instanceof XComponent))
		{
			c = c.getParent();
		}
		return (XComponent) c;
	}
	
	private XScrollPane getDeepestXScrollPaneAt(Component c)
	{
		while (c != null && !(c instanceof XScrollPane))
		{
			c = c.getParent();
		}
		return (XScrollPane) c;
	}

	private ArrayList<XComponent> getNewList(Component c)
	{
		ArrayList<XComponent> list = new ArrayList<XComponent>();
		while (c != null)
		{
			if (c instanceof XComponent)
			{
				list.add((XComponent) c);
			}
			c = c.getParent();
		}
		return list;
	}

}
