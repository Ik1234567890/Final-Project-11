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

        // Auto-load flashcards from file when the app starts
        deck.loadFromFile("flashcards.txt");

        JButton addButton = new JButton("Add Flashcard");
        JButton viewButton = new JButton("View Flashcards");
        JButton studyButton = new JButton("Study Flashcards");
        JButton saveButton = new JButton("Save Flashcards");
        JButton loadButton = new JButton("Load Flashcards");

        addButton.addActionListener(e -> showAddDialog());
        viewButton.addActionListener(e -> showFlashcards());
        studyButton.addActionListener(e -> studyFlashcards());
        saveButton.addActionListener(e -> {
            deck.saveToFile("flashcards.txt");
            JOptionPane.showMessageDialog(null, "Flashcards saved successfully.");
        });
        loadButton.addActionListener(e -> {
            deck.loadFromFile("flashcards.txt");
            JOptionPane.showMessageDialog(null, "Flashcards loaded successfully.");
        });

        frame.add(addButton);
        frame.add(viewButton);
        frame.add(studyButton);
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
            String question = questionField.getText().trim();
            String answer = answerField.getText().trim();

            if (question.isEmpty() || answer.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Question and answer cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Flashcard card = new Flashcard(question, answer);
            deck.addCard(card);
            JOptionPane.showMessageDialog(null, "Flashcard added successfully.");
        }
    }

    private void showFlashcards() {
        if (deck.getCards().isEmpty()) {
            JOptionPane.showMessageDialog(null, "No flashcards available to display.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder builder = new StringBuilder();
        for (Flashcard card : deck.getCards()) {
            builder.append(card.toString()).append("\n\n");
        }

        JTextArea textArea = new JTextArea(builder.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(450, 300));

        JOptionPane.showMessageDialog(null, scrollPane, "All Flashcards",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void studyFlashcards() {
        if (deck.getCards().isEmpty()) {
            JOptionPane.showMessageDialog(null, "No flashcards available for study.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Simple study mode: Show each question one by one with option to reveal answer
        for (Flashcard card : deck.getCards()) {
            int result = JOptionPane.showConfirmDialog(null, card.getQuestion() + "\n\nClick 'Yes' to see the answer.",
                    "Study Flashcard", JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(null, "Answer:\n" + card.getAnswer());
            } else {
                // User skipped answer reveal, move to next card
                continue;
            }
        }
    }
}
