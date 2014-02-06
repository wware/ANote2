package pt.uminho.anote2.aibench.corpus.gui.help.listrenders;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.sql.SQLException;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import pt.uminho.anote2.aibench.corpus.gui.help.listnodes.Top10Term;
import pt.uminho.anote2.aibench.corpus.structures.CorporaProperties;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.annotation.properties.AnnotationColorStyleProperty;

public class ListTop10TermsRenderer extends DefaultListCellRenderer{

	@Override
	public Component getListCellRendererComponent(JList list, Object value,int index, boolean isSelected, boolean cellHasFocus) {
		Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		if(value instanceof Top10Term)
		{
			Top10Term cod = (Top10Term) value;
			Color color = Color.blue;
			try {
				color = Color.decode(((AnnotationColorStyleProperty) CorporaProperties.getCorporaClassColor(cod.getClassID())).getColor());
			} catch (SQLException e) {
				TreatExceptionForAIbench.treatExcepion(e);
			} catch (DatabaseLoadDriverException e) {
				TreatExceptionForAIbench.treatExcepion(e);
			}
			this.setForeground(color);
			this.setFont(new Font("Arial", Font.BOLD, 12));	
		}
		return component;
	}

}
