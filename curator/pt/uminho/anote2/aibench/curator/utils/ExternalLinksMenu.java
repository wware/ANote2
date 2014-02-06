package pt.uminho.anote2.aibench.curator.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import pt.uminho.anote2.aibench.curator.gui.OpenUrlGUI;
import pt.uminho.anote2.aibench.utils.gui.Help;
import es.uvigo.ei.aibench.workbench.Workbench;

public class ExternalLinksMenu extends JMenu{

	private static final long serialVersionUID = 5348597845929126655L;
	
	private JEditorPane editPanel;

	public ExternalLinksMenu(JEditorPane editPanel){	
		super("LinkOut");
		this.editPanel = editPanel;	
		JMenu menu_ebi = new JMenu();
        menu_ebi.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/ebi.png")));	
        JMenuItem item = createItem("ChEBI", "chebi.png", "http://www.ebi.ac.uk/chebi/searchFreeText.do?searchString=#");
		menu_ebi.add(item);	
		item = createItem("Ensembl", "ensembl.png", "http://www.ensembl.org/Homo_sapiens/searchview?species=;idx=;q=#");
		menu_ebi.add(item);		
		JMenu menu_entrez = new JMenu();
		menu_entrez.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/entrez.png")));	
		item = createItem("Gene", "Gene.png", "http://www.ncbi.nlm.nih.gov/sites/entrez?db=gene&cmd=Search&term=#");
		menu_entrez.add(item);	
		item = createItem("Protein", "Protein.png", "http://www.ncbi.nlm.nih.gov/sites/entrez?db=protein&cmd=Search&term=#");
		menu_entrez.add(item);
		item = createItem("PubMed", "pubmed.png", "http://www.ncbi.nlm.nih.gov/sites/entrez?db=pubmed&cmd=Search&term=#");
		menu_entrez.add(item);       
		item = createItem("", "biocyc.png", "http://biocyc.org/substring-search?type=NIL&object=#");
		this.add(item);	
		this.add(menu_ebi);
		this.add(menu_entrez);	
		item = createItem("", "go1.png", "http://amigo.geneontology.org/cgi-bin/amigo/search.cgi?action=query;view=query;query=#;search_constraint=gp");
		this.add(item);	
		item = createItem("", "reactome.png", "http://www.reactome.org/cgi-bin/search2?DB=gk_current&OPERATOR=ALL&QUERY=#");
		this.add(item);	
		item = createItem("", "uniprot.png", "http://www.uniprot.org/uniprot/?query=#&sort=score");
		this.add(item);		
		JMenu menu_resources = new JMenu();
		menu_resources.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/search_bio.png")));	
		item = createItem("", "biogrid.png", "http://www.thebiogrid.org/search.php?keywords=#&organismid=0");
		this.add(item);//menu_resources.add(item);	
		item = createItem("", "ihop.png", "http://www.ihop-net.org/UniPub/iHOP/index.html?field=all&search=#&organism_id=0");
		this.add(item);//menu_resources.add(item);
		item = createItem("", "pfam.png", "http://www.sanger.ac.uk/cgi-bin/Pfam/getacc?#");
		this.add(item);//menu_resources.add(item);	
		item = createItem("Google", "google16.png", "http://www.google.pt/search?q=#");
		menu_resources.add(item);		
		menu_resources.addSeparator();			
		menu_resources.add(otherUrlItem());	
		this.add(menu_resources);	
		this.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/internet.png")));
	}
	
	public static  JMenuItem otherUrlItem(){
		JMenuItem item = new JMenuItem("Other");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				new OpenUrlGUI();
			}
		});
		return item;
	}
	
	public JMenuItem createItem(String title, String icon, String url){
		JMenuItem item = new JMenuItem(title);
		final String new_url = url;
		if(icon!=null)
			item.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/" + icon)));
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				String t = editPanel.getSelectedText();
				if(t==null)
					Workbench.getInstance().warn("No text selected!");
				else
					openURL(new_url.replace("#", t));
			}
		});
		return item;
	}
	
	/**
	 * @param url
	 */
	public void openURL(String url){
		Help.internetAcess(url);
	}

	public JEditorPane getEditPanel() {return editPanel;}
	public void setEditPanel(JEditorPane editPanel) {this.editPanel = editPanel;}
}
