package pt.uminho.anote2.datastructures.utils;

import java.util.List;
import java.util.Set;

import javax.swing.ImageIcon;

public class MenuItem {
	


	public int id;
	public int level;
	public String name;
	public ImageIcon icon;
	public String url;
	public List<MenuItem> subitems;
	public Set<Integer> sourcesLinkage;
	
	public MenuItem(int id,String name,ImageIcon icon,String url,List<MenuItem> subItems,int level,Set<Integer> sourcesLinkage)
	{
		this.id = id;
		this.name = name;
		this.icon = icon;
		this.url = url;
		this.subitems = subItems;
		this.level = level;
		this.sourcesLinkage = sourcesLinkage;
	}
	
	public void setIcon(ImageIcon icon) {
		this.icon = icon;
	}

	public boolean haveSubMenuItems()
	{
		return (subitems!=null && subitems.size()>0);
	}

	public int getId() {
		return id;
	}

	public int getLevel() {
		return level;
	}

	public String getName() {
		return name;
	}

	public ImageIcon getIcon() {
		return icon;
	}

	public String getUrl() {
		return url;
	}

	public List<MenuItem> getSubitems() {
		return subitems;
	}

	public Set<Integer> getSourcesLinkage() {
		return sourcesLinkage;
	}
	
	public String toString()
	{
		String toString = new String();
		if(getName()!=null && getName().length()>0)
			toString = getName() + " ( " + getId() + ") ";
		else
			toString = String.valueOf(getId());
		return toString;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setName(String name) {
		this.name = name;
	}


	public void setUrl(String url) {
		this.url = url;
	}

	public void setSubitems(List<MenuItem> subitems) {
		this.subitems = subitems;
	}

	public void setSourcesLinkage(Set<Integer> sourcesLinkage) {
		this.sourcesLinkage = sourcesLinkage;
	}
}
