package edu.utsa.tagger.view;

import java.util.EventListener;

public interface ViewListener extends EventListener
{
	public void fontSizeChanged(ViewEvent e);
	public void propertiesChanged(ViewEvent e);
	public void dataUpdated(ViewEvent e);
}
