import java.awt.*;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.*;
import javax.swing.event.MouseInputAdapter;



public class MigrationMatrix {
	
	private Migration Mig;
	private JTable Matrix;
	private DefaultTableModel MatrixTableModel;
	private JList RowHeader;
	private Vector ColumnNames;
	private	Vector Data;

	
	/**
	 * Der Konstruktor erzeugt die Matrix.
	 * @param NewMig
	 * @param Data
	 * @param ColumnNames
	 */		
	public MigrationMatrix(Migration NewMig, Vector Data, Vector ColumnNames)  {
		
		Mig = NewMig;
		MatrixTableModel = new DefaultTableModel(Data, ColumnNames) {
			public boolean isCellEditable(int row, int column) { 
				return false;	// readonly setting for all cells 
			}
		};

		Matrix = new JTable(MatrixTableModel);
		Matrix.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		Matrix.setRowHeight(25);
		Matrix.addMouseListener (
			new MouseInputAdapter() {
			  public void mouseClicked(MouseEvent ME) {
				 if (ME.getClickCount() == 1) {
//					reserviert f�r manuelles Setzen der Felder
				 }
			  }
		   }
		);
		
		TableColumn Column;
		TableCellRenderer CellRenderer = new TableCellRenderer();
		HeaderRenderer HRenderer = new HeaderRenderer();

		for (int i = 0; i < ColumnNames.size(); i++ ) {
			Column = Matrix.getColumnModel().getColumn(i);
			Column.setCellRenderer(CellRenderer);
			Column.setHeaderRenderer(HRenderer);
			Column.setPreferredWidth(25);
		}

		RowHeader = new JList(new TableListModel(Matrix));
		RowHeader.setFixedCellWidth(25);
		RowHeader.setFixedCellHeight(Matrix.getRowHeight());
		RowHeader.setCellRenderer(new RowHeaderRenderer(Matrix));
		RowHeader.setBackground(UIManager.getColor("TableHeader.background"));
		Mig.getScrollPanel().setRowHeaderView(RowHeader); // f�gt die Zeilenk�pfe hinzu
		
		Matrix.getTableHeader().setReorderingAllowed(false);
		Matrix.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		// verhindert das Zell-Rendering bei Mausbewegungen
		ToolTipManager.sharedInstance().unregisterComponent(Matrix); 
		ToolTipManager.sharedInstance().unregisterComponent(Matrix.getTableHeader());

	} // end MigrationMatrix (constructor)

	
	
	/**
	 * Gibt die Matrix zur�ck.
	 * @return Matrix
	 */
	public JTable getTable()  {

		return Matrix;

	} // end getTable


	
	/**
	 * Definiert das Aussehen der Spaltenk�pfe.
	 */
	public class HeaderRenderer extends DefaultTableCellRenderer {

		/**
		 * Zentriert den Inhalt der Spaltenk�pfe.
		 */
		public HeaderRenderer()	{
			
			setHorizontalAlignment(CENTER);
			setOpaque(true);

		} // end HeaderRenderer

		
		/**
		 * Formatiert die Spaltenk�pfe.
		 * @param RTable
		 * @param Value
		 * @param isSelected
		 * @param hasFocus
		 * @param row
		 * @param col
		 * @return RComponent
		 */
		public Component getTableCellRendererComponent(JTable RTable, Object Value, boolean isSelected,
														boolean hasFocus, int row, int col) {

			Component RComponent = super.getTableCellRendererComponent(RTable, Value, isSelected, hasFocus, row, col);
			setBorder(UIManager.getBorder("TableHeader.cellBorder"));
			setForeground(UIManager.getColor("TableHeader.foreground"));
			setBackground(UIManager.getColor("TableHeader.background"));
			setFont(UIManager.getFont("TableHeader.font"));
			//setRowHeight(RTable.getRowHeight());
			return RComponent;
		}
			
	} // end class HeaderRenderer
	
	
	
	/**
	 *  Erweitert das TableModel um das ListModel, welches f�r die Zeilenk�pfe ben�tigt wird.
	 */
	class TableListModel extends AbstractListModel {
		JTable Table;

		public TableListModel(JTable Table) {
			this.Table = Table;
		}

		public int getSize() {
			return Table.getRowCount();
		}

		public Object getElementAt(int index) {
			return "" + (index + 1);
		}
	
	} // end class TableListModel

	
	
	/**
	 * Definiert das Aussehen der Zeilenk�pfe und implementiert die Methode zum Setzen der Zellinhalte.
	 **/
	class RowHeaderRenderer extends JLabel implements ListCellRenderer {

		public RowHeaderRenderer(JTable Table)	{
			JTableHeader Header = Table.getTableHeader();
			setOpaque(true);
			setBorder(UIManager.getBorder("TableHeader.cellBorder"));
			setHorizontalAlignment(CENTER);
			setForeground(Header.getForeground());
			setBackground(Header.getBackground());
			setFont(Header.getFont());
		
		} // end RowHeaderRender (constructor)
		
		
		
		/**
		 * Gibt das JLabel nach setzen des Zellinhaltes zur�ck.
		 **/
		public Component getListCellRendererComponent(JList List, Object Value,	int index, 
													  boolean isSelected, boolean cellHasFocus)	{
			if (Value == null) {
				setText("");
			}
			else {
				setText(Value.toString());
			}
			return this;
		}
		
	} // end class RowHeaderRenderer

	
	
	public class TableCellRenderer extends DefaultTableCellRenderer {
		
		/**
		 * Zentriert den Inhalt der Zellen.
		 */
		public TableCellRenderer()	{
			
			setHorizontalAlignment(CENTER);
			setOpaque(true);

		} // end TableCellRenderer
		
		

		/**
		 * This method formats the table cells and returns them as component.
		 * @param RTable
		 * @param Value
		 * @param isSelected
		 * @param hasFocus
		 * @param row
		 * @param col
		 * @return RComponent
		 */
		public Component getTableCellRendererComponent(JTable RTable, Object Value, boolean isSelected,
														boolean hasFocus, int row, int col) {

			Component RComponent = super.getTableCellRendererComponent(RTable, Value, isSelected, hasFocus, row, col);
			String FontName = getFont().getFontName();
				
			setFont(new Font(FontName, Font.PLAIN, 12));			
			setForeground(Color.black);	
			setBackground(new Color(255,255,230));

			return RComponent;

		} // end getTableCellRendererComponent  

	} // end class TableCellRenderer

	
} // end class MigrationMatrix