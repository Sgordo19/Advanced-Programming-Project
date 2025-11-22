package Project;

import java.util.Scanner;

public class MenVal 
{
	public static int readInt(Scanner scanner) 
	{
		while(true)
		{
			String line = scanner.nextLine();
			try 
			{
				return Integer.parseInt(line.trim());
			} catch (NumberFormatException ex)
			{
				System.out.println("Please enter a valid number:");			}
		}
	}
	
	public static double readDouble(Scanner scanner) 
	{
        while (true) 
        {
            String line = scanner.nextLine();
            try 
            {
                return Double.parseDouble(line.trim());
            } catch (NumberFormatException ex) 
            {
                System.out.print("Please enter a valid amount: ");
            }
        }
    }
}


