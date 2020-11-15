import java.util.ArrayList;
import java.util.StringTokenizer;

public class MissionImpossible extends searchProblem {

	public MissionImpossible(ArrayList<String> operators, Node intialState, ArrayList<Node> stateSpace, String goalTest,
			int pathCost) {
		super(operators, intialState, stateSpace, goalTest, pathCost);
		// TODO Auto-generated constructor stub
	}


	public static String genGrid() {
		
		String grid =""; // resulted grid
		int min =5; 
		int max =15;
		int m =(int) (Math.random() * (max - min + 1) + min); // width of the grid
		int n =(int) (Math.random() * (max - min + 1) + min); // height of the grid
//		int m =5;
//		int n=5;
		
		//2D array which represents a grid 
		String [][] arr = new String [m][n];
		
		
		min =0;
		max =m-1;
		int ex =(int) (Math.random() * (max - min + 1) + min); // x coordinate of Ethan
		
		min =0;
		max =n-1;
		int ey =(int) (Math.random() * (max - min + 1) + min); // y coordinate of Ethan
		
		min=1;
		max=3;
		int c =(int) (Math.random() * (max - min + 1) + min); // number that the truck can carry
		arr[ex][ey]="E("+c+")";
		
		
		min =0;
		max =m-1;
		int sx =(int) (Math.random() * (max - min + 1) + min); // x coordinate of submarine
		
		min =0;
		max =n-1;
		int sy =(int) (Math.random() * (max - min + 1) + min); // y coordinate of submarine
		
		//check unique position in the grid
		while(arr[sx][sy]!=null) {
			min =0;
			max =m-1;
			sx =(int) (Math.random() * (max - min + 1) + min); // x coordinate of submarine
			
			min =0;
			max =n-1;
			sy =(int) (Math.random() * (max - min + 1) + min); // y coordinate of submarine
			
		}
		
		arr[sx][sy]="S";
		
		
		min=5;
		max=10;
		int k =(int) (Math.random() * (max - min + 1) + min); // number of IMF members
		
		String IMF ="";
		String health ="";
		
		for (int i=1;i<=k;i++) {
			min =0;
			max =m-1;
			
			int x =(int) (Math.random() * (max - min + 1) + min); // x coordinate of ith member
			
			min =0;
			max =n-1;
			int y =(int) (Math.random() * (max - min + 1) + min); // y coordinate of ith member
			//check if it is unique 
			if(arr[x][y]!=null) {
				i--;
				continue;
			}
			min = 0;
			max =99;
			int h =(int) (Math.random() * (max - min + 1) + min); // start health of ith member
			
			IMF += x+","+y+",";
			health +=h+",";
			arr[x][y]="F("+h+")";
	     	
		}
		
			
		grid = m+","+n+";"+ex+","+ey+";"+sx+","+sy+";"+IMF.substring(0, IMF.length()-1)+";"+health.substring(0, health.length()-1)+";"+c;
		
		// viewing the grid
		for (int i = 0; i < arr.length; i++)
	        {
	            for (int j = 0; (arr[i] != null && j < arr[i].length); j++)
	                System.out.print(arr[i][j]!=null?(" "+arr[i][j] + " "):" _ ");
	 
	            System.out.println();
		
	}
		 return grid;
			
	}
	public static void main(String[]args) {
		System.out.println(genGrid());
		
	}
}
