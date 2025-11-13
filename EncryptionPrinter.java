import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class EncryptionPrinter {
    private static String makeText(Scanner in){
        String text = "";
        boolean exception = true;
        while(exception){
            try {
                String textFile = in.next();
                textFile = textFile.toLowerCase();
                Scanner wordReader = new Scanner(new File(textFile));
                while (wordReader.hasNextLine()) {
                    String data = wordReader.nextLine();
                    text += data;
                }
                wordReader.close();
                exception = false;
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
        return text;
    }

    public static void main(String[] args) {
        CaesarCipher caesar = new CaesarCipher();
        VigenereCipher vigenere = new VigenereCipher();
        System.out.println("Example 1: encrypting 'Why not?' with numericKey 'c'.");
        System.out.println("Expected: yj.pqvb");
        System.out.println("Code output: " + caesar.encrypt("Why not?", "c") + "\n");

        System.out.println("Example 2: encrypting 'Neil Armstrong, Mike Collins, and Buzz Aldrin flew on the Apollo 11 mission.' with key 'm'.");
        System.out.println("Expected: zquxm?yab?.zsjyuwqo.xxuzajmzpnchhmxp?uzrxqe.zbtqm,.xx.yuaau.zi");
        System.out.println("Code output: " + caesar.encrypt("Neil Armstrong, Mike Collins, and Buzz Aldrin flew on the Apollo 11 mission.", "m") + "\n");

        System.out.println("Example 3: decrypting a text with key 'z'.");
        System.out.println("Expected: energyexistsinmanyformsheat,light,chemicalenergy,andelectricalenergy.");
        System.out.println("Code output: " + caesar.decrypt("?i?mbt?sdnondihzitajmhnc?zowgdbcow,c?hd,zg?i?mbtwzi!?g?,omd,zg?i?mbtv", "z") + "\n");

        System.out.println("Example 4: finding the most possible numeric key of the text and decrypting it. ");
        System.out.println("Expected: most possible numeric keys: [f, u, j, k, d], decrypted: alicewasbeginningtogetverytiredofsittingbyhersisteronthebank,andofhavingnothingtodoonceortwiceshehadpeepedintothebookhersisterwasreading,butithadnopicturesorconversationsinit,andwhatistheuseofabook,thoughtalicewithoutpicturesorconversation?soshewasconsideringinherownmindaswellasshecould,forthehotdaymadeherfeelverysleepyandstupid,whetherthepleasureofmakingadaisychainwouldbeworththetroubleofgettingupandpickingthedaisies,whensuddenlyawhiterabbitwithpinkeyesranclosebyher.");
        System.out.println("Code output: " + caesar.mostPossibleNumericKeys("jurlnbj,knprwwrwp!xpn!an.d!r.nmxo,r!!rwpkdqn.,r,!n.xw!qnkjwtgjwmx oqjarwpwx!qrwp!xmxxwlnx.!brln,qnqjmynnynmrw!x!qnkxxtqn.,r,!n.bj ,.njmrwpgk?!r!qjmwxyrl!?.n,x.lxwan.,j!rxw,rwr!gjwmbqj!r,!qn?,nxojkx xtg!qx?pq!jurlnbr!qx?!yrl!?.n,x.lxwan.,j!rxwi,x,qnbj,lxw,rmn.rwprwqn .xbwvrwmj,bnuuj,,qnlx?umgox.!qnqx!mjdvjmnqn.onnuan.d,unnydjwm ,!?yrmgbqn!qn.!qnyunj,?.nxovjtrwpjmjr,dlqjrwbx?umknbx.!q!qn!.x?ku nxopn!!rwp?yjwmyrltrwp!qnmjr,rn,gbqnw,?mmnwudjbqr!n.jkkr!br!qyr wtndn,.jwlux,nkdqn.f"));
        System.out.println("Code output using j as the numeric key: " + caesar.decrypt("jurlnbj,knprwwrwp!xpn!an.d!r.nmxo,r!!rwpkdqn.,r,!n.xw!qnkjwtgjwmx oqjarwpwx!qrwp!xmxxwlnx.!brln,qnqjmynnynmrw!x!qnkxxtqn.,r,!n.bj ,.njmrwpgk?!r!qjmwxyrl!?.n,x.lxwan.,j!rxw,rwr!gjwmbqj!r,!qn?,nxojkx xtg!qx?pq!jurlnbr!qx?!yrl!?.n,x.lxwan.,j!rxwi,x,qnbj,lxw,rmn.rwprwqn .xbwvrwmj,bnuuj,,qnlx?umgox.!qnqx!mjdvjmnqn.onnuan.d,unnydjwm ,!?yrmgbqn!qn.!qnyunj,?.nxovjtrwpjmjr,dlqjrwbx?umknbx.!q!qn!.x?ku nxopn!!rwp?yjwmyrltrwp!qnmjr,rn,gbqnw,?mmnwudjbqr!n.jkkr!br!qyr wtndn,.jwlux,nkdqn.f", "j") + "\n");

        System.out.println("Example 5: encrypting 'A short sample text' with key 'primes!'.");
        System.out.println("Expected: pfp.vhqp?xxihcig");
        System.out.println("Code output: " + vigenere.encrypt("A short sample text", "primes!") + "\n");

        System.out.println("Example 6: decrypting a text with using key 'primes!' again.");
        System.out.println("Expected: moderncryptographyisbasedonmathematicaltheoryandcomputersciencepractice");
        System.out.println("Code output: " + vigenere.decrypt(",blqvbaclxbsyppcpgmg?pfmpsbkpgpqqsrxtixxzc?ecmrva??xcxwpdtqqruca eiox.at", "primes!") + "\n");

        System.out.println("In order to test finding the most possible numeric key of the vigenere cipher in testcase1, 2, and 3.\n");

        System.out.println("Which file would you you like to test?");
        Scanner in = new Scanner(System.in);
        String text = makeText(in);
        while(true){
            System.out.println("\nWhat would you like to use on this file (n: new file, e: encrypt, d: decrypt, or f: find most possible numeric key(s))?");
            String action = in.next();
            action = action.toLowerCase();
            String numericKey = "";
            if(action.equals("e") || action.equals("encrypt") || action.equals("d") || action.equals("decrypt")){
                boolean realNumericKey = false;
                while(!realNumericKey) {
                    System.out.println("\nWhat is the numeric key?");
                    try {
                        numericKey = in.next();
                        vigenere.encrypt("", numericKey);
                        realNumericKey = true;
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
            switch (action) {
                case "new file", "n" -> {
                    System.out.println("\nWhat file would you like to test?");
                    text = makeText(in);
                }
                case "encrypt", "e" -> System.out.println("\n" + vigenere.encrypt(text, numericKey) + "\n");
                case "decrypt", "d" -> System.out.println("\n" + vigenere.decrypt(text, numericKey) + "\n");
                case "find most possible numeric key", "find most possible numeric keys", "f" -> {
                    System.out.println("\n" + vigenere.mostPossibleNumericKey(text, vigenere.mostPossibleLengths(text).get(0)) + "\n");
                }
            }
        }
    }
}
