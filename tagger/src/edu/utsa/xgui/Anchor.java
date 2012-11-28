package edu.utsa.xgui;

import java.awt.Component;
import java.util.ArrayList;

public class Anchor
{
	public static final int TL = 1;
	public static final int TC = 2;
	public static final int TR = 3;
	public static final int CL = 4;
	public static final int CC = 5;
	public static final int CR = 6;
	public static final int BL = 7;
	public static final int BC = 8;
	public static final int BR = 9;

	public static final int TOP = 1;
	public static final int BOTTOM = 2;
	public static final int LEFT = 3;
	public static final int RIGHT = 4;
	
	public static final int NOSTRETCH = 0;
	public static final int HORIZONTAL = 1;
	public static final int VERTICAL = 2;
	public static final int STRETCHALL = 3;

	public int layer;
	public int container_anchor;
	public int component_anchor;
	public int stretch;
	public Margins margins;
	public Component fit_to;

	Component component;
	Component anchor_to;
	ArrayList<Component> child_nodes;

	public Anchor()
	{
		layer = 0;
		container_anchor = TL;
		component_anchor = RIGHT;
		stretch = NOSTRETCH;
		margins = new Margins(0, 0, 0, 0);
		fit_to = null;

		component = null;
		anchor_to = null;
		child_nodes = new ArrayList<Component>();
	}
	
	public void setAnchorTo(Component anchor_to) { this.anchor_to = anchor_to; }
	public void setContainerAnchor(int anchor) { this.container_anchor = anchor; }
	public void setComponentAnchor(int anchor) { this.component_anchor = anchor; }
	public void setLayer(int layer) { this.layer = layer; }

	public Anchor(int anchor, int stretch)
	{
		this();
		this.container_anchor = anchor;
		this.stretch = stretch;
	}
	
	public Anchor(int layer, int anchor, int stretch)
	{
		this(anchor, stretch);
		this.layer = layer;
	}
	
	public Anchor(int anchor, int stretch, Margins margins)
	{
		this(anchor, stretch);
		this.margins = margins;
	}
	
	public Anchor(int layer, int anchor, int stretch, Margins margins)
	{
		this(anchor, stretch, margins);
		this.layer = layer;
	}

	public Anchor(Component anchor_to, int layer, int anchor, int stretch, Margins margins)
	{
		this(layer, anchor, stretch, margins);
		this.anchor_to = anchor_to;
		this.component_anchor = anchor;
	}
	
	public Anchor(Component anchor_to, int layer, int anchor, int stretch)
	{
		this(anchor_to, layer, anchor, stretch, new Margins(0, 0, 0, 0));
	}
	
	public Anchor(Component anchor_to, int anchor, int stretch)
	{
		this(anchor_to, 0, anchor, stretch, new Margins(0, 0, 0, 0));
	}
	
	public Anchor(Component anchor_to, int anchor)
	{
		this(anchor_to, 0, anchor, NOSTRETCH, new Margins(0, 0, 0, 0));
	}
	
	public Anchor(Component anchor_to, int anchor, int stretch, Margins margins)
	{
		this(anchor_to, 0, anchor, stretch, margins);
	}

	public Anchor(Component anchor_to, int anchor, Margins margins, Component fit_to)
	{
		this();
		this.anchor_to = anchor_to;
		if (anchor_to == null) {
			this.container_anchor = anchor;
		} else {
			this.component_anchor = anchor;
		}
		this.margins = margins;
		this.fit_to = fit_to;
	}
}
