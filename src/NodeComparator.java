import java.util.Comparator;

public class NodeComparator implements Comparator<Node>
{
    @Override
    public int compare(Node n1, Node n2)
    {
        return Integer.compare(n1.getF(), n2.getF());
    }
}
