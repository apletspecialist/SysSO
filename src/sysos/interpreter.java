package sysos;

import sysos.process_manager.process;

public class interpreter {
	Memory m = new Memory();
	FileSystem f = new FileSystem();
	process pr;
	interpreter(Memory m,process pr, FileSystem f)
	{
		this.m=m;
		pr = SysOS.T.find(SysOS.OBECNY_PROCES);
		this.f=f;
	}
void exe()
{
	String roz;
	roz=m.readUntilSpace(pr.counter);
	pr.counter+=roz.length()+1;
	switch(roz)
	{
	case "AD":
	{
		int idrej = 0;
		roz="";
		roz=m.readUntilSpace(pr.counter);
		pr.counter+=roz.length()+1;
		if(roz=="R1") idrej=1;
		if(roz=="R2") idrej=2;
		if(roz=="R3") idrej=3;
		if(roz=="R4") idrej=4;
		roz="";
		roz=m.readUntilSpace(pr.counter);
		pr.counter+=roz.length()+1;
		int liczba;
		if(roz.charAt(0)=='[')
		{
			String pom;
			pom=roz.substring(1, 2);
			int p=Integer.valueOf(pom);
			liczba=Integer.valueOf(m.readMemory(p));
		}
		if(roz=="R1")
			liczba=pr.A;
		if(roz=="R2")
			liczba=pr.B;
		if(roz=="R3")
			liczba=pr.C;
		if(roz=="R4")
			liczba=pr.D;
		else
			liczba=Integer.valueOf(roz);
		if(idrej==1)
			pr.A+=liczba;
		if(idrej==2)
			pr.B+=liczba;
		if(idrej==3)
			pr.C+=liczba;
		if(idrej==4)
			pr.D+=liczba;
	} break;
	case "SU":
	{
		int idrej = 0;
		roz="";
		roz=m.readUntilSpace(pr.counter);
		pr.counter+=roz.length()+1;
		if(roz=="R1") idrej=1;
		if(roz=="R2") idrej=2;
		if(roz=="R3") idrej=3;
		if(roz=="R4") idrej=4;
		roz="";
		roz=m.readUntilSpace(pr.counter);
		pr.counter+=roz.length()+1;
		int liczba;
		if(roz.charAt(0)=='[')
		{
			String pom;
			pom=roz.substring(1, 2);
			int p=Integer.valueOf(pom);
			liczba=Integer.valueOf(m.readMemory(p));
		}
		if(roz=="R1")
			liczba=pr.A;
		if(roz=="R2")
			liczba=pr.B;
		if(roz=="R3")
			liczba=pr.C;
		if(roz=="R4")
			liczba=pr.D;
		else
			liczba=Integer.valueOf(roz);
		if(idrej==1)
			pr.A-=liczba;
		if(idrej==2)
			pr.B-=liczba;
		if(idrej==3)
			pr.C-=liczba;
		if(idrej==4)
			pr.D-=liczba;
	} break;
	case "MU":
	{
		int idrej = 0;
		roz="";
		roz=m.readUntilSpace(pr.counter);
		pr.counter+=roz.length()+1;
		if(roz=="R1") idrej=1;
		if(roz=="R2") idrej=2;
		if(roz=="R3") idrej=3;
		if(roz=="R4") idrej=4;
		roz="";
		roz=m.readUntilSpace(pr.counter);
		pr.counter+=roz.length()+1;
		int liczba;
		if(roz.charAt(0)=='[')
		{
			String pom;
			pom=roz.substring(1, 2);
			int p=Integer.valueOf(pom);
			liczba=Integer.valueOf(m.readMemory(p));
		}
		if(roz=="R1")
			liczba=pr.A;
		if(roz=="R2")
			liczba=pr.B;
		if(roz=="R3")
			liczba=pr.C;
		if(roz=="R4")
			liczba=pr.D;
		else
			liczba=Integer.valueOf(roz);
		if(idrej==1)
			pr.A*=liczba;
		if(idrej==2)
			pr.B*=liczba;
		if(idrej==3)
			pr.C*=liczba;
		if(idrej==4)
			pr.D*=liczba;
	} break;
	case "MO":
	{
		int idrej = 0;
		roz="";
		roz=m.readUntilSpace(pr.counter);
		pr.counter+=roz.length()+1;
		if(roz=="R1") idrej=1;
		if(roz=="R2") idrej=2;
		if(roz=="R3") idrej=3;
		if(roz=="R4") idrej=4;
		roz="";
		roz=m.readUntilSpace(pr.counter);
		pr.counter+=roz.length()+1;
		int liczba;
		if(roz.charAt(0)=='[')
		{
			String pom;
			pom=roz.substring(1, 2);
			int p=Integer.valueOf(pom);
			liczba=Integer.valueOf(m.readMemory(p));
		}
		if(roz=="R1")
			liczba=pr.A;
		if(roz=="R2")
			liczba=pr.B;
		if(roz=="R3")
			liczba=pr.C;
		if(roz=="R4")
			liczba =pr.D;
		else
			liczba=Integer.valueOf(roz);
		if(idrej==1)
			pr.A=liczba;
		if(idrej==2)
			pr.B=liczba;
		if(idrej==3)
			pr.C=liczba;
		if(idrej==4)
			pr.D=liczba;
	} break;
	case "JP":
	{
		roz="";
		roz=m.readUntilSpace(pr.counter);
		pr.counter+=roz.length()+1;
		pr.counter=Integer.valueOf(roz);
	} break;
	case "JZ":
	{
		roz="";
		roz=m.readUntilSpace(pr.counter);
		pr.counter+=roz.length()+1;
		if(roz=="R1")
			if(pr.A!=0)
				pr.counter=Integer.valueOf(roz);
		if(roz=="R2")
			if(pr.B!=0)
				pr.counter=Integer.valueOf(roz);
		if(roz=="R3")
			if(pr.C!=0)
			pr.counter=Integer.valueOf(roz);
		if(roz=="R4")
			if(pr.D!=0)
				pr.counter=Integer.valueOf(roz);
	} break;
	case "IC":
	{
		if(roz=="R1")
			pr.A++;
		if(roz=="R2")
			pr.B++;
		if(roz=="R3")
			pr.C++;
		if(roz=="R4")
			pr.D++;
	} break;
	case "DC":
	{
		if(roz=="R1")
			pr.A--;
		if(roz=="R2")
			pr.B--;
		if(roz=="R3")
			pr.C--;
		if(roz=="R4")
			pr.D--;
	} break;
	case "CF":
	{
		roz="";
		roz=m.readUntilSpace(pr.counter);
		pr.counter+=roz.length()+1;
		f.createFile(roz);
	} break;
	case "DF":
	{
		roz="";
		roz=m.readUntilSpace(pr.counter);
		pr.counter+=roz.length()+1;
		f.deleteFile(roz);
	} break;
	case "RF":
	{
		roz="";
		roz=m.readUntilSpace(pr.counter);
		pr.counter+=roz.length()+1;
		System.out.println(f.readFile(roz));
	} break;
	case "WF":
	{
		roz="";
		roz=m.readUntilSpace(pr.counter);
		pr.counter+=roz.length()+1;
		String con=m.readUntilSpace(pr.counter);
		pr.counter+=con.length()+1;
		if(con=="R1")
			con=Integer.toString(pr.A);
		if(con=="R2")
			con=Integer.toString(pr.B);
		if(con=="R3")
			con=Integer.toString(pr.C);
		if(con=="R4")
			con=Integer.toString(pr.D);
		f.writeFile(roz, con);
	} break;
	case "CP":
	{
		roz="";
		roz=m.readUntilSpace(pr.counter);
		pr.counter+=roz.length()+1;
		process pro = new process(roz);
		String rez=m.readUntilSpace(pr.counter);
		pr.counter+=rez.length()+1;
		pro.exec(roz, rez, null);
		
	} break;
	case "DP":
	{
		roz="";
		roz=m.readUntilSpace(pr.counter);
		pr.counter+=roz.length()+1;
		process.kill();
	} break;
	case "CC":
	{
		roz="";
		roz=m.readUntilSpace(pr.counter);
		pr.counter+=roz.length()+1;
		String roz2=m.readUntilSpace(pr.counter);
		pr.counter+=roz2.length()+1;
		//wcześniej powinien być utworzony jakiś proces macierzysty niezbedny do komunikacji
		Potoki.pipe();//w funkcji pipe musi się znaleźć proces dla którego bedzie przebiegała komunikacja
	} break;
	case "SC":
	{
		roz="";
		roz=m.readUntilSpace(pr.counter);
		pr.counter+=roz.length()+1;
		Potoki.write();
	} break;
	case "RC":
	{
		roz="";
		roz=m.readUntilSpace(pr.counter);
		pr.counter+=roz.length()+1;
		Potoki.read();
	} break;
	case "EX":
	{
		pr.kill(pr.PID);
	}
	}
}
}
