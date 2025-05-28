import java.util.ArrayList;

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
}
