import java.util.*;


public class RulesBase {

	private Vector Positions;
	
	public RulesBase(Vector NewPositions) {
		
		Positions = NewPositions;
		
	} // end RulesBase (constructor)
	
	
	
	/**
	 * Prüft, ob das Element an der übergebenen Position bewegt werden kann.
	 * @param row
	 * @param col
	 * @return boolean
	 */	
	public boolean isMoveable(int size, int row, int col) {
		
		if (((Vector)Positions.elementAt(row)).elementAt(col).toString().endsWith("#")) {
			return false;
		}
		for (int i = row - 1; i < row + 2; i++) {
			for (int j = col - 1; j < col + 2; j++) {
				if (i >= 0 && j >= 0 && i < size && j < size) {
					if (((Vector)Positions.elementAt(i)).elementAt(j).toString().equals("")) {
						return true;
					}
				}
			}
		}
		return false;
		
	} // end isMoveable

	
	
	/**
	 * Bewertet die übergebene Position für das spezifizierte Element und gibt den Wert zurück.
	 * String Element
	 * @param row
	 * @param col
	 * @param size
	 * @return value
	 */	
	public int evaluatePosition(String Element, int row, int col, int size) {
		
		int value = 0;
		for (int i = row - 1; i <= row + 1; i++) {
			if (i >= 0 && i < size) {
				Vector PositionSet = (Vector)Positions.elementAt(i);
				for (int j = col - 1; j <= col + 1; j++) {
					if (j >= 0 && j < size && !(row == i && col == j)) {
						if (Element.startsWith("B")) {
							if (((Vector)Positions.elementAt(i)).elementAt(j).toString().startsWith("B")) {
								value = value + 1;
							}
							else if (((Vector)Positions.elementAt(i)).elementAt(j).toString().startsWith("W")) {
								value = value - 1;
							}
							else if (((Vector)Positions.elementAt(i)).elementAt(j).toString().startsWith("R")) {
								value = value + 0;
							}
							else if (((Vector)Positions.elementAt(i)).elementAt(j).toString().startsWith("Y")) {
								value = value + 1;
							}
						}
						else if (Element.startsWith("W")) {
							if (((Vector)Positions.elementAt(i)).elementAt(j).toString().startsWith("B")) {
								value = value - 1;
							}
							else if (((Vector)Positions.elementAt(i)).elementAt(j).toString().startsWith("W")) {
								value = value + 1;
							}
							else if (((Vector)Positions.elementAt(i)).elementAt(j).toString().startsWith("R")) {
								value = value - 1;
							}
							else if (((Vector)Positions.elementAt(i)).elementAt(j).toString().startsWith("Y")) {
								value = value + 1;
							}
						}
						else if (Element.startsWith("R")) {
							if (((Vector)Positions.elementAt(i)).elementAt(j).toString().startsWith("B")) {
								value = value + 0;
							}
							else if (((Vector)Positions.elementAt(i)).elementAt(j).toString().startsWith("W")) {
								value = value - 1;
							}
							else if (((Vector)Positions.elementAt(i)).elementAt(j).toString().startsWith("R")) {
								value = value + 1;
							}
							else if (((Vector)Positions.elementAt(i)).elementAt(j).toString().startsWith("Y")) {
								value = value + 0;
							}
						}
						else if (Element.startsWith("Y")) {
							if (((Vector)Positions.elementAt(i)).elementAt(j).toString().startsWith("B")) {
								value = value - 1;
							}
							else if (((Vector)Positions.elementAt(i)).elementAt(j).toString().startsWith("W")) {
								value = value + 1;
							}
							else if (((Vector)Positions.elementAt(i)).elementAt(j).toString().startsWith("R")) {
								value = value + 0;
							}
							else if (((Vector)Positions.elementAt(i)).elementAt(j).toString().startsWith("Y")) {
								value = value + 1;
							}
						}
					}
				} // end for(j)
			} 
		} // end for(i)
		return value;
		
	} // evalueateCurrentPosition
	
		
	
} // end class RulesBase