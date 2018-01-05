package scheduler;

import java.util.ArrayList;
import java.util.Queue;

public class scheduler {
	public int readyp;
	public scheduler() {
		for(Boolean x:whichqs) {
			x=false;
		}
		readyp=0;
	}
	private ArrayList<ArrayList<PCB>> qs = new ArrayList<ArrayList<PCB>>(128);
	private ArrayList<Boolean> whichqs = new ArrayList<Boolean>(128);
	public PCB runningProcess;
	
	public void add_to_q(PCB x) {
		if(x.usrpri==NULL) {
			x.usrpri=x.pri;
		}
		qs.get(x.usrpri).add(x);
		whichqs.set(x.usrpri, true);
	}
	
	private void divide_cpu() {
		for(int i=0; i<128;i++) {
			for(int j=0;j<qs.get(i).size()-1;j++) {
				if(qs.get(i).get(j).cpu>0)
				qs.get(i).get(j).cpu /= 2;
			}
		}
	}
	
	private void change_q(PCB x) {
		x.usrpri=x.pri+(x.cpu/2);
		qs.get(x.usrpri).add(x);
		whichqs.set(x.usrpri, true);
	}
	
	private void runProcess(procesy p) {
		ArrayList<PCB> ready = p.ready_processes();				//dodaje do kolejki gotowe procesy
		for(;readyp < ready.size()-1;readyp++) {
			add_to_q(ready.get(readyp));
		}
		
		int i=0;
		while(whichqs.get(i)==false) {							//szuka kolejki o najnizszym priorytecie
			if(whichqs.get(i)==true) break;
			i++;
			if(i==127) return;
		}
																//ustawia pierwszy proces o najnizszym priorytecie jako running
		PCB x=qs.get(i).get(0);
		runningProcess=x;
		runningProcess.change_process_state(Running);
		qs.get(i).remove(0);		
	}

	private void check(PCB actual, procesy p) {
		actual.Change_process_state(Ready);						
		if(runningProcess.PID == NULL) {						//jesli zaden proces nie jest running wchodzi do runProcess gdzie wlacza sie proces z najnizszym priorytetem
			runProcess(p);										//aktualny jako running
			actual=runningProcess;
			return;
		}
		if(runningProcess.PID != NULL) {						//running jako aktualny, 
			runningProcess=actual;
			divide_cpu();
			change_q(runningProcess);
			runProcess(p);
			return;
		}
		else {
			actual=runningProcess;
			return;
		}
	}
}
