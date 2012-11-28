package edu.utsa.tagger.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="HED")
@XmlAccessorType(XmlAccessType.FIELD)
public class HedData
{
	@XmlAttribute(name="version") private String version;
    @XmlElement(name="node") private Collection<TagData> tags = new ArrayList<TagData>();

    Collection<TagData> getTags()
    {
        return tags;
    }
    
    @Override public String toString()
    {
    	String s = "";
    	for (TagData n : tags)
    	{
    		s += n.toString() + "\n";
    	}
    	return s;
    }

}

