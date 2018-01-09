package sysos;

/**
 *
 * @author boromir
 */
public class plik1 {
    static Potoki[] tab=new Potoki[16];
    static int rozmiar=16;
    
       public int finddes()
       {
           int number=-1;
           for(int i=0;i<16;i++)
           {
               if(tab[i].open==0)
               {
                   tab[i].open=1;
                   number=i;
               }
           }        
           return number;
       }
       
       void closedes(process p)
       {
           int numb=p.des;
           if(tab[numb].readbytes==tab[numb].writebytes)
           {
           tab[numb].open=0;
           for(int i=tab[numb].qfreespace;i>=0;i--)
               tab[numb].myQueue.poll();
           }
       }
       
    
       
}
