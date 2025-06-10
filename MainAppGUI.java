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
        JButton studyButton = new JButton("Study Flashcards");

        addButton.addActionListener(e -> showAddDialog());

        viewButton.addActionListener(e -> showFlashcards());

        saveButton.addActionListener(e -> {
            deck.saveToFile("flashcards.txt");
            JOptionPane.showMessageDialog(null, "Flashcards saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        loadButton.addActionListener(e -> {
            deck.loadFromFile("flashcards.txt");
            if (deck.getCards().isEmpty()) {
                JOptionPane.showMessageDialog(null, "No flashcards available after loading.", "Info", JOptionPane.INFORMATION_MESSAGE);
            } else {
                StringBuilder builder = new StringBuilder();
                for (Flashcard card : deck.getCards()) {
                    builder.append(card.toString()).append("\n\n");
                }
                JTextArea textArea = new JTextArea(builder.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(450, 300));

                JOptionPane.showMessageDialog(null, scrollPane, "Loaded Flashcards",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        studyButton.addActionListener(e -> {
            if (deck.getCards().isEmpty()) {
                JOptionPane.showMessageDialog(null, "No flashcards to study. Please load or add some first.", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                studyFlashcards();
            }
        });

        frame.add(addButton);
        frame.add(viewButton);
        frame.add(saveButton);
        frame.add(loadButton);
        frame.add(studyButton);

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
        if (deck.getCards().isEmpty()) {
            JOptionPane.showMessageDialog(null, "No flashcards to display.", "Info", JOptionPane.INFORMATION_MESSAGE);
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
        JFrame studyFrame = new JFrame("Study Flashcards");
        studyFrame.setSize(400, 200);
        studyFrame.setLayout(new BorderLayout());

        JLabel cardLabel = new JLabel("", SwingConstants.CENTER);
        cardLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        studyFrame.add(cardLabel, BorderLayout.CENTER);

        JButton nextButton = new JButton("Show Answer");
        studyFrame.add(nextButton, BorderLayout.SOUTH);

        final int[] index = {0};
        final boolean[] showingQuestion = {true};

        cardLabel.setText("<html><b>Q:</b> " + deck.getCards().get(index[0]).getQuestion() + "</html>");

        nextButton.addActionListener(e -> {
            if (showingQuestion[0]) {
                cardLabel.setText("<html><b>A:</b> " + deck.getCards().get(index[0]).getAnswer() + "</html>");
                nextButton.setText("Next Card");
                showingQuestion[0] = false;
            } else {
                index[0]++;
                if (index[0] >= deck.getCards().size()) {
                    JOptionPane.showMessageDialog(studyFrame, "You have reached the end of the flashcards.", "End", JOptionPane.INFORMATION_MESSAGE);
                    index[0] = 0;
                }
                cardLabel.setText("<html><b>Q:</b> " + deck.getCards().get(index[0]).getQuestion() + "</html>");
                nextButton.setText("Show Answer");
                showingQuestion[0] = true;
            }
        });

        studyFrame.setLocationRelativeTo(null);
        studyFrame.setVisible(true);
    }
}
