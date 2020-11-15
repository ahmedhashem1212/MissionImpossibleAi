
public class Node {
String state;
Node parent;
String operator;
int depth;
int pathCost;

public Node(String state, Node parent, String operator, int depth, int pathCost) {
	this.state = state;
	this.parent = parent;
	this.operator = operator;
	this.depth = depth;
	this.pathCost = pathCost;
}


}
