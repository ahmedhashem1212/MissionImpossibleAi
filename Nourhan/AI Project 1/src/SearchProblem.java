import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public abstract class SearchProblem{

		Operator operator;
		
		String initialState;
		
		public SearchProblem(String initialState) {
			this.initialState=initialState;
		}
		
		abstract Node[] expand(Node parent, Operator[] operators); //here our state space is defined
		abstract boolean goalTest(String state, MissionImpossible problem);
	//	abstract int pathCost(Node node);
		
		static HashMap<Integer, Node> expandedNodes;

		public static Node generalSearch(SearchProblem problem, String strategy) {
			
			Queue<Node> nodes = new LinkedList<Node>();
			
			//create root based on info from grid   -state operator parent depth cost-
			Node root = new Node(problem.initialState, null, null, 0, 0,null);
			nodes.add(root);
			
			//key index of each node in the hash map storing the expanded nodes
			int i = 0; 
			
			for (Node node; (node = nodes.poll()) != null;){
				
				//expanded node
				node = nodes.remove();
				
				expandedNodes.put(i, node); i++;
				
				if(problem.goalTest(node.state, (MissionImpossible)problem)) {
					return node;
				}
				
				//else we should expand this node and add its children to the queue according to the given strategy
				Operator[] operators = new Operator[]{Operator.UP, Operator.DOWN, Operator.RIGHT, Operator.LEFT, Operator.CARRY, Operator.DROP};
				Node[] children = problem.expand(node, operators);
				
				switch (strategy) {
				
				case "BF": nodes = breadthFirst(nodes, children); break;
				case "DFS": nodes = depthFirst(nodes, children); break;
				case "ID": nodes = iterativeDeeping(nodes, children); break;
				case "UC": nodes = uniformCost(nodes, children, (MissionImpossible)(problem)); break;
				case "GR1": nodes = greedyH1(nodes, children); break;
				case "GR2": nodes = greedyH2(nodes, children); break;
				case "AS1": nodes = aStarH1(nodes, children); break;
				case "AS2": nodes = aStarH2(nodes, children); break;
				default: break;
				
				}
				
			}
			return null;
		}
		
		
		public static Queue<Node> breadthFirst(Queue<Node> nodes, Node[] children) {
			return null;
		}
		
		
		public static Queue<Node> depthFirst(Queue<Node> nodes, Node[] children) {
			return null;
		}
		
		
		public static Queue<Node> iterativeDeeping(Queue<Node> nodes, Node[] children) {
			return null;
		}
		
		
		public static Queue<Node> uniformCost(Queue<Node> nodes, Node[] children, MissionImpossible problem) {
			return null;
		}
		
		
		public static Queue<Node> greedyH1(Queue<Node> nodes, Node[] children) {
			return null;
		}
		
		
		public static Queue<Node> greedyH2(Queue<Node> nodes, Node[] children) {
			return null;
		}
		
		
		public static Queue<Node> aStarH1(Queue<Node> nodes, Node[] children) {
			return null;
		}
		
		
		public static Queue<Node> aStarH2(Queue<Node> nodes, Node[] children) {
			return null;
		}





	}