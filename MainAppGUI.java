import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainAppGUI {
    private FlashcardDeck deck = new FlashcardDeck();
    private int currentCardIndex = 0;
    private boolean showingAnswer = false;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainAppGUI().createAndShowGUI());
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Flashcardio - Flashcard App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new FlowLayout());

        JButton addButton = new JButton("Add Flashcard");
        JButton studyButton = new JButton("Study Flashcards");
        JButton saveButton = new JButton("Save Flashcards");
        JButton loadButton = new JButton("Load Flashcards");

        addButton.addActionListener(e -> showAddDialog());
        studyButton.addActionListener(e -> startStudyMode());
        saveButton.addActionListener(e -> {
            deck.saveToFile("flashcards.txt");
            JOptionPane.showMessageDialog(null, "Flashcards saved successfully!");
        });
        loadButton.addActionListener(e -> {
            deck.loadFromFile("flashcards.txt");
            JOptionPane.showMessageDialog(null, "Flashcards loaded successfully!");

            // Optional: preview flashcards after loading
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

        frame.add(addButton);
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
            String question = questionField.getText();
            String answer = answerField.getText();
            Flashcard card = new Flashcard(question, answer);
            deck.addCard(card);
        }
    }

    private void startStudyMode() {
        if (deck.getCards().isEmpty()) {
            JOptionPane.showMessageDialog(null, "No flashcards to study. Please load or add some first.");
            return;
        }

        currentCardIndex = 0;
        showingAnswer = false;

        JFrame studyFrame = new JFrame("Study Flashcards");
        studyFrame.setSize(400, 200);
        studyFrame.setLayout(new BorderLayout());

        JLabel cardLabel = new JLabel(deck.getCards().get(currentCardIndex).getQuestion(), SwingConstants.CENTER);
        JButton showAnswerButton = new JButton("Show Answer");
        JButton nextButton = new JButton("Next");

        showAnswerButton.addActionListener(e -> {
            if (!showingAnswer) {
                cardLabel.setText(deck.getCards().get(currentCardIndex).getAnswer());
                showingAnswer = true;
            }
        });

        nextButton.addActionListener(e -> {
            currentCardIndex++;
            if (currentCardIndex >= deck.getCards().size()) {
                JOptionPane.showMessageDialog(studyFrame, "You’ve finished all flashcards!");
                studyFrame.dispose();
            } else {
                cardLabel.setText(deck.getCards().get(currentCardIndex).getQuestion());
                showingAnswer = false;
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(showAnswerButton);
        buttonPanel.add(nextButton);

        studyFrame.add(cardLabel, BorderLayout.CENTER);
        studyFrame.add(buttonPanel, BorderLayout.SOUTH);

        studyFrame.setLocationRelativeTo(null);
        studyFrame.setVisible(true);
    }
}
