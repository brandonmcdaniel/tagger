package edu.utsa.xgui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JLayeredPane;
import javax.swing.Timer;

import edu.utsa.tagger.view.Properties;

@SuppressWarnings("serial")
public abstract class XComponent extends JLayeredPane
{
	private boolean mouseover = false;
	private boolean mousedirectlyover = false;
	private boolean mousepressed = false;
	private boolean draggable = false;
	private boolean droppable = false;
	private boolean being_dragged = false;
	private boolean pending_drop = false;
	
	private XContextMenu xcontextmenu;
	
	public XComponent()
	{
		setLayout(new XLayout());
	}
	
	@Override final protected void paintComponent(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		draw(g2d);
	}
	
	final public boolean isMouseover() { return mouseover; }
	final public boolean isMousedirectlyover() { return mousedirectlyover; }
	final public boolean isMousepressed() { return mousepressed; }
	final public boolean isDraggable() { return draggable; }
	final public boolean isDroppable() { return droppable; }
	final public boolean isBeingDragged() { return being_dragged; }
	final public boolean isPendingDrop() { return pending_drop; }
	final public boolean isActive() { return mouseover || being_dragged || pending_drop; }
	
	final void setMouseover(boolean mouseover) {
		this.mouseover = mouseover;
	}
	final void setMousedirectlyover(boolean mousedirectlyover) {
		this.mousedirectlyover = mousedirectlyover;
	}
	final void setMousePressed(boolean mousepressed) { this.mousepressed = mousepressed; }
	final public void setDraggable(boolean draggable) { this.draggable = draggable; }
	final public void setDroppable(boolean droppable) { this.droppable = droppable; }
	final void setBeingDragged(boolean being_dragged) { this.being_dragged = being_dragged; }
	final void setPendingDropped(boolean pending_drop) { this.pending_drop = pending_drop; }
	
	final public XContextMenu getXContextMenu() { return xcontextmenu; }
	final public void setXContextMenu(XContextMenu xcontextmenu) { this.xcontextmenu = xcontextmenu; }
	
	public void swipeIn()
	{
		if (!isVisible())
		{
			setVisible(true);
			Dimension d = getPreferredSize();
			setPreferredSize(null);
			desired_height = getPreferredSize().height;
			setPreferredSize(d);
			current_height = 0;
			timer.start();
		}
	}

	public void swipeOut()
	{
		if (isVisible())
		{
			desired_height = 0;
			current_height = getPreferredSize().height;
			timer.start();
		}
	}
	
	int desired_height = 0;
	int current_height = 0;
	private Timer timer = new Timer(20, new ActionListener()
	{
		@Override public void actionPerformed(ActionEvent e)
		{
			double speed = 2;
			int step = (int) ((desired_height - current_height) * (1 - 1/speed));
			setPreferredSize(new Dimension(0, getHeight() + step));
			current_height += step;
			if (step == 0)
			{
				Dimension d = desired_height == 0 ? new Dimension(0, 0) : null;
				setPreferredSize(d);
				timer.stop();
				if (desired_height == 0)
				{
					setVisible(false);
				}
			}
			XComponent.this.revalidate();
		}
	});
	
	// *******************
	// OVERRIDABLE METHODS
	// *******************
	
	public void entered() {}
	public void enteredDescendent(Component descendent) {}
	public void exited() {}
	public void exitedDescendent(Component descendent) {}
	public void moved() {}
	public void movedDescendent(Component descendent) {}
	public void pressed() {}
	public void released() {}
	public void clicked() {}
	public void clicked(Component xcomponent) {}
	public void rightclicked() {}
	public void rightclicked(Component xcomponent, MouseEvent e) {}
	public Object dragExport() { return null; }
	public void dropImport(Object o) {}
	public String getDragText() { return null; }
	public String getDropActionText(XComponent being_dragged) { return null; }
	public String getDropText() { return null; }
	public void draw(Graphics2D g) { super.paintComponent(g); }
	
}
