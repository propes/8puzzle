/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Comparator;

public class HammingComparator implements Comparator<SearchNode> {
    public int compare(SearchNode searchNode, SearchNode t1) {
        return searchNode.priority() - t1.priority();
    }
}
