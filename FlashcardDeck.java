import java.util.ArrayList;
import java.io.*;

public class FlashcardDeck {
    private ArrayList<Flashcard> flashcards;

    public FlashcardDeck() {
        flashcards = new ArrayList<>();
    }

    public void addCard(Flashcard card) {
        flashcards.add(card);
    }

    public ArrayList<Flashcard> getCards() {
        return flashcards;
    }

    public void saveToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Flashcard card : flashcards) {
                writer.write(card.getQuestion() + "," + card.getAnswer());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFromFile(String filename) {
        flashcards.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length == 2) {
                    String question = parts[0].trim();
                    String answer = parts[1].trim();
                    Flashcard card = new Flashcard(question, answer);
                    flashcards.add(card);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
