import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

public class MissionImpossible extends SearchProblem {

	// Reference for the static info in our problem
	static String grid;
	//static HashMap<Integer, String> expandedNodes;
	static int namesCount = 0;
	static String IMF_health;
	static HashMap<String, Integer> health = new HashMap<String, Integer>();
	// Boolean[] memberIsCollected;

	public MissionImpossible(String grid) {
		super(extractState(grid));
		MissionImpossible.grid = grid;
		//MissionImpossible.expandedNodes = new HashMap<Integer, String>();
		String[] healthString = grid.split(";")[grid.split(";").length - 2].split(",");
		String[] IMFString = grid.split(";")[3].split(",");
		for (int i = 0; i < healthString.length; i++) {
			health.put(IMFString[i * 2] + "," + IMFString[i * 2 + 1], Integer.parseInt(healthString[i]));
		}
		// TODO Auto-generated constructor stub
	}
	
	public static String[] getDamage(Node node) {
		String imfHealth_="";
		String imfLoc="";
		String[] imfArray = node.state.split(";").length == 3 ? node.state.split(";")[2].split(",") : new String[0];
		String[] healthXloc= new String[imfArray.length/2];

		int[] ethanArray = { Integer.parseInt(node.state.split(";")[0].split(",")[0]),
				Integer.parseInt(node.state.split(";")[0].split(",")[1]) };
		if(imfArray.length/2>2) {
		int distance;
		// 3.Find pathCost
		int i =0;
		for ( i = 0; i < imfArray.length/2; i++) {
			distance = Math.abs(ethanArray[0] - Integer.parseInt(imfArray[i * 2]))
					+ Math.abs(ethanArray[1] - Integer.parseInt(imfArray[i * 2 + 1]));
			int IMFHealth = node.depth * 2 + health.get(imfArray[i * 2] + "," + imfArray[i * 2 + 1]);
			imfHealth_=imfHealth_+""+IMFHealth+",";
			imfLoc=imfArray[i * 2]+","+imfArray[i * 2 + 1]+";";
		}
		
		healthXloc[0]=imfHealth_;
		healthXloc[1]=imfLoc;
		System.out.println(imfHealth_);
		}
		return healthXloc;
	}
	// ------------------------------------------------------ Main Methods
	// --------------------------------------------------

	public static String genGrid() {

		Random rand = new Random();
		String grid = "";

		// Grid dimensions
		int max = 15;
		int min = 5;
		int dimensions_x = rand.nextInt(max - min + 1) + min;
		int dimensions_y = rand.nextInt(max - min + 1) + min;
		grid += dimensions_x + "," + dimensions_y + ";";

		// Position of Ethan
		// - 1 for zero indexing
		int ex = rand.nextInt(dimensions_x - 1);
		int ey = rand.nextInt(dimensions_y - 1);
		grid += ex + "," + ey + ";";

		// Position of submarine
		int sx = rand.nextInt(dimensions_x - 1);
		int sy = rand.nextInt(dimensions_y - 1);
		grid += sx + "," + sy + ";";

		// Locations of the IMF members
		// Can two members be in the same cell????
		int totalIMF = rand.nextInt(6) + 5;

		System.out.println(totalIMF);
		for (int i = 0; i < totalIMF; i++) {
			int x = rand.nextInt(dimensions_x - 1);
			int y = rand.nextInt(dimensions_y - 1);
			grid += x + "," + y;
			if (i < totalIMF - 1)
				grid += ",";
			else
				grid += ";";
		}

		// Health of the IMF members
		for (int i = 0; i < totalIMF; i++) {
			int h = rand.nextInt(99) + 1;
			grid += h;
			if (i < totalIMF - 1)
				grid += ",";
			else
				grid += ";";
		}

		// The number of members the truck can carry
		// What is the range of C????
		int c = rand.nextInt(totalIMF) + 1;
		grid += c;

		return grid;
	}

	// uses search to try to formulate a winning plan --- calls generalSearch on
	// our problem
	public String solve(String grid, String strategy, boolean visualize) {

		MissionImpossible problem = new MissionImpossible(grid);
		Node pathExitNode = generalSearch(problem, strategy);
	
		// We should get the full path followed as a string using parent and
		// returned nodes
		
		if (pathExitNode != null) {
			int deaths = 0;
			int nodesCount = 0;
			int imfGridLocIndex;
			int imfStateLocIndex;

			String pathFollowed = "";
			String plan = "" + pathExitNode.operator;
			String damage = null;
			
			String[] gridSeparated = grid.split(";", -1);
			String[] gridDimentions = gridSeparated[0].split(",");
			String[] submarineLoc = gridSeparated[2].split(",");
			String[] ethanLoc = gridSeparated[1].split(",");
			String[] orgToBeUpdatedDamage = new String[gridSeparated[4].length()];
			String[] imfGridLocSeparated = gridSeparated[3].split(",");
			
			String[] imfStateLocSeparated;
			String[] damageSeparated;
			String[] stateSeparated = pathExitNode.state.split(";", -1);

			
			if (visualize == true) {
				System.out.println("-----------------------------------------------------------------------------------");
				System.out.print(pathExitNode.operator.toString());
				printGrid(Integer.parseInt(gridDimentions[0]), Integer.parseInt(gridDimentions[1]),
						Integer.parseInt(ethanLoc[0]), Integer.parseInt(ethanLoc[1]), Integer.parseInt(submarineLoc[0]),
						Integer.parseInt(submarineLoc[1]), "", "");
				System.out.println("-----------------------------------------------------------------------------------");

			}

			/**
			 * I need to get all nodes expanded through this path in order to print: Plan -
			 * Deaths - Health - Nodes Number
			 * 
			 */
			Node previousNode = pathExitNode.parent;
			System.out.println("PATH:");
			String[] damageXloc=getDamage(previousNode);
			// looping till the root
			while (previousNode != null) {
				
				System.out.print(previousNode.name+":   ");
				System.out.println(previousNode.operator + " " + previousNode.pathCost);
				
				if(damageXloc.length>0)
				damage =getDamage(previousNode)[0];
				if (previousNode.operator != null)
					plan = previousNode.operator.toString() + "," + plan;
				nodesCount++;

				//if (plan.substring(0, 5) == "carry") {
					stateSeparated = previousNode.state.split(";", -1);
					imfStateLocSeparated = stateSeparated[2].split(",");

					ethanLoc = stateSeparated[0].split(",");

					imfGridLocIndex = findIMFLoc(imfGridLocSeparated, ethanLoc);
					imfStateLocIndex = findIMFLoc(imfStateLocSeparated, ethanLoc);

					if (plan.substring(0, 5) == "carry") {
					damageSeparated = damage.split(",");
					orgToBeUpdatedDamage[imfGridLocIndex] = damageSeparated[imfStateLocIndex];

					if (orgToBeUpdatedDamage[imfGridLocIndex] == "100")
						deaths++;
					}

				//}

				if (visualize == true) {
					System.out.println("-----------------------------------------------------------------------------------");
					if(previousNode.operator!=null)
					System.out.print(previousNode.operator.toString());
					System.out.println(stateSeparated[2]);
					printGrid(Integer.parseInt(gridDimentions[0]), Integer.parseInt(gridDimentions[1]),
							Integer.parseInt(ethanLoc[0]), Integer.parseInt(ethanLoc[1]),
							Integer.parseInt(submarineLoc[0]), Integer.parseInt(submarineLoc[1]), stateSeparated[2],
							damage);
					System.out.println("-----------------------------------------------------------------------------------");

				}

				previousNode = previousNode.parent;

			}
			String damageConcatenated = String.join(",", orgToBeUpdatedDamage);
			pathFollowed = plan + ";" + deaths + ";" + damageConcatenated + ";" + nodesCount;

			return pathFollowed;
		}
		return "_";

	}

	public static Node generalSearch(MissionImpossible problem, String strategy) {
	//	Queue<Node> nodes = new LinkedList<Node>();
		ArrayList<Node> nodes = new ArrayList<Node>();

		// create root based on info from grid -state operator parent depth
		// cost-
		Node root = new Node(problem.initialState, null, null, 0, 0, GenerateNodeName());
		nodes.add(root);
		int i = 0;

		// loop on queue (expanding and adding new nodes until goal is found or
		// queue is empty)
			while(!nodes.isEmpty()) {
				Node node=nodes.get(0);
				nodes.remove(0);
				String parent = node.parent != null ? node.parent.name : "";
				
				System.out.print(i + ".New State:" + node.state + node.operator);
				System.out.println("   "+node.name);
				
				node.expandedStates.put(i, node.state);
				i++;

				if (problem.goalTest(node.state, problem)) {
					return node;
				}

				Operator[] operators = new Operator[] { Operator.UP, Operator.DOWN, Operator.RIGHT, Operator.LEFT };
				Node[] children = problem.expand(node, operators);
				for (int j = 0; j < children.length; j++) {
					children[j].expandedStates.putAll(node.expandedStates);
				}
				
				switch (strategy) {

				case "BF":
					nodes = breadthFirst(nodes, children);
					break;
				case "DFS":
					nodes = depthFirst(nodes, children);
					break;
				case "ID":
					nodes = iterativeDeeping(nodes, children);
					break;
				case "UC":
					nodes = uniformCost(nodes, children);
					break;
				case "GR1":
					nodes = greedyH1(nodes, children, problem);
					break;
				case "GR2":
					nodes = greedyH2(nodes, children, problem);
					break;
				case "AS1":
					nodes = aStarH1(nodes, children, problem);
					break;
				case "AS2":
					nodes = aStarH2(nodes, children, problem);
					break;
				default:
					break;

				}
			}
			return null;

	}

	// ------------------------------------------------- Overridden Methods
	// -----------------------------------------------------
	// expand returns all possible next states, through invoking all operators
	// on the given state

	/**
	 * Carry operator should update IMF locations after a member is carried, his
	 * location should be removed from the state string his final damage should be
	 * stored in the global variable <membersOverallDamage>: stored as Key: Location
	 * "xy", Health I did it in a hash map 3lshan we7na bn print el output string
	 * a2dar a display health of members be nafs el order el kano displayed be fel
	 * grid
	 */

	String Up(int ex, int ey, String[] IMF, int truck, boolean lastIMF) {

		ex--;
		String new_state = ex + "," + ey + ";";
		new_state += truck + ";";

		if (!lastIMF) {
			for (int i = 0; i < IMF.length; i++) {
				new_state += IMF[i];
				if (i < IMF.length - 1)
					new_state += ',';
				else
					new_state += ";";
			}
			// for (int i = 0; i < health_values.length; i++) {
			// health_values[i] = health_values[i] + health_damage;
			//
			// if (health_values[i] > 100)
			// health_values[i] = 100;
			// new_state += health_values[i];
			// if (i < health_values.length - 1)
			// new_state += ',';
			// else
			// new_state += ";";
			// }
		}
		return new_state;

	}

	String Down(int ex, int ey, String[] IMF, int truck, boolean lastIMF) {
		ex++;
		String new_state = ex + "," + ey + ";";
		new_state += truck + ";";

		if (!lastIMF) {

			for (int i = 0; i < IMF.length; i++) {
				new_state += IMF[i];
				if (i < IMF.length - 1)
					new_state += ',';
				else
					new_state += ";";
			}
			// for (int i = 0; i < health_values.length; i++) {
			// health_values[i] = health_values[i] + health_damage;
			//
			// if (health_values[i] > 100)
			// health_values[i] = 100;
			// new_state += health_values[i];
			// if (i < health_values.length - 1)
			// new_state += ',';
			// else
			// new_state += ";";
			// }
		}
		return new_state;
	}

	String Left(int ex, int ey, String[] IMF, int truck, boolean lastIMF) {
		ey--;
		String new_state = ex + "," + ey + ";";
		new_state += truck + ";";
		if (!lastIMF) {
			for (int i = 0; i < IMF.length; i++) {
				new_state += IMF[i];
				if (i < IMF.length - 1)
					new_state += ',';
				else
					new_state += ";";
			}
			// for (int i = 0; i < health_values.length; i++) {
			// health_values[i] = health_values[i] + health_damage;
			//
			// if (health_values[i] > 100)
			// health_values[i] = 100;
			// new_state += health_values[i];
			// if (i < health_values.length - 1)
			// new_state += ',';
			// else
			// new_state += ";";
			// }
		}

		return new_state;
	}

	String Right(int ex, int ey, String[] IMF, int truck, boolean lastIMF) {
		ey++;
		String new_state = ex + "," + ey + ";";
		new_state += truck + ";";

		if (!lastIMF) {
			for (int i = 0; i < IMF.length; i++) {
				new_state += IMF[i];
				if (i < IMF.length - 1)
					new_state += ',';
				else
					new_state += ";";
			}
			// for (int i = 0; i < health_values.length; i++) {
			// health_values[i] = health_values[i] + health_damage;
			//
			// if (health_values[i] > 100)
			// health_values[i] = 100;
			// new_state += health_values[i];
			// if (i < health_values.length - 1)
			// new_state += ',';
			// else
			// new_state += ";";
			// }
		}
		return new_state;
	}

	String Carry(int ex, int ey, String[] IMF, int truck, int index, boolean lastIMF) {
		String new_state = ex + "," + ey + ";";
		new_state += truck + ";";

		if (!lastIMF) {
			for (int i = 0; i < IMF.length; i++) {
				if ((i / 2) != index) {
					new_state += IMF[i];
					if (i < IMF.length - 1)
						new_state += ',';
				}
			}

			new_state += ";";
		}

		return new_state;
	}

	String Drop(int ex, int ey, String[] IMF, int truck, boolean lastIMF) {
		truck = Character.getNumericValue(grid.charAt(grid.length() - 1));
		String new_state = ex + "," + ey + ";";
		new_state += truck + ";";

		if (!lastIMF) {
			for (int i = 0; i < IMF.length; i++) {
				new_state += IMF[i];
				if (i < IMF.length - 1)
					new_state += ',';
				else
					new_state += ";";
			}
			// for (int i = 0; i < health_values.length; i++) {
			// health_values[i] = health_values[i] + health_damage;
			//
			// if (health_values[i] > 100)
			// health_values[i] = 100;
			// new_state += health_values[i];
			// if (i < health_values.length - 1)
			// new_state += ',';
			// else
			// new_state += ";";
			// }
		}

		return new_state;

	}

	Node[] expand(Node parent, Operator[] operators) {

		boolean lastIMF = false;
		ArrayList<Node> children = new ArrayList<Node>();
		String[] parent_state = parent.state.split(";");

		// System.out.println(Arrays.toString(parent_state));

		int ex = Character.getNumericValue(parent_state[0].charAt(0));
		int ey = Character.getNumericValue(parent_state[0].charAt(2));

		// submarine
		int sx = Character.getNumericValue(grid.charAt(8));
		int sy = Character.getNumericValue(grid.charAt(10));

		int truck = Integer.parseInt(parent_state[1]);

		// IMF members
		// System.out.println(parent_state.length);
		String[] IMF = null;
		int[] IMF_x = null;
		int[] IMF_y = null;
		if (parent_state.length > 2) {
			IMF = parent_state[2].split(",");

			IMF_x = new int[IMF.length / 2];
			IMF_y = new int[IMF.length / 2];

			for (int i = 0; i < IMF.length; i = i + 2) {
				IMF_x[i / 2] = Integer.parseInt(IMF[i]);
				IMF_y[i / 2] = Integer.parseInt(IMF[i + 1]);
			}

			// IMF health
			/*
			 * for (int i = 0; i < health_values.length; i++) { health_values[i] =
			 * Integer.parseInt(health[i]); }
			 */
		} else {
			lastIMF = true;
		}

		int depth = parent.depth + 1;

		boolean carry = false, drop = false;

		// carry
		if (!lastIMF) {
			for (int i = 0; i < IMF_x.length; i++) {
				if (ex == IMF_x[i] && ey == IMF_y[i] && truck > 0) {
					truck--;
					String state = Carry(ex, ey, IMF, truck, i, lastIMF);
					carry = true;
					// System.out.println("Carry" + state);
					// child_node.setState(updateState(child_node.state));
					if (!parent.expandedStates.containsValue(state)) {
						Node child_node = new Node(state, Operator.CARRY, parent, depth, 0, GenerateNodeName());
						children.add(child_node);
					}
					// System.out.println("Carry");
					// health_damage += 2;
				}
			}
		}

		// drop - if ethan at the submarine and the truck is full
		int TruckLimit = Character.getNumericValue(grid.charAt(grid.length() - 1));
		if (ex == sx && ey == sy && truck == 0 && !carry) {
			String state = Drop(ex, ey, IMF, truck, lastIMF);
			drop = true;
			// child_node.setState(updateState(child_node.state));
			if (!parent.expandedStates.containsValue(state)) {
				Node child_node = new Node(state, Operator.DROP, parent, depth, 0, GenerateNodeName());
				children.add(child_node);
			}
		}

		// drop - if ethan at the submarine but the truck is not full
		else if (ex == sx && ey == sy && truck < TruckLimit && !carry) {
			String state = Drop(ex, ey, IMF, truck, lastIMF);
			drop = true;
			if (!parent.expandedStates.containsValue(state)) {
				Node child_node = new Node(state, Operator.DROP, parent, depth, 0, GenerateNodeName());
				// child_node.setState(updateState(child_node.state));
				children.add(child_node);
			}
		}

		// if the truck is full, go straight to the submarine
		boolean flagSubmarine = false;
		boolean moved = false;
		if (truck == 0) {
			flagSubmarine = true;
			moved = false;
		}

		// up
		if ((ex - 1) >= 0 && !carry && !drop) {
			if (!flagSubmarine) {
				String state = Up(ex, ey, IMF, truck, lastIMF);
				if (!parent.expandedStates.containsValue(state)) {
					Node child_node = new Node(state, Operator.UP, parent, depth, 0, GenerateNodeName());
					children.add(child_node);
				}
			} else if (flagSubmarine && ex > sx) {
				String state = Up(ex, ey, IMF, truck, lastIMF);
				if (!parent.expandedStates.containsValue(state)) {
					Node child_node = new Node(state, Operator.UP, parent, depth, 0, GenerateNodeName());
					children.add(child_node);
				}
				moved = true;
			}
		}
		// down
		if ((ex + 1) < Character.getNumericValue(grid.charAt(0)) && !carry && !drop) {
			if (!flagSubmarine) {
				String state = Down(ex, ey, IMF, truck, lastIMF);
				if (!parent.expandedStates.containsValue(state)) {
					Node child_node = new Node(state, Operator.DOWN, parent, depth, 0, GenerateNodeName());
					children.add(child_node);
				}
			} else if (flagSubmarine && ex < sx && !moved) {
				String state = Down(ex, ey, IMF, truck, lastIMF);
				if (!parent.expandedStates.containsValue(state)) {
					Node child_node = new Node(state, Operator.DOWN, parent, depth, 0, GenerateNodeName());
					children.add(child_node);
				}
			}
		}
		// left
		if ((ey - 1) >= 0 && !carry && !drop) {
			if (!flagSubmarine) {
				String state = Left(ex, ey, IMF, truck, lastIMF);
				if (!parent.expandedStates.containsValue(state)) {
					Node child_node = new Node(state, Operator.LEFT, parent, depth, 0, GenerateNodeName());
					children.add(child_node);
				}
			} else if (flagSubmarine && ey > sy && !moved) {
				String state = Left(ex, ey, IMF, truck, lastIMF);
				if (!parent.expandedStates.containsValue(state)) {
					Node child_node = new Node(state, Operator.LEFT, parent, depth, 0, GenerateNodeName());
					children.add(child_node);
				}
				moved = true;
			}
		}
		// right
		if ((ey + 1) < Character.getNumericValue(grid.charAt(2)) && !carry && !drop) {
			if (!flagSubmarine) {
				String state = Right(ex, ey, IMF, truck, lastIMF);
				if (!parent.expandedStates.containsValue(state)) {
					Node child_node = new Node(state, Operator.RIGHT, parent, depth, 0, GenerateNodeName());
					children.add(child_node);
				}
			} else if (flagSubmarine && ey < sy && !moved) {
				String state = Right(ex, ey, IMF, truck, lastIMF);
				if (!parent.expandedStates.containsValue(state)) {
					Node child_node = new Node(state, Operator.RIGHT, parent, depth, 0, GenerateNodeName());
					children.add(child_node);
				}
				moved = true;
			}
		}

		// from ArrayList to array
		Node[] child_nodes = new Node[children.size()];
		for (int i = 0; i < children.size(); i++) {
			child_nodes[i] = children.get(i);
			int parentCost = child_nodes[i].parent != null ? child_nodes[i].parent.pathCost : 0;
			child_nodes[i].setPathCost(parentCost + this.pathCost(child_nodes[i]));
		}

		return child_nodes;
	}

	@Override
	boolean goalTest(String state, MissionImpossible problem) {
		/**
		 * Our goal is to collect all IMF members, which will be achieved when the state
		 * contains no IMFs and truck capacity equal to original capacity and Ethan at
		 * the submarine. state should look like this: "1,2;0"
		 */
		if (state.split(";").length == 2 &&
				state.split(";")[1].equals(problem.initialState.split(";")[1])
				&& state.split(";")[0].equals(grid.split(";")[2])
				/* && state.split(";")[0].equals("1,1") */) {
			System.out.println("GOAL FOUND");
			return true;
		}
		return false;
	}

	public static int minimum(int x, int y) {
		if (x < y)
			return x;
		else
			return y;
	}

	public static int maximum(int x, int y) {
		if (x < y)
			return y;
		else
			return x;
	}

	static
	int pathCost(Node node) {
		// Ethan States
		// ------------
		// State 1. Ethan carrying IMF or Ethan dropping IMF
		// State 2. Ethan going to submarine (collected all IMF or full capacity)
		// State 3. Ethan looking for IMFs (capacity not full)

		// State 1:
		// --------
		if ((node.operator == Operator.CARRY && node.state.split(";").length != 2)
				|| (node.operator == Operator.DROP && node.state.split(";")[1].equals("0"))) {
			return 1;
		}
		// State 2:
		// --------
		if (node.state.split(";")[1].equals("0") || node.state.split(";").length == 2) {
			// 1.Extract location of Ethan in node
			int[] ethanArray = { Integer.parseInt(node.state.split(";")[0].split(",")[0]),
					Integer.parseInt(node.state.split(";")[0].split(",")[1]) };

			// 2.Extract location of submarine
			int[] submarineLocation = { Integer.parseInt(MissionImpossible.grid.split(";")[2].split(",")[0]),
					Integer.parseInt(MissionImpossible.grid.split(";")[2].split(",")[1]) };

			// 3.Find pathCost
			int pathCost = Math.abs(ethanArray[0] - submarineLocation[0])
					+ Math.abs(ethanArray[1] - submarineLocation[1]);
			return pathCost;

		}
		// State 3:
		// --------
		else {
			// pathCost = Sum of (HealthOfIMF*DistanceFromIMF) of all IMF members where
			// HealthOfIMF=100-Damage

			// 1.Extract location of Ethan in node
			int[] ethanArray = { Integer.parseInt(node.state.split(";")[0].split(",")[0]),
					Integer.parseInt(node.state.split(";")[0].split(",")[1]) };

			// 2.Extract location of IMFS in node
			String[] imfArray = node.state.split(";").length == 3 ? node.state.split(";")[2].split(",") : new String[0];

			// 3.Find pathCost
			int pathCost = 0;
			int maxImfDamage = 0;
			int maxImfDistance = 0;
			boolean isIMF = false;
			int IMFHealthVal = 0;
			for (int i = 0; i < imfArray.length / 2; i++) {
				int temp = maxImfDamage;
				int distance = Math.abs(ethanArray[0] - Integer.parseInt(imfArray[i * 2]))
						+ Math.abs(ethanArray[1] - Integer.parseInt(imfArray[i * 2 + 1]));
				int IMFHealth = node.depth * 2 + health.get(imfArray[i * 2] + "," + imfArray[i * 2 + 1]);
				if (distance == 0) {
					isIMF = true;
					IMFHealthVal = IMFHealth;
				}
				int health = 100 - IMFHealth > 0 ? 100 - IMFHealth : 1000;
				// pathCost=minimum(distance*health,pathCost);
				// pathCost += IMFHealth;
				maxImfDamage = maximum(IMFHealth, maxImfDamage);
				if (temp != maxImfDamage)
					maxImfDistance = distance;
			}
			pathCost = maxImfDamage * maxImfDistance;
			if (isIMF)
				return 100 - IMFHealthVal;
			return pathCost;
		}
	}

	// ----------------------------------------------------- Helper Methods
	// --------------------------------------------------------

	public static String GenerateNodeName() {

		String name = "";
		int quotient, remainder;
		quotient = namesCount;

		while (quotient >= 0) {
			remainder = quotient % 26;
			name = (char) (remainder + 65) + name;
			quotient = (int) Math.floor(quotient / 26) - 1;
		}
		MissionImpossible.namesCount++;
		return name;

	}

	public static String extractState(String grid) {
		String[] gridSeparated = grid.split(";", -1);

		// "ex,ey;truckCapacity;x1,y1...xi,yi;h1...hi"
		return "" + gridSeparated[1] + ";" + gridSeparated[5] + ";" + gridSeparated[3] + ";" + gridSeparated[4];

	}

	public static void printGrid(int gridX, int gridY, int ethanX, int ethanY, int submarineX, int submarineY,
			String imfLocations, String imfHealth) {
		String[] healthSeparated =imfHealth.length()>0? imfHealth.split(",", -1):null;
		String[] locSeparated =imfLocations.length()>0? imfLocations.split(",", -1):null;
		int i = 0;
		int j = 1;
		int h = 0;
		System.out.println();
		for (int x = 0; x < gridX; x++) {
			for (int y = 0; y < gridY; y++) {
				if (submarineX == x && submarineY == y) {
					System.out.print("S     ");
				}
				else if (ethanX == x && ethanY == y && imfLocations.length()>0  &&  healthSeparated!=null && h<healthSeparated.length
						&& Integer.parseInt(locSeparated[i]) == x && Integer.parseInt(locSeparated[j]) == y) {
					System.out.print("E F(" + healthSeparated[h] + ") ");
					i += 2;
					j = i + 1;
					h += 1;

				} 
				else if (ethanX == x && ethanY == y) {
					System.out.print("E     ");
				}


				else if (j < imfLocations.length() && healthSeparated!=null && h<healthSeparated.length 
						&& Integer.parseInt(locSeparated[i]) == x && Integer.parseInt(locSeparated[j]) == y) {
					System.out.print("F(" + healthSeparated[h] + ") ");
					i += 2;
					j = i + 1;
					h += 1;

				} else
					System.out.print("-     ");

			}
			System.out.println();
		}
	}

	public static int findIMFLoc(String[] imfCoordinates, String[] ethanLoc) {
		for (int i = 0; i < imfCoordinates.length - 1; i++) {
			if (ethanLoc[0] == imfCoordinates[i] && ethanLoc[2] == imfCoordinates[i + 1])
				return i;
		}
		return -1;
	}

	public static String updateState(String state) {
		
		//Location of Ethan
		int xNode = Integer.parseInt(state.split(";")[0].split(",")[0]);
		int yNode = Integer.parseInt(state.split(";")[0].split(",")[1]);
		
		String[] imfArray = state.split(";").length == 4 ? state.split(";")[2].split(",") : new String[0];
		// String[] healthArray = state.split(";").length == 4 ?
		// state.split(";")[3].split(",") : new String[0];
		String newIMF = "";
		// String newHealth = "";
		
		boolean isIMF = false;
		
		for (int j = 0; j < imfArray.length; j++) {
			if (Integer.parseInt(imfArray[j * 2]) == xNode && Integer.parseInt(imfArray[(j * 2) + 1]) == yNode)
				isIMF = true;
			else {
				newIMF += imfArray[j * 2] + "," + imfArray[(j * 2) + 1] + ",";
				// newHealth += healthArray[j] + ",";
			}
		}
		if (newIMF.length() > 0)
			newIMF = newIMF.substring(0, newIMF.length() - 1);
		// if (newHealth.length() > 0)
		// newHealth = newHealth.substring(0, newHealth.length() - 1);
		if (isIMF) {
			String newState = state.split(";")[0] + ";" + state.split(";")[1];
			if (newIMF != "")
				newState += ";" + newIMF + ";";
			state = newState;
		}
		return state;
	}

	// ---------------------------------------------------- Search Strategies
	// ------------------------------------------------------

	// Before adding nodes to the queue we should check whether they are
	// expanded before or not .. in the hash map

	public static ArrayList<Node> breadthFirst(ArrayList<Node> nodes, Node[] children) {

		// System.out.println("BF+ "+ children[0].state);

		for (int i = 0; i < children.length; i++) {
			nodes.add(children[i]);
		}
		// for(Node s : nodes) {
		// System.out.println(s.name);
		// }

		return nodes;
	}

	public static ArrayList<Node> depthFirst(ArrayList<Node> nodes, Node[] children) {

		for (int j =0; j < children.length; j++) {
				if (nodes.size() == 0) {
					nodes.add(children[j]);
				} else {
					nodes.add(j, children[j]);
					}
				}
		return nodes;
	}

	public static ArrayList<Node> iterativeDeeping(ArrayList<Node> nodes, Node[] children) {
		return null;
	}

	public static  ArrayList<Node> uniformCost(ArrayList<Node> nodes, Node[] children) {
		for (int j = 0; j < children.length; j++) {
		//	System.out.print(nodes.size());
			if (nodes.size() == 0) {
				nodes.add(children[j]);
			} else {
				for (int i = 0; i < nodes.size(); i++) {
					 if(i==nodes.size()-1) {
							nodes.add(i, children[j]);
							break;
						}
					 else if (pathCost(nodes.get(i)) > pathCost(children[j])) {
						nodes.add(i, children[j]);
						i=nodes.size();
					}
				}
			}
		}
		return nodes;
	}

	public static ArrayList<Node> greedyH1(ArrayList<Node> nodes, Node[] children, MissionImpossible problem) {
		int currentNodeCost=0;
		int nextNodeCost=0;
		for (int j = 0; j < children.length; j++) {
			//	System.out.print(nodes.size());
				if (nodes.size() == 0) {
					nodes.add(children[j]);
				} else {
					for (int i = 0; i < nodes.size(); i++) {
						
						currentNodeCost = admissbleHeuristic3(nodes.get(i), problem);
						nextNodeCost = admissbleHeuristic3(children[j], problem);
						 if(i==nodes.size()-1) {
								nodes.add(i, children[j]);
								break;
							}
						 else if (currentNodeCost  > nextNodeCost ) {
							nodes.add(i, children[j]);
							i=nodes.size();
						}
					}
				}
			}
		
			return nodes;
	}

	public static ArrayList<Node> greedyH2(ArrayList<Node> nodes, Node[] children, MissionImpossible problem) {
		int currentNodeCost=0;
		int nextNodeCost=0;
		for (int j = 0; j < children.length; j++) {
			//	System.out.print(nodes.size());
				if (nodes.size() == 0) {
					nodes.add(children[j]);
				} else {
					for (int i = 0; i < nodes.size(); i++) {
						
						currentNodeCost = admissbleHeuristic2(nodes.get(i), problem);
						nextNodeCost =  admissbleHeuristic2(children[j], problem);
						 if(i==nodes.size()-1) {
								nodes.add(i, children[j]);
								break;
							}
						 else if (currentNodeCost  > nextNodeCost ) {
							nodes.add(i, children[j]);
							i=nodes.size();
						}
					}
				}
			}
		
			return nodes;
	}

	public static ArrayList<Node> aStarH1(ArrayList<Node> nodes, Node[] children, MissionImpossible problem) {
		int currentNodeCost=0;
		int nextNodeCost=0;
		for (int j = 0; j < children.length; j++) {
			//	System.out.print(nodes.size());
				if (nodes.size() == 0) {
					nodes.add(children[j]);
				} else {
					for (int i = 0; i < nodes.size(); i++) {
						
						currentNodeCost = nodes.get(i).pathCost +admissbleHeuristic1(nodes.get(i), problem);
						nextNodeCost = children[j].pathCost + admissbleHeuristic1(children[j], problem);
						 if(i==nodes.size()-1) {
								nodes.add(i, children[j]);
								break;
							}
						 else if (currentNodeCost  > nextNodeCost ) {
							nodes.add(i, children[j]);
							i=nodes.size();
						}
					}
				}
			}
		
			return nodes;
		}

	public static ArrayList<Node> aStarH2(ArrayList<Node> nodes, Node[] children, MissionImpossible problem) {
		int currentNodeCost=0;
		int nextNodeCost=0;
		for (int j = 0; j < children.length; j++) {
			//	System.out.print(nodes.size());
				if (nodes.size() == 0) {
					nodes.add(children[j]);
				} else {
					for (int i = 0; i < nodes.size(); i++) {
						
						currentNodeCost = pathCost(nodes.get(i)) +admissbleHeuristic2(nodes.get(i), problem);
						nextNodeCost = pathCost(children[j]) + admissbleHeuristic2(children[j], problem);
						 if(i==nodes.size()-1) {
								nodes.add(i, children[j]);
								break;
							}
						 else if (currentNodeCost  > nextNodeCost ) {
							nodes.add(i, children[j]);
							i=nodes.size();
						}
					}
				}
			}
		
			return nodes;
	}


	
	static int admissbleHeuristic3(Node node, MissionImpossible problem) {
		int heuristicCost=-1;
		
		int[] ethanLoc = { Integer.parseInt(node.state.split(";")[0].split(",")[0]),
				Integer.parseInt(node.state.split(";")[0].split(",")[1]) };
		
		String[] healthXloc = getDamage(node);
		if(healthXloc.length>0 && healthXloc[0]!=null) {

		String imfHealth_=healthXloc[0];
		String imfLoc=healthXloc[1];
		

		String[] imfHealthSeparated = imfHealth_.split(",");
		
		String[] imfLoc_ = node.state.split(";").length == 3 ? node.state.split(";")[2].split(",") : new String[0];

		String[] stateSeparated = node.state.split(";", -1);
		String[] imfStateLocSeparated = stateSeparated[2].split(",");
		int truckCapacity= Integer.parseInt(node.state.split(";")[1]);
		int[] submarineLocation = { Integer.parseInt(MissionImpossible.grid.split(";")[2].split(",")[0]),
				Integer.parseInt(MissionImpossible.grid.split(";")[2].split(",")[1]) };
		//Check if goal reached
		if(problem.goalTest(node.state, problem)) return 0;
		
		//Check that there are not IMFs in the grid, then distance to submarine
		else if(imfStateLocSeparated.length==0) {
			heuristicCost = Math.abs(ethanLoc[0] - submarineLocation[0])
					+ Math.abs(ethanLoc[1] - submarineLocation[1]);
			}
		
		else {
		//There are IMFs in the grid .. go to the imf with the least health 
		boolean[] healthChecked = new boolean[imfHealthSeparated.length];
		

		
		int[] highestDamageXloc = maxDamage(imfHealthSeparated, healthChecked);
		int health = highestDamageXloc[0];
		int i = highestDamageXloc[1];
		healthChecked[i]=true;
		int distanceToIMF = Math.abs(ethanLoc[0] - Integer.parseInt(imfLoc_[i * 2]))
					+ Math.abs(ethanLoc[1] - Integer.parseInt(imfLoc_[i * 2 + 1]));
			
			heuristicCost=distanceToIMF;
		}
		}
		return heuristicCost;
		
		}
	
	private static boolean allTrue (boolean[] values) {
	    for (boolean value : values) {
	        if (!value)
	            return false;
	    }
	    return true;
	}
	
	public static int h3Cost(String[] imfHealthSeparated, int truckCapacity, int[] ethanLoc, 
			String[] imfLoc_, boolean[] healthChecked,String[] imfStateLocSeparated, int h3Cost, int collectedIMFs) {
		if(collectedIMFs==truckCapacity) {
			return h3Cost;
		}
		else {

			int[] highestDamageXloc = maxDamage(imfHealthSeparated, healthChecked);
			int health = highestDamageXloc[0];
			int i = highestDamageXloc[1];
			healthChecked = new boolean[imfHealthSeparated.length];
			healthChecked[i]=true;
			int distanceToIMF = Math.abs(ethanLoc[0] - Integer.parseInt(imfLoc_[i * 2]))
					+ Math.abs(ethanLoc[1] - Integer.parseInt(imfLoc_[i * 2 + 1]));
			if(allTrue(healthChecked)) { //must collect dead too
				collectedIMFs+=1;
				return h3Cost(imfHealthSeparated, truckCapacity, 
						ethanLoc, imfLoc_, healthChecked, imfStateLocSeparated,
						distanceToIMF+h3Cost, collectedIMFs);
			}
			
			//least health
			highestDamageXloc = maxDamage(imfHealthSeparated, healthChecked);
			health = highestDamageXloc[0];
			i = highestDamageXloc[1];
			healthChecked[i]=true; //max damage of member to be collected is known
			System.out.println("COLLECTED IMFS: "+collectedIMFs);

			if(health+2*distanceToIMF<100) { //if won't die until i reach him .. then i should go to him
				ethanLoc[0]=Integer.parseInt(imfLoc_[i * 2]);
				ethanLoc[1]=Integer.parseInt(imfLoc_[i * 2 + 1]);
				return h3Cost(imfHealthSeparated, truckCapacity, 
						ethanLoc, imfLoc_, healthChecked, imfStateLocSeparated,
						distanceToIMF+h3Cost, collectedIMFs+=1);
			}
			else { //if will die, i should skip him for now
				return h3Cost(imfHealthSeparated, truckCapacity, 
						ethanLoc, imfLoc_, healthChecked, imfStateLocSeparated,
						100+h3Cost, collectedIMFs);
			}
			
		}
	}
	
	
	static int admissbleHeuristic2(Node node, MissionImpossible problem) {
		
		int[] ethanLoc = { Integer.parseInt(node.state.split(";")[0].split(",")[0]),
				Integer.parseInt(node.state.split(";")[0].split(",")[1]) };
		
		

		int[] submarineLocation = { Integer.parseInt(MissionImpossible.grid.split(";")[2].split(",")[0]),
				Integer.parseInt(MissionImpossible.grid.split(";")[2].split(",")[1]) };

		return Math.abs(ethanLoc[0] - submarineLocation[0])
				+ Math.abs(ethanLoc[1] - submarineLocation[1]);
		
		}

	
static int admissbleHeuristic1(Node node, MissionImpossible problem) {
		
	//System.out.println();
	//System.out.println("NODE: "+node.name+"        ACTION DONE: "+ node.operator+ "       STATE: "+node.state);
	//System.out.println();
	String[] stateSeparated = node.state.split(";", -1);
		String[] imfStateLocSeparated = stateSeparated[2].split(",");
		int[] ethanLoc = { Integer.parseInt(node.state.split(";")[0].split(",")[0]),
				Integer.parseInt(node.state.split(";")[0].split(",")[1]) };
		
		
		String[] imfLoc;
		
		int distance=-1;
		int[] distancesToIMFs= new int[imfStateLocSeparated.length/2];
		
		int[] submarineLocation = { Integer.parseInt(MissionImpossible.grid.split(";")[2].split(",")[0]),
				Integer.parseInt(MissionImpossible.grid.split(";")[2].split(",")[1]) };

		// 3.Find pathCost
		int distanceToSubmarine = Math.abs(ethanLoc[0] - submarineLocation[0])
				+ Math.abs(ethanLoc[1] - submarineLocation[1]);
		

		//System.out.println("Distance to submarine after this action: "+distanceToSubmarine);
			//System.out.println("IMF number: "+imfStateLocSeparated.length/2);
			//Goal test has h(n)=0
			if(problem.goalTest(node.state, problem)) return 0;
			
			//States having IMFs number = 0 ---> nodes nearer to the submarine, h(n) = manh(ethanLoc, submarineLoc)
			else if(imfStateLocSeparated.length==0) {
				//System.out.println("H(n) is: distance to submarine");
				distance = distanceToSubmarine;
				}
	
			
			//Ethan and IMF in grid .. look for the nearest IMF
			else {
				//System.out.println("H(n) is: distance to IMF");

				imfLoc = node.state.split(";").length == 3 ? node.state.split(";")[2].split(",") : new String[0];
				for (int i =0; i<imfLoc.length/2; i++) {
					distance = Math.abs(ethanLoc[0] - Integer.parseInt(imfLoc[i * 2]))
							+ Math.abs(ethanLoc[1] - Integer.parseInt(imfLoc[i * 2 + 1]));
					
					distancesToIMFs[i]=distance;
				}
				
				//System.out.println("Min distance to imf "+min(distancesToIMFs));
				//System.out.println("Max distance to imf "+max(distancesToIMFs));
				
					distance = max(distancesToIMFs) + distanceToSubmarine;
				
			}
			//System.out.println("Admissible Heuristic for node "+node.name +" is : "+ distance);
			return distance;
	
		}
	

	public static int[] maxDamage(String [] imfHealth, boolean[] healthChecked) {
		int max = 0;
		int[] healthXloc= new int[2];
		int loc=0;
		for(int i=0; i<imfHealth.length; i++ ) {
			if(Integer.parseInt(imfHealth[i])>=max && healthChecked[i]!=true) {
				max = Integer.parseInt(imfHealth[i]);
				loc=i;
			}
		}

		healthXloc[0]=max;
		healthXloc[1]=loc;
		return healthXloc;
	}

	public static int max(int [] array) {
		int max = 0;
   
		for(int i=0; i<array.length; i++ ) {
			if(array[i]>max) {
				max = array[i];
			}
		}
		return max;
		}

	public static int min(int [] array) {
	      int min = array[0];
	     
	      for(int i=0; i<array.length; i++ ) {
	         if(array[i]<min) {
	            min = array[i];
	         }
	      }
	      return min;
	   }
	
	
	public static void main(String[] args) {
		String grid ="5,5;1,2;4,0;0,3,2,1,3,0,3,2,3,4,4,3;20,30,90,80,70,60;3";
		//"5,5;1,2;4,0;0,3,2,1,3,0,3,2,3,4,4,3;20,30,90,80,70,60;3";
		// String grid = "3,3;0,0;2,2;0,2,2,0;90,70;2";
		// String grid = "3,3;0,0;2,2;0,2,2,0,1,1;90,70,60;3";
		// String grid = "2,2;0,0;1,1;0,1,1,0;70,90;2";
		MissionImpossible m = new MissionImpossible(grid);
		m.solve(grid, "AS1", false);
	}

}
