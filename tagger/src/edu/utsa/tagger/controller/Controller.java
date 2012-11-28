package edu.utsa.tagger.controller;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Collection;

import edu.utsa.tagger.model.EventData;
import edu.utsa.tagger.model.Model;
import edu.utsa.tagger.model.TagData;
import edu.utsa.tagger.view.View;
import edu.utsa.tagger.view.ViewEvent;

public class Controller
{
	// for testing
	public static void main(String[] args)
	{   // Must pass in path to specification as a command line argument
		String tags = "";
		String events = ToolBox.TEST_EVENTS;
		String path = "";
		if (args.length > 0)
			path = args[0];
		try
		{
			File file = new File(path);
			tags = ToolBox.fileToString(file, Charset.forName("UTF-8"));
			//System.out.println("events: " + events);
			//System.out.println("tags: " + tags);
			String result = showDialog(tags, events);
			//System.out.println(result);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		System.exit(0);
	}
	
	public static String showDialog(String tags_string, String events_string) throws InterruptedException
	{
		Controller controller = new Controller(tags_string, events_string);
		while (controller.view.isShowing())
		{
			Thread.sleep(100);
		}
		return controller.model.getReturnString();
	}

	private Model model = null;
	private View view = null;
	private EventData active_event = null;
	private TagData active_tag = null;

	public Controller(String tags_string, String events_string)
	{
		model = new Model();
		view = new View(this);
		model.loadTags(tags_string);
		view.loadTags(model.getTags());
		model.loadEvents(events_string);
		view.loadEvents(model.getEvents());
	}

	////////////
	// EVENTS //
	////////////
	
	public EventData addEvent()
	{
		return model.addEvent();
	}

	public void updateEventData(EventData eventdata, String code, String name, String description)
	{
		model.updateEventCode(eventdata, code);
		model.updateEventName(eventdata, name);
		model.updateEventDescription(eventdata, description);
		view.refreshData(eventdata);
	}

	public void removeEvent(EventData event)
	{
		model.removeEvent(event);
		view.loadEvents(model.getEvents());
	}

	public void setActiveEvent(EventData eventdata)
	{
		active_event = eventdata;
		view.refreshEvents();
	}

	public EventData getActiveEvent()
	{
		return active_event;
	}
	
	//////////
	// TAGS //
	//////////

	public void setActiveTag(TagData tagdata)
	{
		active_tag = tagdata;
		view.refreshEvents();
	}

	public TagData getActiveTag()
	{
		return active_tag;
	}
	
	public void addTag(TagData parent_tagdata)
	{
		TagData child_tagdata = model.addTag(parent_tagdata);
		view.addTag(child_tagdata);
		view.refreshTags();
		view.firePropertiesChanged(new ViewEvent(view, child_tagdata));
		view.scrollTo(child_tagdata);
	}

	public void updateTagData(TagData tagdata, String name, String description)
	{
		model.updateTagName(tagdata, name);
		model.updateTagDescription(tagdata, description);
		view.refreshData(tagdata);
	}

	public void removeTag(TagData tagdata)
	{
		view.firePropertiesChanged(new ViewEvent(view, null));
		model.removeTag(tagdata);
		view.loadTags(model.getTags());
		view.loadEvents(model.getEvents());
	}

	public void toggleTag(EventData event, TagData tag)
	{
		model.toggleTag(event, tag);
		view.refreshEvents();
	}

	public void toggleTag(TagData tag)
	{
		toggleTag(active_event, tag);
	}
	
	public void selectTag(TagData tagdata)
	{
		model.selectTag(active_event, tagdata);
		view.refreshEvents();
	}
	
	public void unselectTag(TagData tagdata)
	{
		model.unselectTag(active_event, tagdata);
		view.refreshEvents();
	}

	public Collection<TagData> searchTagsData(String s)
	{
		return model.search(s);
	}
}
