
import java.util.*;

public class Main {
    final static int ADDITIONAL_POINT = 250;
    final static int REPETITIONS = 5;
    final static int BOUND = 2;
    final static int MENU_ONE = 1;
    final static int MENU_TWO = 2;
    final static int MENU_TREE = 3;


    public static void main(String[] args){

        Scanner intScanner = new Scanner(System.in);
        Scanner lnScanner = new Scanner(System.in);

        boolean running = true;

        System.out.println(
                "********guess the most frequent words on the site********"
                +"\n"+ "choose 1 for loading: www.ynet.co.il "
                +"\n"+ "choose 2 for loading: www.mako.co.il "
                +"\n"+ "choose 3 for loading: www.walla.co.il"
                );

        while (running) {
            try {

                System.out.print("\nyour choice: ");
                int userChoice = intScanner.nextInt();
                int[] wordsScore;
                int sequenceScore;
                String userSequence;


                switch (userChoice) {
                    case MENU_ONE:

                        YNetBot yNetBot = new YNetBot("https://www.ynet.co.il/home/0,7340,L-8,00.html");
                        System.out.println("Longest title: " + yNetBot.getLongestArticleTitle());
                        wordsScore = runGuessing(yNetBot.getMap());
                        System.out.println("Enter the most frequent sequence from titles and subtitles: ");
                        userSequence = lnScanner.nextLine();
                        sequenceScore = runGuessingFrequentSequence(yNetBot.countInArticlesTitles(userSequence));
                        System.out.println("total points scored: " + (wordsScore[0]+sequenceScore) + " from " + ((wordsScore[1]*REPETITIONS)+ADDITIONAL_POINT));
                        break;

                    case MENU_TWO:

                        MakoBot makoBot = new MakoBot("https://www.mako.co.il/");
                        System.out.println("Longest title: " + makoBot.getLongestArticleTitle());
                        wordsScore = runGuessing(makoBot.getMap());
                        System.out.println("Enter the most frequent sequence from titles and subtitles: ");
                        userSequence = lnScanner.nextLine();
                        sequenceScore = runGuessingFrequentSequence(makoBot.countInArticlesTitles(userSequence));
                        System.out.println("total points scored: " + (wordsScore[0]+sequenceScore) + " from " + ((wordsScore[1]*REPETITIONS)+ADDITIONAL_POINT));
                        break;

                    case MENU_TREE:

                        WallaBot wallaBot = new WallaBot("https://www.walla.co.il/");
                        System.out.println("Longest title: " + wallaBot.getLongestArticleTitle());
                        wordsScore = runGuessing(wallaBot.getMap());
                        System.out.println("Enter the most frequent sequence from titles and subtitles: ");
                        userSequence = lnScanner.nextLine();
                        sequenceScore = runGuessingFrequentSequence(wallaBot.countInArticlesTitles(userSequence));
                        System.out.println("total points scored: " + (wordsScore[0]+sequenceScore) + " from " + ((wordsScore[1]*REPETITIONS)+ADDITIONAL_POINT));
                        break;

                    default:
                        System.out.print("unknown option");
                }
                running=false;


            } catch (InputMismatchException e) {
                System.out.println("Enter only numbers 1, 2, 3");
                intScanner.nextLine();

            }
        }
    }

    public static int[] runGuessing(Map<String, Integer> map){
        int[] value = {0, Collections.max(map.values())};
        Scanner lnScanner = new Scanner(System.in);

        for (int i = 0; i < REPETITIONS; i++) {
            System.out.print("Enter the word: ");
            String word = lnScanner.nextLine();
            if (map.get(word) != null) {
                value[0] += map.get(word);
            }
        }

        return value;
    }

    public static int runGuessingFrequentSequence(int counter) {
        int additionalPoints = 0;
        int usrInputValue;
        Scanner intScanner = new Scanner(System.in);

        try {
            System.out.println("enter the number of repetitions of the sequence: ");
            usrInputValue = intScanner.nextInt();
            if (usrInputValue <= counter+BOUND && usrInputValue >= counter-BOUND) {
                System.out.println("you got : 250 points.");
                additionalPoints = ADDITIONAL_POINT;
            }
            else {
                System.out.println("you got zero points");
            }

        } catch (InputMismatchException e) {
            System.out.println("you got zero points");
            intScanner.nextLine();
        }

        return additionalPoints;
    }

}

