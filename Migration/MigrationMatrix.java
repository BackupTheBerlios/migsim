import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.*;
import javax.swing.event.MouseInputAdapter;



public class MigrationMatrix {
	
	private Migration Mig;
	private RulesBase Rules;
	private JTable Matrix;
	private DefaultTableModel MatrixTableModel;
	private JList RowHeader;
	private Vector Positions;
	private	Vector Data;
	
	
	/**
	 * Der Konstruktor erzeugt die Matrix.
	 * @param NewMig
	 * @param Data
	 * @param ColumnNames
	 * @param NewPositions
	 * @param NewRules
	 */		
	public MigrationMatrix(Migration NewMig, Vector NewData, Vector ColumnNames, 
						   Vector NewPositions, RulesBase NewRules)  {
		
		Mig = NewMig;
		Rules = NewRules;
		Positions = NewPositions;
		Data = NewData;
		MatrixTableModel = new DefaultTableModel(Data, ColumnNames) {
			public boolean isCellEditable(int row, int column) { 
				return false;	// Schreibschutz für alle Zellen 
			}
		};

		Matrix = new JTable(MatrixTableModel);
		Matrix.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		Matrix.setRowHeight(25);
		Matrix.addMouseListener (
			new MouseInputAdapter() {
			  public void mouseClicked(MouseEvent ME) {
				 if (ME.getClickCount() == 1) {
					setElement();
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
	 * Setzt ein Element in die Matrix bzw. löscht es.
	 */	
	private void setElement() {
		
		int row = Matrix.getSelectedRow();
		int col = Matrix.getSelectedColumn();
		if (((Vector)Positions.elementAt(row)).elementAt(col).toString().startsWith("B") &&
			Mig.getSelection().equals("Black")) {
			((Vector)Positions.elementAt(row)).setElementAt("", col);
			Matrix.setValueAt("", row, col);
		}
		else if	(((Vector)Positions.elementAt(row)).elementAt(col).toString().startsWith("W") &&
			Mig.getSelection().equals("White")) {
			((Vector)Positions.elementAt(row)).setElementAt("", col);
			Matrix.setValueAt("", row, col);
		}
		else if	(((Vector)Positions.elementAt(row)).elementAt(col).toString().startsWith("R") &&
			Mig.getSelection().equals("Red")) {
			((Vector)Positions.elementAt(row)).setElementAt("", col);
			Matrix.setValueAt("", row, col);
		}
		else if	(((Vector)Positions.elementAt(row)).elementAt(col).toString().startsWith("Y") &&
			Mig.getSelection().equals("Yellow")) {
			((Vector)Positions.elementAt(row)).setElementAt("", col);
			Matrix.setValueAt("", row, col);
		}
		else {
			int size = Positions.size();
			if (Mig.getSumOfColoredFields() < size * size * 2 / 3) {
				if (Mig.getSelection().equals("Black")) {
					((Vector)Positions.elementAt(row)).setElementAt("B?", col);
				}
				else if (Mig.getSelection().equals("White")) {
					((Vector)Positions.elementAt(row)).setElementAt("W?", col);
				}
				else if (Mig.getSelection().equals("Red")) {
					((Vector)Positions.elementAt(row)).setElementAt("R?", col);
				}
				else if (Mig.getSelection().equals("Yellow")) {
					((Vector)Positions.elementAt(row)).setElementAt("Y?", col);
				}
				Matrix.setValueAt("\u25cf", row, col);
			}
			else {
				// Der TableCellRenderer muss gestoppt werden damit die Fehlermeldung lesbar wird
				Mig.getScrollPanel().getViewport().removeAll();
				Mig.messageHandling("MaxExceeded", String.valueOf(size * size));
				Mig.getScrollPanel().getViewport().add(Matrix);
				Mig.getScrollPanel().validate();
			}
		}
		Mig.resetPeriod();
	
	} // end setElement
	
	
	/**
	 * Wechselt in die nächste Periode.
	 */
	public boolean changePeriod() {
	
		System.out.println("\n" + Mig.getPeriod() + " Periode:");
		System.out.println("----------");
		for (int row = 0; row < Positions.size(); row++) {
			Vector PositionSet = (Vector)Positions.elementAt(row);
			for (int col = 0; col < PositionSet.size(); col++) {
				if (Rules.isMoveable(Positions.size(), row, col)) {
					String Element = ((Vector)Positions.elementAt(row)).elementAt(col).toString();
					int currentValue = Rules.evaluatePosition(Element, row, col, Positions.size());
					setOptimalPosition(Element, row, col, currentValue);
				}
			}
		}
		setMoveable(Positions.size());
		// System.out.println(Positions);
		// System.out.println(Data);
		return false;
		
	} // end changePeriod 


	
	/**
	 * Bestimmt für das übergebene Element die optimalste angrenzende Position und setzt
	 * es gegebenfalls auf die neue Position.
	 * @param Element
	 * @param row
	 * @param col
	 * @param currentPosition 
	 */
	private void setOptimalPosition(String Element, int row, int col, int value) {
		
		int optRow = row;
		int optCol = col;
		int optValue = value;
		
		for (int i = row - 1; i <= row + 1; i++) {
			if (i >= 0 && i < Positions.size()) {
				Vector PosSet = (Vector)Positions.elementAt(i);
				for (int j = col - 1; j <= col + 1; j++) {
					if (j >= 0 && j < Positions.size()) {
						if (((Vector)Positions.elementAt(i)).elementAt(j).toString().equals("")) {
							//-1, da das neue Feld das aktuelle Feld bei der Bewertung als gleichfarbigen Nachbarn hat
							int newValue = Rules.evaluatePosition(Element, i, j, Positions.size()) - 1;
							if (optValue < newValue) {
								optValue = newValue;
								optRow = i;
								optCol = j;
								// Kontrollausgabe
								String Output = "Alt: (Farbe: " + Element.substring(0,1) + "  Pos: " + row + ";" + col + "  Wert: " + value + ")";
								Output += "  ->   Neu: (Farbe: " + Element.substring(0,1) + "  Pos: " + optRow + ";" + optCol + "  Wert: " + optValue + ")";
								System.out.println(Output);
							}
						}
					}
				} // end for(j)
			}
		} // end for(i)
	
		if (optValue != value) {
			Element = Element.substring(0,1) + "#";
			((Vector)Data.elementAt(row)).setElementAt("", col);
			((Vector)Positions.elementAt(row)).setElementAt("", col);
			((Vector)Positions.elementAt(optRow)).setElementAt(Element, optCol);
			Matrix.setValueAt("\u25cf", optRow, optCol);
		}
		
	} // end setOptimalPosition


	
	/**
	 * Markiert alle nicht leeren Zelle am Ende mit einem "?" 
	 * (= Element kann sich in dieser Periode noch bewegen).
	 * @param size 
	 */
	private void setMoveable(int size) {
		
		for (int row = 0; row < size; row++) {
			Vector PositionSet = (Vector)Positions.elementAt(row);
			for (int col = 0; col < size; col++) {
				if (!PositionSet.elementAt(col).toString().equals("")) {
					String Element = PositionSet.elementAt(col).toString();
					Element = Element.substring(0,1) + "?";
					PositionSet.setElementAt(Element, col);
				}
			} // end for(col)
		} // for(row)

	} // end setMoveable
	
	
	
	/**
	 * Definiert das Aussehen der Spaltenköpfe.
	 */
	private class HeaderRenderer extends DefaultTableCellRenderer {

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
			return RComponent;
		}
			
	} // end class HeaderRenderer
	
	
	
	/**
	 *  Erweitert das TableModel um das ListModel, welches für die Zeilenköpfe benötigt wird.
	 */
	private class TableListModel extends AbstractListModel {
		
		private JTable Table;

		public TableListModel(JTable Table) {
			
			this.Table = Table;
			
		} // end TableListModel (constructor)

		
		
		public int getSize() {
			
			return Table.getRowCount();
		
		} // end getSize

		
		
		public Object getElementAt(int index) {
			
			return "" + (index);
		
		} // end getElementAt
	
	} // end class TableListModel

	
	
	/**
	 * Definiert das Aussehen der Zeilenköpfe und implementiert die Methode zum Setzen der Zellinhalte.
	 **/
	private class RowHeaderRenderer extends JLabel implements ListCellRenderer {

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
		
		} // end getListCellRendererComponent
		
	} // end class RowHeaderRenderer

	
	/**
	 * Definiert das Aussehen der einzelnen Zellen der Matrix.
	 **/	
	private class TableCellRenderer extends DefaultTableCellRenderer {
		
		/**
		 * Zentriert den Inhalt der Zellen.
		 */
		public TableCellRenderer()	{
			
			setHorizontalAlignment(CENTER);
			setOpaque(true);

		} // end TableCellRenderer (constructor)
		
		

		/**
		 * Diese Methode gibt die formatierten Zellen zurück.
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
			setBackground(Color.LIGHT_GRAY);
			
			if (((Vector)Positions.elementAt(row)).elementAt(col).toString().startsWith("B")) {
				setForeground(Color.BLACK);
				RTable.setValueAt("\u25cf", row, col);
			}
			if (((Vector)Positions.elementAt(row)).elementAt(col).toString().startsWith("W")) {
				setForeground(Color.WHITE);
				RTable.setValueAt("\u25cf", row, col);
			}
			if (((Vector)Positions.elementAt(row)).elementAt(col).toString().startsWith("R")) {
				setForeground(Color.RED);
				RTable.setValueAt("\u25cf", row, col);
			}
			if (((Vector)Positions.elementAt(row)).elementAt(col).toString().startsWith("Y")) {
				setForeground(Color.YELLOW);
				RTable.setValueAt("\u25cf", row, col);
			}

			return RComponent;

		} // end getTableCellRendererComponent  

	} // end class TableCellRenderer

	
} // end class MigrationMatrix