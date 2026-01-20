

דילוג לתוכן
שימוש ב-Gmail עם קוראי מסך
שיחות
0% מתוך 2,048GB בשימוש
תנאים · פרטיות · מדיניות התוכנית
פעילות אחרונה בחשבון: לפני 12 שעות
פרטים
Gemini
חיפוש מידע
‫‫Gemini ב-Workspace עלול לטעות. מידע נוסף
import java.util.HashMap;
import java.util.Random;

public class LanguageModel {

    // The map of this model.
    // Maps windows to lists of charachter data objects.
    HashMap<String, List> CharDataMap; 
    
    // The window length used in this model.
    int windowLength;
    
    // The random number generator used by this model. 
	private Random randomGenerator;

    /** Constructs a language model with the given window length and a given
     *  seed value. Generating texts from this model multiple times with the 
     *  same seed value will produce the same random texts. Good for debugging. */
    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<String, List>();
    }

    /** Constructs a language model with the given window length.
     * Generating texts from this model multiple times will produce
     * different random texts. Good for production. */
    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<String, List>();
    }

    /** Builds a language model from the text in the given file (the corpus). */
	public void train(String fileName) {
        String window = "";
        char c;
        In in = new In(fileName);
        // Reads just enough characters to form the first window
        while (window.length() < windowLength && !in.isEmpty()) {
            c = in.readChar();
            if (c == '\r') {
                continue;
            }
            window += c;
        }
        // Processes the entire text, one character at a time
        while (!in.isEmpty()) {
            // Gets the next character
            c = in.readChar();
            if (c == '\r') {
                continue;
            }
            // Checks if the window is already in the map
            List probs = CharDataMap.get(window);
            // If the window was not found in the map
            // Creates a new empty list, and adds (window,list) to the map
            if (probs == null) {
                probs = new List();
                CharDataMap.put(window, probs);
            }
            // Calculates the counts of the current character.
            probs.update(c);
            // Advances the window: adds c to the window’s end, and deletes the
            // window's first character.
            window = window.substring(1) + c;
        }
        // The entire file has been processed, and all the characters have been counted.
        // Proceeds to compute and set the p and cp fields of all the CharData objects
        // in each linked list in the map.
        for (List probs : CharDataMap.values()) {
            calculateProbabilities(probs);
        }
    }

    // Computes and sets the probabilities (p and cp fields) of all the
	// characters in the given list. */
	public void calculateProbabilities(List probs) {				
		long numOfLetters = 0;
        for (int i = 0; i < probs.getSize(); i++) {
            numOfLetters += probs.get(i).count;
        }
        double cumulativeP = 0.0;
        for (int i = 0; i < probs.getSize(); i++) {
            CharData currentCD = probs.get(i); 
            currentCD.p = (double) currentCD.count / numOfLetters;
            cumulativeP += currentCD.p;
            currentCD.cp = cumulativeP;
        }
	}

    // Returns a random character from the given probabilities list.
	char getRandomChar(List probs) {
		double r = randomGenerator.nextDouble();
        for (int i = 0; i < probs.getSize(); i++) {
            CharData currentCD = probs.get(i);
            if (r < currentCD.cp) return currentCD.chr;
        }
		return probs.get(probs.getSize() -1).chr;
	}
    
    /**
	 * Generates a random text, based on the probabilities that were learned during training. 
	 * @param initialText - text to start with. If initialText's last substring of size numberOfLetters
	 * doesn't appear as a key in Map, we generate no text and return only the initial text. 
	 * @param numberOfLetters - the size of text to generate
	 * @return the generated text
	 */
	public String generate(String initialText, int textLength) {
		if (initialText.length() < windowLength) {
            return initialText;
        }  
        StringBuilder generatedText = new StringBuilder(initialText);
        String window = initialText.substring(initialText.length() - windowLength); 
        while (generatedText.length() < initialText.length() + textLength) {
            List probs = CharDataMap.get(window);
            if (probs == null) {
                break;
            } 
            char nextCh = getRandomChar(probs);
            generatedText.append(nextCh);            
            window = window.substring(1) + nextCh;
        }
        return generatedText.toString();
    }

    /** Returns a string representing the map of this language model. */
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (String key : CharDataMap.keySet()) {
			List keyProbs = CharDataMap.get(key);
			str.append(key + " : " + keyProbs + "\n");
		}
		return str.toString();
	}

    public static void main(String[] args) {
        int windowLength = Integer.parseInt(args[0]);
        String initialText = args[1];
        int generatedTextLength = Integer.parseInt(args[2]);
        Boolean randomGeneration = args[3].equals("random");
        String fileName = args[4];
        // Create the LanguageModel object
        LanguageModel lm;
        if (randomGeneration)
        lm = new LanguageModel(windowLength);
        else
        lm = new LanguageModel(windowLength, 20);
        // Trains the model, creating the map.
        lm.train(fileName);
        // Generates text, and prints it.
        System.out.println(lm.generate(initialText, generatedTextLength));
    }
}
        