package sysos;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

public class Memory {
	private char[] ram; // tablica zawierajaca pamiec
	private File swap; // plik wymiany
	public FFA ffa; // tablica wolnych ramek
	public Stack<Integer> lruStack; // stos do LRU
	public int SWAP_END = 1;
	//////////////////////////////// KOSNSTRUKTOR //////////////////////////

	public Memory() { // konstruktor, inicjalizacja
		ram = new char[128];
		for (int i = 0; i < ram.length; i++)
			ram[i] = ' ';
		ffa = new FFA();
		swap = new File("swap.txt");
		wipeSwap();
		lruStack = new Stack<>();
	}

	////////////////////////////////// METODY /////////////////////////////////

	public char readMemory(int l_addr) { // zwraca char z pami�ci
		if (l_addr > getProgramSize()) {
			System.out.println("ADRES LOGICZNY WIEKSZY NIZ ROZMIAR PROGRAMU");
			return ' ';
		}
		int strona = l_addr / 16;
		int ramka = Main.T.getPcb(Main.OBECNY_PROCES).pageCheck(strona);

		if (ramka != -1) {
			updateStack(ramka);
			return ram[ramka * 16 + l_addr % 16];
		}

		if (isRamFull()) {
			int victim = getVictim();
			Main.T.getPcb(ffa.checkFrame(victim)).pageDisable(ffa.checkFrame(victim));
			putPageToSwap(victim, Main.T.getPcb(ffa.checkFrame(victim)).swapFileBeginning * 16 + strona);
			ffa.releaseFrame(victim);
		}

		int free = findFreeFrame();
		String toPut = getPageFromSwap(Main.T.getPcb(Main.OBECNY_PROCES).swapFileBeginning + strona);
		for (int i = free * 16, j = 0; j < 16 && j < toPut.length(); i++, j++) {
			ram[i] = toPut.charAt(j);
		}
		Main.T.getPcb(Main.OBECNY_PROCES).pageEnable(strona, free);
		ramka = Main.T.getPcb(Main.OBECNY_PROCES).pageCheck(strona);
		ffa.occupyFrame(ramka, Main.OBECNY_PROCES);
		updateStack(ramka);

		int doZwrotu = ramka * 16 + l_addr % 16;
		return ram[doZwrotu];
	}

	public void writeMemory(int l_addr, char value) {
		if (l_addr > getProgramSize()) {
			System.out.println("ADRES LOGICZNY WIEKSZY NIZ ROZMIAR PROGRAMU");
		}
		int strona = l_addr / 16;
		int ramka = Main.T.getPcb(Main.OBECNY_PROCES).pageCheck(strona);
		if (ramka != -1) {
			updateStack(ramka);
			ram[ramka * 16 + l_addr % 16] = value;
			return;
		}

		if (isRamFull()) {
			int victim = getVictim();
			Main.T.getPcb(ffa.checkFrame(victim)).pageDisable(ffa.checkFrame(victim));
			putPageToSwap(victim, Main.T.getPcb(ffa.checkFrame(victim)).swapFileBeginning * 16 + strona);
			ffa.releaseFrame(victim);
		}

		int free = findFreeFrame();
		String toPut = getPageFromSwap(Main.T.getPcb(Main.OBECNY_PROCES).swapFileBeginning + strona);
		for (int i = free * 16, j = 0; j < 16; i++, j++) {
			ram[i] = toPut.charAt(j);
		}
		Main.T.getPcb(Main.OBECNY_PROCES).pageEnable(strona, free);
		ramka = Main.T.getPcb(Main.OBECNY_PROCES).pageCheck(strona);
		ffa.occupyFrame(ramka, Main.OBECNY_PROCES);
		updateStack(ramka);

		ram[ramka * 16 + l_addr % 16] = value;
	}

	public void putPageToSwap(int victim, int place) { // umieszcza stron� w podanym miejscu w pliku wymiany
		String toPut = new String();
		for (int i = victim * 16; i < (victim * 16) + 16; i++) {
			toPut += ram[i];
		}
		String input = new String();
		try {
			Scanner scan = new Scanner(swap);
			input = scan.nextLine();
			scan.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String temp = new String();
		temp += input.substring(0, place);
		temp += toPut;
		temp += input.substring((place) + 16);
		// System.out.println("INPUT: " + input);
		// System.out.println("TEMP: " + temp);

		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(swap));
			out.write(temp);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void allocateMemory(String program, int size) { // Wpisuje program do pliku wymiany
		PCB pcb = Main.T.getPcb(Main.OBECNY_PROCES);
		pcb.createPageTable(size);
		pcb.swapFileBeginning = SWAP_END;
		pcb.programSize = size;
		SWAP_END += (size + 15) / 16;

		String temp = new String();
		try {
			Scanner scan = new Scanner(swap);

			temp = scan.nextLine();
			scan.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(swap));

			temp += program;
			int l = 16 - (program.length() % 16);
			if (16 - (size % 16) > l)
				l = 16 - (size % 16);
			
			for (int i = 0; i < l; i++) {
				temp += ' ';
			}
			out.write(temp);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	//////////////////////////////// GOTOWE /////////////////////////////
	public String getPageFromSwap(int pageNumber) { // zwraca stron� o danym numerze z pliku wymiany
		String out = new String();
		try {
			FileInputStream fis = new FileInputStream(swap);
			char temp;
			for (int i = 0; fis.available() > 0 && i < pageNumber * 16 + 16; i++) {
				temp = (char) fis.read();
				if (i >= pageNumber * 16)
					out += temp;
			}
			fis.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return out;
	}

	public int findFreeFrame() { // zwraca numer wolnej ramki
		for (int i = 0; i < 8; i++) {
			if (ffa.checkFrame(i) == -1)
				return i;
		}
		return -1;
	}

	public int getVictim() { // zwraca numer ofiary LRU
		return lruStack.firstElement();
	}

	public int getProgramSize() { // zwraca rozmiar obecnego programu
		return Main.T.getPcb(Main.OBECNY_PROCES).programSize;
	}

	public boolean isRamFull() { // sprawdza czy wszystkie ramki sa zajete
		for (int i = 0; i < 8; i++) {
			if (ffa.checkFrame(i) == -1) {
				return false;
			}
		}
		return true;
	}

	public String readUntilSpace(int l_addr) { // zwraca string do spacji
		String out = new String();
		for (int i = 0; l_addr + i < getProgramSize(); i++) {
			char temp = readMemory(l_addr + i);
			if (temp == ' ')
				break;
			out += temp;
		}
		return out;
	}

	public void updateStack(int nr) { // aktualizuje stos LRU podanym numerem ramki
		int position = lruStack.search(nr);
		if (position != -1) {
			lruStack.remove(lruStack.size() - position);
			lruStack.push(nr);
		} else
			lruStack.push(nr);
	}

	public void wipeSwap() { // zeruje plik wymiany i umieszcza kod bezczynnosci
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(swap));
			out.write("1&&&&&&&3&&&&&&0"); // tutaj umiescic kod bezczynnosci
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//////////////////////////// TESTOWE ////////////////////////////////////

	public void printStack() { // wyswietla status stosu LRU
		for (int i = 0; i < lruStack.size(); i++)
			System.out.println(lruStack.get(i));
	}

	public void printMemory() { // wyswietla zawartosc pamieci
		for (int i = 0; i < 128; i++) {
			System.out.println(i + ": " + ram[i]);
		}
	}

	public void printMemory(int addr, int size) { // wyswietla konkretny przedzial pamieci
		for (int i = addr; i < 128 && i < addr + size; i++) {
			System.out.println(i + ": " + ram[i]);
		}
	}

	public void printFramesStatus() { // wyswietla status wolnych/zajetych ramek
		for (int i = 0; i < 8; i++) {
			if (ffa.isFree(i))
				System.out.println("P" + i + ": free");
			else
				System.out.println("P" + i + ": occupied");
		}
	}
}
