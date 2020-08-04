import java.util.Comparator;

public class SearchNodeComparator implements Comparator<SearchNode> {
    public int compare(SearchNode searchNode, SearchNode t1) {
        return searchNode.priority() - t1.priority();
    }
}
