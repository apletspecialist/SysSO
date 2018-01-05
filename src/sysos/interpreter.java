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

public class interpreter {
	interpreter(){}
void exe()
{
	String roz;
	//pobranie rozkazu lr+1
	switch(roz)
	{
	case "AD":
	{
		int idrej;
		//ponowne pobranie rozkazu lr+1
		if(roz=="R1")
			idrej=1;
		if(roz=="R2")
			idrej=2;
		if(roz=="R3")
			idrej=3;
		//ponowne pobranie rozkazu lr+1
		int liczba;
		if(roz[0]=='[')
		{
			String pom;
			pom=roz[1]+roz[2];
			int p=Integer.valueOf(pom);
			//pobranie z pamięci i przypisanie do zmiennej
		}
		if(roz=="R1")
			//liczba=Preces.R1;
		if(roz=="R2")
			//liczba=Preces.R2;
		if(roz=="R3")
			//liczba=Preces.R3;
		else
			//liczba=Integer.valueOf(roz);
		if(idrej==1)
			//Proces.R1+=liczba;
		if(idrej==2)
			//Proces.R2+=liczba;
		if(idrej==3)
			//Proces.R3+=liczba;
	} break;
	case "SU":
	{
		int idrej;
		//ponowne pobranie rozkazu lr+1
		if(roz=="R1")
			idrej=1;
		if(roz=="R2")
			idrej=2;
		if(roz=="R3")
			idrej=3;
		//ponowne pobranie rozkazu lr+1
		int liczba;
		if(roz[0]=='[')
		{
			String pom;
			pom=roz[1]+roz[2];
			int p=Integer.valueOf(pom);
			//pobranie z pamięci i przypisanie do zmiennej
		}
		if(roz=="R1")
			//liczba=Preces.R1;
		if(roz=="R2")
			//liczba=Preces.R2;
		if(roz=="R3")
			//liczba=Preces.R3;
		else
			//liczba=Integer.valueOf(roz);
		if(idrej==1)
			//Proces.R1-=liczba;
		if(idrej==2)
			//Proces.R2-=liczba;
		if(idrej==3)
			//Proces.R3-=liczba;
	} break;
	case "MU":
	{
		int idrej;
		//ponowne pobranie rozkazu lr+1
		if(roz=="R1")
			idrej=1;
		if(roz=="R2")
			idrej=2;
		if(roz=="R3")
			idrej=3;
		//ponowne pobranie rozkazu lr+1
		int liczba;
		if(roz[0]=='[')
		{
			String pom;
			pom=roz[1]+roz[2];
			int p=Integer.valueOf(pom);
			//pobranie z pamięci i przypisanie do zmiennej
		}
		if(roz=="R1")
			//liczba=Preces.R1;
		if(roz=="R2")
			//liczba=Preces.R2;
		if(roz=="R3")
			//liczba=Preces.R3;
		else
			//liczba=Integer.valueOf(roz);
		if(idrej==1)
			//Proces.R1*=liczba;
		if(idrej==2)
			//Proces.R2*=liczba;
		if(idrej==3)
			//Proces.R3*=liczba;
	} break;
	case "MO":
	{
		int idrej;
		//ponowne pobranie rozkazu lr+1
		if(roz=="R1")
			idrej=1;
		if(roz=="R2")
			idrej=2;
		if(roz=="R3")
			idrej=3;
		//ponowne pobranie rozkazu lr+1
		int liczba;
		if(roz[0]=='[')
		{
			String pom;
			pom=roz[1]+roz[2];
			int p=Integer.valueOf(pom);
			//pobranie z pamięci i przypisanie do zmiennej
		}
		if(roz=="R1")
			//liczba=Preces.R1;
		if(roz=="R2")
			//liczba=Preces.R2;
		if(roz=="R3")
			//liczba=Preces.R3;
		else
			//liczba=Integer.valueOf(roz);
		if(idrej==1)
			//Proces.R1=liczba;
		if(idrej==2)
			//Proces.R2=liczba;
		if(idrej==3)
			//Proces.R3=liczba;
	} break;
	case "JP":
	{
		//pobranie rozkazu z pamięci
		//przypisanie wartości z pamięci do licznika rozkazów
	} break;
	case "JZ":
	{
		//pobranie miejsca docelowego skoku
		//pobranie rejestru z pamięci
		if(roz=="R1")
			//if(proces.r1!=0)
			//licznik rozkazow=wartosc pobrana r1--;
		if(roz=="R2")
			//to samo co wyzej
		if(roz=="R3")
			//to samo co wyzej
	}
	case "IC":
	{
		if(roz=="R1")
			//process.r1++;
		if(roz=="R2")
			//process.r2++;
		if(roz=="R3")
			//process.r2++;
	} break;
	case "DC":
	{
		if(roz=="R1")
			//process.r1--;
		if(roz=="R2")
			//process.r2--;
		if(roz=="R3")
			//process.r2--;
	} break;
	}
}
}

