package edu.utsa.tagger.model;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public final class Model
{
	private Collection<EventData> events = new ArrayList<EventData>();
	private HedData hed = null;
	private Collection<TagData> tags = new ArrayList<TagData>();

	public Model() {}

	public void loadTags(String tags_string)
	{
		try
		{
			StringReader stringreader = new StringReader(tags_string);
			JAXBContext context = JAXBContext.newInstance(HedData.class);
			hed = (HedData) context.createUnmarshaller().unmarshal(stringreader);
			tags = hed.getTags();
		}
		catch (JAXBException e)
		{
			e.printStackTrace();
		}

		for (TagData tag : tags)
		{
			updateTagParents(tag);
		}
		for (TagData tag : tags)
		{
			updateTagPaths(tag);
		}
	}

	public void loadEvents(String event_strings)
	{
		String [] splitstring = event_strings.split(";");
		for (String s : splitstring)
		{
			String[] data = s.split(",");
			if (data.length < 3)
			{
				System.out.println("Invalid event data format.");
				return;
			}

			EventData event = new EventData();
			event.setCode(data[0]);
			event.setLabel(data[1]);
			event.setDescription(data[2]);
			events.add(event);
			Collection<TagData> event_tags = event.getTags();
			for (int i = 3; i < data.length; i++)
			{
				TagData tag = getTagFromFullPath(data[i], tags);
				if (tag != null)
				{
					event_tags.add(tag);
				}
			}
		}
	}

	private static TagData getTagFromFullPath(String fullpath, Collection<TagData> tags)
	{
		for (TagData tag : tags)
		{
			if ((tag.getPath() + tag.getLabel()).equals(fullpath))
			{
				return tag;
			}
			TagData temp = getTagFromFullPath(fullpath, tag.getChildTags());
			if (temp != null)
			{
				return temp;
			}
		}
		return null;
	}

	public String getReturnString()
	{
		String list = marshalTags();

		for (EventData event : events)
		{
			String tmp = "";
			tmp += event.getCode() + ",";
			tmp += event.getLabel() + ",";
			tmp += event.getDescription() + ",";

			Collection<TagData> event_tags = event.getTags();
			for (TagData event_tag : event_tags)
			{
				tmp += event_tag.getFullPath() + ",";
			}
			tmp = tmp.substring(0, tmp.length()-1);
			list += ";" + tmp;
		}

		return list;
	}

	private String marshalTags()
	{
		try
		{
			StringWriter stringwriter = new StringWriter();
			JAXBContext context = JAXBContext.newInstance(HedData.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.marshal(hed, stringwriter);
			return stringwriter.toString();
		}
		catch (JAXBException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public Collection<TagData> search(String s)
	{
		Collection<TagData> results = new ArrayList<TagData>();
		searchHelper(s, results, tags);
		return results;
	}

	private static void searchHelper(String s, Collection<TagData> results, Collection<TagData> tags)
	{
		for (TagData tag : tags)
		{
			if (tag.getLabel().toLowerCase().indexOf(s.toLowerCase()) != -1)
			{
				results.add(tag);
			}
			searchHelper(s, results, tag.getChildTags());
		}
	}

	public TagData addTag(TagData parent)
	{
		TagData tag = new TagData();
		tag.setParent(parent);
		tag.setPath(parent.getFullPath() + "/");
		tag.setCustom(true);
		parent.getChildTags().add(tag);
		return tag;
	}

	public void removeTag(TagData tag)
	{
		tag.getParent().getChildTags().remove(tag);
		for (EventData event : events)
		{
			Collection<TagData> event_tags = event.getTags();
			if (event_tags.contains(tag))
			{
				event_tags.remove(tag);
			}
		}
	}

	public void updateTagName(TagData tag, String name)
	{
		tag.setLabel(name);
		for (TagData child_tag : tag.getChildTags())
		{
			updateTagPaths(child_tag);
		}
	}

	public void updateTagParents(TagData tagdata)
	{
		for (TagData child_tagdata : tagdata.getChildTags())
		{
			child_tagdata.setParent(tagdata);
			updateTagParents(child_tagdata);
		}
	}

	public void updateTagPaths(TagData tagdata)
	{
		tagdata.updatePath();
		for (TagData child_tagdata : tagdata.getChildTags())
		{
			updateTagPaths(child_tagdata);
		}
	}

	public void updateTagDescription(TagData tag, String description)
	{
		tag.setDescription(description);
	}

	public void toggleTag(EventData eventdata, TagData tagdata)
	{
		if (eventdata == null)
		{
			return;
		}
		Collection<TagData> event_tags = eventdata.getTags();
		if (event_tags.contains(tagdata))
		{
			event_tags.remove(tagdata);
		}
		else
		{
			event_tags.add(tagdata);
		}
	}
	
	public void selectTag(EventData eventdata, TagData tagdata)
	{
		if (eventdata == null)
		{
			return;
		}
		Collection<TagData> event_tags = eventdata.getTags();
		if (!event_tags.contains(tagdata))
		{
			event_tags.add(tagdata);
		}
	}
	
	public void unselectTag(EventData eventdata, TagData tagdata)
	{
		if (eventdata == null)
		{
			return;
		}
		Collection<TagData> event_tags = eventdata.getTags();
		if (event_tags.contains(tagdata))
		{
			event_tags.remove(tagdata);
		}
	}

	public Collection<TagData> getTags()
	{
		return tags;
	}

	public EventData addEvent()
	{
		EventData event = new EventData();
		events.add(event);
		return event;
	}

	public void removeEvent(EventData event)
	{
		events.remove(event);
	}

	public void updateEventCode(EventData event, String code)
	{
		event.setCode(code);
	}

	public void updateEventName(EventData event, String name)
	{
		event.setLabel(name);
	}

	public void updateEventDescription(EventData event, String description)
	{
		event.setDescription(description);
	}

	public Collection<EventData> getEvents()
	{
		return events;
	}
}
