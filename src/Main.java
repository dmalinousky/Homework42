import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws IOException {
        task1Method();
        task2Method();
        task3Method();

    }

    public static void task1Method() throws IOException {
        System.out.println("Task #1");
        // File with lyrics and its reader
        File file1 = new File(System.getProperty("user.dir") + File.separator + "File1.txt");
        FileInputStream fileInputStream = new FileInputStream(file1);

        // Deleting special symbols
        HashSet<Character> specials = new HashSet<>();
        specials.add(';'); specials.add(':'); specials.add(']');
        specials.add('.'); specials.add(','); specials.add('?');
        specials.add('!'); specials.add('('); specials.add(')');
        specials.add('[');

        // Putting "purified" word-list into array, then - into file
        ArrayList<String> words = new ArrayList<>();
        String word = "";
        while (true) {
            char buffer = (char) fileInputStream.read();
            if (!specials.contains(buffer)) {
                if (fileInputStream.available() != 0)  {
                    if (buffer == ' ') {
                        words.add(word);
                        word = "";
                    } else {
                        word += buffer;
                    }
                } else {
                    // Finish line
                    String firstWord = words.get(0);
                    word += buffer;
                    words.set(0, word);
                    words.add(firstWord);

                    // Writing to a new file
                    BufferedWriter fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file1)));
                    fileWriter.write(words.toString());

                    // Closing in-/output
                    fileInputStream.close();
                    fileWriter.close();
                    break;
                }
            }
        }
    }

    public static void task2Method() throws IOException {
        // A little help
        System.out.println("Task #2\nUse those paths:");
        System.out.println(System.getProperty("user.dir") + File.separator + "File2.txt");
        System.out.println(System.getProperty("user.dir") + File.separator + "File3.txt");
        System.out.println(System.getProperty("user.dir") + File.separator + "File4.txt");

        // Creating result file
        File resultFile = new File(System.getProperty("user.dir") + File.separator + "Result.txt");
        BufferedWriter fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(resultFile)));

        // Reader preparing
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader fileReader = null;

        // Reading 3 files
        for (int i = 0; i < 3; i++) {
            System.out.println("Provide a path:");
            File file = new File(bufferedReader.readLine());
            fileReader = new BufferedReader(new FileReader(file));
            fileReader.transferTo(fileWriter);
        }

        // Closing in-/output
        fileReader.close();
        fileWriter.close();
    }

    public static void task3Method() throws IOException {
        System.out.println("Task #3");
        // Creating a file and its reader
        File scoreFile = new File(System.getProperty("user.dir") + File.separator + "Scores.txt");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(scoreFile)));

        // Creating a copy
        File copy = new File(System.getProperty("user.dir") + File.separator + "Scores (copy).txt");
        copy.createNewFile();
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(copy)));

        // Creating patterns
        Pattern namePattern = Pattern.compile("[A-Z][a-z]+");
        Pattern scorePattern = Pattern.compile("([0-9]+)");

        while (true) {
            String line = bufferedReader.readLine(); // Reading line by line
            String newLine = ""; // Needed for making name in CAPITAL
            Matcher matcher; // Matcher object
            String name = null; // Student's name itself
            Double score = null; // Student's score

            if (line != null) { // Checking if the line is not null

                matcher = namePattern.matcher(line); // Matcher checking #1
                if (matcher.find()) {
                    name = line.substring(matcher.start(), matcher.end()); // Getting the name
                }

                String noNameLine= line.replace(name, ""); // Deleting the name from the line
                matcher = scorePattern.matcher(noNameLine); // Matcher checking #2 (new pattern)
                if (matcher.find()) {
                    int sum = 0; // Score sum counter
                    int counter = 0; // The amount of marks
                    String digit = ""; // Mark may contain of 2 or more digits
                    for (int i = 0; i < noNameLine.length(); i++) { // Looking for digits in the line
                        if (noNameLine.charAt(i) != ' ') {
                            digit += noNameLine.charAt(i);
                            if (i == noNameLine.length() - 1) {
                                sum += Integer.parseInt(digit);
                                counter++;
                            }
                        } else if (digit != "") {
                            sum += Integer.parseInt(digit);
                            counter++;
                            digit = "";
                        }
                    }
                    score = (double) sum / counter; // Counting student's score
                    if (score > 7) { // And checking it
                        newLine += name.toUpperCase() + noNameLine; // Making it UPPERCASE
                        bufferedWriter.write(newLine + "\n");
                    } else {
                        bufferedWriter.write(line + "\n"); // Or just writing it with no change
                    }
                } else {
                    bufferedWriter.write(line + "\n"); // If student has no marks
                }
            } else {
                // Closing in-/output
                bufferedWriter.close();
                bufferedReader.close();
                System.out.println("Done!");
                break;
            }
        }
    }
}