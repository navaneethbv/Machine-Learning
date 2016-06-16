package utd.ml.assign1.craps;

import java.util.Random;
import java.util.Scanner;

public class GameOfCrap {

	public Scanner inputBet;
	public Random r;  
	//Start the game
	void startGame() 
	{
		int strategy = 1;
		while(strategy < 4) 
		{
			int startBalance = 1000;
			int currentBalance = startBalance;
			int count = 1;
			int betAmount = 100;
			int dice1Value, dice2Value,diceValue;
			do 
			{
				boolean win = false;
				System.out.println("Rolling dice ...........");
				System.out.println("Wager =================>"+betAmount);
				dice1Value = getdiceValue();
				dice2Value = getdiceValue();
				diceValue = add(dice1Value, dice2Value);
				System.out.println("Dice 1 = "+dice1Value+"   : Dice 2 = "+dice2Value);
				System.out.println("dice Value = "+diceValue);

				if(diceValue == 7 || diceValue == 11) 
				{
					System.out.println("You won the bet worth $"+betAmount);
					currentBalance = add(currentBalance, betAmount);
					win = true;
				}

				else if(diceValue == 2 || diceValue == 3 || diceValue == 12 ) 
				{
					System.out.println("You lost the bet worth $"+betAmount);
					currentBalance = substract(currentBalance, betAmount);
					win = false;
				} 

				else 
				{
					final int shootPoint = diceValue;
					int shootdice1, shootdice2, shootdicevalue;
					System.out.println("Shoot point set to "+shootPoint);

					shootdice1 = getdiceValue();
					shootdice2 = getdiceValue();
					shootdicevalue = add(shootdice1, shootdice2);
					System.out.println("Shoot Dice 1 = "+shootdice1+"   : Shoot Dice 2 = "+shootdice2);
					System.out.println("Shoot dice Value = "+shootdicevalue);

					while(shootdicevalue != shootPoint && shootdicevalue != 7) 
					{
						shootdice1 = getdiceValue();
						shootdice2 = getdiceValue();
						shootdicevalue = add(shootdice1, shootdice2);
						System.out.println("Shoot Dice 1 = "+shootdice1+"   : Shoot Dice 2 = "+shootdice2);
						System.out.println("Shoot dice Value = "+shootdicevalue);
					}

					if(shootdicevalue == shootPoint) 
					{
						System.out.println("You won the bet worth $"+betAmount);
						currentBalance = add(currentBalance, betAmount);
						win = true;
					}
					if(shootdicevalue == 7) 
					{
						System.out.println("You lost the bet worth $"+betAmount);
						currentBalance = substract(currentBalance, betAmount);
						win = false;
					}

				}

				if(strategy == 1)
					betAmount = 100;


				if(strategy == 2) {
					if(win) {
						betAmount = 100;
					} 
					if(!win) {
						betAmount = 2 * betAmount;
						if (betAmount > currentBalance) {
							betAmount = currentBalance;
						}
					}
				}
				
				if(strategy == 3) {
					if(!win) {
						betAmount = 100;
					} 
					if(win) {
						betAmount = 2 * betAmount;
						if (betAmount > currentBalance) {
							betAmount = currentBalance;
						}
					}
				}


				System.out.println("You current Balance is : $"+currentBalance);
				count++;
			}
			while(!(currentBalance <= 0 || count > 10 ));

			System.out.println("=========================This Round has Ended====================");
			System.out.println("Strategy being played : "+strategy);
			System.out.println("Ending Balance : $"+currentBalance);
			System.out.println("Total number of games played : "+(count-1));
			System.out.println("=================================================================");
			strategy++;
		}
	}



	// Get the value of a dice randomly
	int getdiceValue() {

		int min = 1;
		int max = 6;

		r = new Random();
		int val = r.nextInt(max - min + 1) + min;

		return val;
	}

	// Addition of any two values
	int add(int a, int b) {
		return a + b;
	}

	// Subtraction of any two values
	int substract(int a, int b) {
		return a - b;
	}

}
 
