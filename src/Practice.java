import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class Practice {

  /**
   * Returns the count of vertices with odd values that can be reached from the given starting vertex.
   * The starting vertex is included in the count if its value is odd.
   * If the starting vertex is null, returns 0.
   *
   * Example:
   * Consider a graph where:
   *   5 --> 4
   *   |     |
   *   v     v
   *   8 --> 7 < -- 1
   *   |
   *   v
   *   9
   * 
   * Starting from 5, the odd nodes that can be reached are 5, 7, and 9.
   * Thus, given 5, the number of reachable odd nodes is 3.
   * @param starting the starting vertex (may be null)
   * @return the number of vertices with odd values reachable from the starting vertex
   */
  public static int oddVertices(Vertex<Integer> starting) {
    if (starting == null) return 0;

    //initialize visited Set
    Set<Vertex<Integer>> visited = new HashSet<>();

    //start recursive traversal with starting vertex
    return oddVerticesHelper(starting, visited);
  }
  //helper method-
  //no extra shared mutable variables needed—recursion handles
  //accumulation through returns
  private static int oddVerticesHelper(Vertex<Integer> current, Set<Vertex<Integer>> visited) {
    //base case-if we've already visited this vertex return immediately
    if(visited.contains(current)) return 0;
    //mark current as visited to avoid infinite loops
    visited.add(current);
    //count this vertex.data if odd
    int count = (current.data % 2 != 0) ? 1 : 0;
    //for-each iterates each neighbor of current
    for (Vertex<Integer> neighbor : current.neighbors) {
      //recursion step adds up counts returned from neighbors
      count += oddVerticesHelper(neighbor, visited);
    }
    //return total count accumulated from this vertex downward
    return count;
  }

  /**
   * Returns a *sorted* list of all values reachable from the starting vertex (including the starting vertex itself).
   * If duplicate vertex data exists, duplicates should appear in the output.
   * If the starting vertex is null, returns an empty list.
   * They should be sorted in ascending numerical order.
   *
   * Example:
   * Consider a graph where:
   *   5 --> 8
   *   |     |
   *   v     v
   *   8 --> 2 <-- 4
   * When starting from the vertex with value 5, the output should be:
   *   [2, 5, 8, 8]
   *
   * @param starting the starting vertex (may be null)
   * @return a sorted list of all reachable vertex values by 
   */
  public static List<Integer> sortedReachable(Vertex<Integer> starting) {
    // Unimplemented: perform a depth-first search and sort the collected values.
    //base case
    if (starting == null) return new ArrayList<>();
    //initialize empty visited Set/empty results List
    Set<Vertex<Integer>> visited = new HashSet<>();
    List<Integer> results = new ArrayList<>();
    //
    sortedReachableHelper(starting, visited, results);
    //sort results List into asc order
    Collections.sort(results);
    return results;
  }
  private static void sortedReachableHelper(Vertex<Integer> current, Set<Vertex<Integer>> visited, List<Integer> results) {
    //base case
    if (visited.contains(current)) return;
    //add current and current.data to visited and results trackers
    visited.add(current);
    results.add(current.data);
    //for each to iterate through each neighbor of current
    for (Vertex<Integer> neighbor : current.neighbors) {
      //recurse all neighbors
      sortedReachableHelper(neighbor, visited, results);
    }
  }

  /**
   * Returns a sorted list of all values reachable from the given starting vertex in the provided graph.
   * The graph is represented as a map where each key is a vertex and its corresponding value is a set of neighbors.
   * It is assumed that there are no duplicate vertices.
   * If the starting vertex is not present as a key in the map, returns an empty list.
   *
   * @param graph a map representing the adjacency list of the graph
   * @param starting the starting vertex value
   * @return a sorted list of all reachable vertex values
   */
  public static List<Integer> sortedReachable(Map<Integer, Set<Integer>> graph, int starting) {
    //base case (  more generic: return Collections.emptySet();  )
    if (graph == null || !graph.containsKey(starting)) return new ArrayList<>();
    //instantiate visited Set
    Set<Integer> visited = new HashSet<>();

    sortedReachableHelper(graph, starting, visited);

    //convert visited data into List format
    List<Integer> sorted = new ArrayList<>(visited);
    //sort newly formed List
    Collections.sort(sorted);
    //return List of sorted reachable 'vertices'
    return sorted;
  }

  private static void sortedReachableHelper(Map<Integer, Set<Integer>> graph, int current, Set<Integer> visited) {
    //base case - if we've been here already then return immediately
    if (visited.contains(current)) return;
    //add current to visited
    visited.add(current);
    //for each avoids getting a null pointer exception or needing 
    //an extra check for null values explicitly
    for (Integer neighbor : graph.getOrDefault(current, Collections.emptySet())) { //recursively transverse to fill up visited Set
      sortedReachableHelper(graph, neighbor, visited);
    }
  }
  /**
   * Returns true if and only if it is possible both to reach v2 from v1 and to reach v1 from v2.
   * A vertex is always considered reachable from itself.
   * If either v1 or v2 is null or if one cannot reach the other, returns false.
   *
   * Example:
   * If v1 and v2 are connected in a cycle, the method should return true.
   * If v1 equals v2, the method should also return true.
   *
   * @param <T> the type of data stored in the vertex
   * @param v1 the starting vertex
   * @param v2 the target vertex
   * @return true if there is a two-way connection between v1 and v2, false otherwise
   */
  public static <T> boolean twoWay(Vertex<T> v1, Vertex<T> v2) {
    //base case
    if (v1 == null || v2 == null) return false;
    //instantiate Set inside helper method
    //call helper method
    boolean oneWay = twoWayHelper(v1, v2, new HashSet<>());
    //repeat for opposite direction
    
    //call helper method
    boolean otherWay = twoWayHelper(v2, v1, new HashSet<>());
    return oneWay && otherWay;
  }
  private static <T> boolean twoWayHelper(Vertex<T> current, Vertex<T> v2, Set<Vertex<T>> visited) {
    //base case -> successfully reached target vertex
    if (current == v2) return true;
    //already visited? stop recursion!
    if (visited.contains(current)) return false;
    //add current to visited to avoid looping
    visited.add(current);
    //for each iterate all neighbors of current
    for (Vertex<T> neighbor : current.neighbors) {
      //recursively transverse each neighbor of current
      if (twoWayHelper(neighbor, v2, visited)) return true;
    }
    return false;
  }

  /**
   * Returns whether there exists a path from the starting to ending vertex that includes only positive values.
   * 
   * The graph is represented as a map where each key is a vertex and each value is a set of directly reachable neighbor vertices. A vertex is always considered reachable from itself.
   * If the starting or ending vertex is not positive or is not present in the keys of the map, or if no valid path exists,
   * returns false.
   *
   * @param graph a map representing the graph
   * @param starting the starting vertex value
   * @param ending the ending vertex value
   * @return whether there exists a valid positive path from starting to ending
   */
  public static boolean positivePathExists(Map<Integer, Set<Integer>> graph, int starting, int ending) {
    //base case
    if (starting < 0 || ending < 0 || graph == null || !graph.containsKey(starting) || !graph.containsKey(ending)) return false;
    //call helper method inside boolean solution return
    return positivePathExistsHelper(graph, starting, ending, new HashSet<>());
  }
  private static boolean positivePathExistsHelper(Map<Integer, Set<Integer>> graph, int current, int ending, Set<Integer> visited) {
    //base case -> have we reached ending via current?
    if (current == ending) return true;
    //have we been here already? if so return false
    if (visited.contains(current)) return false;
    //add current to visited as tracking to avoid looping
    visited.add(current);
    //for each to iterate neighbors of current
    for (Integer neighbor : graph.getOrDefault(current, Collections.emptySet())) {
      if (neighbor > 0 && positivePathExistsHelper(graph, neighbor, ending, visited)) return true;
    }
    return false;
  }

  /**
   * Returns true if a professional has anyone in their extended network (reachable through any number of links)
   * that works for the given company. The search includes the professional themself.
   * If the professional is null, returns false.
   *
   * @param person the professional to start the search from (may be null)
   * @param companyName the name of the company to check for employment
   * @return true if a person in the extended network works at the specified company, false otherwise
   */
  public static boolean hasExtendedConnectionAtCompany(Professional person, String companyName) {
    //base case
    if (person == null) return false;
    //call helper
    return hasExtendedConnectionAtCompanyHelper(person, companyName, new HashSet<>());
  }
  private static boolean hasExtendedConnectionAtCompanyHelper(Professional currentPerson, String companyName, Set<Professional> visited) {
    //base case: if we've reached via DFS a person
    //working at same company as starting person
    //return true; **use .equals() for String comparisons
    if (currentPerson.getCompany().equals(companyName)) return true;
    //have we visited this person already? return false!
    if (visited.contains(currentPerson)) return false;
    //add currentPerson to visited Set
    visited.add(currentPerson);
    //for-each iterates all connections currentPerson has
    for (Professional connection : currentPerson.getConnections()) {
      //if recursion step is true, return true
      if (hasExtendedConnectionAtCompanyHelper(connection, companyName, visited)) {
        return true;
      } 
    }
    return false;
  }
}
