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
	public boolean isMoveable(int row, int col) {
		
		for (int i = row - 1; i < row + 2; i++) {
			for (int j = col - 1; j < col + 2; j++) {
				if (((Vector)Positions.elementAt(i)).elementAt(j).toString().equals("")) {
					System.out.println(i + ";" + j);
					return true;
				}
			}
		}
		return false;
		
	} // end isMoveable
	
	
	
	/**
	 * Prüft, ob das Element an der übergebenen Position, zu einer Position
	 * mit mehr gleichen Nachbarn bewegt werden kann.
	 * @param row
	 * @param col
	 * @return boolean
	 */	
	public boolean sameNeighbors(int row, int col) {
		
		return false;
		
	} // sameNeighbors
		
	
} // end class RulesBase