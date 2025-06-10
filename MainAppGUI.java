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
        JButton viewButton = new JButton("View Flashcards");
        JButton saveButton = new JButton("Save Flashcards");
        JButton loadButton = new JButton("Load Flashcards");

        addButton.addActionListener(e -> showAddDialog());
        viewButton.addActionListener(e -> showFlashcards());
        saveButton.addActionListener(e -> deck.saveToFile("flashcards.txt"));
        loadButton.addActionListener(e -> {
            deck.loadFromFile("flashcards.txt");
            showFlashcards(); // Show loaded flashcards immediately after loading
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

    private void showFlashcards() {
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
}
