package fileSystem;

import java.text.SimpleDateFormat;
import java.util.Date;

public class File {
	public String name, fileCreatedDate, fileModifiedDate;;
	public int blockIndex;
	
	public File(String name_, int blockIndex_){
		name = name_;
		blockIndex = blockIndex_;
		fileCreatedDate = getTime();
		fileModifiedDate = fileCreatedDate;
	}
	private String getTime(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}
}
