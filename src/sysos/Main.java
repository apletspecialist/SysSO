
public class Main {
	public static int OBECNY_PROCES = 1;

	public static Memory M = new Memory();
	public static Tomek T = new Tomek();

	public static void main(String[] args) {

		String test = new String("Pierwszy teges Testowy");
		M.allocateMemory(test, test.length());

		OBECNY_PROCES = 2;
		M.allocateMemory("Nowy program lolxD", 18);
		OBECNY_PROCES = 1;

		System.out.println(M.readUntilSpace(9));
		M.writeMemory(10, '&');
		System.out.println(M.readUntilSpace(9));

		System.out.println(M.readUntilSpace(15));

		OBECNY_PROCES = 2;
		System.out.println(M.readUntilSpace(6));

		OBECNY_PROCES = 3;
		M.allocateMemory("lol", 3);
		System.out.println(M.readUntilSpace(1));

		OBECNY_PROCES = 4;
		M.allocateMemory("seks", 6);
		System.out.println(M.readUntilSpace(0));

		OBECNY_PROCES = 5;
		M.allocateMemory("cywagapu", 9);
		System.out.println(M.readUntilSpace(2));

		OBECNY_PROCES = 7;
		M.allocateMemory("Przedo stat pro", 20);
		M.writeMemory(14, 'X');
		System.out.println(M.readUntilSpace(12));

		OBECNY_PROCES = 6;
		M.allocateMemory("Cokolwieeeeek", 17);
		System.out.println(M.readUntilSpace(9));

		
		
		OBECNY_PROCES = 9;
		M.allocateMemory("Przepelnienie ramu", 32);
		System.out.println(M.readUntilSpace(9));
		// OBECNY_PROCES = 10;
		// M.allocateMemory("To juz koniec ", 17);
		// System.out.println(M.readUntilSpace(9));

		// M.printFramesStatus();
		// M.printMemory();

	}
}
