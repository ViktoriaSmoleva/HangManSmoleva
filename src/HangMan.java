import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class HangMan {

    private static int ATTEMPTS_COUNT = 6;
    private static File file = new File("Dictionary.txt");
    private static Scanner scanner = new Scanner(System.in);
    private static Random random = new Random();
    private static String hiddenWord;

    public static void main(String[] args) throws FileNotFoundException {
        while (true) {
            startGame();
        }
    }

    public static String chooseWord() {
        String maskWord = "";
        int n = 0;
        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        while (sc.hasNext()) {
            ++n;
            String line = sc.nextLine();
            if (random.nextInt(n) == 0)
                maskWord = line;
        }
        return maskWord;
    }

    public static String maskWord(String hiddenWord, String guessedLetters) {
        StringBuilder maskedWord = new StringBuilder();

        for (int i = 0; i < hiddenWord.length(); i++) {
            char currentChar = hiddenWord.charAt(i);
            if (guessedLetters.indexOf(currentChar) >= 0) {
                maskedWord.append(currentChar);
            } else {
                maskedWord.append('*');
            }
        }

        return maskedWord.toString();
    }

    public static void playGame(String word) {
        StringBuilder guessedLetters = new StringBuilder();
        StringBuilder enteredLetters = new StringBuilder();
        hiddenWord = word;

        do {
            System.out.println(maskWord(hiddenWord, guessedLetters.toString()));
            System.out.println("Введите букву: ");
            char c = scanner.next().toLowerCase().charAt(0);

            if (hiddenWord.toLowerCase().indexOf(c) >= 0) {
                if (guessedLetters.indexOf(String.valueOf(c)) < 0) {
                    System.out.println("Есть такая буква в слове!");
                    guessedLetters.append(c);
                    enteredLetters.append(c);
                    System.out.println("Список введенных букв: " + enteredLetters);

                } else {
                    System.out.println("Эту букву вы уже угадывали!");

                }
            } else {
                System.out.println("Не правильно, попробуем еще раз");
                ATTEMPTS_COUNT--;
                gallows();
                System.out.println("Осталось попыток: " + ATTEMPTS_COUNT);
                enteredLetters.append(c);
                System.out.println("Список введенных букв: " + enteredLetters);
            }

            if (maskWord(hiddenWord, guessedLetters.toString()).indexOf('*') < 0) {
                isWin();
                return;
            }

        } while (ATTEMPTS_COUNT > 0);

        isLose();
    }

    public static void startGame(){
        System.out.println("Если хотите начать новую игру введите 'да', если хотите выйти введите 'нет':");
        String input = scanner.nextLine();

        if (input.equalsIgnoreCase("да")) {
            System.out.println("Игра начинается...");
            String chooseWord = chooseWord();
            ATTEMPTS_COUNT = 6;
            playGame(chooseWord);
        } else if (input.equalsIgnoreCase("нет")) {
            System.out.println("Выход из игры...");
            System.exit(0);
        } else {
            System.out.println("Неправильный ввод, попробуйте снова.");
        }
    }

    private static boolean isWin() {
        System.out.println("Вы выиграли!!!!! Загаданное слово " + hiddenWord);
        System.out.println();
        return true;
    }

    private static boolean isLose() {
        System.out.println("Вы проиграли. Загаданное слово " + hiddenWord);
        System.out.println();
        return false;
    }

    public static String[]gallows() {
        String[] gallows = new String[]{
                """
        +------+
        |
        |
        |
        |
      =====
""",
                """ 
        +------+
        |      O
        |
        |
        |
      =====
""", """ 
        +------+
        |      O
        |      |
        |
        |
      =====
""", """ 
        +------+
        |      O
        |     /|
        |
        |
      =====
""", """ 
         +------+
         |      O
         |     /|\\
         |
         |
       =====
""", """ 
         +------+
         |      O
         |     /|\\
         |     /
         |
       =====
""", """ 
         +------+
         |      O
         |     /|\\
         |     / \\
         |
       =====
"""};
        System.out.println(gallows[6 - ATTEMPTS_COUNT]);
        return gallows;
    }

}

