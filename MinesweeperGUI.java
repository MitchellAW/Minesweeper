import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MinesweeperGUI extends JFrame {

	private static final long serialVersionUID = -7790575795524991131L;

	Board board = new Board("EASY");

	JButton[][] boardButtons = new JButton[board.getHeight()][board.getWidth()];
	JButton resetButton = new JButton("Reset");
	JFrame frame = new JFrame("Mitch's Minesweeper");

	boolean gameOver = false;

	String flag = Character.toString(board.flag);
	String mine = Character.toString(board.mine);

	Color background = new Color(236,236,236);
	Color defaultBg = new JButton().getBackground();

	public MinesweeperGUI() {
		frame.setSize(600, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(true);
	}
	private void initialise() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		JPanel gameBoard = new JPanel(new GridLayout(board.getHeight(),
				board.getWidth()));		

		frame.add(mainPanel);

		gameBoard.setPreferredSize(new Dimension(500,500));

		mainPanel.add(gameBoard, BorderLayout.NORTH);
		mainPanel.add(resetButton);

		resetButton.addMouseListener(new mouseListener());

		// Initialise all the buttons
		for(int i=0; i<board.getHeight(); i++) {
			for (int j=0; j<board.getWidth(); j++) {
				boardButtons[i][j] = new JButton();
				boardButtons[i][j].setVisible(true);
				setColour(i, j);

				gameBoard.add(boardButtons[i][j]); 
				boardButtons[i][j].addMouseListener(new mouseListener());
				boardButtons[i][j].setFont(new Font("Arial Unicode MS", 
						Font.BOLD, 30));
			}
		}
	}
	// Sets the piece to the appropriate colour based on the number of mines
	// surrounding it
	public void setColour(int row, int col) {
		String currentPiece = board.getPieceAt(row, col);
		switch (currentPiece) {
		case "1":
			boardButtons[row][col].setForeground(Color.BLUE);
			break;
		case "2":
			boardButtons[row][col].setForeground(Color.GREEN.darker());
			break;
		case "3":
			boardButtons[row][col].setForeground(Color.RED);
			break;
		case "4":
			boardButtons[row][col].setForeground(Color.BLUE.darker());
			break;
		case "5":
			boardButtons[row][col].setForeground(Color.RED.darker());
			break;
		case "6":
			boardButtons[row][col].setForeground(Color.BLUE.brighter());
			break;
		case "7":
			boardButtons[row][col].setForeground(Color.BLACK);
			break;
		case "8":
			boardButtons[row][col].setForeground(Color.GRAY.brighter());
			break;	
		}
	}
	// Will reveal what is on the board at row & column
	public void reveal(int row, int col) {
		if (board.isPiece(row, col, ' ')) {
			revealBlanks(row, col);
		} else {
			boardButtons[row][col].setText(board.getPieceAt(row, col));
			boardButtons[row][col].setBackground(background);
			setColour(row, col);
		}
	}
	// Will reveal all the mines on the board
	public void revealMines() {
		for (int i=0; i<boardButtons.length; i++) {
			for (int j=0; j<boardButtons[0].length; j++) {
				if (board.isPiece(i, j, board.mine)) {
					boardButtons[i][j].setForeground(Color.BLACK);
					boardButtons[i][j].setText(board.getPieceAt(i, j));
				}
			}
		}
	}
	// Will reveal all adjacent blank places
	// TODO make it reveal one layer further
	public void revealBlanks(int row, int col) {
		if (!boardButtons[row][col].getBackground().equals(defaultBg)) {
			return;
		}
		if (board.isPiece(row, col, ' ')) {
			boardButtons[row][col].setBackground(background);
			for (int i=row-1; i<=row+1; i++) {
				for (int j=col-1; j<=col+1; j++) {
					if (board.isPiece(i, j, ' ')) {
						revealBlanks(i, j);
					}
				}
			}
		}
	}
	public boolean isWinner() {
		for (int i=0; i<boardButtons.length; i++) {
			for (int j=0; j<boardButtons[0].length; j++) {
				if (!board.isPiece(i, j, board.mine) && 
						boardButtons[i][j].getBackground().equals(defaultBg)) {
					return false;
				}
			}
		}
		return true;
	}
	// Changes title to display that they have won
	public void winner() {
		frame.setTitle("Mitch's Minesweeper: ***** YOU WIN *****");
	}
	// Place / Remove flag
	public void setFlag(int row, int col) {
		if (!boardButtons[row][col].getText().equals(flag)) {
			boardButtons[row][col].setText(flag);
			boardButtons[row][col].setForeground(Color.RED);
		} else {
			boardButtons[row][col].setText("");
		}
	}
	// When buttons are clicked
	public class mouseListener implements MouseListener {
		// Playing on left clicks
		public void mouseClicked(MouseEvent e) {
			if (gameOver == false && e.getButton() != 3) {
				for(int i=0; i<board.getHeight(); i++) {
					for (int j=0; j<board.getWidth(); j++) {
						if(e.getSource() == boardButtons[i][j]
								&&!boardButtons[i][j].getText().equals(flag)) {

							// If a mine is clicked, reveal them
							if (board.isPiece(i, j, board.mine)) {
								gameOver = true;
								revealMines();
								boardButtons[i][j].setBackground(Color.RED);
								// If piece was blank, reveal rest of blanks
							} else {
								reveal(i, j);
								if (isWinner()) {
									gameOver = true;
									winner();
								}
							}
						}
					}
				}
			}
			// When reset button is clicked
			if(e.getSource() == resetButton) {
				board.reset();
				gameOver = false;
				for(int i=0; i<board.getHeight(); i++) {
					for (int j=0; j<board.getWidth(); j++) { 
						boardButtons[i][j].setBackground(defaultBg);
						boardButtons[i][j].setText("");
						frame.setTitle("Mitch's Minesweeper");
					}
				}
			}
		}
		// Flagging on right click press
		public void mousePressed(MouseEvent e) {
			if (gameOver == false) {
				for (int i=0; i<boardButtons.length; i++) {
					for (int j=0; j<boardButtons[0].length; j++) {
						String buttonText = boardButtons[i][j].getText();
						if (e.getButton() == 3 && 
								e.getSource() == boardButtons[i][j]) {
							// Flag if not already flagged
							if (buttonText.equals("")) {
								setFlag(i, j);
								// Remove flag if already flagged
							} else if (buttonText.equals(flag)) {
								setFlag(i, j);
							}
						}
					}
				}
			}
		}
		public void mouseEntered(MouseEvent arg0) { }
		public void mouseExited(MouseEvent arg0) { }
		public void mouseReleased(MouseEvent arg0) { }
	}
	public static void main(String[] args) {
		MinesweeperGUI game = new MinesweeperGUI();
		game.initialise();
	}
}
