import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.IntStream;

/**
 * Creates a VigenereCipher class
 */
public class VigenereCipher {
    private final HashMap<Integer, String> letterCorrespondence = new HashMap<Integer, String>();
    private final HashMap<String, Integer> reverseLetterCorrespondence = new HashMap<String, Integer>();

    /**
     * Creates a constructor VigenereCipher, creates important hashmaps
     */
    public VigenereCipher(){
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
     * Encrypts a text using a word numericKey
     * @param text the text to encrypt
     * @param numericKey the word numericKey
     * @return the encrypted text
     */
    public String encrypt(String text, String numericKey){
        numericKey = numericKey.toLowerCase();
        if(numericKey.matches("[^a-z,.!?]+$")){
            throw new RuntimeException("Not a real numericKey");
        }
        char[] strEncryptKey = numericKey.toCharArray();
        ArrayList<Integer> numEncryptKey = new ArrayList<Integer>();
        for(char letter : strEncryptKey){
            numEncryptKey.add(reverseLetterCorrespondence.get(String.valueOf(letter)));
        }

        String filteredText = filterText(text);
        char[] filteredTextList = filteredText.toCharArray();
        String result = "";
        for(int i = 0; i < filteredTextList.length; i++){
            result += letterCorrespondence.get((reverseLetterCorrespondence.get(String.valueOf(filteredTextList[i])) + numEncryptKey.get(i%numEncryptKey.size()))%30);
        }
        return result;
    }

    /**
     * Decrypts a text using a word numericKey
     * @param encryptedText the encrypted text
     * @param numericKey the word numericKey
     * @return the decrypted text
     */
    public String decrypt(String encryptedText, String numericKey){
        numericKey = numericKey.toLowerCase();
        if(encryptedText.matches("[^a-z,.!?]+$")){
            throw new RuntimeException("Not a real numericKey");
        }
        char[] strEncryptKey = numericKey.toCharArray();
        ArrayList<Integer> numEncryptKey = new ArrayList<Integer>();
        for(char letter : strEncryptKey){
            numEncryptKey.add(reverseLetterCorrespondence.get(String.valueOf(letter)));
        }

        String filteredText = filterText(encryptedText);
        char[] filteredTextList = filteredText.toCharArray();
        String result = "";
        for(int i = 0; i < filteredTextList.length; i++){
            result += letterCorrespondence.get(((reverseLetterCorrespondence.get(String.valueOf(filteredTextList[i])) - numEncryptKey.get(i%numEncryptKey.size()))%30+30)%30);
        }
        return result;
    }

    /**
     * Returns the most possible word numericKey given a length
     * @param encryptedText the encryptedText
     * @param length the given length
     * @return the most possible word numericKey
     */
    public String mostPossibleNumericKey(String encryptedText, int length){
        encryptedText = filterText(encryptedText);
        String result = "";
        for(int i = 0; i < length; i++){
            String partition = "";
            int j = i;
            while(j < encryptedText.length()){
                partition += encryptedText.charAt(j);
                j += length;
            }
            HashMap<Integer, Double> resemblance = new HashMap<Integer, Double>();
            for(int h = 0; h < 30; h++){
                Integer[] letterOccurrences = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                letterOccurrences[0] += letterOccurrence("s", partition, h);
                letterOccurrences[1] += letterOccurrence("t", partition, h);
                letterOccurrences[2] += letterOccurrence("i", partition, h);
                letterOccurrences[3] += letterOccurrence("a", partition, h);
                letterOccurrences[4] += letterOccurrence("e", partition, h);
                letterOccurrences[5] += letterOccurrence("g", partition, h);
                letterOccurrences[6] += letterOccurrence("h", partition, h);
                letterOccurrences[7] += letterOccurrence("n", partition, h);
                letterOccurrences[8] += letterOccurrence("r", partition, h);
                letterOccurrences[9] += letterOccurrence(".", partition, h);
                letterOccurrences[10] = partition.length();
                resemblance.put(h, 0.25 * Math.abs(letterOccurrences[0] - 0.2222 * letterOccurrences[10]) + 0.25 * Math.abs(letterOccurrences[1] - 0.2222 * letterOccurrences[10]) + 0.3333 * Math.abs(letterOccurrences[2] - 0.1667 * letterOccurrences[10]) + Math.abs(letterOccurrences[4] - 0.0556 * letterOccurrences[10]) + Math.abs(letterOccurrences[5] - 0.0556 * letterOccurrences[10]) + Math.abs(letterOccurrences[6] - 0.0556 * letterOccurrences[10]) + Math.abs(letterOccurrences[7] - 0.0556 * letterOccurrences[10]) + Math.abs(letterOccurrences[8] - 0.0556 * letterOccurrences[10]) + Math.abs(letterOccurrences[9] - 0.0556 * letterOccurrences[10]));
            }
            Integer resembleKey = 0;
            Double resembleValue = resemblance.get(resemblance.keySet().stream().iterator().next());
            for(Map.Entry<Integer, Double> entry : resemblance.entrySet()){
                if(entry.getValue() < resembleValue){
                    resembleKey = entry.getKey();
                    resembleValue = entry.getValue();
                }
            }
            result += letterCorrespondence.get(resembleKey-1);
        }
        return result;
    }

    /**
     * Returns the mostPossibleLengths of the word numericKey of a given encrypted text
     * @param encryptedText the encrypted text
     * @return the most possible lengths
     */
    public ArrayList<Integer> mostPossibleLengths(String encryptedText){
        encryptedText = filterText(encryptedText);
        HashMap<Integer, Double> resemblance = new HashMap<Integer, Double>();
        for(int i = 3; i <= 15; i++){
            int j = 0;
            String partition = "";
            while(j < encryptedText.length()){
                partition += encryptedText.charAt(j);
                j += i;
            }
            Integer[] letterOccurrences = letterOccurrences(partition);
            Arrays.sort(letterOccurrences, Collections.reverseOrder());
            letterOccurrences[10] = partition.length();
            resemblance.put(i, (i/15.0)*(0.25 * Math.abs(letterOccurrences[0] - 0.2222 * letterOccurrences[10]) + 0.25 * Math.abs(letterOccurrences[1] - 0.2222 * letterOccurrences[10]) + 0.3333 * Math.abs(letterOccurrences[2] - 0.1667 * letterOccurrences[10]) + Math.abs(letterOccurrences[4] - 0.0556 * letterOccurrences[10]) + Math.abs(letterOccurrences[5] - 0.0556 * letterOccurrences[10]) + Math.abs(letterOccurrences[6] - 0.0556 * letterOccurrences[10]) + Math.abs(letterOccurrences[7] - 0.0556 * letterOccurrences[10]) + Math.abs(letterOccurrences[8] - 0.0556 * letterOccurrences[10]) + Math.abs(letterOccurrences[9] - 0.0556 * letterOccurrences[10])));
        }
        ArrayList<Integer> result = new ArrayList<Integer>();
        for(int i = 0; i < 3; i++){
            Integer resembleKey = 0;
            Double resembleValue = resemblance.get(resemblance.keySet().stream().iterator().next());
            for(Map.Entry<Integer, Double> entry : resemblance.entrySet()){
                if(entry.getValue() < resembleValue){
                    resembleKey = entry.getKey();
                    resembleValue = entry.getValue();
                }
            }
            result.add(resembleKey);
            resemblance.remove(resembleKey);
        }
        return result;
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
     * Returns an array of Integers with the number of occurrences of each letter (a in 0, b in 1, ..., ? in 29)
     * @param text the text
     * @return the array of occurrences of each character
     */
    private Integer[] letterOccurrences(String text){
        text = filterText(text);
        char[] textList = text.toCharArray();
        Integer[] letterOccurrences = new Integer[30];
        for(int i = 0; i < 30; i++){
            letterOccurrences[i] = 0;
        }
        for(char letter : textList){
            letterOccurrences[reverseLetterCorrespondence.get(String.valueOf(letter))] += 1;
        }
        return letterOccurrences;
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
