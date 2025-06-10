import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainAppGUI {
    private FlashcardDeck deck = new FlashcardDeck();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainAppGUI().createAndShowGUI());
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Flashcardio - Flashcard App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new FlowLayout());

        JButton addButton = new JButton("Add Flashcard");
        JButton viewButton = new JButton("Study Flashcards");
        JButton saveButton = new JButton("Save Flashcards");
        JButton loadButton = new JButton("Load Flashcards");

        addButton.addActionListener(e -> showAddDialog());
        viewButton.addActionListener(e -> startStudyMode());
        saveButton.addActionListener(e -> {
            deck.saveToFile("flashcards.txt");
            JOptionPane.showMessageDialog(null, "Flashcards saved successfully!");
        });
        loadButton.addActionListener(e -> {
            deck.loadFromFile("flashcards.txt");
            JOptionPane.showMessageDialog(null, "Flashcards loaded successfully!");
        });

        frame.add(addButton);
        frame.add(viewButton);
        frame.add(saveButton);
        frame.add(loadButton);

        frame.setVisible(true);
    }

    private void showAddDialog() {
        JTextField questionField = new JTextField(20);
        JTextField answerField = new JTextField(20);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Question:"));
        panel.add(questionField);
        panel.add(new JLabel("Answer:"));
        panel.add(answerField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add Flashcard",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String question = questionField.getText();
            String answer = answerField.getText();
            Flashcard card = new Flashcard(question, answer);
            deck.addCard(card);
        }
    }

    private void startStudyMode() {
        java.util.List<Flashcard> cards = deck.getCards();
        if (cards.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No flashcards to study.");
            return;
        }

        final int[] index = {0};

        JFrame studyFrame = new JFrame("Study Mode");
        studyFrame.setSize(400, 250);
        studyFrame.setLayout(new BorderLayout());

        JLabel questionLabel = new JLabel("<html><b>Q: </b>" + cards.get(index[0]).getQuestion() + "</html>", SwingConstants.CENTER);
        JLabel answerLabel = new JLabel("", SwingConstants.CENTER);

        JButton showAnswer = new JButton("Show Answer");
        JButton nextButton = new JButton("Next");

        showAnswer.addActionListener(e -> {
            answerLabel.setText("<html><b>A: </b>" + cards.get(index[0]).getAnswer() + "</html>");
        });

        nextButton.addActionListener(e -> {
            index[0]++;
            if (index[0] >= cards.size()) {
                JOptionPane.showMessageDialog(null, "You’ve reached the end of the flashcards.");
                studyFrame.dispose();
            } else {
                questionLabel.setText("<html><b>Q: </b>" + cards.get(index[0]).getQuestion() + "</html>");
                answerLabel.setText("");
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(showAnswer);
        buttonPanel.add(nextButton);

        studyFrame.add(questionLabel, BorderLayout.NORTH);
        studyFrame.add(answerLabel, BorderLayout.CENTER);
        studyFrame.add(buttonPanel, BorderLayout.SOUTH);

        studyFrame.setVisible(true);
    }
}
