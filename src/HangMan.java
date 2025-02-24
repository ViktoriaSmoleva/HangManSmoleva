import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;
import java.lang.System;

public class HangMan {

    private static int attemptsCount = 6;
    private static File file = new File("Dictionary.txt");
    private static Scanner scanner = new Scanner(System.in);
    private static Random random = new Random();
    private static String hiddenWord;
    private static StringBuilder guessedLetters = new StringBuilder();
    private static StringBuilder enteredLetters = new StringBuilder();
    private final static char MASK_SIMBOL = '*';
    private final static String START = "да";
    private final static String QUIT = "нет";
    private static final String RUSSIAN_ALPHABET = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";

    class ReedFile {
        public static String chooseWord() {
            String maskWord = "";
            int n = 0;
            Scanner scanner1 = null;
            try {
                scanner1 = new Scanner(file);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            while (scanner1.hasNext()) {
                n++;
                String line = scanner1.nextLine();
                if (random.nextInt(n) == 0) {
                    maskWord = line;
                }
            }
            return maskWord;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        while (true) {
            startGame();
        }
    }

    public static String maskWord(String hiddenWord, String guessedLetters) {
        StringBuilder maskedWord = new StringBuilder();

        for (int i = 0; i < hiddenWord.length(); i++) {
            char currentChar = hiddenWord.charAt(i);
            if (contains(guessedLetters, currentChar)) {
                maskedWord.append(currentChar);
            } else {
                maskedWord.append(MASK_SIMBOL);
            }
        }

        return maskedWord.toString();
    }

    public static void playGame(String word) {

        hiddenWord = word;

        while (attemptsCount > 0) {
            String mask = maskWord(hiddenWord, guessedLetters.toString());
            System.out.println(mask);
            System.out.println("Введите букву: ");
            String input = scanner.next();
            scanner.nextLine();
            if (input.length() != 1) {
                System.out.println("Вы ввели несколько символов! Пожалуйста, введите одну букву.");
                continue;
            }
            char c = input.toLowerCase().charAt(0);


            if (!RUSSIAN_ALPHABET.contains(String.valueOf(c))) {
                System.out.println("Пожалуйста, вводите только одиночные буквы русского алфавита.");
                continue;
            }

            if (contains(hiddenWord, c)) {
                if (guessedLetters.indexOf(String.valueOf(c)) < 0) {
                    System.out.println("Есть такая буква в слове!");
                    guessedLetters.append(c);
                    enteredLetters.append(c);
                    System.out.println("Список введенных букв: " + enteredLetters);

                } else {
                    System.out.println("Эту букву вы уже угадывали!");

                }
            } else {
                if (enteredLetters.indexOf(String.valueOf(c)) < 0) {
                    System.out.println("Неправильно, попробуем еще раз");
                    attemptsCount--;
                    Gallows.printGallows(attemptsCount);
                    System.out.println("Осталось попыток: " + attemptsCount);
                    enteredLetters.append(c);
                    System.out.println("Список введенных букв: " + enteredLetters);
                } else {
                    System.out.println("Эту букву вы уже вводили и она неверная!");
                }
            }

            if (isWin()) {
                printWinMessage();
                return;
            }

        }

        printLoseMessage();
    }

    private static boolean contains(String word, char letter) {
        letter = Character.toLowerCase(letter);
        return word.toLowerCase().indexOf(letter) >= 0;
    }

    public static void startGame() {
        System.out.printf("Если хотите начать новую игру введите '%s', если хотите выйти введите '%s': %n", START, QUIT);
        String input = scanner.nextLine();

        if (input.equalsIgnoreCase(START)) {
            System.out.println("Игра начинается...");
            String chooseWord = ReedFile.chooseWord();
            attemptsCount = 6;
            enteredLetters.setLength(0);
            guessedLetters.setLength(0);
            playGame(chooseWord);
        } else if (input.equalsIgnoreCase(QUIT)) {
            System.out.println("Выход из игры...");
            System.exit(0);
        } else {
            System.out.println("Неправильный ввод, попробуйте снова.");
        }
    }

    private static boolean isWin() {
        String mask = maskWord(hiddenWord, guessedLetters.toString());
        return mask.indexOf(MASK_SIMBOL) < 0;
    }

    private static boolean printWinMessage() {
        System.out.println("Вы выиграли!!!!! Загаданное слово " + hiddenWord);
        System.out.println();
        return true;
    }

    private static boolean printLoseMessage() {
        System.out.println("Вы проиграли. Загаданное слово " + hiddenWord);
        System.out.println();
        return false;
    }
}
class Gallows {

    private static final String[] PICTURES = {
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



    public static void printGallows(int attemptsCount) {
        String[] gallows = PICTURES;
        int index = Math.max(0, Math.min(gallows.length - 1, 6 - attemptsCount));
        System.out.println(gallows[index]);
    }

}
