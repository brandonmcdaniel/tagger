package edu.utsa.tagger.model;
import java.util.ArrayList;
import java.util.Collection;

public class EventData
{
	private String code;
	private String label;
	private String description;
	private Collection<TagData> tags = new ArrayList<TagData>();

	EventData()
	{
		this.code = "";
		this.label = "(new event)";
		this.description = "";
	}
	
	public String getCode() { return code; }
	public String getLabel() { return label; }
	public String getDescription() { return description; }
	public Collection<TagData> getTags() { return tags; }
	
	void setCode(String code) { this.code = code; }
	void setLabel(String label) { this.label = label; }
	void setDescription(String description) { this.description = description; }
	
	public boolean containsTagData(TagData tagdata)
	{
		return tags.contains(tagdata);
	}
}
