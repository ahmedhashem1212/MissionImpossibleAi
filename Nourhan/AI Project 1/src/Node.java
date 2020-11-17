import java.util.HashMap;

public class Node{  
		
		/**
		 * Ethan position --> from genGrid
		 * Truck capacity --> how many members in truch so far
		 * Location of IMF members --> updated each time a member is picked up
		 * Damage of IMF members --> updated with each action Ethan does
		 */
	
        String state;
        Operator operator;
        Node parent;  
        int depth;
        int pathCost;
        String name;
    	 HashMap<Integer, String> expandedStates;

  
        public Node(String state, Operator operator, Node parent, int depth, int pathCost, String name){  
          this.state = state;  
          this.parent = parent;  
          this.operator = operator;
          this.depth = depth;
          this.pathCost = pathCost;
        this.name=name;
		this.expandedStates = new HashMap<Integer, String>();

          
        }  
      
        public void setState(String state)
        {
        	this.state = state;  
        }
        
        public void setPathCost(int pathCost)
        {
        	this.pathCost = pathCost;  
        }
      
}  