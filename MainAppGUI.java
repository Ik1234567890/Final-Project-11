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

        // Action listeners
        addButton.addActionListener(e -> showAddDialog());
        viewButton.addActionListener(e -> showFlashcards());
        saveButton.addActionListener(e -> {
            deck.saveToFile("flashcards.txt");
            JOptionPane.showMessageDialog(null, "Flashcards saved successfully!");
        });
        loadButton.addActionListener(e -> {
            deck.loadFromFile("flashcards.txt");
            JOptionPane.showMessageDialog(null, "Flashcards loaded successfully!");

            // Display loaded flashcards in a scrollable window
            StringBuilder builder = new StringBuilder();
            for (Flashcard card : deck.getCards()) {
                builder.append("Q: ").append(card.getQuestion()).append("\n");
                builder.append("A: ").append(card.getAnswer()).append("\n\n");
            }

            JTextArea textArea = new JTextArea(builder.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(450, 300));

            JOptionPane.showMessageDialog(null, scrollPane, "Loaded Flashcards", JOptionPane.INFORMATION_MESSAGE);
        });

        // Add buttons to frame
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

    private void showFlashcards() {
        StringBuilder builder = new StringBuilder();
        for (Flashcard card : deck.getCards()) {
            builder.append("Q: ").append(card.getQuestion()).append("\n");
            builder.append("A: ").append(card.getAnswer()).append("\n\n");
        }

        JTextArea textArea = new JTextArea(builder.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(450, 300));

        JOptionPane.showMessageDialog(null, scrollPane, "All Flashcards",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
