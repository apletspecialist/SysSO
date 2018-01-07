package sysos;
public class Tomek {

	public PCB[] testList = new PCB[12];

	Tomek() {
		testList[0] = new PCB();
		testList[1] = new PCB();
		testList[2] = new PCB();
		testList[3] = new PCB();
		testList[4] = new PCB();
		testList[5] = new PCB();
		testList[6] = new PCB();
		testList[7] = new PCB();
		testList[8] = new PCB();
		testList[9] = new PCB();
		testList[10] = new PCB();
		testList[11] = new PCB();

		testList[1].programSize = 246;

	}

	public PCB getPcb(int in) {
		return testList[in];
	}
}
