import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
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

	Color bgColour = new Color(236,236,236);

	public MinesweeperGUI() {
		frame.setSize(600, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(true);
	}
	private void initialise() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		JPanel gameBoard = new JPanel(new GridLayout(board.getHeight(),board.getWidth()));		

		frame.add(mainPanel);

		gameBoard.setPreferredSize(new Dimension(500,500));

		mainPanel.add(gameBoard, BorderLayout.NORTH);
		mainPanel.add(resetButton);

		resetButton.addActionListener(new myActionListener());

		// Initialise all the buttons
		for(int i=0; i<board.getHeight(); i++) {
			for (int j=0; j<board.getWidth(); j++) {
				boardButtons[i][j] = new JButton();
				boardButtons[i][j].setVisible(true);
				setColour(i, j);

				gameBoard.add(boardButtons[i][j]); 
				boardButtons[i][j].addActionListener(new myActionListener());
				boardButtons[i][j].addMouseListener(new mouseListener());

				// 30 for easy, 
				boardButtons[i][j].setFont(new Font("Arial Unicode MS", Font.BOLD, 30));
			}
		}
	}
	// Listen for when the buttons are clicked
	private class myActionListener implements ActionListener {
		public void actionPerformed(ActionEvent action) {

			//Display the player's piece on the buttons
			if (gameOver == false) {
				for(int i=0; i<board.getHeight(); i++) {
					for (int j=0; j<board.getWidth(); j++) {
						if (action.getSource() == boardButtons[i][j] && 
								board.isPiece(i, j, board.getMine())) {
							gameOver = true;
							revealMines();
							boardButtons[i][j].setBackground(Color.RED);
						} else if (action.getSource() == boardButtons[i][j] &&
								board.isPiece(i, j, ' ')) {
							revealBlanks(i, j);
							if (isWinner()) {
								gameOver = true;
								winner();
							}
						} else if (action.getSource() == boardButtons[i][j]) {
							reveal(i, j);
							if (isWinner()) {
								gameOver = true;
								winner();
							}
						}
					}
				}
			}
			// When the reset button is clicked, reset the board & the display
			if(action.getSource() == resetButton) {
				board.reset();
				gameOver = false;
				for(int i=0; i<board.getHeight(); i++) {
					for (int j=0; j<board.getWidth(); j++) { 
						boardButtons[i][j].setEnabled(true);
						boardButtons[i][j].setBackground(new JButton().getBackground());
						boardButtons[i][j].setForeground(Color.BLACK);
						boardButtons[i][j].setText("");
						setColour(i, j);
						frame.setTitle("Mitch's Minesweeper");
					}
				}
			}
		}
	}
	public class mouseListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent arg0) {
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
			for (int i=0; i<boardButtons.length; i++) {
				for (int j=0; j<boardButtons[0].length; j++) {
					if (e.getButton() == 3 && e.getSource() == boardButtons[i][j]) {
						setFlag(i, j);
					}
				}
			}
		}
		@Override
		public void mouseReleased(MouseEvent arg0) {
		}

	}
	public void setColour(int row, int column) {
		String currentPiece = board.getPieceAt(row, column);
		switch (currentPiece) {
		case "1":
			boardButtons[row][column].setForeground(Color.BLUE);
			break;
		case "2":
			boardButtons[row][column].setForeground(Color.GREEN.darker());
			break;
		case "3":
			boardButtons[row][column].setForeground(Color.RED);
			break;
		case "4":
			boardButtons[row][column].setForeground(Color.BLUE.darker());
			break;
		case "5":
			boardButtons[row][column].setForeground(Color.RED.darker());
			break;
		case "6":
			boardButtons[row][column].setForeground(Color.BLUE.brighter());
			break;
		case "7":
			boardButtons[row][column].setForeground(Color.BLACK);
			break;
		case "8":
			boardButtons[row][column].setForeground(Color.GRAY.brighter());
			break;	
		}
	}
	public void reveal(int row, int column) {
		boardButtons[row][column].setText(board.getPieceAt(row, column));
		boardButtons[row][column].setBackground(bgColour);
		setColour(row, column);
	}
	public void revealMines() {
		for (int i=0; i<boardButtons.length; i++) {
			for (int j=0; j<boardButtons[0].length; j++) {
				if (board.isPiece(i, j, board.getMine())) {
					boardButtons[i][j].setForeground(Color.BLACK);
					boardButtons[i][j].setText(board.getPieceAt(i, j));
				}
			}
		}
	}
	public void revealBlanks(int row, int column) {
		if (!boardButtons[row][column].getBackground().equals(new JButton().getBackground())) {
			return;
		}
		if (board.isPiece(row, column, ' ')) {
			boardButtons[row][column].setBackground(bgColour);
			if (board.isPiece(row - 1, column, ' ')) { revealBlanks(row-1, column);}
			if (board.isPiece(row - 1, column + 1, ' ')) { revealBlanks(row-1, column+1);}
			if (board.isPiece(row, column + 1, ' ')) { revealBlanks(row, column+1);}
			if (board.isPiece(row + 1, column + 1, ' ')) { revealBlanks(row+1, column+1);}
			if (board.isPiece(row + 1, column, ' ')) { revealBlanks(row+1, column);}
			if (board.isPiece(row + 1, column - 1, ' ')) { revealBlanks(row+1, column-1);}
			if (board.isPiece(row, column - 1, ' ')) { revealBlanks(row, column-1);}
			if (board.isPiece(row - 1, column - 1, ' ')) { revealBlanks(row-1, column-1);}
		}
	}
	public static void main(String[] args) {
		MinesweeperGUI game = new MinesweeperGUI();
		game.initialise();
	}
	public boolean isWinner() {
		for (int i=0; i<boardButtons.length; i++) {
			for (int j=0; j<boardButtons[0].length; j++) {
				if (!board.isPiece(i, j, board.getMine()) && boardButtons[i][j].getBackground().equals(new JButton().getBackground())) {
					return false;
				}
			}
		}
		return true;
	}
	public void winner() {
		frame.setTitle("Mitch's Minesweeper: ***** YOU WIN *****");
	}
	public void setFlag(int row, int column) {
		if (!boardButtons[row][column].getText().equals(Character.toString(board.getFlag()))) {
			boardButtons[row][column].setText(Character.toString(board.getFlag()));
			boardButtons[row][column].setForeground(Color.RED);
		} else {
			boardButtons[row][column].setText("");
		}
	}
}
