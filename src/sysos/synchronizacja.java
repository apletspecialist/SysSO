
package sysos;

/**
 * @author M. Kuc
 */
public class synchronizacja {
    
    public static boolean T_S(boolean VALUE_OF_MEMORY) //wczytywanie flagi zajetosci danego procesu
    {
        boolean return_val = VALUE_OF_MEMORY;          //zapisanie starej wartosci flagi zajetosci do innej zmiennej
        VALUE_OF_MEMORY = true;                        //ustawianie na nowo flage zajetosci procesu na TRUE, po tym jak dany proces spelni swoje zadanie
        return return_val;                             //zwracam stara wartosc flagi zajetosci tego procesu
    }
    
    public static boolean C_S(boolean OLD_VALUE, boolean EXPECTED_VALUE, boolean NEW_VALUE)
    {                               //aktualna wartosc zajetosci flagi | oczekiwana wartosc flagi zajetosci procesu (TRUE)(0) | jest to zmienna boolean
        boolean temp = OLD_VALUE;   //zapisujemy do innej zmiennej stara wartosc flagi zajetosci
        if(OLD_VALUE==EXPECTED_VALUE) //sprawdzamy warunek czy stara wartosc rowna sie nowej/oczekiwanej
        {                             //jesli tak, to stara wartosc rowna sie nowej/oczekiwanej
            OLD_VALUE=NEW_VALUE;    
        }
        return temp;                  //zwracamy stara wartosc flagi zajetosci
    }
    
}
