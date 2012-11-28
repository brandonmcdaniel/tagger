package edu.utsa.tagger.model;

import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="node")
@XmlAccessorType(XmlAccessType.NONE)
public class TagData
{
	private TagData parent;
	private String path = "/";
	
	@XmlElement(name="node") private Collection<TagData> child_tags = new ArrayList<TagData>();
	@XmlElement(name="name") private String label;
	@XmlAttribute(name="custom") private boolean custom;
	@XmlAttribute(name="default") private boolean dfault;
	@XmlAttribute(name="extensionAllowed") private boolean extension_allowed;
	@XmlAttribute(name="requireChild") private boolean require_child;
	@XmlElement(name="description") private String description;
	
	TagData()
	{
		this.parent = null;
		this.label = "(new tag)";
		this.path = "";
		this.description = "";
	}
	
	public TagData getParent() { return parent; }
	public Collection<TagData> getChildTags() { return child_tags; }
	public String getPath() { return path; }
	public String getFullPath() { return path + label; }
	public String getLabel() { return label; }
	public String getDescription() { return description; }
	public boolean isCustom() { return custom; }
	public boolean isDefault() { return dfault; }
	public boolean isExtensionAllowed() { return extension_allowed; }
	public boolean isChildRequired() { return require_child; }
	
	void setParent(TagData parent) { this.parent = parent; }
	void setPath(String path) { this.path = path; }
	void setLabel(String label) { this.label = label; }
	void setDescription(String description) { this.description = description; }
	void setCustom(boolean custom) { this.custom = custom; }
	
	void updatePath()
	{
		TagData tag = this;
		path = "/";
		while ((tag = tag.getParent()) != null)
		{
			path = "/" + tag.getLabel() + path;
		}
	}
	
	public int getTagDepth()
	{
		int depth = 0;
		TagData t = this;
		while ((t = t.getParent()) != null) depth++;
		return depth;
	}
}
