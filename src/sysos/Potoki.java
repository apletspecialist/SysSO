package sysos;

import java.util.PriorityQueue;
import java.util.LinkedList;
import java.util.Queue;
import sysos.process_manager.process;

/**
 *
 * @author boromir
 */
public class Potoki {

    /**
     * @param args the command line arguments
     */
    //struktura danych
    Queue<Character> myQueue = new LinkedList<Character>();
    int qfreespace = 256;//max rozmiar na dane
    int readbytes = 0;//zapisane bity
    int writebytes = 0;//odczytane bity
    int open = 0;//czy potok jest wykorzystywany przez potok

//funkcje odczytu z potoku
    static public int read(process p) {
        int index=p.next.des;
        if (plik1.tab[index].myQueue.peek() == null) {
            return 0;
        } else {
            while (plik1.tab[index].myQueue.peek() != null) {
                p.next.IO.offer(plik1.tab[index].myQueue.poll());
                plik1.tab[index].readbytes++;
                plik1.tab[index].qfreespace++;
            }
            return 1;
        }

    }

    //funcja zapisu do potoku, zwraca 0 dla błędu 1 dla zapisania wszystkich info,2 dla przepałnienia 
   static public int write(process p) {
       Character znak ;//buffor znaku
       int index=p.des;
        if (plik1.tab[index].qfreespace == 0) {
            return 0;
        } else {
            while (p.IO.peek() != null) {
                plik1.tab[index].readbytes++;//zapisane bity
                znak = p.IO.poll();
                plik1.tab[index].myQueue.offer(znak);
                plik1.tab[index].qfreespace--;
                 if ( plik1.tab[index].qfreespace == 0) {//zapełnienie całego potoku tutaj powinna być synchronizacja
            return 2;            
            }
        }
        return 1;
        }
    }

    //ta funkcja chyba powinna być w klasie procesu
    void pipe(process p)//służy do utworzenia potoku
    {
        //proces znajduje wolny deskryptor inicjalizuje swoje indexy deskryptora;
        int index;//od obiektu file
        index = plik1.finddes();
        if (index == -1) {
            System.out.println("Błąd deskryptora");
        } else {
            p.des=index;
            p.next.des=index;
            plik1.tab[index].open=1;
        }
    }
}
