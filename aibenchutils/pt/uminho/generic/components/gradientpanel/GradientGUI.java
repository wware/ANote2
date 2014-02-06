package pt.uminho.generic.components.gradientpanel;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/**
 * DynamicModelSimulator
 * Created By
 * User: ptiago
 * Date: Mar 6, 2009
 * Time: 5:45:56 PM
 */
public class GradientGUI extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Color color1;
    protected Color color2;
    protected GradientType gradientType;
    protected boolean isAcyclic;

    public GradientGUI(){
        color1 = getBackground();
        color2 = color1.darker();
        isAcyclic = false;
        gradientType = GradientType.HORIZONTAL;
    }
    
    public GradientGUI(Color color1, Color color2) {
        this.color1 = color1;
        this.color2 = color2;
    }
    
    public GradientGUI(Color color1,Color color2,GradientType gradientType,boolean isAcyclic){
    	this.color1 = color1;
    	this.color2 = color2;
    	this.gradientType = gradientType;
    	this.isAcyclic = isAcyclic;
    }

    protected void paintComponent( Graphics g ){
    if ( !isOpaque( ) ){
        super.paintComponent( g );
        return;
    }

    try {
		paintBackground(g);
	} catch (InvalidGradientTypeException e) {
		e.printStackTrace();
		System.exit(ABORT);
	}


    setOpaque(false);
    super.paintComponent(g);
    setOpaque(true);
}

    protected void paintBackground(Graphics g) throws InvalidGradientTypeException {
        Graphics2D g2d = (Graphics2D)g;

        int width = getWidth( );
        int height = getHeight( );

        // Paint a gradient from top to bottom
        GradientPaint gp = generateGradientPaint(width,height);

        g2d.setPaint(gp);
        g2d.fillRect( 0, 0, width,height);

    }
    
    public GradientPaint generateGradientPaint(int width,int height) throws InvalidGradientTypeException{
    	
    	switch(gradientType){
    		case HORIZONTAL:
    			return new GradientPaint(0, 0, color1,0,height, color2,isAcyclic);
    		case VERTICAL:
    			return new GradientPaint(0,0,color1,width,0,color2,isAcyclic);
    		case DIAGONAL:
    			return new GradientPaint(0,0,color1,width,height,color2,isAcyclic);
    	}
    	
    	throw new InvalidGradientTypeException();
    }

	public void setColor1(Color color1) {
		this.color1 = color1;
	}

	public void setColor2(Color color2) {
		this.color2 = color2;
	}

	public void setGradientType(GradientType gradientType) {
		this.gradientType = gradientType;
	}

	public void setAcyclic(boolean isAcyclic) {
		this.isAcyclic = isAcyclic;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Color getColor1() {
		return color1;
	}

	public Color getColor2() {
		return color2;
	}

	public GradientType getGradientType() {
		return gradientType;
	}

	public boolean isAcyclic() {
		return isAcyclic;
	}
    
    
}
