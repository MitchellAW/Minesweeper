import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MinesweeperGUI extends JFrame {

	private static final long serialVersionUID = -7790575795524991131L;

	Board board = new Board("EASY");

	JButton[][] boardButtons;
	JButton resetButton = new JButton("Reset");
	JFrame frame = new JFrame("Mitch's Minesweeper");

	JMenuItem easyOption = new JMenuItem("Easy");
	JMenuItem mediumOption = new JMenuItem("Medium");
	JMenuItem hardOption = new JMenuItem("Hard");

	boolean gameOver = false;

	String flag = Character.toString(board.flag);
	String mine = Character.toString(board.mine);

	Color background = new Color(236, 236, 236);
	Color defaultBg = new JButton().getBackground();

	public MinesweeperGUI() {
		frame.setJMenuBar(createMenuBar());

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(createBoard(board.getDifficulty()));
		frame.setVisible(true);
		frame.setResizable(true);
	}

	public JMenuBar createMenuBar() {
		JMenuBar menuBar;
		JMenu menu, submenu;

		// Initalise the menu bar
		menuBar = new JMenuBar();
		menu = new JMenu("File");

		frame.add(menu);

		menuBar.add(menu);

		// Submenu of difficulties
		submenu = new JMenu("Change Difficulty");
		submenu.setMnemonic(KeyEvent.VK_S);

		submenu.add(easyOption);
		easyOption.addActionListener(new MyActionListener());

		submenu.add(mediumOption);
		mediumOption.addActionListener(new MyActionListener());

		submenu.add(hardOption);
		hardOption.addActionListener(new MyActionListener());

		menu.add(submenu);

		return menuBar;
	}

	public JPanel createBoard(String difficulty) {
		board.setDifficulty(difficulty);
		board.reset();

		gameOver = false;

		JPanel mainPanel = new JPanel(new BorderLayout());
		JPanel gameBoard = new JPanel(
				new GridLayout(board.getHeight(), board.getWidth()));

		boardButtons = new JButton[board.getHeight()][board.getWidth()];

		frame.add(mainPanel);

		// Set the window sizes appropriately based on the difficulty
		switch (difficulty) {
		case "EASY":
			frame.setSize(500, 600);
			gameBoard.setPreferredSize(new Dimension(450, 450));
			break;
		case "MEDIUM":
			frame.setSize(900, 1000);
			gameBoard.setPreferredSize(new Dimension(800, 800));
			break;
		case "HARD":
			frame.setSize(1700, 1000);
			gameBoard.setPreferredSize(new Dimension(1600, 800));
			break;
		}

		mainPanel.add(gameBoard, BorderLayout.NORTH);
		mainPanel.add(resetButton);

		resetButton.addActionListener(new MyActionListener());

		// Initialise all the buttons
		for (int i = 0; i < board.getHeight(); i++) {
			for (int j = 0; j < board.getWidth(); j++) {
				boardButtons[i][j] = new JButton();
				boardButtons[i][j].setVisible(true);
				setColour(i, j);

				gameBoard.add(boardButtons[i][j]);
				boardButtons[i][j].addMouseListener(new MyMouseListener());
				boardButtons[i][j].addActionListener(new MyActionListener());
				boardButtons[i][j]
						.setFont(new Font("Arial Unicode MS", Font.BOLD, 20));
			}
		}
		return mainPanel;
	}

	// Sets the piece to the appropriate colour based on the number of mines
	// surrounding it
	public void setColour(int row, int col) {
		String currentPiece = board.getPieceAt(row, col);
		switch (currentPiece) {
		case "1":
			boardButtons[row][col].setForeground(new Color(1, 0, 254));
			break;
		case "2":
			boardButtons[row][col].setForeground(new Color(1, 127, 1));
			break;
		case "3":
			boardButtons[row][col].setForeground(new Color(254, 0, 0));
			break;
		case "4":
			boardButtons[row][col].setForeground(new Color(0, 0, 127));
			break;
		case "5":
			boardButtons[row][col].setForeground(new Color(129, 1, 2));
			break;
		case "6":
			boardButtons[row][col].setForeground(new Color(0, 128, 129));
			break;
		case "7":
			boardButtons[row][col].setForeground(new Color(0, 0, 0));
			break;
		case "8":
			boardButtons[row][col].setForeground(new Color(128, 128, 128));
			break;
		}
	}

	// Will reveal what is on the board at row & column
	public void reveal(int row, int col) {
		if (board.isPiece(row, col, ' ')) {
			revealBlanks(row, col);
		} else {
			if (board.isValid(row, col)
					&& !board.isPiece(row, col, board.getMine())) {
				boardButtons[row][col].setText(board.getPieceAt(row, col));
				boardButtons[row][col].setBackground(background);
				setColour(row, col);
			}
		}
	}

	// Will reveal all the mines on the board
	public void revealMines() {
		for (int i = 0; i < boardButtons.length; i++) {
			for (int j = 0; j < boardButtons[0].length; j++) {
				if (board.isPiece(i, j, board.mine)) {
					if (boardButtons[i][j].getText().equals(flag) == false) {
						boardButtons[i][j].setForeground(Color.BLACK);
						boardButtons[i][j].setText(board.getPieceAt(i, j));
					} else if (board.isPiece(i, j, board.mine) && 
					boardButtons[i][j].getText().equals(flag)) {
						boardButtons[i][j].setForeground(Color.BLACK);
						boardButtons[i][j].setBackground(Color.RED);
					}
				}
			}
		}
	}

	// Will reveal all adjacent blank places and the outer layer of hints
	public void revealBlanks(int row, int col) {
		if (!boardButtons[row][col].getBackground().equals(defaultBg)) {
			return;
		}
		if (board.isPiece(row, col, ' ')) {
			boardButtons[row][col].setBackground(background);
			for (int i = row - 1; i <= row + 1; i++) {
				for (int j = col - 1; j <= col + 1; j++) {
					if (board.isPiece(i, j, ' ')) {
						revealBlanks(i, j);
					} else {
						reveal(i, j);
					}
				}
			}
		}
	}

	// Return true if the player has won the game
	public boolean isWinner() {
		for (int i = 0; i < boardButtons.length; i++) {
			for (int j = 0; j < boardButtons[0].length; j++) {
				if (!board.isPiece(i, j, board.mine) && boardButtons[i][j]
						.getBackground().equals(defaultBg)) {
					return false;
				}
			}
		}
		return true;
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

	public class MyActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// When a new difficulty is selected in the menu
			if (e.getSource() == easyOption) {
				frame.setContentPane(createBoard("EASY"));
			} else if (e.getSource() == mediumOption) {
				frame.setContentPane(createBoard("MEDIUM"));
			} else if (e.getSource() == hardOption) {
				frame.setContentPane(createBoard("HARD"));
				// When reset button is pressed
			} else if (e.getSource() == resetButton) {
				board.reset();
				gameOver = false;
				frame.setTitle("Mitch's Minesweeper");
				for (int i = 0; i < boardButtons.length; i++) {
					for (int j = 0; j < boardButtons[0].length; j++) {
						boardButtons[i][j].setBackground(defaultBg);
						boardButtons[i][j].setText("");
					}
				}
			}
			// When any board button is clicked
			if (gameOver == false) {
				for (int i = 0; i < board.getHeight(); i++) {
					for (int j = 0; j < board.getWidth(); j++) {
						if (e.getSource() == boardButtons[i][j]
								&& !boardButtons[i][j].getText().equals(flag)) {

							// If a mine is clicked, reveal them
							if (board.isPiece(i, j, board.mine)) {
								gameOver = true;
								revealMines();
								frame.setTitle(
										"Mitch's Minesweeper: *** YOU LOSE ***");
								boardButtons[i][j].setBackground(Color.RED);
								JOptionPane.showMessageDialog(frame,
										"Bad Luck. You lose.");
								// If piece wasn't blank, reveal
							} else {
								reveal(i, j);
								if (isWinner()) {
									gameOver = true;
									frame.setTitle(
											"Mitch's Minesweeper: *** YOU WIN ***");
									JOptionPane.showMessageDialog(frame,
											"Congratulations! You win!");
								}
							}
						}
					}
				}
			}
		}
	}

	// When a board button is right clicked, flag the mine
	public class MyMouseListener implements MouseListener {
		public void mousePressed(MouseEvent e) {
			if (gameOver == false) {
				for (int i = 0; i < boardButtons.length; i++) {
					for (int j = 0; j < boardButtons[0].length; j++) {
						String buttonText = boardButtons[i][j].getText();
						if (e.getButton() == 3
								&& e.getSource() == boardButtons[i][j]) {
							// Flag if not already flagged, otherwise unflag
							if (buttonText.equals("")
									|| buttonText.equals(flag)) {
								setFlag(i, j);
							}
						}
					}
				}
			}
		}

		public void mouseClicked(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}
	}

	public static void main(String[] args) {
		new MinesweeperGUI();
	}
}
