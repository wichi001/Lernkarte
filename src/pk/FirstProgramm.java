package pk;

public class FirstProgramm {
	
	/*
	    Diese Methode maleTreppe(int hoehe, int stufentiefe) gibt eine Treppe aus
	*/ 
	public void maleTreppe (int hoehe, int stufentiefe) {
		
		for (int i = 1; i <= hoehe; i++) {
			for (int j = 1; j <= hoehe - i; j++) {
				for (int k = 0; k < stufentiefe; k++) {
					System.out.print(" ");
				}
			}
			
			for (int j = 0; j < stufentiefe * i; j++) {
				System.out.print("*");
			}
			
			System.out.println();
		}
	}
	
	
	public static void main (String[] args) {
		FirstProgramm fp = new FirstProgramm();
		fp.maleTreppe(8, 3);
	}
}
