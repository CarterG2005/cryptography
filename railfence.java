import java.util.*;
import java.io.*;

public class railfence {

	public static String encrypt(String message, int n, int r) {
		
		if (n == 1) return message;
		
		// Get the message's length
		int length = message.length();
		
		// Create a 2D grid, with n rows and columns based on message length
		char[][] grid = new char[n][length];
		
		// Initialize the grid with the null character \0
		// Loop through the rows
		for (int i = 0; i < n; i++) {
			// Loop through the columns
			for (int j = 0; j < length; j++) {
				// Set each cell to the null character 
				grid[i][j] = '\0';
			}
		}
		
		// Fill the grid diagnonally 
		
		// Start at the top row 
		int row = 0;
		// Move indicates which direction to move in. 1 means go down a row, -1 means go up a row.
		int move = 1;
		
		// Loop through each column
		for (int column = 0; column < length; column++) {
			// Take the next character in our message put it in the row and column we are in 
			grid[row][column] = message.charAt(column);
			// If we are at the top row, change direction and move down 1 row
			if (row == 0) {
				move = 1;
			// If we reach the bottom row, change direction and go up 1 row
			} else if (row == n - 1) {
				move = -1;
			}
			// Move to the next row in the current direction we are headed
			row += move;
		}
		
		// Map the row numbering based on the row we start in (r) 
		// Create an array that tells us which row in the 2d grid corresponds to each row in the encrypted output
		
		// Create an array size n to hold the mapped indexes 
		int[] map = new int[n];
		// Iterate through the map array
		for (int i = 0;  i < n; i++) {
			// Calculate the mapped row index for every row
			// use r-1 since our index begins at 0
			// + i to move down
			// % is used to wrap around to the starting row if the number is greater than the number of rows
			map[i] = (r - 1 + i) % n;
		}
		
		// Build encrypted string row by row using Mapped order
		String encryptedMessage = "";
		for (int i = 0; i < n; i++) {
			int mapIndex = map[i];  // the actual row to read
			// Loop through all columns in the mapped row
			for (int column = 0; column < length; column++) {
				// Null check. We are only interested in characters we placed in the grid
				if (grid[mapIndex][column] != '\0') {
					// Add character to the encrypted message
					encryptedMessage += grid[mapIndex][column];
				}
			}
		}
		
		return encryptedMessage;
	}
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter a positive integer, number of messages you wish to encrypt: ");
		int m = scanner.nextInt(); // number of messages
        scanner.nextLine(); // new line

        for (int t = 0; t < m; t++) {
			System.out.print("Enter two space-separated positive integers, number of rows & the starting row: ");
            int n = scanner.nextInt(); // number of rows
            int r = scanner.nextInt(); // starting row
            scanner.nextLine(); // newline
            
            System.out.print("Enter message: ");
            String message = scanner.nextLine().replaceAll(" ", ""); // read the message

            String encrypted = encrypt(message, n, r);
            System.out.println("Encrypted message: " + encrypted);
        }

        scanner.close();
    }
		
}
