import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;


/**
 * @author Lan Nguyen
 */

public class Migration implements ActionListener {

	private static final int SETUP = 0;
	private static final int START = 1;
	private static final int END = 2;
	
	private static final int MATRIX = 0;
	private static final int BLACK = 1;
	private static final int WHITE = 2;
	private static final int RED = 3;
	private static final int YELLOW = 4;

	private static final int ROW = 0;
	private static final int COL = 1;

	private JFrame Frame;	
	private JButton Help;
	private JButton Reset;
	private JButton Exit;
	private JButton Next;
	private JButton Back;
	private JLabel Headline;
	private JScrollPane ScrollPanel;
	private JPanel PagePanel;
	private JPanel SouthPanel;
	private JPanel South;
	private JTextField CurrentTField;
	private ButtonGroup RadioButtonGroup;
	private GridBagLayout Layout;
	private	GridBagConstraints GBC;
	private Vector Settings;
	private Vector Positions;
	private Random Randomizer;
	int currentPage;
	boolean error;

	
	
	/**
	 * Der Konstruktor erzeugt den Anwendungsframe mit allen Komponenten.
	 */
	public Migration() {

		Frame = new JFrame("Völkerwanderung");
		Frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		Frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent We) {
				if (exitApplication()) {
					System.exit(0); 
				}
			}
		});

		Frame.getContentPane().setLayout(new BorderLayout());
		((BorderLayout)Frame.getContentPane().getLayout()).setVgap(10);
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}
		catch (Exception Ex) { 
			messageHandling("FrameError", Ex.getMessage());
		}                
		
		Headline = new JLabel();
		Headline.setHorizontalAlignment(JLabel.CENTER);
		Headline.setFont(new Font("Arial", Font.BOLD, 18));
		Headline.setForeground(new Color(0,0,100));
		
		GridBagLayout Layout = new GridBagLayout();
		GridBagConstraints GBC = new GridBagConstraints();
		GBC.fill = GridBagConstraints.BOTH;
		Layout.setConstraints(Headline, GBC);

		JPanel North = new JPanel(Layout);	// erzeught den oberen Rand mit der Überschrift
		North.setPreferredSize(new Dimension(500,50));
		North.add(Headline);
		
		JPanel West = new JPanel();		// erzeugt den linken Rand
		West.setPreferredSize(new Dimension(50,400));

		int row = 0;
		Layout = new GridBagLayout();
		GBC = new GridBagConstraints();
		GBC.fill = GridBagConstraints.BOTH;
		GBC.gridx = 0;
		GBC.insets = getInsets(row, "Button");
		
		Help = new JButton("Hilfe");	// erzeugt den Help-Button
		Help.setPreferredSize(new Dimension(90,25));
		Help.addActionListener(this);
		Help.setEnabled(false);
		GBC.gridy = row++;
		Layout.setConstraints(Help, GBC);
				
		Exit = new JButton("Exit");		// erzeugt den Exit-Button
		Exit.setPreferredSize(new Dimension(90,25));
		Exit.addActionListener(this);
		Exit.setEnabled(false);
		GBC.gridy = row++;
		GBC.insets = getInsets(row, "ButtonBlock");
		Layout.setConstraints(Exit, GBC);
				
		Reset = new JButton("Reset");	// erzeugt den Reset-Button
		Reset.setPreferredSize(new Dimension(90,25));
		Reset.addActionListener(this);
		Reset.setEnabled(false);
		GBC.gridy = row++;
		GBC.insets = getInsets(row, "Button");
		Layout.setConstraints(Reset, GBC);
		
		Next = new JButton("Weiter");	// erzeugt den Weiter-Button
		Next.setPreferredSize(new Dimension(90,25));
		Next.addActionListener(this);
		Next.setEnabled(false);
		GBC.gridy = row++;
		GBC.insets = getInsets(row, "ButtonBlock");
		Layout.setConstraints(Next, GBC);

		Back = new JButton("Zurück");	// erzeugt den Back-Button
		Back.setPreferredSize(new Dimension(90,25));
		Back.addActionListener(this);
		Back.setEnabled(false);
		GBC.gridy = row++;
		GBC.insets = getInsets(row, "Button");
		Layout.setConstraints(Back, GBC);

		JPanel East = new JPanel(Layout);	// erzeugt den rechten Rand mit allen Buttons
		East.add(Help);
		East.add(Exit);
		East.add(Reset);
		East.add(Next);
		East.add(Back);
		
		SouthPanel = createSouthPanel();
		SouthPanel.setVisible(false);
		Layout = new GridBagLayout();
		GBC = new GridBagConstraints();
		GBC.fill = GridBagConstraints.BOTH;
		GBC.insets = getInsets(row, "ButtonGroup");
		Layout.setConstraints(SouthPanel, GBC);

		South = new JPanel(Layout);		// erzeugt den unteren Rand
		South.setPreferredSize(new Dimension(500,50));
		South.add(SouthPanel);
		ScrollPanel = new JScrollPane();	// erzeugt das Hauptanzeigefenster in der Mitte des Frames
				
		Frame.getContentPane().add(North,BorderLayout.NORTH);	// fügt alle Komponenten dem Frame hinzu
		Frame.getContentPane().add(East,BorderLayout.EAST);
		Frame.getContentPane().add(West,BorderLayout.WEST);
		Frame.getContentPane().add(ScrollPanel,BorderLayout.CENTER);
		Frame.getContentPane().add(South,BorderLayout.SOUTH);

		Toolkit Kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = Kit.getScreenSize();

		int screenHeight = screenSize.height;
		int screenWidth = screenSize.width;

		Frame.setSize(new Dimension(800,600));
		Frame.setLocation(screenWidth/6, screenHeight/6);
		Frame.getContentPane().setBackground(new Color(0,0,200));
		Frame.setVisible(true);
		initialize(); // initialisiert die Anwendung
		
	} // end Migration (constructor)

	
	
	/**
	 * Initialisiert die Anwendung.
	 */
	private void initialize() {
	
		Settings = new Vector();
		Settings.addElement("5");
		for (int i = BLACK; i <= YELLOW; i++) {
			Settings.addElement("0");
		}
		error = false;
		currentPage = SETUP;
		showPage();
		setActivity(); // setzt die Button-Aktivität
		
	} // end initialize()

	

	/**
	 * Implements the ActionListener for the frame components.
	 * @param AE
	 */
	public void actionPerformed(ActionEvent AE) {
		
		setWaitCursor();

		if (AE.getSource() == Exit) {
			if (exitApplication()) {
				System.exit(0);
			}
		}

		else if (AE.getSource() == Back) {
			ScrollPanel.getColumnHeader().removeAll();
			ScrollPanel.getRowHeader().removeAll();
			currentPage = SETUP;
			showPage();
			setActivity();
		}
		
		else if (AE.getSource() == Reset) {
			if (currentPage == SETUP) {
				initialize();
			}
			else if (currentPage == START) {
				showStartPage();
			}
		}
		
		else if (AE.getSource() == Next) {
			boolean valuesValid = true;
			if (CurrentTField != null) {
				if (!changeValue(CurrentTField.getName(), CurrentTField.getText())) {
					CurrentTField.grabFocus();
					valuesValid = false;
				}
			}
			if (valuesValid) {
				Settings.setElementAt(((JTextField)getComponent("Matrix")).getText(), MATRIX);
				Settings.setElementAt(((JTextField)getComponent("Black")).getText(), BLACK);
				Settings.setElementAt(((JTextField)getComponent("White")).getText(), WHITE);
				Settings.setElementAt(((JTextField)getComponent("Red")).getText(), RED);
				Settings.setElementAt(((JTextField)getComponent("Yellow")).getText(), YELLOW);
				currentPage++;
				showPage();
				setActivity();
			}
		}
		setDefaultCursor();

	} // end actionPerformed

	
	
	/**
 	 * Zeigt die entsprechende Seite an.  
 	 */
	private void showPage() {

		switch (currentPage) {

			case SETUP:
				showSetupPage();
				break;

			case START:
				showStartPage();
				break;
		}
	} // end showPage()
	

	
	/**
	 * Generiert die Einstellungsseite.  
	 */
	private void showSetupPage() {

		setHeadlineText("Bitte die Initialisierungswerte festlegen:");
		Layout = new GridBagLayout();
		GBC = new GridBagConstraints();
		GBC.fill = GridBagConstraints.BOTH;
		PagePanel = new JPanel(Layout);
		PagePanel.setBackground(Color.LIGHT_GRAY);

		String Item = "Größe der Feldermatrix";
		String Hint = "(5 \u2264 Wert \u2264 30)";
		String Comp = "TextField";
		String Value = Settings.elementAt(MATRIX).toString();
		String Name = "Matrix";
		int row = 0;
		setComponents(Item, Hint, Comp, Value, Name, row);

		Item = "\u2192 16 Felder von 16 möglichen übrig.";
		Hint = "";
		Comp = "JLabel";
		Value = "";
		Name = "InfoLabel";
		row++;
		setComponents(Item, Hint, Comp, Value, Name, row);
			
		Item = "Anzahl Felder 'schwarz'";
		Hint = "Black";
		Comp = "TextField";
		Value = Settings.elementAt(BLACK).toString();
		Name = "Black";
		row++;
		setComponents(Item, Hint, Comp, Value, Name, row);

		Item = "Anzahl Felder 'weiß'";
		Hint = "White";
		Comp = "TextField";
		Value = Settings.elementAt(WHITE).toString();
		Name = "White";
		row++;
		setComponents(Item, Hint, Comp, Value, Name, row);

		Item = "Anzahl Felder 'rot'";
		Hint = "Red";
		Comp = "TextField";
		Value = Settings.elementAt(RED).toString();
		Name = "Red";
		row++;
		setComponents(Item, Hint, Comp, Value, Name, row);

		Item = "Anzahl Felder 'gelb'";
		Hint = "Yellow";
		Comp = "TextField";
		Value = Settings.elementAt(YELLOW).toString();
		Name = "Yellow";
		row++;
		setComponents(Item, Hint, Comp, Value, Name, row);

		setPagePanel(PagePanel);
		refreshInfoLabel(Integer.parseInt(Settings.elementAt(MATRIX).toString()));
		
	} // end showSettings

	
	
	/**
	 * Generiert die Startpositionen in der Feldermatrix.  
	 */	
	private void showStartPage() {
	
		setHeadlineText("Simulationsmatrix:");
		Vector Data = new Vector();
		Positions = new Vector();
		Randomizer = new Random();
		
		int size = Integer.parseInt(Settings.elementAt(MATRIX).toString());
		for (int i = 0; i < size; i++) {
			Vector DataSet = new Vector();
			Vector PositionSet = new Vector();
			for (int j = 0; j < size; j++) {	
				DataSet.addElement("");
				PositionSet.addElement("");
			}
			Data.addElement(DataSet);
			Positions.addElement(PositionSet);
		}
		
		int black = Integer.parseInt(Settings.elementAt(BLACK).toString());
		for (int i = 0; i < black; i++) {
			int Position[] = getRandomPosition(Positions, Randomizer);
			((Vector)Positions.elementAt(Position[ROW])).setElementAt("B", Position[COL]);
		}

		int white = Integer.parseInt(Settings.elementAt(WHITE).toString());
		for (int i = 0; i < white; i++) {
			int Position[] = getRandomPosition(Positions, Randomizer);
			((Vector)Positions.elementAt(Position[ROW])).setElementAt("W", Position[COL]);
		}

		int red = Integer.parseInt(Settings.elementAt(RED).toString());
		for (int i = 0; i < red; i++) {
			int Position[] = getRandomPosition(Positions, Randomizer);
			((Vector)Positions.elementAt(Position[ROW])).setElementAt("R", Position[COL]);
		}

		int yellow = Integer.parseInt(Settings.elementAt(YELLOW).toString());
		for (int i = 0; i < yellow; i++) {
			int Position[] = getRandomPosition(Positions, Randomizer);
			((Vector)Positions.elementAt(Position[ROW])).setElementAt("Y", Position[COL]);
		}
		
		Vector ColumnNames = new Vector();
		for (int i = 1; i <= size; i++) {
			ColumnNames.addElement(String.valueOf(i));
		}
		
		MigrationMatrix Matrix = new MigrationMatrix(this, Data, ColumnNames, Positions);
		ScrollPanel.getViewport().removeAll();
		ScrollPanel.getViewport().add(Matrix.getTable());
		ScrollPanel.validate();
	
	} // end showStartPage
	

	
	/**
	 * Erzeugt eine zufällige gültige Position.
	 * @param Data
	 * @param size
	 * @param Randomizer
	 * @return Position
	 **/
	private int[] getRandomPosition(Vector Positions, Random Randomizer) {
		
		int size = Integer.parseInt(Settings.elementAt(MATRIX).toString());
		int randomRow = Randomizer.nextInt(size);
		int randomCol = Randomizer.nextInt(size);
		
		while (!((Vector)Positions.elementAt(randomRow)).elementAt(randomCol).toString().equals("")) {
			randomRow = Randomizer.nextInt(size);
			randomCol = Randomizer.nextInt(size);
		}
		int Position[] = new int[2]; 
		Position[ROW] = randomRow;
		Position[COL] = randomCol;
		
		return Position;
	
	} // end getRandomPosition
	

	
	/**
	 * Setzt die Komponenten auf die aktuelle Seite.
	 * @param Item
	 * @param Hint
	 * @param Comp
	 * @param Value
	 * @param Name
	 * @param row
	 **/
	public void setComponents(String Item, String Hint, String Comp, String Value, String Name, int row) {
		
		if (Comp.equalsIgnoreCase("JLabel")) {
			JLabel ItemLabel = new JLabel(Item);
			ItemLabel.setName(Name);
			ItemLabel.setFont(new Font("Arial", Font.BOLD, 15));
			ItemLabel.setForeground(new Color(0,0,220));
			ItemLabel.setBackground(Color.LIGHT_GRAY);

			GBC.gridy = row;
			GBC.gridx = 0;
			GBC.gridwidth = 3;
			GBC.insets = getInsets(row, "Label");
			Layout.setConstraints(ItemLabel, GBC);
			PagePanel.add(ItemLabel);
		}
		else {
			JLabel ItemLabel = new JLabel(Item + ":");
			ItemLabel.setFont(new Font("Arial", Font.BOLD, 15));
			ItemLabel.setBackground(Color.LIGHT_GRAY);

			GBC.gridy = row;
			GBC.gridx = 0;
			GBC.gridwidth = 1;
			GBC.insets = getInsets(row, "Label");
			Layout.setConstraints(ItemLabel, GBC);
			PagePanel.add(ItemLabel);
		}
		
		if (Comp.equalsIgnoreCase("TextField")) {

			final JTextField TField = new JTextField(Value); // "final" ist notwendig für den Focus-Listener
			TField.setName(Name);
			TField.setFont(new Font("Arial", Font.PLAIN, 13));
			TField.setPreferredSize(new Dimension(40, 25));
			TField.setHorizontalAlignment(JTextField.RIGHT);
			TField.addFocusListener(new FocusAdapter() { // Selectiert den Inhalt des Textfeldes sobald es angeklickt wird
				public void focusGained (FocusEvent FE){
					if (error) {
						CurrentTField.grabFocus();
					}
					TField.selectAll();
				}
				public void focusLost (FocusEvent FE){
					if (!error) {	
						if (FE.getOppositeComponent() instanceof JTextField) {
							if (!changeValue(TField.getName(), TField.getText())) {
								CurrentTField = TField;
								error = true;
							}
						}
						else if (FE.getOppositeComponent() instanceof JButton) {
							if (((JButton)FE.getOppositeComponent()).getText().equals("Weiter")) {
								CurrentTField = TField;
							}
						}
					}
					else {
						error = false;
					}
				}
			});

			GBC.gridx = GridBagConstraints.RELATIVE;
			GBC.insets = getInsets(row, "Field");
			Layout.setConstraints(TField, GBC);
			PagePanel.add(TField);
			JLabel HintLabel;
			
			if (Hint.equals("Black") || Hint.equals("White") || Hint.equals("Red") || Hint.equals("Yellow")) {
				HintLabel = new JLabel("\u25cf");
				HintLabel.setFont(new Font(HintLabel.getFont().getFontName(), Font.BOLD, 20));
				HintLabel.setBackground(Color.LIGHT_GRAY);
				if (Hint.equals("Black")) {
					HintLabel.setForeground(Color.BLACK);
				}
				else if (Hint.equals("White")) {
					HintLabel.setForeground(Color.WHITE);	
				}
				else if (Hint.equals("Red")) {
					HintLabel.setForeground(Color.RED);	
				}
				else if (Hint.equals("Yellow")) {
					HintLabel.setForeground(Color.YELLOW);	
				}
			}
			else {
				HintLabel = new JLabel(Hint);
				HintLabel.setFont(new Font("Arial", Font.PLAIN, 13));
				HintLabel.setBackground(Color.LIGHT_GRAY);
			}
			GBC.gridx = GridBagConstraints.RELATIVE;
			GBC.insets = getInsets(row, "Hint");
			Layout.setConstraints(HintLabel, GBC);
			PagePanel.add(HintLabel);
		}
		
	} // end setComponents
	
	
	
	/**
	 * Erzeugt das untere Panel mit der RadioButton-Gruppe.
	 * return SouthPanel
	 **/	
	private JPanel createSouthPanel() {
		
		RadioButtonGroup = new ButtonGroup();
		Layout = new GridBagLayout();
		GBC = new GridBagConstraints();
		GBC.fill = GridBagConstraints.BOTH;

		JLabel BlackLabel = new JLabel("schwarz:");
		BlackLabel.setFont(new Font(BlackLabel.getFont().getFontName(), Font.BOLD, 12));
		BlackLabel.setBackground(Color.LIGHT_GRAY);
		GBC.gridx = 0;
		GBC.insets = getInsets(0, "RadioLabel");
		Layout.setConstraints(BlackLabel, GBC);
		
		JRadioButton BlackButton = new JRadioButton("\u25cf", true); 
		BlackButton.setName("Black");
		BlackButton.setFont(new Font(BlackButton.getFont().getFontName(), Font.BOLD, 20));
		BlackButton.setForeground(Color.BLACK);
		BlackButton.setBackground(Color.LIGHT_GRAY);
		GBC.gridx = GridBagConstraints.RELATIVE;
		GBC.insets = getInsets(0, "RadioButton");
		Layout.setConstraints(BlackButton, GBC);
		RadioButtonGroup.add(BlackButton);

		JLabel WhiteLabel = new JLabel("weiß:");
		WhiteLabel.setFont(new Font(WhiteLabel.getFont().getFontName(), Font.BOLD, 12));
		WhiteLabel.setBackground(Color.LIGHT_GRAY);
		GBC.gridx = GridBagConstraints.RELATIVE;;
		GBC.insets = getInsets(0, "RadioLabel");
		Layout.setConstraints(WhiteLabel, GBC);

		JRadioButton WhiteButton = new JRadioButton("\u25cf"); 
		WhiteButton.setName("White");
		WhiteButton.setFont(new Font(WhiteButton.getFont().getFontName(), Font.BOLD, 20));
		WhiteButton.setForeground(Color.WHITE);
		WhiteButton.setBackground(Color.LIGHT_GRAY);
		GBC.gridx = GridBagConstraints.RELATIVE;
		GBC.insets = getInsets(0, "RadioButton");
		Layout.setConstraints(WhiteButton, GBC);
		RadioButtonGroup.add(WhiteButton);

		JLabel RedLabel = new JLabel("rot:");
		RedLabel.setFont(new Font(RedLabel.getFont().getFontName(), Font.BOLD, 12));
		RedLabel.setBackground(Color.LIGHT_GRAY);
		GBC.gridx = GridBagConstraints.RELATIVE;;
		GBC.insets = getInsets(0, "RadioLabel");
		Layout.setConstraints(RedLabel, GBC);

		JRadioButton RedButton = new JRadioButton("\u25cf");
		RedButton.setName("Red");
		RedButton.setFont(new Font(RedButton.getFont().getFontName(), Font.BOLD, 20));
		RedButton.setForeground(Color.RED);
		RedButton.setBackground(Color.LIGHT_GRAY);
		GBC.gridx = GridBagConstraints.RELATIVE;
		GBC.insets = getInsets(0, "RadioButton");
		Layout.setConstraints(RedButton, GBC);
		RadioButtonGroup.add(RedButton);

		JLabel YellowLabel = new JLabel("gelb:");
		YellowLabel.setFont(new Font(YellowLabel.getFont().getFontName(), Font.BOLD, 12));
		YellowLabel.setBackground(Color.LIGHT_GRAY);
		GBC.gridx = GridBagConstraints.RELATIVE;;
		GBC.insets = getInsets(0, "RadioLabel");
		Layout.setConstraints(YellowLabel, GBC);

		JRadioButton YellowButton = new JRadioButton("\u25cf");
		YellowButton.setName("Yellow");
		YellowButton.setFont(new Font(YellowButton.getFont().getFontName(), Font.BOLD, 20));
		YellowButton.setForeground(Color.YELLOW);
		YellowButton.setBackground(Color.LIGHT_GRAY);
		GBC.gridx = GridBagConstraints.RELATIVE;
		GBC.insets = getInsets(0, "RadioButton");
		Layout.setConstraints(YellowButton, GBC);
		RadioButtonGroup.add(YellowButton);

		SouthPanel = new JPanel(Layout);
		SouthPanel.setBackground(Color.LIGHT_GRAY);
		SouthPanel.add(BlackLabel);
		SouthPanel.add(BlackButton);
		SouthPanel.add(WhiteLabel);
		SouthPanel.add(WhiteButton);
		SouthPanel.add(RedLabel);
		SouthPanel.add(RedButton);
		SouthPanel.add(YellowLabel);
		SouthPanel.add(YellowButton);
		return SouthPanel;
		
	} // end createSouthPanel
	
	
	
	/**
	 * Setzt das übergebene PagePanel in den Frame.
	 * @param PagePanel  
	 */
	public void setPagePanel(JPanel PagePanel) {
	
		ScrollPanel.getViewport().removeAll();
		ScrollPanel.getViewport().add(PagePanel);
		ScrollPanel.getViewport().validate();
					
	} // end setPagePanel

	
	
	/**
	 * Aktualisiert die Felderanzahl im InfoLabel.    
	 * @param size
	 **/
	private void refreshInfoLabel(int size) {
		
		int coloredNumber = size * size * 2 / 3;
		int left = coloredNumber - getSumOfColoredFields();
		if (left == 1) { 
			((JLabel)getComponent("InfoLabel")).setText("\u2192 1 Feld von " + coloredNumber + " möglichen übrig");
		}
		else {
			((JLabel)getComponent("InfoLabel")).setText("\u2192 " + left + " Felder von " + coloredNumber + " möglichen übrig");
		}
	
	} // end refreshInfoLabel

	

	/**
	 * Ändert die Eingabewerte.    
	 * @param Name
	 * @param Value
	 * @return boolean
	 **/
	private boolean changeValue(String Name, String Value) {
		
		if (Name.equals("Matrix")) {
			String Item = "Matrixgröße";
			int value = getIntValue(Value, Item);
			if (!error) {
				if (value >= 5 && value <= 30) {
					if (value * value * 2 / 3 - getSumOfColoredFields() >= 0) {
						refreshInfoLabel(value);
						return true;
					}
					else {
						messageHandling("MatrixSizeError1", Item);
					}
				}
				else {
					messageHandling("MatrixSizeError2", Item);
				}
			}
			else {
				error = false;
			}
		}
		else {
			String Item = "";
			if (Name.equals("Black")) {
				Item = "Anteil Schwarz";
			}
			else if (Name.equals("White")) {
				Item = "Anteil Weiß";
			}
			else if (Name.equals("Red")) {
				Item = "Anteil Rot";
			}
			else if (Name.equals("Yellow")) {
				Item = "Anteil Gelb";
			}
			int value = getIntValue(Value, Item);
			if (!error) {
				int size = Integer.parseInt(((JTextField)getComponent("Matrix")).getText());
				if (getSumOfColoredFields() <= (size * size * 2 / 3)) {
					refreshInfoLabel(size);
					return true;
				}
				else {
					messageHandling("MaxExceeded", String.valueOf(size * size));
				}
			}
			else {
				error = false;
			}			
		}

		return false;
		
	} // end changeValues
	
	
	
	/**
	 * Gibt für den übergebenen String-Wert den Integer-Wert zurück.    
	 * @param Value
	 * @param Item
	 * @return value
	 **/
	private int getIntValue(String Value, String Item) {

		try {
			int value = Integer.parseInt(Value);
			return value;
		}
		catch (Exception Ex) {
			messageHandling("NotNumber", Item);
			error = true;
			return 0;
		}		
	
	} // end getIntValue

	
	
	/**
	 * Gibt die Summe der farbigen Felder der Matrix zurück    
	 * @return sum
	 **/
	public int getSumOfColoredFields() {
		
		int sum = 0;
		if (currentPage == SETUP) {
			sum = Integer.parseInt(((JTextField)getComponent("Black")).getText()); 
			sum += Integer.parseInt(((JTextField)getComponent("White")).getText());
			sum += Integer.parseInt(((JTextField)getComponent("Red")).getText());
			sum += Integer.parseInt(((JTextField)getComponent("Yellow")).getText());
		}
		else {
			for (int i = 0; i < Positions.size(); i++) {
				Vector PositionSet = (Vector)Positions.elementAt(i);
				for (int j = 0; j < PositionSet.size(); j++) {
					if (!PositionSet.elementAt(j).toString().equals("")) {
						sum++;
					}
				}
			}
		}
		return sum;
		
	} // end getSumOfColoredFields

	

	/**
	 * Gibt die Abstände zwischen den J-Komponenten zurück.    
	 * @param row
	 * @param Element
	 * @return NewInsets
	 **/
	public Insets getInsets(int row, String Element) {

		int first = 15, def = 10;
		Insets NewInsets = new Insets(0, 0, 0, 0);

		if (Element.equalsIgnoreCase("Button")) {
			return new Insets(def, def, def, def);
		}
		else if (Element.equalsIgnoreCase("ButtonBlock")) {
			return new Insets(40, def, def, def);
		}
		else if (Element.equalsIgnoreCase("ButtonGroup")) {
			return new Insets(def, def, def, def);
		}		
		else if (Element.equalsIgnoreCase("HelpArea")) {
			return new Insets(def, def, def, def);
		}
		else if (Element.equalsIgnoreCase("RadioButton")) {
			return new Insets(def, 0, def, def);
		}
		else if (Element.equalsIgnoreCase("RadioLabel")) {
			return new Insets(def, def, def, 0);
		}
		else if (Element.equalsIgnoreCase("Label") && row == 0) {
			return new Insets(first, def, def, def);
		}
		else if (Element.equalsIgnoreCase("Label") && row != 0) {
			return new Insets(def, def, def, def);
		}
		else if (Element.equalsIgnoreCase("Field") && row == 0) {
			return new Insets(first, def, def, 5);
		}
		else if (Element.equalsIgnoreCase("Field") && row != 0) {
			return new Insets(def, def, def, 5);
		}
		else if (Element.equalsIgnoreCase("Hint") && row == 0) {
			return new Insets(first, 5, def, 0);
		}
		else if (Element.equalsIgnoreCase("Hint") && row != 0) {
			return new Insets(def, 5, def, def);
		}
		return NewInsets;
	
	} // end getInsets
	
	
	
	/**
	 * Verwaltet alle Meldungen der Anwendung.
	 * @param Cause
	 * @param Item
	 * @return input
	 */
	public int messageHandling(String Cause, String Item) {

		String Message;
		int input = -1;

		if (Cause.equalsIgnoreCase("FrameError")) {
			Message = "Fehler bei der Frame-Erzeugung:\n" + Item;
			showMessage("Error", "Error", Message); 
		}
		else if (Cause.equalsIgnoreCase("Exit")) {
			Message = "Möchten Sie die Anwendung wirklich beenden?";
			input = showMessage("Exit", "Confirmation", Message);
		}
		else if (Cause.equalsIgnoreCase("NotNumber")) {
			Message = "Im Feld \"" + Item + "\" sind nur natürliche Zahlen zugelassen!";
			showMessage("Error", "Error", Message); 
		}
		else if (Cause.equalsIgnoreCase("MatrixSizeError1")) {
			double tempValue = Math.sqrt(getSumOfColoredFields() * 3 / 2);
			int value = (int)tempValue + 1;
			Message = "Für die spezifizierte Anzahl farbiger Felder muss die Matricgröße mindestens " + value + " betragen!";
			showMessage("Error", "Error", Message); 
		}
		else if (Cause.equalsIgnoreCase("MatrixSizeError2")) {
			Message = "Im Feld \"" + Item + "\" sind nur natürliche Zahlen zwischen 5 und 30 zugelassen!";
			showMessage("Error", "Error", Message); 
		}
		else if (Cause.equalsIgnoreCase("MaxExceeded")) {
			int value = Integer.parseInt(Item) * 2 / 3;
			Message = "Die Anzahl der farbigen Felder darf nicht höher als " + value + " sein!\n";
			Message += "Das entspricht 2/3 der Gesamtanzahl der Felder von " + Item + ".";
			showMessage("Error", "Error", Message); 
		}
		return input;
		
	} // end messageHandling

	
	
	/**
	 * Zeigt eine MessageBox an und verwaltet die Buttonklicks innerhalb der MessageBox.
	 * @param Subject
	 * @param Category
	 * @param Message
	 * @return input;
	 */
	private int showMessage(String Subject, String Category, String Message) {

		setDefaultCursor();
		int input = -1;
		
		if (Category.equalsIgnoreCase("Error")) {
			String[] Buttons = {"OK"};
			JOptionPane.showOptionDialog(Frame, Message, Subject, JOptionPane.DEFAULT_OPTION,
										JOptionPane.ERROR_MESSAGE, null, Buttons, Buttons[0]);
		}
		else if (Category.equalsIgnoreCase("Warning")) {
			String[] Buttons = {"OK"};
			JOptionPane.showOptionDialog(Frame, Message, Subject, JOptionPane.DEFAULT_OPTION,
										JOptionPane.WARNING_MESSAGE, null, Buttons, Buttons[0]);
		}
		else if (Category.equalsIgnoreCase("Confirmation")) {
			String[] Buttons = {"Ja", "Nein"};
			input = JOptionPane.showOptionDialog(Frame, Message, Subject, JOptionPane.DEFAULT_OPTION,
												 JOptionPane.QUESTION_MESSAGE, null, Buttons, Buttons[0]);
		}
		else if (Category.equalsIgnoreCase("WarningConfirmation")) {
			String[] Buttons = {"Ja", "Nein"};
			input = JOptionPane.showOptionDialog(Frame, Message, Subject, JOptionPane.DEFAULT_OPTION,
												 JOptionPane.WARNING_MESSAGE, null, Buttons, Buttons[0]);
		}

		return input;
	
	} // end showMessage

	
	
	/**
	 * Gibt die Komponente mit dem spezifierten Namen zurück.    
	 * @return Component
	 **/
	private Component getComponent(String Name) {

		Component[] ComponentArray = PagePanel.getComponents();
		for (int i = 0; i < ComponentArray.length; i++) {
			if (ComponentArray[i].getName() != null) {
				if (ComponentArray[i].getName().equals(Name)) {
					return ComponentArray[i];
				}
			}
		}
		return null;

	} // end getComponent
	
	
	
	/**
	 * Setzt die Button-Aktivität.
	 */
	public void setActivity() {

		Exit.setEnabled(true);
		Reset.setEnabled(true);

		if (currentPage > SETUP) {
			SouthPanel.setVisible(true);
			South.setBackground(Color.LIGHT_GRAY);
			Back.setEnabled(true);
			Next.setEnabled(false);
		}
		else {
			SouthPanel.setVisible(false);
			South.setBackground(UIManager.getColor("TableHeader.background"));
			Back.setEnabled(false);
			Next.setEnabled(true);
			Next.grabFocus();
		}
		
	} // end setActivity

	
	
	/**
	 * Setzt den Standard-Cursor.
	 */
	public void setDefaultCursor() {
		
		Frame.setCursor(Cursor.getDefaultCursor());
		
	} // end setDefaultCursor



	/**
	 * Setzt den Warte-Cursor.
	 */
	public void setWaitCursor() {
		
		Frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
	} // end setDefaultCursor

	
	
	/**
	 * Behandelt das Verhalten beim Schließen der Anwendung.
	 * @return boolean
	 */	
	private boolean exitApplication() {
		
/*		int input = messageHandling("Exit", "");
		if (input == 0) {
			return true; 
		}
		return false;
*/      return true; // nur zum schnellen Testen
	
	} // end exitApplication

	
	
	/**
	 * Setzt den übergebenen Überschriftentext.
	 * @param Text  
	 */
	public void setHeadlineText(String Text) {
		
		Headline.setText(Text);
		
	} // end setHeadlineText
	
	
	
	/**
	 * Gibt das ScrollPanel zurück.
	 */
	public JScrollPane getScrollPanel() {
		
		return ScrollPanel;
		
	} // end getScrollPanel


	
	/**
	 * Gibt den Namen des selktierten RadioButtons zurück.
	 * @return String
	 */
	public String getSelection() {
		
		Enumeration RadioButtons = RadioButtonGroup.getElements();	
		while (RadioButtons.hasMoreElements()) {
			JRadioButton RButton = (JRadioButton)RadioButtons.nextElement();
			if (RButton.isSelected()) {
				return RButton.getName();
			}
		}
		return "";
		
	} // end getSelection
	
	
	
	/**
	 * Die Main-Methode erteugt das Migration-Object.
	 * @param args
	 */
	public static void main(String[] args) {

		Migration Mig = new Migration();

	} // end main

	
} // end class Migration