import java.awt.*;
import java.awt.event.MouseEvent;
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
	private Vector Positions;
	private	Vector Data;

	
	/**
	 * Der Konstruktor erzeugt die Matrix.
	 * @param NewMig
	 * @param Data
	 * @param ColumnNames
	 */		
	public MigrationMatrix(Migration NewMig, Vector Data, Vector ColumnNames, Vector NewPositions)  {
		
		Mig = NewMig;
		Positions = NewPositions;
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
//					reserviert für manuelles Setzen der Felder
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
		Mig.getScrollPanel().setRowHeaderView(RowHeader); // fügt die Zeilenköpfe hinzu
		
		Matrix.getTableHeader().setReorderingAllowed(false);
		Matrix.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		// verhindert das Zell-Rendering bei Mausbewegungen
		ToolTipManager.sharedInstance().unregisterComponent(Matrix); 
		ToolTipManager.sharedInstance().unregisterComponent(Matrix.getTableHeader());
		
	} // end MigrationMatrix (constructor)

	
	
	/**
	 * Gibt die Matrix zurück.
	 * @return Matrix
	 */
	public JTable getTable()  {

		return Matrix;

	} // end getTable


	
	/**
	 * Definiert das Aussehen der Spaltenköpfe.
	 */
	public class HeaderRenderer extends DefaultTableCellRenderer {

		/**
		 * Zentriert den Inhalt der Spaltenköpfe.
		 */
		public HeaderRenderer()	{
			
			setHorizontalAlignment(CENTER);
			setOpaque(true);

		} // end HeaderRenderer

		
		/**
		 * Formatiert die Spaltenköpfe.
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
	 *  Erweitert das TableModel um das ListModel, welches für die Zeilenköpfe benötigt wird.
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
	 * Definiert das Aussehen der Zeilenköpfe und implementiert die Methode zum Setzen der Zellinhalte.
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
		 * Gibt das JLabel nach setzen des Zellinhaltes zurück.
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

	
	/**
	 * Definiert das Aussehen der einzelnen Zellen der Matrix.
	 **/	
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
			setFont(new Font(FontName, Font.BOLD, 20));
			setBackground(new Color(180,180,180));
			
			if (((Vector)Positions.elementAt(row)).elementAt(col).toString().equals("B")) {
				setForeground(Color.BLACK);
				RTable.setValueAt("\u25cf", row, col);
			}
			if (((Vector)Positions.elementAt(row)).elementAt(col).toString().equals("W")) {
				setForeground(Color.WHITE);
				RTable.setValueAt("\u25cf", row, col);
			}
			if (((Vector)Positions.elementAt(row)).elementAt(col).toString().equals("R")) {
				setForeground(Color.RED);
				RTable.setValueAt("\u25cf", row, col);
			}
			if (((Vector)Positions.elementAt(row)).elementAt(col).toString().equals("Y")) {
				setForeground(Color.YELLOW);
				RTable.setValueAt("\u25cf", row, col);
			}

			return RComponent;

		} // end getTableCellRendererComponent  

	} // end class TableCellRenderer

	
} // end class MigrationMatrix