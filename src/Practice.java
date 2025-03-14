import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.Comparator;

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
    int oddCount = 0;
    oddCount += oddVertices(starting, oddCount, new HashSet<Vertex<Integer>>());
    return oddCount;
  }

  public static int oddVertices(Vertex<Integer> current, int oddCount, HashSet<Vertex<Integer>> record) {
    if (current == null) return oddCount;
    if (record.contains(current)) return oddCount;
    record.add(current);

    if (current.data % 2 != 0) {
      oddCount++;
    }

    for (var neighbor: current.neighbors) {
      oddCount = oddVertices(neighbor, oddCount, record);
    }

    return oddCount;
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
    List<Integer> sortedReachedValues = new ArrayList<>();
    if (starting == null) return sortedReachedValues;
    sortedReachable(starting, sortedReachedValues, new HashSet<Vertex<Integer>>());
    // I looked up how to make sort() work. I'm using the Comparator class as an arg because Integers implement the Comparable interface
    sortedReachedValues.sort(Comparator.naturalOrder());
    return sortedReachedValues;
  }

  public static void sortedReachable(Vertex<Integer> current, List<Integer> sortedReacheableValues, HashSet<Vertex<Integer>> record) {
    if (record.contains(current)) return;
    record.add(current);
    sortedReacheableValues.add(current.data);

    for (var neighbor: current.neighbors) {
      sortedReachable(neighbor, sortedReacheableValues, record);
    }
  }

  /**
   * Returns a sorted list of all values reachable from the given starting vertex in the provided graph.
   * The graph is represented as a map where each key is a vertex and its corresponding value is a set of neighbors.
   * It is assumed that there are no duplicate vertices.
   * If the starting vertex is not present as a key in the map, returns an empty list.
   *
   * @param graph a map representing the graph
   * @param starting the starting vertex value
   * @return a sorted list of all reachable vertex values
   */
  public static List<Integer> sortedReachable(Map<Integer, Set<Integer>> graph, int starting) {
    List<Integer> sortedReachableValues = new ArrayList<>();
    if (!graph.containsKey(starting)) {
      return sortedReachableValues;
    }

    sortedReachable(graph, starting, sortedReachableValues);

    sortedReachableValues.sort(Comparator.naturalOrder());
    return sortedReachableValues;
  }

  public static void sortedReachable(Map<Integer, Set<Integer>> graph, int current, List<Integer> sortedReachableValues) {
    if (sortedReachableValues.contains(current)) return;
    sortedReachableValues.add(current);

    for (var neighbor: graph.get(current)) {
      sortedReachable(graph, neighbor, sortedReachableValues);
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
    if (v1 == null || v2 == null) return false;
    if (v1 == v2) return true;

    boolean v1Tov2 = twoWay(v1, v2, new HashSet<Vertex<T>>(), false);
    boolean v2Tov1 = twoWay(v2, v1, new HashSet<Vertex<T>>(), false);
    return (v1Tov2 && v2Tov1);
  }

  public static <T> boolean twoWay(Vertex<T> current, Vertex<T> end, HashSet<Vertex<T>> record, boolean connect) {
    if (record.contains(current)) return connect;
    record.add(current);
    if (current == end) return true;

    for (var neighbor: current.neighbors) {
      connect = twoWay(neighbor, end, record, connect);
    }
    return connect;
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
    if (!graph.containsKey(starting) || !graph.containsKey(ending) || starting < 0 || ending < 0) return false;

    boolean exists = false;

    exists = positivePathExists(graph, starting, ending, exists, new HashSet<Integer>());

    return exists;
  }

  public static boolean positivePathExists(Map<Integer, Set<Integer>> graph, int current, int ending, boolean exists, HashSet<Integer> record) {
    if (record.contains(current)) return exists;
    record.add(current);
    if (current == ending) return true;

    if (current < 0) return false;

    for (var neighbor: graph.get(current)) {
      exists = positivePathExists(graph, neighbor, ending, exists, record);
    }
    return exists;
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
    if (person == null) return false;
    boolean connection = false;

    connection = hasExtendedConnectionAtCompany(person, companyName, connection, new HashSet<Professional>());

    return connection;
  }

  public static boolean hasExtendedConnectionAtCompany(Professional person, String companyName, boolean connection, HashSet<Professional> record) {
    if (record.contains(person)) return connection;
    record.add(person);

    if (person.getCompany().equals(companyName)) return true;

    for (Professional acquaintance: person.getConnections()) {
      connection = hasExtendedConnectionAtCompany(acquaintance, companyName, connection, record);
    }

    return connection;
  }
}
