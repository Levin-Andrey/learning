/**
 * A set of useful functions:
 * Read int[] from file.
 * Generate int[] randomly.
 * Prints array of elements. 
 * Run instructions: java MergeSort path/to/file.txt
 */


package utils;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;


public class Utils {
  /*
   * Reads input from file, handles the errors.
   * @param filePath Path to the input file.
   * @return Array of integers, one integer from each line.
   *  if an error occured, empty array is returned.
   */
  public static int[] readFromFile(String filePath) {
    System.out.println("Trying to read file "+ filePath);
    int[] result = new int[]{};
    // TODO (@lenny) Refactor this to make Exceptions handling nicer.
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(filePath));
    } catch (FileNotFoundException e) {
      System.out.println("File not found");
      return result;
    }
    try {
      StringBuilder sb = new StringBuilder();
      String line = br.readLine();
      while (line != null) {
          sb.append(line);
          sb.append(',');
          line = br.readLine();
      }
      String[] lines = sb.toString().split(",");
      System.out.println(Integer.toString(lines.length) + " lines read");
      result = new int[lines.length];
      for (int i = 0; i < lines.length; i++) {
        result[i] = Integer.parseInt(lines[i]);
      }
    } finally {
      try {
        br.close();
      } catch (IOException e) {
        System.out.println("Error while reading file");
      }
      return result;
    }
  }


  /*
   * Generates a random array of integers.
   * @param size Size of the array.
   * @param maxValue Maximum allowed value, not inclusive.
   * @return Array of randomly generated integers.
   */
  public static int[] generateArray(int size, int maxValue) {
    int[] result = new int[size];
    Random generator = new Random();
    for (int k = 0; k < size; k++) {
      result[k] = generator.nextInt(maxValue);
    }
    return result;
  }


  /*
   * Prints the array in a nice readable manner.
   * @param arr Array to print
   */
  public static void printArr(int[] arr) {
    System.out.print('[');
    for (int x : arr) {
      System.out.print(Integer.toString(x) + ' ');
    }
    System.out.println(']');
  }
}
