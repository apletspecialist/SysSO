package sysos;

import java.util.ArrayList;

/*
 KODY BLEDOW:
0 - wszystko ok
1 - plik o podanej nazwie juz istnieje
2 - brak wolnego bloku
3 - plik nie znaleziony
metoda String readFile(String name) zwraca null, gdy wystapi blad 3
metoda String getFileData(String name) zwraca null, gdy wystapi blad 3
-1 - zwracany przez metode assignIndex(), gdy brak wolnych blokow
9 - blad nieokreslony
 */
class File {
	public String name;
	public int blockIndex;
	
	public File(String name_, int blockIndex_){
		name = name_;
		blockIndex = blockIndex_;
	}
}


class Catalog {
	ArrayList<File> catalog;
	
	public Catalog(){
		catalog = new ArrayList<>();
	}
}


public class FileSystem {
	private final int discSize = 1024, bitVectorSize = 32, nrOfBlocks = 32; // bo 32*32 = 1024
	final char emptySign = '^';
	char[] disc;
	boolean[] bitVector;
	Catalog root;

	public FileSystem() {
		disc = new char[discSize];
		for (int i = 0; i < discSize; i++) {
			disc[i] = emptySign;
		}
		bitVector = new boolean[bitVectorSize];
		root = new Catalog();
	}

	public int createFile(String name) {
		for (File f : root.catalog) {
			if (f.name == name) {
				return 1;
			}
		}
		int tempIndex = assignIndex();
		if (tempIndex != -1) {
			File f = new File(name, tempIndex);
			root.catalog.add(f);
		} else {
			return 2;
		}
		return 0;
	}

	public int writeFile(String name, String content) {
		for (File f : root.catalog) {
			if (f.name == name) {
				int neededBlocks = content.length() / nrOfBlocks + 1;
				int index = 0;
				for (int i = 0; i < neededBlocks; i++) {
					int tempIndex = assignIndex();
					if (tempIndex != -1) {
						for (int j = f.blockIndex; j < f.blockIndex + nrOfBlocks; j++) {
							if (disc[j] == emptySign) {
								disc[j] = (char) tempIndex;
								for (int k = tempIndex; k < tempIndex + nrOfBlocks; k++) {
									if (index < content.length()) {
										disc[k] = content.charAt(index++);
									}
								}
								break;
							}
						}
					} else {
						return 2;
					}
				}
				return 0;
			}
		}
		return 3;
	}

	public String readFile(String name) {
		for (File f : root.catalog) {
			if (f.name == name) {
				String data = "";
				for (int i = f.blockIndex; i < f.blockIndex + nrOfBlocks; i++) {
					if (disc[i] != emptySign) {
						int currentBlockNr = (int) disc[i];
						for (int j = currentBlockNr; j < currentBlockNr + nrOfBlocks; j++) {
							if (disc[j] != emptySign) {
								data += disc[j];
							}
						}
					}
				}
				return data;
			}
		}
		// System.out.println("ERROR - file " + name + " not found");
		return null;
	}

	public int deleteFile(String name) {
		for (File f : root.catalog) {
			if (f.name == name) {

				for (int i = f.blockIndex; i < f.blockIndex + nrOfBlocks; i++) {
					if (disc[i] != emptySign) {
						int currentBlockNr = (int) disc[i];
						for (int j = currentBlockNr; j < currentBlockNr + 32; j++) {
							disc[j] = emptySign;
						}
						bitVector[currentBlockNr / nrOfBlocks] = false;
						disc[i] = emptySign;
					}
				}
				bitVector[f.blockIndex / nrOfBlocks] = false;
				root.catalog.remove(f);
				return 0;
			}
			return 3;
		}
		return 9;
	}

	public String getFileData(String name) {
		for (File f : root.catalog) {
			if (f.name == name) {
				return f.name + " " + f.blockIndex;
			}
		}
		// System.out.println("ERROR - file " + name + " not found");
		return null;
	}

	private int assignIndex() {
		for (int i = 0; i < bitVectorSize; i++) {
			if (!bitVector[i]) {
				bitVector[i] = true;
				return i * nrOfBlocks;
			}
		}
		return -1;
	}

	public void printDisc() {
		for (int i = 0; i < disc.length; i++) {
			if ((i + 1) % 128 == 0) {
				System.out.println(disc[i]);
			} else {
				System.out.print(disc[i]);
			}
		}
	}
}
