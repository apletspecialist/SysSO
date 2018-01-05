package fileSystem;

public class FileSystem {
	private final int discSize = 1024, bitVectorSize = 32, nrOfBlocks = 32; //bo 32*32 = 1024
	
	char[] disc;
	boolean[] bitVector;
	Catalog root;
	
	public FileSystem(){
		disc = new char[discSize];
		for(int i=0; i < discSize;i++){disc[i]='^';} //int value of '^' is 94 <=> empty
		bitVector = new boolean[bitVectorSize];
		root = new Catalog();
	}
	
	public void createFile(String name){
		for(File f : root.catalog){
			if(f.name == name){
				System.out.println("File already exist");
				return;
			}
		}
		int tempIndex = assignIndex();
		if(tempIndex != -1){
			File f = new File(name, tempIndex);
			root.catalog.add(f);
		}else{
			System.out.println("ERROR - no free block/index");
		}
	}
	
	public void writeFile(String name, String content){
		for(File f : root.catalog){
			if(f.name == name){
				int neededBlocks = content.length()/nrOfBlocks + 1;
				int index = 0;
				for(int i = 0; i < neededBlocks; i++){
					int tempIndex = assignIndex();
					if(tempIndex != -1){
						for(int j = f.blockIndex; j < f.blockIndex + nrOfBlocks; j++){
							if(disc[j] == '^'){
								disc[j] = (char) tempIndex;
								for(int k = tempIndex; k < tempIndex + nrOfBlocks; k++){
									if(index < content.length()){
										disc[k] = content.charAt(index++);
									}
								}
								break;
							}
						}
					}else{
						System.out.println("ERROR - no free block/index");
					}
				}
				return;
			}
		}
		System.out.println("ERROR - file " + name + " not found");
	}
	
	public String readFile(String name){
		for(File f : root.catalog){
			if(f.name == name){
				String data = "";
				for(int i = f.blockIndex; i < f.blockIndex + nrOfBlocks; i++){
					if(disc[i] != '^'){
						int currentBlockNr = (int) disc[i];
						for(int j = currentBlockNr; j < currentBlockNr + nrOfBlocks; j++){
							if(disc[j] != '^'){
								data += disc[j];								
							}
						}
					}
				}
				return data;
			}
		}
		System.out.println("ERROR - file " + name + " not found");
		return null;
	}
	
	public void deleteFile(String name){
		for(File f: root.catalog){
			if(f.name == name){
				
				for(int i = f.blockIndex; i < f.blockIndex + nrOfBlocks; i++){
					if(disc[i] != '^'){
						int currentBlockNr = (int) disc[i];
						for(int j = currentBlockNr; j < currentBlockNr + 32; j++){
							disc[j] = '^';
						}
						bitVector[currentBlockNr/nrOfBlocks] = false;
						disc[i] = '^';
					}
				}
				bitVector[f.blockIndex/nrOfBlocks] = false;
				root.catalog.remove(f);
				return;
			}
			
		}
	}
	
	public String getFileData(String name){
		for(File f : root.catalog){
			if(f.name == name){
				return f.name + " " + f.blockIndex;
			}
		}
		System.out.println("ERROR - file " + name + " not found");
		return null;
	}
	
	private int assignIndex(){
		for(int i = 0; i < bitVectorSize; i++){
			if(!bitVector[i]){
				bitVector[i] = true;
				return i*nrOfBlocks;
			}
		} return -1;
	}
	public void printDisc(){
		for(int i = 0; i < disc.length; i++){
			if((i+1) % 128 == 0){
				System.out.println(disc[i]);
			}
			else{
				System.out.print(disc[i]);
			}
		}
	}
}
