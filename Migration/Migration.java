import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;


/**
 * @author Lan Nguyen
 */

public class Migration implements ActionListener {

	private JFrame Frame;	
	private JButton Help;
	private JButton Reset;
	private JButton Exit;
	private JLabel Headline;
	private JPanel MainPanel;
	private GridBagLayout Layout;
	private	GridBagConstraints GBC;
	
	
	/**
	 * Der Konstrukter erzeugt den Anwendungsframe mit allen Komponenten.
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
		
		Layout = new GridBagLayout();
		GBC = new GridBagConstraints();
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
		
		Help = new JButton("Hilfe");		// erzeugt den Help-Button
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
		Layout.setConstraints(Exit, GBC);
		
		
		Reset = new JButton("Reset");	// erzeugt den Reset-Button
		Reset.setPreferredSize(new Dimension(90,25));
		Reset.addActionListener(this);
		Reset.setEnabled(false);
		GBC.gridy = row++;
		Layout.setConstraints(Reset, GBC);
		
		JPanel East = new JPanel(Layout);	// erzeugt den rechten Rand mit allen Buttons
		East.add(Help);
		East.add(Exit);
		East.add(Reset);
		
		JPanel South = new JPanel();		// erzeugt den unteren Rand
		South.setPreferredSize(new Dimension(500,50));

		MainPanel = new JPanel();	// erzeugt das Hauptanzeigefenster in der Mitte des Frames
		MainPanel.setBackground(new Color(255,255,230));
		
		Frame.getContentPane().add(North,BorderLayout.NORTH);	// fügt alle Komponenten dem Frame hinzu
		Frame.getContentPane().add(East,BorderLayout.EAST);
		Frame.getContentPane().add(West,BorderLayout.WEST);
		Frame.getContentPane().add(MainPanel,BorderLayout.CENTER);
		Frame.getContentPane().add(South,BorderLayout.SOUTH);

		Toolkit Kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = Kit.getScreenSize();

		int screenHeight = screenSize.height;
		int screenWidth = screenSize.width;

		Frame.setSize(new Dimension(800,600));
		Frame.setLocation(screenWidth/6, screenHeight/6);
		Frame.getContentPane().setBackground(new Color(100,200,255));
		Frame.setVisible(true);

		initialize(); // initialisiert dei Anwendung
		setActivity(); // setzt die Button-Aktivität
	
	} // end ASP_Selection (constructor)

	
	
	/**
	 * Implements the ActionListener for the frame components.
	 * @param AE
	 */
	public void actionPerformed(ActionEvent AE) {
		
		setWaitCursor();

		if (AE.getSource() == Help) {
			
		}

		setDefaultCursor();

	} // end actionPerformed

	
	
	/**
	 * Initialisiert die Anwendung.
	 */
	private void initialize() {
	
		setHeadlineText("Bitte die Initialisierungswerte festlegen:");
		
	} // end initialize()

	
	
	/**
	 * Gibt die Abstände zwischen den J-Komponenten zurück.    
	 * @param row
	 * @param Element
	 * @return NewInsets
	 **/
	public Insets getInsets(int row, String Element) {

		int first = 15, other = 10;
		Insets NewInsets = new Insets(0,0,0,0);

		if (Element.equalsIgnoreCase("Button")) {
			return new Insets(10, 10, 10, 10);
		}		
		else if (Element.equalsIgnoreCase("HelpArea")) {
			return new Insets(10, 10, 10, 10);
		}
		else if (Element.equalsIgnoreCase("Label") && row == 0) {
			return new Insets(first, other, other, other);
		}
		else if (Element.equalsIgnoreCase("Label") && row != 0) {
			return new Insets(other, other, other, other);
		}
		else if (Element.equalsIgnoreCase("Box") && row == 0) {
			return new Insets(first, other, other, other);
		}
		else if (Element.equalsIgnoreCase("Box") && row != 0) {
			return new Insets(other, other, other, other);
		}
		else if (Element.equalsIgnoreCase("Field") && row == 0) {
			return new Insets(first, other, other, 5);
		}
		else if (Element.equalsIgnoreCase("Field") && row != 0) {
			return new Insets(other, other, other, 5);
		}
		else if (Element.equalsIgnoreCase("Hint") && row == 0) {
			return new Insets(first, 0, other, 0);
		}
		else if (Element.equalsIgnoreCase("Hint") && row != 0) {
			return new Insets(other, 0, other, other);
		}
		else if (Element.equalsIgnoreCase("Radio") && row == 0) {
			return new Insets(first, 0, other, 0);
		}
		else if (Element.equalsIgnoreCase("Radio") && row != 0) {
			return new Insets(other, 0, other, other);
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
		return input;		

	} // messageHandling

	
	
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
	 * Setzt die Button-Aktivität.
	 */
	public void setActivity() {

		Help.setEnabled(true);
		
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
		
		int input = messageHandling("Exit", "");
		if (input == 0) {
			return true; 
		}
		return false;
	
	} // end exitApplication

	
	
	/**
	 * Setzt den übergebenen Überschriftentext.
	 * @param Text  
	 */
	public void setHeadlineText(String Text) {
		
		Headline.setText(Text);
		
	} // end setHeadlineText
	
	
	
	/**
	 * Die Main-Methode erteugt das Migration-Object.
	 * @param args
	 */
	public static void main(String[] args) {

		Migration Mig = new Migration();

	} // end main

	
} // end class ASP_Selection