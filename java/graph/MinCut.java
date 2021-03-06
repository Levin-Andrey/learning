/**
 * Performs mincut search by random edge contraction.
 * TODO (lenny): use common Graph class.
 */

package graph;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;


/**
 * Result of single random contraction.
 * adjList - resulting adjustment list.
 * merged - the merged edge.
 */
final class ContractionResult {
  final int[][] adjList;
  final int[] merged;


  /**
   * Constructor, assigning parameters to class properties.
   * @param adjList Adjustment list after contraction.
   * @param merged Edge that was merged.
   */
  ContractionResult(int[][] adjList, int[] merged) {
    this.adjList = adjList;
    this.merged = merged;
  }
}


/**
 * Result of minCut algorithm (as it's random, it can be correct or not).
 * minCut -  Detected number of minimum cuts.
 * cuts - Resulting cuts.
 */
final class MinCutResult {
  final int minCut;
  final List<List<Integer>> cuts;


  /**
   * Constructor, assigning parameters to class properties.
   * @param minCut Detected number of minimum cuts.
   * @param cuts Resulting cuts.
   */
  MinCutResult(int minCut,  List<List<Integer>> cuts) {
    this.minCut = minCut;
    this.cuts = cuts;
  }
}


/**
 * Class to find mincut of a graph.
 */
public class MinCut {

  // Generator of random numbers.
  static Random generator = new Random();


  /**
   * Sets an adjustment list, runs random MinCut algorithm, selects
   * the best resykt and prints it.
   * @param args Array of arguments for launching the program. Ignored.
   * @export
   */
  public static void main(String[] args) {
    int[][] adjList = {
      {1, 2}, {1, 5},
      {2, 5}, {2, 6},
      {5, 6},
      {6, 7},
    };
    printGraph(adjList);
    int n = getNodes(adjList).length;
    MinCutResult result = null;
    for (int i = 0; i < Math.round(n*n*Math.log(n)); i++) {
      MinCutResult attempt = getRandomMinCut(adjList);
      if (result == null || result.minCut > attempt.minCut) {
        result = attempt;
      }
      // minCut in a connected graph can't be smaller than 1
      if (result.minCut == 1) {
        break;
      }
    }
    System.out.println("Mincut: " + Integer.toString(result.minCut));
    System.out.println(result.cuts.toString());
  }


  /**
   * Performs a random min cut algorithm:  performs random contraction
   * until the list of cuts is of size 2 and returns it's result.
   * @param adjList The adjustment list of the graph.
   * @return MinCutResult object with the mincut and cuts.
   */
  static MinCutResult getRandomMinCut(int[][] adjList) {
    List<List<Integer>> groups = new ArrayList<List<Integer>>();
    Integer[] nodes = getNodes(adjList);
    for (int i = 0; i < nodes.length; i++) {
      List<Integer> group = new ArrayList<Integer>();
      group.add(nodes[i]);
      groups.add(group);
    }
    while (groups.size() > 2) {
      ContractionResult iteration = contractRandomEdge(adjList);
      adjList = iteration.adjList;
      groups = addMergedEdge(groups, iteration.merged);
      int[] merged = iteration.merged;
    }
    return new MinCutResult(adjList.length, groups);
  }


  /**
   * Manges groups of merged edges. Following cases are possible:
   * - If nodes are set in one group, then add new node to this group.
   * - If nodes are part of two groups, merge the group.
   * @param groups Current list of cuts.
   * @param merged The edge that was merged.
   * @return New list of cuts.
   */
   static List<List<Integer>> addMergedEdge(List<List<Integer>> groups, int[] merged) {
    List<Integer> intersected = new ArrayList<Integer>();
    for (int i = 0; i < groups.size(); i++) {
      List<Integer> group = groups.get(i);
      for (int j = 0; j < group.size(); j++) {
        if (group.get(j) == merged[0] || group.get(j) == merged[1]) {
          intersected.add(i);
        }
      }
    }
    if (intersected.size() == 1) {
      List<Integer> edges = groups.get(intersected.get(0));
      for (int i = 0; i < 2; i++) {
        if (edges.indexOf(merged[i]) < 0) {
          edges.add(merged[i]);
        }
      }
    }
    else {
      List<Integer> group = groups.get(intersected.get(0));
      group.addAll(groups.get(intersected.get(1)));
      groups.remove(intersected.get(1).intValue());
    }
    return groups;
  }


  /**
   * Performs a ranndom contraction and returns the result adjList with
   * the merged edge.
   * @param adjList The adjustment list of the edge.
   * @return ContructionResult object with the resulting adjustment list
   * and merged edge.
   */
  public static ContractionResult contractRandomEdge(int[][] adjList) {
    int edgeIndex = generator.nextInt(adjList.length);
    int[] delEdge = {adjList[edgeIndex][0], adjList[edgeIndex][1]};
    int[][] resultType = {};
    int num = 0;
    List<int[]> result = new ArrayList<int[]>();
    for (int i = 0; i < adjList.length; i++) {
      if (isParallelEdge(adjList[i], delEdge)) {
        continue;
      }
      int[] tmpEdge = new int[2];
      for (int j = 0; j < 2; j++) {
        if (adjList[i][j] == delEdge[1]) {
          tmpEdge[j] = delEdge[0];
        } else {
          tmpEdge[j] = adjList[i][j];
        }
      }
      result.add(tmpEdge);
    }
    return new ContractionResult(result.toArray(resultType), delEdge);
  }


  /**
   * Detects if two edges are parallel.
   * @param edge1 The first edge.
   * @param edge2 The second edge.
   * @return True if the edges are parallel, false otherwise.
   **/
  static boolean isParallelEdge(int[] edge1, int[] edge2) {
    return ((edge1[0] == edge2[0] && edge1[1] == edge2[1]) ||
            (edge1[0] == edge2[1] && edge1[1] == edge2[0]));
  }


  /**
   * Gets all nodes of the graph.
   * @param adjList The adjustment list of the graph.
   * @return An array of all nodes in the graph with no duplicates.
   */
  static Integer[] getNodes(int[][] adjList) {
    HashSet<Integer> nodes = new HashSet<Integer>();
    for (int[] edge : adjList) {
      for (int node : edge) {
        nodes.add(node);
      }
    }
    return nodes.toArray(new Integer[0]);
  }


  /**
   * Prints the graph nicely.
   * @param adjList The adjustment list of the graph.
   */
  static void printGraph(int[][] adjList) {
    for (int[] edge : adjList) {
      System.out.println(Integer.toString(edge[0]) + " -> " +
        Integer.toString(edge[1]));
    }
  }
}
