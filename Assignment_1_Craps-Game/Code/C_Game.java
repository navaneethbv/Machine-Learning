package utd.ml.assign1.craps;

public class C_Game 
{
	public static void main(String[] args) 
	{
		System.out.println("-----Let's Play the GAME of CRAPS-----");
		System.out.println("Starting Balance is  $1000");
		GameOfCrap goc = new GameOfCrap();
		goc.startGame();
	}
} 
