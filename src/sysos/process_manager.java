package sysos;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

public class process_manager {

    //protezy
    String code;
    int a, b, c, d, co;

    boolean free_m(String name) {
        boolean success = true;
        return success;
    }

    boolean reserve_m(String file, String name, String c) {
        boolean success = true;
        if (c.isEmpty()) {
            code = "old code";
        } else {
            code = c;
        }
        return success;
    }
    //globalne
    public process INIT, pip;
    public int last_PID = 1;

    public class exit {

        public int who, res;
    }

    public class wait {

        public int who, for_who;
    }

    public ArrayList<exit> ex = new ArrayList<exit>();
    public ArrayList<wait> wa = new ArrayList<wait>();
    public ArrayList<process> ready = new ArrayList<process>();

    //typ enum stanu procesu
    public enum status {
        NEW, ACTIVE, WAITING, READY, TERMINATED, ZOMBIE, INIT
    }

    public process find(int pid) {
        if (pid == 0) {
            return INIT;
        }
        process p = INIT;
        while (p.next != null) {
            p = p.next;
            if (p.PID == pid) {
                return p;
            }
        }
        System.out.println("Nie ma procesu o podanym PID!");
        return null;
    }

    public ArrayList<process> ready_processes() {

        ArrayList<process> x = new ArrayList<process>();
        for (wait i : wa) {
            ready.add(find(i.who));
        }
        return x;
    }

    //klasa zagnieżdżona procesu
    public class process {

        //wskaźniki na procesy pokrewne
        public process father, big_bro, little_bro, child;

        //ID
        public int PPID;
        public String name;

        //szeregowanie
        public status s;
        public int pri, cpu;
        public Integer usrpri, PID;
        public int id, move;
        public final Queue<Character> IO = new LinkedList<>();
        public boolean input;
        public boolean output;
        public boolean res_flag;
        public process previous, next;

        //kontekst procesu
        public int A, B, C, D, counter;
        
        //funkcja zapisu stringa do kolejki charów;komunikacja procesów
        public void write_to_process(String buffor)
        {
            int i=buffor.length();
            for(int x=0;x<i;x++)
            IO.offer(buffor.charAt(1));
        }
        
        public process(String n) {

            this.name = n;
            this.father = null;
            this.big_bro = null;
            this.little_bro = null;
            this.child = null;
            this.PID = 0;
            this.PPID = 0;
            this.s = status.NEW;
            this.pri = 0;
            this.cpu = 0;
            this.previous = null;
            this.next = null;
            this.A = 0;
            this.B = 0;
            this.C = 0;
            this.D = 0;
            this.counter = 0;
        }

        public void change_process_state(status status) {
            this.s = status;
        }

        public int free_PID() {
            if (!(last_PID >= 1 && last_PID <= 0xffff8000)) {
                last_PID = 1;
            }
            process p = INIT;
            while (p.next != null) {
                p = p.next;
            }
            last_PID = p.PID;
            return (last_PID + 1);
        }

        public void read_context() {
            this.A = a;
            this.B = b;
            this.C = c;
            this.D = d;
            this.counter = co;
        }

        public void init() {
            INIT = new process("INIT");
            INIT.s = status.INIT;
            INIT.PID = 0;
            INIT.PPID = 0;
            INIT.father = null;
            INIT.child = null;
            INIT.little_bro = null;
            INIT.big_bro = null;
            INIT.previous = null;
            INIT.next = null;
        }

        public int fork() {
            //utworzenie nowego procesu i nadanie mu nazwy
            String n_name = this.name + 'c';
            process p = new process(n_name);
            p.PID = free_PID();
            //pamięć
            if (reserve_m(this.PID, p.PID, "") != -1) {
                p.s = status.READY;
                Random gen = new Random();
                int i = gen.nextInt(127) + 1;
                p.A = this.A;
                p.B = this.B;
                p.C = this.C;
                p.D = this.D;
                p.counter = this.counter;
                p.cpu = 0;
                p.pri = i;
                p.PPID = this.PID;
                p.father = this;
                //rodzina
                if (this.child != null) {
                    this.child = p;
                } else {
                    process p1 = this.child;
                    while (p1.little_bro != null) {
                        p1 = p1.little_bro;
                    }
                    p1.little_bro = p;
                    p.big_bro = p1;
                }
                //dodanie do listy
                process p2 = this;
                while (p2.next != null) {
                    p2 = p2.next;
                }
                p2.next = p;
                p.next = null;
                p.previous = p2;
                //jeśli proces został poprawnie utworzony
                System.out.println("Utworzono proces potomny o PID: " + p.PID);
                show_process(p.PID);
                return p.PID;
            } //jeśli nie został
            else {
                System.out.println("Nie utworzono procesu potomnego!");

                return -1;
            }
        }

        public boolean exec(String name, String name1, String path) {
            //sprawdzenie pamięci
            if (reserve_m(this.PID, this.PID, path)) {
                //reset składowych
                this.A = 0;
                this.B = 0;
                this.C = 0;
                this.D = 0;
                this.counter = 0;
                this.s = status.READY;
                System.out.println("Proces o PID: " + this.PID + " otrzymał nowy kod do wykonania.");
                return true;
            } else {
                return false;
            }
        }

        public boolean wait_PID() {
            int temp = -1;
            exit e = null;
            if (this.s == status.ZOMBIE) {
                System.out.println("Proces z PID: " + this.PID + " nie istnieje, więc nie można wywołać tej metody na jego dziecku.");
            } else {
                if (this.child != null) {
                    //sprawdzenie czy proces jest na liście zakończonych
                    for (int i = 0; i < ex.size(); i++) {
                        e = ex.get(i);
                        if (e.who == this.child.PID) {
                            temp = i;
                        }
                    }
                    //jeśli jest
                    if (temp >= 0) {
                        e.res = 1;
                        //usunięcie go z listy
                        process p = INIT;
                        while (p.next != null) {
                            p = p.next;
                            if (p.PID == this.child.PID) {
                                System.out.println(p.PID);
                                process p1 = p.previous;
                                if (p.next != null) {
                                    process p2 = p.next;
                                    System.out.println(p1.PID);
                                    System.out.println(p2.PID);
                                    p1.next = p2;
                                    p2.previous = p1;
                                } else {
                                    p1.next = null;
                                }
                                p.previous = null;
                                p.next = null;
                                for (int i = 0; i < ex.size(); i++) {
                                    if (ex.get(i).who == this.child.PID) {
                                        ex.remove(i);
                                        return true;
                                    }
                                }
                            }
                        }
                        //jeśli nie ma
                    } else {
                        if (this.s == status.ACTIVE) {
                            //read_context();
                        }
                        if (this.PID != 0) {
                            this.s = status.WAITING;
                        }
                        wait w = null;
                        w.who = this.PID;
                        w.for_who = this.child.PID;
                        wa.add(w);
                        return false;
                    }
                }
            }
            //NWM MAX
            return false;
        }
        //tu

        public boolean exit(int stat) {
            boolean del = false;
            int temp = -1;
            wait w = null;
            for (int i = 0; i < wa.size(); i++) {
                w = wa.get(i);
                if (w.for_who == this.PID) {
                    temp = i;
                }
            }
            if (temp >= 0) {
                boolean only = true;
                for (int i = 0; i < wa.size(); i++) {
                    wa.set(i, w);
                    if (w.who == this.PPID && w.for_who != this.PID) {
                        only = false;
                    }
                }
                if (only == false) {
                    if (free_m(this.PID)) {
                        for (int i = 0; i < wa.size(); i++) {
                            if (wa.get(i).for_who == this.PID) {
                                wa.remove(i);
                            }
                        }
                        this.s = status.TERMINATED;
                        process p1 = this.next;
                        process p2 = this.previous;
                        p1.previous = p2;
                        p2.next = p1;
                        this.previous = null;
                        this.next = null;
                        p1 = p2 = null;
                        if (this.child != null) {
                            process p = this.child;
                            p.PPID = 1;
                            if (INIT.child != null) {
                                process p4 = INIT.child;
                                while (p4.little_bro != null) {
                                    p4 = p4.little_bro;
                                }
                                p4.little_bro = p;
                                p.big_bro = p4;
                                if (this.big_bro != null) {
                                    process p3 = this.big_bro;
                                    p3.little_bro = this.little_bro;
                                }
                                if (this.little_bro != null) {
                                    process p3 = this.little_bro;
                                    p3.big_bro = this.big_bro;
                                }
                            }
                            while (p.little_bro != null) {
                                p = p.little_bro;
                                p.PPID = 1;
                            }
                        }
                        System.out.println("Usunięto proces o PID: " + this.PID + ".");
                        //FINALIZE
                        del = true;
                        return del;
                    } else {
                        System.out.println("Błąd zwalniania pamięci!");
                    }
                }
            } else {
                if (free_m(this.PID)) {
                    this.s = status.ZOMBIE;
                    if (this.child != null) {
                        process p = this.child;
                        p.PPID = 1;
                        while (p.little_bro != null) {
                            p = p.little_bro;
                            p.PPID = 1;
                        }
                        p = this.child;
                        if (INIT.child != null) {
                            process p2 = INIT.child;
                            while (p2.little_bro != null) {
                                p2 = p2.little_bro;
                            }
                            p2.little_bro = p;
                            p.big_bro = p2;
                            if (this.big_bro != null) {
                                process p3 = this.big_bro;
                                p3.little_bro = this.little_bro;
                            }
                        }
                    }
                    exit e = null;
                    e.who = this.PID;
                    e.res = stat;
                    ex.add(e);
                    System.out.println("Na procesie nie wykonano jeszcze metody wait_PID, więc został dodany do listy procesów ZOMBIE.");
                    del = true;
                    return del;
                } else {
                    System.out.println("Na procesie nie wykonano metody wait_PID, jednak wystąpił błąd pamięci!");
                }
            }
            //NWM MAX
            return false;
        }

        public boolean kill(int pid) {
            if (pid == 0) {
                System.out.println("Ta operacja spowoduje zamknięcie systemu!\nCzy na pewno chcesz ją wykonać?\n1- Tak/0 - Nie");
                int e;
                Scanner s = new Scanner(System.in);
                e = s.nextInt();
                if (c == 1) {
                    System.out.println("Zamykanie systemu...");
                    System.exit(0);
                }
                if (c == 0) {
                    System.out.println("Anulowano.");
                } else {
                    System.out.println("Wprowadzono złe dane!");
                }
            }
            process p = INIT;
            boolean is = false;
            while (p.next != null) {
                p = p.next;
                if (p.PID == pid) {
                    if (p.s == status.ZOMBIE) {
                        System.out.println("Ten proces jest w stanie ZOMBIE, więc metoda kill nie może zostać użyta.");
                    }
                    is = true;
                    break;
                }
            }

            if (is == true) {
                boolean del = false;
                int temp = -1;
                wait w = null;
                for (int i = 0; i < wa.size(); i++) {
                    w = wa.get(i);
                    if (w.for_who == p.PID) {
                        temp = i;
                    }
                }
                if (temp >= 0) {
                    boolean only = true;
                    for (int i = 0; i < wa.size(); i++) {
                        wa.set(i, w);
                        if (w.who == p.PPID && w.for_who != p.PID) {
                            only = false;
                        }
                    }
                    if (only == false) {
                        if (free_m(p.PID)) {
                            for (int i = 0; i < wa.size(); i++) {
                                if (wa.get(i).for_who == p.PID) {
                                    wa.remove(i);
                                }
                            }
                            p.s = status.TERMINATED;
                            process p2 = p.previous;
                            if (p.next != null) {
                                process p1 = p.next;
                                p1.previous = p2;
                                p2.next = p1;
                            } else {
                                p2.next = null;
                            }
                            p.previous = null;
                            p.next = null;

                            if (p.child != null) {
                                process t = p.child;
                                t.PPID = 1;

                                if (INIT.child != null) {
                                    process p4 = INIT.child;
                                    while (p4.little_bro != null) {
                                        p4 = p4.little_bro;
                                    }
                                    p4.little_bro = t;
                                    t.big_bro = p4;

                                    if (p.big_bro != null) {
                                        process p3 = p.big_bro;
                                        p3.little_bro = p.little_bro;
                                    }
                                    if (p.little_bro != null) {
                                        process p3 = p.little_bro;
                                        p3.big_bro = p.big_bro;
                                    }
                                }

                                while (t.little_bro != null) {
                                    t = t.little_bro;
                                    t.PPID = 1;
                                }
                            }
                            System.out.println("Usunięto proces o PID: " + p.PID + ".");
                            //FINALIZE
                            del = true;
                            return del;
                        } else {
                            System.out.println("Błąd zwalniania pamięci!");
                        }
                    } else {
                        if (free_m(p.PID)) {
                            for (int i = 0; i < wa.size(); i++) {
                                if (wa.get(i).for_who == p.PID) {
                                    wa.remove(i);
                                    break;
                                }
                            }

                            p.s = status.TERMINATED;
                            p.father.s = status.READY;

                            process p2 = p.previous;
                            if (p.next != null) {
                                process p1 = p.next;
                                p1.previous = p2;
                                p2.next = p1;
                            } else {
                                p2.next = null;
                            }
                            p.previous = null;
                            p.next = null;

                            if (p.child != null) {
                                process t = p.child;
                                t.PPID = 1;

                                if (INIT.child != null) {
                                    p2 = INIT.child;
                                    while (p2.little_bro != null) {
                                        p2 = p2.little_bro;
                                    }
                                    p2.little_bro = t;
                                    t.big_bro = p2;

                                    if (p.big_bro != null) {
                                        process p3 = p.big_bro;
                                        p3.little_bro = p.little_bro;
                                    }
                                    if (p.little_bro != null) {
                                        process p3 = p.little_bro;
                                        p3.big_bro = p.big_bro;
                                    }
                                }

                                while (t.little_bro != null) {
                                    t = t.little_bro;
                                    t.PPID = 1;
                                }
                            }

                            System.out.println("Proces o PID: " + p.PID + "został usunięty.");
                            //FINALIZE
                            del = true;
                            return del;
                        } else {
                            System.out.println("Błąd zwalniania pamięci!");
                        }
                    }
                } else {
                    if (free_m(p.PID)) {

                        p.s = status.TERMINATED;
                        process z = p.next;
                        process v = p.previous;
                        z.previous = v;
                        v.next = z;
                        p.next = null;
                        p.previous = null;
                        if (p.child != null) {
                            process t = p.child;
                            t.PPID = 1;

                            if (INIT.child != null) {
                                process p2 = INIT.child;
                                while (p2.little_bro != null) {
                                    p2 = p2.little_bro;
                                }
                                p2.little_bro = t;
                                t.big_bro = p2;

                                if (p.big_bro != null) {
                                    process p3 = p.big_bro;
                                    p3.little_bro = p.little_bro;
                                }
                                if (p.little_bro != null) {
                                    process p3 = p.little_bro;
                                    p3.big_bro = p.big_bro;
                                }
                            }

                            while (t.little_bro != null) {
                                t = t.little_bro;
                                t.PPID = 1;
                            }
                        }

                        exit e = null;
                        e.who = p.PID;
                        e.res = 0;
                        ex.add(e);
                        System.out.println("Na procesie nie wykonano jeszcze metody wait_PID, więc został dodany do listy procesów ZOMBIE.");
                        del = true;
                        return del;
                    } else {
                        System.out.println("Na procesie nie wykonano metody wait_PID, jednak wystąpił błąd pamięci!");
                    }
                }

            }
            if (is == false) {
                System.out.println("Nie istnieje taki proces.");
            }
            //NWM MAX
            return false;
        }

        public process find_process(int pid) {
            if (pid == 0) {
                return INIT;
            }
            process p = INIT;
            while (p.next != null) {
                p = p.next;
                if (p.PID == pid) {
                    return p;
                }
            }
            System.out.println("Nie ma procesu o podanym PID!");
            return null;
        }

        public void show_list() {
            process i = INIT;
            System.out.println("PID/PPID/STATUS\n" + i.PID + "/" + i.PPID + "/" + i.s);
            while (i.next != null) {
                i = i.next;
                System.out.println(i.PID + "/" + i.PPID + "/" + i.s);
            }
        }

        public void show_process(int pid) {
            process p1 = null;
            process p = null;
            /*if (p1 == find_process(pid)) {*/
            if (find_process(pid) != null) {
                p = find_process(pid);
                p1 = find_process(pid);
                System.out.println("PID: " + p1.PID + "\nPPID: " + p1.PPID);

                System.out.println("Status: " + p1.s + "Priorytet: " + p1.pri + "\nRejestr A: " + p1.A
                        + "\nRejestr B: " + p1.B + "\nRejestr C: " + p1.C + "\nLicznik: " + p1.counter);

                System.out.println("Rodzina:");
                if (p1.child != null) {
                    System.out.println("PID dziecka: " + p1.child.PID);
                    p = p1.child;
                    while (p.little_bro != null) {
                        p = p.little_bro;
                        System.out.println(p.PID);
                    }
                } else {
                    System.out.println("Proces nie ma dziecka.");
                }

                if (p1.little_bro != null) {
                    System.out.println("PID młodszego brata: " + p1.little_bro.PID);
                } else {
                    System.out.println("Proces nie ma młodszego brata.");
                }
                if (p1.big_bro != null) {
                    System.out.println("PID starszego brata: " + p1.big_bro.PID);
                } else {
                    System.out.println("Proces nie ma starszego brata.");
                }

            } else {
                System.out.println("Nie ma procesu o podanym PID!");
            }
        }

        public void show_waiting() {
            for (int i = 0; i < wa.size(); i++) {
                System.out.println(wa.get(i).who + " - " + wa.get(i).for_who);
            }
        }

        public void show_zombie() {
            for (int i = 0; i < ex.size(); i++) {
                System.out.println(ex.get(i).who + " - " + ex.get(i).res);
            }
        }

        ////////////// TUTAJ_PROSZE_NIE_RUSZAC_~FILIP////////////////
        public class Pair {

            public boolean isInRam = false;
            public int inWhichFrame = -1;
        }

        public int swapFileBeginning; // gdize w pliku wymiany zaczyna sie porgram
        public Pair[] pageTable;

        public void createPageTable(int SIZE) {
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

            for (int i = 0; i < pageTable.length; i++) {
                if (pageTable[i].inWhichFrame == what) {
                    pageTable[i].isInRam = false;
                    pageTable[i].inWhichFrame = -1;
                }
            }

        }

        public void pageEnable(int what, int where) {
            if (!pageTable[what].isInRam) {
                pageTable[what].isInRam = true;
                pageTable[what].inWhichFrame = where;
            } else //	System.out.println("STRONICA ZNAJDUJE SIE JUZ W RAMIE");
            ///////////////////////TUTAJ_JUZ_SPOKO_MOZNA/////////////////////////
            {

            }
        }

    }
}
