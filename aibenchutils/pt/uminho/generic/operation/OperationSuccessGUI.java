package pt.uminho.generic.operation;


/**
*
* @author pedro
*/
public class OperationSuccessGUI extends javax.swing.JPanel {

   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/** Creates new form OperationSuccessGUI */
   public OperationSuccessGUI() {
       initComponents();
   }

   /** This method is called from within the constructor to
    * initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is
    * always regenerated by the Form Editor.
    */
   // <editor-fold defaultstate="collapsed" desc="Generated Code">
   private void initComponents() {

       jLabel1 = new javax.swing.JLabel();
       jLabel2 = new javax.swing.JLabel();

       jLabel1.setFont(new java.awt.Font("DejaVu Sans", 3, 36)); // NOI18N
       jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
       jLabel1.setText("SUCCESS");

       jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
       jLabel2.setText("The results will be posted in Clipboard");

       javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
       this.setLayout(layout);
       layout.setHorizontalGroup(
           layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
           .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
               .addContainerGap()
               .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                   .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                   .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE))
               .addContainerGap())
       );
       layout.setVerticalGroup(
           layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
           .addGroup(layout.createSequentialGroup()
               .addGap(23, 23, 23)
               .addComponent(jLabel1)
               .addGap(38, 38, 38)
               .addComponent(jLabel2)
               .addContainerGap(179, Short.MAX_VALUE))
       );
   }// </editor-fold>


   // Variables declaration - do not modify
   private javax.swing.JLabel jLabel1;
   private javax.swing.JLabel jLabel2;
   // End of variables declaration

}
