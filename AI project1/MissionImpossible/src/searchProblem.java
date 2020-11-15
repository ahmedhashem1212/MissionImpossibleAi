import java.util.ArrayList;

abstract public class searchProblem {
ArrayList<String> operators;
Node intialState;
ArrayList<Node> stateSpace;
String goalTest;
int pathCost;
public searchProblem(ArrayList<String> operators, Node intialState, ArrayList<Node>  stateSpace, String goalTest, int pathCost) {

	this.operators = operators;
	this.intialState = intialState;
	this.stateSpace = stateSpace;
	this.goalTest = goalTest;
	this.pathCost = pathCost;
}

}
