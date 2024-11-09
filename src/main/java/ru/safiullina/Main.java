package ru.safiullina;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static AtomicInteger three = new AtomicInteger(0);
    public static AtomicInteger four = new AtomicInteger(0);
    public static AtomicInteger five = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {

        // Для генерации 100 000 коротких слов вы использовали:
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        // Слово является палиндромом (и не из одной буквы)
        Thread palindrome = new Thread(() -> {
            for (String text : texts) {
                if (isPalindrome(text) && !isOneLetter(text)) {
                    counter(text, "palindrome");
                }
            }
        });

        // Слово состоит из одной и той же буквы, например, aaa
        Thread oneLetter = new Thread(() -> {
            for (String text : texts) {
                if (isOneLetter(text)) {
                    counter(text, "one letter");
                }
            }
        });

        // Буквы в слове идут по возрастанию (и не из одной буквы)
        Thread ascent = new Thread(() -> {
            for (String text : texts) {
                if (isAscent(text) && !isOneLetter(text)) {
                    counter(text, "ascent");
                }
            }
        });

        // Стартуем
        palindrome.start();
        oneLetter.start();
        ascent.start();

        // Ждем
        palindrome.join();
        oneLetter.join();
        ascent.join();

        // Выводим
        System.out.printf("Красивых слов с длиной 3: %d шт \n", three.get());
        System.out.printf("Красивых слов с длиной 4: %d шт \n", four.get());
        System.out.printf("Красивых слов с длиной 5: %d шт \n", five.get());

    }

    public static void counter(String text, String reason) {
        //System.out.printf("Красивое = %s, причина = %s \n", text, reason);

        if (text.length() == 3) {
            three.incrementAndGet();
        } else if (text.length() == 4) {
            four.incrementAndGet();
        } else if (text.length() == 5) {
            five.incrementAndGet();
        }
    }

    public static boolean isPalindrome(String text) {
        return text.toLowerCase().contentEquals(new StringBuilder(text.toLowerCase()).reverse());
    }

    public static boolean isAscent(String text) {
        return (text.indexOf('b') == -1 || text.lastIndexOf('a') < text.indexOf('b'))
                && (text.indexOf('c') == -1 || text.lastIndexOf('a') < text.indexOf('c'))
                && (text.indexOf('c') == -1 || text.lastIndexOf('b') < text.indexOf('c'));
    }

    public static boolean isOneLetter(String text) {
        return ((text.indexOf('a') > -1 && text.indexOf('b') == -1 && text.indexOf('c') == -1)
                || (text.indexOf('a') == -1 && text.indexOf('b') > -1 && text.indexOf('c') == -1)
                || (text.indexOf('a') == -1 && text.indexOf('b') == -1 && text.indexOf('c') > -1));
    }

    /**
     * Генератор никнейм
     * String letters - из каких букв
     * int length - какой длины
     */
    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));

        }
        return text.toString();
    }
}