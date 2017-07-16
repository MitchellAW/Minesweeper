import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

public class mouseListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (gameOver == false && e.getButton() != 3) {
				for(int i=0; i<board.getHeight(); i++) {
					for (int j=0; j<board.getWidth(); j++) {
						if(e.getSource() == boardButtons[i][j]
								&&!boardButtons[i][j].getText().equals(
										flag)) {

							// If a mine is clicked, reveal them
							if (board.isPiece(i, j, board.mine)) {
								gameOver = true;
								revealMines();
								boardButtons[i][j].setBackground(Color.RED);
								// If piece was blank, reveal rest of blanks
							} else if (board.isPiece(i, j, ' ')) {
								revealBlanks(i, j);
								if (isWinner()) {
									gameOver = true;
									winner();
								}
								// Reveal regular square
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
		if(e.getSource() == resetButton) {
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
	public void mousePressed(MouseEvent e) {
		if (gameOver == false) {
			for (int i=0; i<boardButtons.length; i++) {
				for (int j=0; j<boardButtons[0].length; j++) {
					if (e.getButton() == 3 && 
							e.getSource() == boardButtons[i][j]) {
						// Flag if not already flagged
						if (boardButtons[i][j].getText().equals("")) {
							setFlag(i, j);
							// Remove flag if already flagged
						} else if (boardButtons[i][j].getText().equals(flag)) {
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