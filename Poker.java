import java.util.*;
import java.io.*;
public class Poker
{
	public static String input = null;																						//to read input
	public static int p1Games = 0;																								//variable to store the games won by player 1
	public static int p2Games = 0;																								//variable to store the games won by player 2
	public static String [] t1Cards = new String [5];															//array to store player 1 cards
	public static String [] t2Cards = new String [5];															//array to store player 2 cards
	public static String [] forPair = new String [3];															//array to store player 1 card's pair, if any
	public static String [] forPairs = new String [3];														//array to store player 2 card's pair, if any
	public static int t1Result = 0;																								//variable to store the rank of player 1 cards
	public static int t2Result = 0;																								//variable to store the rank of player 2 cards
	public static int t1Highest = 0;																							//variable to store the highest card in player 1 cards
	public static int t2Highest = 0;																							//variable to store the highest card in player 1 cards
  //Boolean varaibles//
	public static boolean p1Cards;																								//boolean to indentify player 1 cards
	public static boolean invalidCard;																						//boolean to indentify invalid cards
	public static boolean sameSuit;													 											//boolean to identify same suit
	public static boolean inOrder;																								//boolean to identify order
	public static boolean royal;																									//boolean to identify royalflush
	public static boolean pair;																										//boolean to identify pair
	public static boolean threeOfAKind;																						//boolean to identify three of a kind
	public static boolean fourOfAKind;																						//boolean to identify four of a kind
	public static boolean doublePair;																							//boolean to identify double pair
  public static boolean highest;																								//boolean to identify heighest
//******************************************************************************//

public static void main(String []args)
{
	try
	{
		File pHands = new File("poker-hands.txt");																	//open file
	  Scanner myReader = new Scanner(pHands);																			//canner to read the file
	  while (myReader.hasNextLine())
		{
			input = myReader.nextLine();																							//read lines
			splitCards();																															//split cards for poker
			if(invalidCard == false)																									//if any invalid card is found, skip process
			{
				p1Cards = true;																													//set player 1 card identifier
				checkCombo(t1Cards);																										//check the combination for ranking
				p1Cards = false;																												//set player 1 card identifier
				checkCombo(t2Cards);																										//check the combination for ranking
				findHighest(t1Cards, t2Cards);																					//find higest card for each player
					if(t1Result == 1 && t2Result == 1)																		//if no combinations are found
					{
						if(t1Highest > t2Highest)																						//check the higest card
						{
							p1Games = p1Games + 1;
						}
						else
						{
							p2Games = p2Games + 1;
						}
					}
					else if(t1Result == 5 && t2Result == 5)																//if both player's cards are straight
					{
						if(t1Highest > t2Highest)																						//check the highest card
						{
							p1Games = p1Games + 1;
						}
						else
						{
							p2Games = p2Games + 1;
						}
					}
					else if(t1Result > t2Result)																					//if player one cards ranks higher than player two cards
					{
						p1Games = p1Games + 1;
					}
					else if(t2Result > t1Result)																					//if player one cards ranks lesser than player two cards
					{
						p2Games = p2Games + 1;
					}
					else if(t2Result == 2 && t1Result ==2 ||															//if two players are having pairs, check for highest pair
					 				t2Result == 3 && t1Result ==3 ||
									t2Result == 4 && t1Result ==4 )
					{
						boolean found = false;
						for(int i=0; i<forPair.length-1; i++)
						{																																		//check the value to find the highest
						if((Integer.parseInt(""+forPair[i].substring(0,forPair[i].length() - 1))) >
							(Integer.parseInt(""+forPairs[i].substring(0,forPairs[i].length() - 1))))
							{
								p1Games = p1Games + 1;
								found = true;
								i=forPair.length-1;																							//to break the look
							}
							else if (Integer.parseInt(""+forPairs[i].substring(0,forPairs[i].length() - 1)) >
										  (Integer.parseInt(""+forPair[i].substring(0,forPair[i].length() - 1))))
							{
								p2Games = p2Games + 1;
								found = true;
								i=forPair.length-1;
							}
						}
						if(found == false && t1Highest > t2Highest)													//if all the pairs are same, then check the highest card
						{
							p1Games = p1Games + 1;
						}
						else
						{
							p2Games = p2Games + 1;
						}
					}
				}
		}
		myReader.close();
	}catch (FileNotFoundException e)
	 {
      System.out.println("An error occurred.");
	 }
	System.out.println("Player 1: "+p1Games);
  System.out.println("Player 2: "+p2Games);
	}

//******************************************************************************//

public static void splitCards()
{
	invalidCard = false;
	String [] tCards = input.split(" ");                      										//Split the string containing cards and save in the array
	if(tCards.length == 10)																												//Check if there are 10 cards ( 5 each player) as per rule
	{
		convertCards(tCards);																												//Convert Characters to number for simplification
		for (int i=0; i<10; i++)
		{
			char suit = tCards[i].charAt(tCards[i].length() - 1);											//Temporary variable for storing Suit
			int number = Integer.parseInt(""+tCards[i].substring(0,tCards[i].length() - 1));
			if(suit == 'C' || suit == 'D' || suit == 'H' || suit == 'S') 							//Check if the suits are correct
			{
				if (number > 14 ||   																										//Check if the cards are more than Ace (the highest order)
						number < 2) 																												//Check if the cards are less than 2 (the lowest order)
					{
						invalidCard = true;																									//Invlaid card flag
					}
			}
			else
			{
				invalidCard = true;																											//Ivalid card flag if invalid suit is found
			}
		}
	}
	if(invalidCard == false)																											//If no invalid card flag is raised
	{
		t1Cards = Arrays.copyOfRange(tCards,0,5);			             								 	//Copy second five cards into P1's array
		arraySorter(t1Cards);																												//sort the array
		t2Cards = Arrays.copyOfRange(tCards,5,10);				       							      //Copy second five cards into P2's array
		arraySorter(t2Cards);																												//sort the array
	}
}
//******************************************************************************//
public static void arraySorter (String [] cards)
{
	String temp;
	for (int i = 0; i < t1Cards.length; i++)
			{
				for (int j = i + 1; j < t1Cards.length; j++)
				 {
					 if ((Integer.parseInt(""+(cards[i].substring
					     (0,cards[i].length()-1)))) > (Integer.parseInt
							 (""+(cards[j].substring(0,cards[j].length()-1)))))
							{
									temp = cards[i];
									cards[i] = cards[j];
									cards[j] = temp;
							}
					}
			}
}
//******************************************************************************//
public static void checkCombo(String [] cards)
{
	checkForSameSuit(cards);
	checkforOrder(cards);
	checkForRoyal(cards);
	checkForPair(cards);
	if(sameSuit == true && inOrder == true && royal == true)
	{
		t2Result=10;
	}
	else if (sameSuit == true && inOrder == true)
	{
		t2Result=9;
	}
	else if(sameSuit == true)
	{
		t2Result=6;
	}
	else if(inOrder == true)
	{
		t2Result=5;
	}
	else
	{
		if(fourOfAKind == true)
		{
			t2Result=8;
		}
		else if(threeOfAKind == true && pair == true)
		{
			t2Result=7;
		}
		else if (threeOfAKind == true)
		{
			t2Result=4;
		}
		else if(doublePair == true)
		{
			t2Result=3;
		}
		else if(pair == true && doublePair == false)
		{
			t2Result=2;
		}
		else
		{
			t2Result=1;
		}
	}
	if(p1Cards == true)
	{
		t1Result = t2Result;
	}
}
//******************************************************************************//
public static boolean checkForSameSuit(String [] tempCards)
{
	sameSuit = false;
	for (int i=1; i<tempCards.length; i++)
	{
		if(tempCards[i].substring(tempCards[i].length() -1).equals
		   (tempCards[0].substring(tempCards[i].length() -1)))											//Check if the Suits are same and the Flag is not set to flase
		{
			sameSuit=true;																														//If Yes, keep the flag at True
		}
		else
		{
			sameSuit=false;																														//If No, set the flag to False
		}
	}
	return sameSuit;																															//Return boolean
}
//******************************************************************************//
public static boolean checkforOrder(String [] tempCards)
{
	inOrder = false;
	for(int i=0; i<4; i++)
	{
		if((Integer.parseInt(""+tempCards[i+1].substring(0,tempCards[i+1].length() -1))) ==    //Check if the next number is consecutive
		 	 (Integer.parseInt(""+tempCards[i].substring(0,tempCards[i].length() -1))+1))        // and the Flag is not set to false
		{
			inOrder = true;																														//If Yes, keep the flag at True
			i = 4;
		}
		else
		{
			inOrder = false;																													//If No, set the flag to false
		}
	}
	return inOrder;																																//Return boolean
}
//******************************************************************************//
public static boolean checkForRoyal(String [] tempCards)
{
	royal = false;
	if((Integer.parseInt(""+tempCards[0].substring(0,tempCards[0].length() -1)) == 10) &&     //Check if the first is Ten
		 (Integer.parseInt(""+tempCards[4].substring(0,tempCards[4].length() -1)) == 14))				//And last card is Ace
	{
		royal=true;																																	//If Yes, keep the flag at True
	}
	else
	{
		royal = false;																															//If No, set the flag to No
	}
	return royal;																																	//return boolean
}
//****************************************************************//

public static void checkForPair(String [] tempCards)
{
  pair=false;
	threeOfAKind=false;
	fourOfAKind=false;
	doublePair=false;
	int count;																																		//counter to store the occurences
	for(int i=0; i<5; i++)
	{
		count = 0 ;
		for(int j=0; j<5; j++)
		{
			if((tempCards[j].substring(0,tempCards[j].length() - 1)).equals 					//Check the value for pair
				((tempCards[i].substring(0,tempCards[i].length() - 1))))
			{
				count = count+ 1;																												//If Yes, increase counter
			}
		}
		if((count-1) == 1 && pair == false)																					//If the count is 1, that is another card of same value
		{
			pair = true;																															//Then set true for Pair
			i=i+1;																																		//Increase the value of I to skip the recurrent process for pair
		}
		else if ((count -1) == 2)																										//If the count is 2, that is another 2 cards of same value
		{
			threeOfAKind = true;																											//Then set true for threeOfAKind
			i=i+2;																																		//Increase the value of I to skip the recurrent process
		}
		else if((count -1) == 3)																										//If the count is 3, that is another 3 cards of same value
		{
			fourOfAKind = true;																												//Then set true for fourOfAKind
			i=i+3;																																		//Increase the value of I to skip recurrent process
		}
		else if ((count-1) == 1 && pair == true)                     							  //If the pair is set and another pair is found
		{
			doublePair = true;																												//Then set true for doublePair
			i=i+1;																																		//Increase the value of I to skip recurrent process
		}
	}
	for(int i = 0; i<forPair.length; i++)																					//copy to array
	{
    if(forPair[i] == null)
    {
        forPair[i]=tempCards[i];
    }
	}
	if(!p1Cards == true)
	{
		forPairs = Arrays.copyOfRange(forPair,0,2);
	}
}
//******************************************************************************//
public static String[] convertCards (String [] tempCards)
{
	for(int i=0; i<10; i++ )
	{
 		if(tempCards[i].charAt(0) == 'T')																						//If "T", set value to 10
		{
			tempCards[i]=tempCards[i].replace("T","10");
		}
		else if(tempCards[i].charAt(0) == 'J')                       								//If "J", set value to 11
		{
			tempCards[i]=tempCards[i].replace("J","11");
		}
		else if(tempCards[i].charAt(0) == 'Q')                        							//If "Q", set value to 12
		{
			tempCards[i]=tempCards[i].replace("Q","12");
		}
		else if(tempCards[i].charAt(0) == 'K')                        							//If "K", set value to 13
		{
			tempCards[i]=tempCards[i].replace("K","13");
		}
		else  if(tempCards[i].charAt(0) =='A')                        							//If "A", set value to 14
		{
			tempCards[i]=tempCards[i].replace("A","14");
		}
		else if (Character.isLetter(tempCards[i].charAt(0)))
		{
			tempCards[i] = null;
		}
	}
	return tempCards;
}
//*****************************************************************//
public static void findHighest (String [] cards1, String[] cards2)
{
	for(int i=cards1.length-1; i>0; i--)
	{
		if(!(cards1[i].substring(0,cards1[i].length() -1)).equals(cards2[i].substring(0,cards2[i].length() -1)))
		{
			t1Highest = Integer.parseInt(""+cards1[i].substring(0,cards1[i].length() -1));
			t2Highest = Integer.parseInt(""+cards2[i].substring(0,cards2[i].length() -1));
			i=0;
		}
	}
}
}
