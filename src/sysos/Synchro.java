/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sysos;

/**
 *
 * @author Matitam
 */
public class Synchro {

    public String nazwa;
    
    public int PROCESS_FROM_MEMORY = 0;
    public int PROCESS_FROM_QUEUE_OF_PROCESSESS = 0;
    
    public final Proces p;
    public final Lock lock;
    
    public Synchro(String nazwa, Proces p, Lock lock)
    {
        this.nazwa = nazwa;
        this.p = p;
        this.lock = lock;
    }
    public boolean Test_and_Set(Boolean lock)
    {
        if(lock == true)
        {
            System.out.println("INSIDE LOOP");
            return true;
        }
        System.out.println("OUTSIDE LOOP");
        return false;
    }
    public boolean Compare_And_Swap(Boolean OLD_VALUE, boolean EXPECTED_VALUE, boolean NEW_VALUE)
    {
        boolean TEMP = OLD_VALUE;
        if(OLD_VALUE==EXPECTED_VALUE)
        {
            System.out.println("INSIDE LOOP");
            OLD_VALUE=NEW_VALUE;
        }
        System.out.println("OUTSIDE LOOP");
        return TEMP;
    }
    
    
    public void TO_CRITICAL_SECTION_TAS() throws InterruptedException 
    {
        System.out.println(nazwa+" GOT INSIDE CRITICAL SECTION");
        while(Test_and_Set(lock.lock))
        {
            Thread.sleep(500);
            System.out.println("BREAK");
            break;
        }
        lock.lock = false;
    }
    
    public void TO_CRITICAL_SECTION_CAS() throws InterruptedException 
    {
        System.out.println(nazwa+" GOT INSIDE CRITICAL SECTION");
        while(Compare_And_Swap(lock.lock,true,false))
        {
            Thread.sleep(500);
            System.out.println("BREAK");
            break;
        }
        lock.lock = false;
    }
    
    public void OFF_CRITICAL_SECTION()
    {
        System.out.println(nazwa+" GOT OFF CRITICAL SECTION");
        lock.lock = false;
    }
    
    public int sumN(int sum)
    {
        sum=sum-1;
        return sum;
    }
    public int sumM(int sum)
    {
        sum=sum-1;
        return sum;
    }
    
    
    
    public static void main(String[] args) throws InterruptedException 
    {
        
        int sum =0;
        
        Lock lock = new Lock();
        Proces ONE = new Proces();
        Proces TWO = new Proces();
        
        Synchro SYNC1 = new Synchro("SYNC1", ONE, lock);
        Synchro SYNC2 = new Synchro("SYNC2", TWO, lock);
        
        
        for(int i=1; i<=3; i++)
        {
            SYNC1.TO_CRITICAL_SECTION_CAS();
            sum = SYNC1.sumN(sum);
            System.out.println("s "+sum);
            SYNC1.OFF_CRITICAL_SECTION();
            
            SYNC2.TO_CRITICAL_SECTION_CAS();
            sum = SYNC2.sumN(sum);
            System.out.println("s "+sum);
            SYNC2.OFF_CRITICAL_SECTION();
            
            
        }
    }
    
}
