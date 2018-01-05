
public class FFA { // Free Frames Array, Tablica wolnych ramek

	private class Pair {
		public boolean isFree = true;
		public int processID = -1;
	}

	public Pair[] ffa = new Pair[8];

	public FFA() {
		for (int i = 0; i < 8; i++) {
			ffa[i] = new Pair();
		}
	}

	public int checkFrame(int which) {
		if (ffa[which].isFree)
			return -1;
		else
			return ffa[which].processID;
	}

	public boolean isFree(int which) {
		return ffa[which].isFree;
	}

	public void occupyFrame(int which, int processID) {
		if (ffa[which].isFree) {
			ffa[which].isFree = false;
			ffa[which].processID = processID;
		} else
			System.out.println("RAMKA ZAJETA");
	}

	public void releaseFrame(int which) {
		ffa[which].isFree = true;
		ffa[which].processID = -1;
	}
}
