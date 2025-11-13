import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Creates class CaesarCipher
 */
public class CaesarCipher {
    private final HashMap<Integer, String> letterCorrespondence = new HashMap<Integer, String>();
    private final HashMap<String, Integer> reverseLetterCorrespondence = new HashMap<String, Integer>();
    private final HashMap<String, String> encryptKey = new HashMap<String, String>();
    private final HashMap<String, String> reverseEncryptKey = new HashMap<String, String>();

    /**
     * Constructor for CaesarCipher, creates important hashmaps
     */
    public CaesarCipher(){
        ArrayList<String> alphabet = new ArrayList<String>();
        try {
            File allWords = new File("alphabet");
            Scanner wordReader = new Scanner(allWords);
            while (wordReader.hasNextLine()) {
                String data = wordReader.nextLine();
                alphabet.add(data);
            }
            wordReader.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Fail to load alphabet file.");
        }

        for(int i = 0; i < alphabet.size(); i++){
            letterCorrespondence.put(i, alphabet.get(i));
        }
        for(int i = 0; i < alphabet.size(); i++){
            reverseLetterCorrespondence.put(alphabet.get(i), i);
        }
    }

    /**
     * Encrypts a text using a letter numericKey
     * @param text the text to encrypt
     * @param numericKey the numericKey
     * @return the encrypted text
     */
    public String encrypt(String text, String numericKey){
        boolean realNumericKey = false;
        for(String letter : letterCorrespondence.values()){
            if(numericKey.equals(letter)) {
                realNumericKey = true;
                break;
            }
        }
        if(!realNumericKey){
            throw new RuntimeException("Not a real numericKey");
        }
        updateEncryptKey(numericKey);
        String filteredText = filterText(text);
        char[] filteredTextList = filteredText.toCharArray();
        String result = "";
        for(char letter : filteredTextList){
            result += encryptKey.get(String.valueOf(letter));
        }
        return result;
    }

    /**
     * Decrypts an encrypted text using a letter numericKey
     * @param encryptedText the encrypted text
     * @param numericKey the numericKey
     * @return the decrypted text
     */
    public String decrypt(String encryptedText, String numericKey){
        boolean realNumericKey = false;
        for(String letter : letterCorrespondence.values()){
            if(numericKey.equals(letter)) {
                realNumericKey = true;
                break;
            }
        }
        if(!realNumericKey){
            throw new RuntimeException("Not a real numericKey");
        }
        updateReverseCryptKey(numericKey);
        String filteredText = filterText(encryptedText);
        char[] textList = filteredText.toCharArray();
        String result = "";
        for(char letter : textList){
            result += reverseEncryptKey.get(String.valueOf(letter));
        }
        return result;
    }

    /**
     * Returns the 5 most possible numericKeys of an encrypted text
     * @param encryptedText the encrypted text
     * @return the decrypted text
     */
    public ArrayList<String> mostPossibleNumericKeys(String encryptedText){
        encryptedText = filterText(encryptedText);
        HashMap<Integer, Double> resemblance = new HashMap<Integer, Double>();
        ArrayList<String> result = new ArrayList<String>();
        for(int i = 0; i < 30; i++){
            Integer[] letterOccurrences = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            letterOccurrences[0] += letterOccurrence("s", encryptedText, i);
            letterOccurrences[1] += letterOccurrence("t", encryptedText, i);
            letterOccurrences[2] += letterOccurrence("i", encryptedText, i);
            letterOccurrences[3] += letterOccurrence("a", encryptedText, i);
            letterOccurrences[4] += letterOccurrence("e", encryptedText, i);
            letterOccurrences[5] += letterOccurrence("g", encryptedText, i);
            letterOccurrences[6] += letterOccurrence("h", encryptedText, i);
            letterOccurrences[7] += letterOccurrence("n", encryptedText, i);
            letterOccurrences[8] += letterOccurrence("r", encryptedText, i);
            letterOccurrences[9] += letterOccurrence(".", encryptedText, i);
            letterOccurrences[10] = encryptedText.length();
            resemblance.put(i, 0.25 * Math.abs(letterOccurrences[0] - 0.2222 * letterOccurrences[10]) + 0.25 * Math.abs(letterOccurrences[1] - 0.2222 * letterOccurrences[10]) + 0.3333 * Math.abs(letterOccurrences[2] - 0.1667 * letterOccurrences[10]) + Math.abs(letterOccurrences[4] - 0.0556 * letterOccurrences[10]) + Math.abs(letterOccurrences[5] - 0.0556 * letterOccurrences[10]) + Math.abs(letterOccurrences[6] - 0.0556 * letterOccurrences[10]) + Math.abs(letterOccurrences[7] - 0.0556 * letterOccurrences[10]) + Math.abs(letterOccurrences[8] - 0.0556 * letterOccurrences[10]) + Math.abs(letterOccurrences[9] - 0.0556 * letterOccurrences[10]));
        }
        for(int i = 0; i < 5; i++) {
            Integer resembleKey = 0;
            Double resembleValue = resemblance.get(resemblance.keySet().stream().iterator().next());
            for(Map.Entry<Integer, Double> entry : resemblance.entrySet()){
                if(entry.getValue() < resembleValue){
                    resembleKey = entry.getKey();
                    resembleValue = entry.getValue();
                }
            }
            result.add(letterCorrespondence.get(resembleKey));
            resemblance.remove(resembleKey);
        }
        return result;
    }

    /**
     * Updates the encryptKey hashmap given a specific numericKey
     * @param numericKey the numericKey
     */
    private void updateEncryptKey(String numericKey){
        encryptKey.clear();
        for(int i = 0; i < letterCorrespondence.size(); i++){
            encryptKey.put(letterCorrespondence.get(i), letterCorrespondence.get((i + reverseLetterCorrespondence.get(numericKey)) % 30));
        }
    }

    /**
     * Updates the reverseEncryptKey given a specific numericKey
     * @param numericKey the numericKey
     */
    private void updateReverseCryptKey(String numericKey){
        reverseEncryptKey.clear();
        for(int i = 0; i < letterCorrespondence.size(); i++){
            reverseEncryptKey.put(letterCorrespondence.get(i), letterCorrespondence.get(((i - reverseLetterCorrespondence.get(numericKey)) % 30 + 30) % 30));
        }
    }

    /**
     * Filters the text (makes everything lowercase, and removes everything that isn't alphabetical, numerical, or specific punctuation)
     * @param text the text to filter
     * @return the filtered text
     */
    private String filterText(String text){
        String filteredText = text.toLowerCase();
        filteredText = filteredText.replaceAll("[^a-z,.!?]", "");
        return filteredText;
    }

    /**
     * Finds the number of occurrences of a specific letter within a string
     * @param letter the letter
     * @param text the text
     * @param numericKey the numericKey in int form
     * @return the number of occurrences of that letter
     */
    private int letterOccurrence(String letter, String text, int numericKey){
        int num = reverseLetterCorrespondence.get(letter);
        String realLetter = letterCorrespondence.get((num + numericKey)%30);
        text = filterText(text);
        int count = 0;
        for(int i = 0; i < text.length(); i++){
            if(String.valueOf(text.charAt(i)).equals(realLetter)){
                count += 1;
            }
        }
        return count;
    }
}
