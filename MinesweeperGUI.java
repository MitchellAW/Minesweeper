import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MinesweeperGUI extends JFrame {

	private static final long serialVersionUID = -7790575795524991131L;

	Board board = new Board("EASY");

	JButton[][] boardButtons = new JButton[board.getHeight()][board.getWidth()];
	JButton resetButton = new JButton("Reset");
	JFrame frame = new JFrame("Mitch's Minesweeper");

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
				setColours(i, j);

				gameBoard.add(boardButtons[i][j]); 
				boardButtons[i][j].addActionListener(new myActionListener());
				boardButtons[i][j].setFont(new Font("Arial Unicode MS", Font.BOLD, 30));
			}
		}
	}
	// Listen for when the buttons are clicked
	private class myActionListener implements ActionListener {
		public void actionPerformed(ActionEvent action) {

			//Display the player's piece on the buttons
			for(int i=0; i<board.getHeight(); i++) {
				for (int j=0; j<board.getWidth(); j++) {
					if (action.getSource() == boardButtons[i][j] && 
							board.isPiece(i, j, board.getMine())) {
						revealMines();
						boardButtons[i][j].setBackground(Color.RED);
					} else if (action.getSource() == boardButtons[i][j]) {
						reveal(i, j);
					}
				}
			}
			// When the reset button is clicked, reset the board & the display
			if(action.getSource() == resetButton) {
				board.reset();
				for(int i=0; i<board.getHeight(); i++) {
					for (int j=0; j<board.getWidth(); j++) { 
						boardButtons[i][j].setEnabled(true);
						boardButtons[i][j].setBackground(new JButton().getBackground());
						boardButtons[i][j].setForeground(Color.BLACK);
						boardButtons[i][j].setText("");
						setColours(i, j);
						frame.setTitle("Mitch's Minesweeper");
					}
				}
			}
		}
	}
	public void setColours(int row, int column) {
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
	}
	public void revealMines() {
		for (int i=0; i<boardButtons.length; i++) {
			for (int j=0; j<boardButtons[0].length; j++) {
				if (board.isPiece(i, j, board.getMine())) {
					boardButtons[i][j].setForeground(Color.BLACK);
					boardButtons[i][j].setText(board.getPieceAt(i, j));
				} else {
					if (board.isPiece(i, j, ' ')) {
						boardButtons[i][j].setEnabled(false);
					}
					reveal(i, j);
				}
			}
		}
	}


	public static void main(String[] args) {
		MinesweeperGUI game = new MinesweeperGUI();
		game.initialise();
	}
}
