import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToeGUI extends JFrame {
    private JButton[][] buttons = new JButton[3][3];
    private char currentPlayer = 'X';
    private boolean gameActive = true;
    private JLabel statusLabel;
    private JPanel gamePanel;
    private int xWins = 0;
    private int oWins = 0;
    private int draws = 0;
    private JLabel scoreLabel;

    public TicTacToeGUI() {
        // Set up the main window
        setTitle("Tic-Tac-Toe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(500, 600);
        setResizable(false);

        // Center the window on screen
        setLocationRelativeTo(null);

        // Create top panel for title and status
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBackground(new Color(70, 130, 180));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title label
        JLabel titleLabel = new JLabel("Tic-Tac-Toe", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel, BorderLayout.NORTH);

        // Status label
        statusLabel = new JLabel("Player X's Turn", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        statusLabel.setForeground(Color.YELLOW);
        topPanel.add(statusLabel, BorderLayout.CENTER);

        // Score label
        scoreLabel = new JLabel("X: 0 | O: 0 | Draws: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        scoreLabel.setForeground(Color.WHITE);
        topPanel.add(scoreLabel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // Create the game panel with 3x3 grid
        gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(3, 3, 5, 5));
        gamePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        gamePanel.setBackground(new Color(240, 240, 240));

        // Initialize buttons
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col] = new JButton("");
                buttons[row][col].setFont(new Font("Arial", Font.BOLD, 60));
                buttons[row][col].setFocusPainted(false);
                buttons[row][col].setBackground(Color.WHITE);
                buttons[row][col].setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 3));

                // Add action listener
                int finalRow = row;
                int finalCol = col;
                buttons[row][col].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        buttonClicked(finalRow, finalCol);
                    }
                });

                gamePanel.add(buttons[row][col]);
            }
        }

        add(gamePanel, BorderLayout.CENTER);

        // Create control panel with buttons
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controlPanel.setBackground(new Color(220, 220, 220));

        // New Game button
        JButton newGameButton = new JButton("New Game");
        newGameButton.setFont(new Font("Arial", Font.BOLD, 16));
        newGameButton.setBackground(new Color(70, 130, 180));
        newGameButton.setForeground(Color.WHITE);
        newGameButton.setFocusPainted(false);
        newGameButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        newGameButton.addActionListener(e -> resetGame());

        // Reset Score button
        JButton resetScoreButton = new JButton("Reset Score");
        resetScoreButton.setFont(new Font("Arial", Font.BOLD, 16));
        resetScoreButton.setBackground(new Color(220, 20, 60));
        resetScoreButton.setForeground(Color.WHITE);
        resetScoreButton.setFocusPainted(false);
        resetScoreButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        resetScoreButton.addActionListener(e -> resetScore());

        // Exit button
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 16));
        exitButton.setBackground(new Color(50, 50, 50));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        exitButton.addActionListener(e -> System.exit(0));

        controlPanel.add(newGameButton);
        controlPanel.add(resetScoreButton);
        controlPanel.add(exitButton);

        add(controlPanel, BorderLayout.SOUTH);

        // Make the window visible
        setVisible(true);
    }

    private void buttonClicked(int row, int col) {
        // If game is not active or button already clicked, do nothing
        if (!gameActive || !buttons[row][col].getText().equals("")) {
            return;
        }

        // Set the button text to current player's symbol
        buttons[row][col].setText(String.valueOf(currentPlayer));

        // Set color based on player
        if (currentPlayer == 'X') {
            buttons[row][col].setForeground(new Color(220, 20, 60)); // Red for X
        } else {
            buttons[row][col].setForeground(new Color(30, 144, 255)); // Blue for O
        }

        // Check for win or draw
        if (checkWin()) {
            statusLabel.setText("Player " + currentPlayer + " Wins!");
            gameActive = false;

            // Update score
            if (currentPlayer == 'X') {
                xWins++;
            } else {
                oWins++;
            }
            updateScore();

            // Highlight winning line
            highlightWinningCells();

        } else if (checkDraw()) {
            statusLabel.setText("Game Draw!");
            gameActive = false;
            draws++;
            updateScore();
        } else {
            // Switch player
            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
            statusLabel.setText("Player " + currentPlayer + "'s Turn");
        }
    }

    private boolean checkWin() {
        String[][] board = new String[3][3];

        // Convert button texts to a 2D array
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = buttons[i][j].getText();
            }
        }

        // Check rows
        for (int i = 0; i < 3; i++) {
            if (!board[i][0].equals("") &&
                    board[i][0].equals(board[i][1]) &&
                    board[i][1].equals(board[i][2])) {
                return true;
            }
        }

        // Check columns
        for (int i = 0; i < 3; i++) {
            if (!board[0][i].equals("") &&
                    board[0][i].equals(board[1][i]) &&
                    board[1][i].equals(board[2][i])) {
                return true;
            }
        }

        // Check diagonals
        if (!board[0][0].equals("") &&
                board[0][0].equals(board[1][1]) &&
                board[1][1].equals(board[2][2])) {
            return true;
        }

        if (!board[0][2].equals("") &&
                board[0][2].equals(board[1][1]) &&
                board[1][1].equals(board[2][0])) {
            return true;
        }

        return false;
    }

    private void highlightWinningCells() {
        String[][] board = new String[3][3];

        // Convert button texts to a 2D array
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = buttons[i][j].getText();
            }
        }

        Color winColor = new Color(144, 238, 144); // Light green for winning cells

        // Check rows
        for (int i = 0; i < 3; i++) {
            if (!board[i][0].equals("") &&
                    board[i][0].equals(board[i][1]) &&
                    board[i][1].equals(board[i][2])) {
                buttons[i][0].setBackground(winColor);
                buttons[i][1].setBackground(winColor);
                buttons[i][2].setBackground(winColor);
                return;
            }
        }

        // Check columns
        for (int i = 0; i < 3; i++) {
            if (!board[0][i].equals("") &&
                    board[0][i].equals(board[1][i]) &&
                    board[1][i].equals(board[2][i])) {
                buttons[0][i].setBackground(winColor);
                buttons[1][i].setBackground(winColor);
                buttons[2][i].setBackground(winColor);
                return;
            }
        }

        // Check diagonals
        if (!board[0][0].equals("") &&
                board[0][0].equals(board[1][1]) &&
                board[1][1].equals(board[2][2])) {
            buttons[0][0].setBackground(winColor);
            buttons[1][1].setBackground(winColor);
            buttons[2][2].setBackground(winColor);
            return;
        }

        if (!board[0][2].equals("") &&
                board[0][2].equals(board[1][1]) &&
                board[1][1].equals(board[2][0])) {
            buttons[0][2].setBackground(winColor);
            buttons[1][1].setBackground(winColor);
            buttons[2][0].setBackground(winColor);
            return;
        }
    }

    private boolean checkDraw() {
        // Check if all cells are filled
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().equals("")) {
                    return false;
                }
            }
        }
        return true;
    }

    private void resetGame() {
        // Reset all buttons
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setBackground(Color.WHITE);
            }
        }

        // Reset game state
        currentPlayer = 'X';
        gameActive = true;
        statusLabel.setText("Player X's Turn");
    }

    private void resetScore() {
        xWins = 0;
        oWins = 0;
        draws = 0;
        updateScore();
    }

    private void updateScore() {
        scoreLabel.setText("X: " + xWins + " | O: " + oWins + " | Draws: " + draws);
    }

    public static void main(String[] args) {
        // Run the GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TicTacToeGUI();
            }
        });
    }
}