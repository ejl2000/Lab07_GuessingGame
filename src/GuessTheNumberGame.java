import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * A simple GUI-based game where the user guesses a number between 1 and 100.
 *
 * @author Emma Lee, Chris Helmhold
 * @version 2024
 */
public class GuessTheNumberGame extends JFrame implements ActionListener
{
    private static final int LOWEST_SCORE = 0;
    private static final int EXIT_STATUS = 0;
    private static final int MINIMUM_GUESS_VALUE = 1;
    private static final int WINDOW_WIDTH = 400;
    private static final int WINDOW_HEIGHT = 300;
    private static final int TEXT_FIELD_COLUMNS = 10;
    private static final int FEEDBACK_TEXT_AREA_ROWS = 10;
    private static final int FEEDBACK_TEXT_AREA_COLUMNS = 30;
    private static final int NUMBER_RANGE = 100;

    private int targetNumber;
    private int score;
    private JTextField guessField;
    private JLabel resultLabel;
    private JLabel scoreLabel;

    /**
     * Constructs the game window and widgets, and starts a new game.
     */
    public GuessTheNumberGame()
    {
        setTitle("Guess the Number Game");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        createMenu();
        createWidgets();

        resetGame();
    }

    /**
     * Creates the menu bar with options for how to play, sending feedback, restarting the game, and exiting.
     */
    private void createMenu()
    {
        JMenuBar menuBar = new JMenuBar();

        JMenu optionsMenu = new JMenu("Options");

        JMenuItem howToPlayItem = new JMenuItem("How to Play");
        howToPlayItem.addActionListener(e -> showInstructions());
        optionsMenu.add(howToPlayItem);

        JMenuItem sendFeedbackItem = new JMenuItem("Send Feedback");
        sendFeedbackItem.addActionListener(e -> sendFeedback());
        optionsMenu.add(sendFeedbackItem);

        optionsMenu.addSeparator();

        JMenuItem restartGameItem = new JMenuItem("Restart Game");
        restartGameItem.addActionListener(e -> resetGame());
        optionsMenu.add(restartGameItem);

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(EXIT_STATUS));
        optionsMenu.add(exitItem);

        menuBar.add(optionsMenu);
        setJMenuBar(menuBar);
    }

    /**
     * Creates the main widgets for the game interface for guesses.
     */
    private void createWidgets()
    {
        JLabel instructions = new JLabel("Guess a number between 1 and " + NUMBER_RANGE);
        add(instructions);

        guessField = new JTextField(TEXT_FIELD_COLUMNS);
        add(guessField);

        JButton guessButton = new JButton("Submit Guess");
        guessButton.addActionListener(this);
        add(guessButton);

        resultLabel = new JLabel("");
        add(resultLabel);

        scoreLabel = new JLabel("Score: 0");
        add(scoreLabel);
    }

    /**
     * Shows instructions on how to play the game in a message dialog.
     */
    private void showInstructions()
    {
        JOptionPane.showMessageDialog(this, "Instructions:\n\n" +
                "1. Enter a number between 1 and " + NUMBER_RANGE + " in the text field.\n" +
                "2. Click 'Submit Guess' to check your guess.\n" +
                "3. If your guess is too low or too high, you will receive feedback.\n" +
                "4. Keep guessing until you find the correct number. \n" +
                "5. Your score will be displayed and updated after each guess. \n" +
                "6. You can restart the game or exit from the 'Options' menu.");
    }

    /**
     * Opens a feedback dialog allowing the user to enter feedback and save it to a file.
     */
    private void sendFeedback()
    {
        JTextArea feedbackTextArea = new JTextArea(FEEDBACK_TEXT_AREA_ROWS, FEEDBACK_TEXT_AREA_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(feedbackTextArea);
        int result = JOptionPane.showConfirmDialog(this, scrollPane, "Enter your feedback", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION)
        {
            String feedback = feedbackTextArea.getText();
            saveFeedback(feedback);
        }
    }

    /**
     * Saves the user's feedback to a file.
     *
     * @param feedback The feedback text entered by the user.
     */
    private void saveFeedback(final String feedback)
    {
        try (FileWriter writer = new FileWriter("feedback.txt", true))
        {
            writer.write(feedback + "\n");
            JOptionPane.showMessageDialog(this, "Feedback submitted successfully.");
        }

        catch (IOException e)
        {
            JOptionPane.showMessageDialog(this, "Error saving feedback.");
            e.printStackTrace();
        }
    }

    /**
     * Resets the game by generating a new target number and resetting the score.
     */
    private void resetGame()
    {
        targetNumber = new Random().nextInt(NUMBER_RANGE) + MINIMUM_GUESS_VALUE;
        score = LOWEST_SCORE ;
        updateScore();
        resultLabel.setText("");
        guessField.setText("");
    }

    /**
     * Updates the score label to reflect the current score.
     */
    private void updateScore()
    {
        scoreLabel.setText("Score: " + score);
    }

    /**
     * Handles the user's guess, providing feedback and updating the score.
     *
     * @param e The action event triggered by the user submitting a guess.
     */
    @Override
    public void actionPerformed(final ActionEvent e)
    {
        try
        {
            int guess = Integer.parseInt(guessField.getText());
            if (guess < MINIMUM_GUESS_VALUE || guess > NUMBER_RANGE)
            {
                throw new NumberFormatException();
            }
            score++;
            if (guess < targetNumber)
            {
                resultLabel.setText("Too low! Try again.");
            }

            else if (guess > targetNumber)
            {
                resultLabel.setText("Too high! Try again.");
            }

            else
            {
                resultLabel.setText("Correct! You guessed the number in " + score + " attempts.");
                score = LOWEST_SCORE ;
            }
            updateScore();
        }

        catch (NumberFormatException ex)
        {
            JOptionPane.showMessageDialog(this, "Please enter a valid number between 1 and " + NUMBER_RANGE + ".");
        }
    }

    /**
     * The main method to start the Guess the Number game.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(final String[] args)
    {
        SwingUtilities.invokeLater(() -> {
            GuessTheNumberGame game = new GuessTheNumberGame();
            game.setVisible(true);
        });
    }
}
