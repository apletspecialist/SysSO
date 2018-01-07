package sysos;
public class PCB {
	//To bym chcia³ ¿ebyœ zaimplementowa³, zeby proces przy tworzeniu wypelnial rozmiar programu//
	public int programSize; // rozmiar programu
	
	//Tego poni¿ej nie ruszaj//
	public class Pair {
		public boolean isInRam = false;
		public int inWhichFrame = -1;
	}

	public int swapFileBeginning; // gdize w pliku wymiany zaczyna sie porgram

	public Pair[] pageTable;

	public void createPageTable(int SIZE) { // ile bajtów zajmuje program
		pageTable = new Pair[(SIZE + 15) / 16];
		for (int i = 0; i < pageTable.length; i++) {
			pageTable[i] = new Pair();
		}
	}

	public int pageCheck(int what) {
		if (pageTable[what].isInRam) {
			return pageTable[what].inWhichFrame;
		} else {
			return -1;
		}
	}

	public void pageDisable(int what) {
		pageTable[what].isInRam = false;
		pageTable[what].inWhichFrame = -1;
	}

	public void pageEnable(int what, int where) {
		if (!pageTable[what].isInRam) {
			pageTable[what].isInRam = true;
			pageTable[what].inWhichFrame = where;
		} else
			System.out.println("STRONICA ZNAJDUJE SIE JUZ W RAMIE");
	}
}
