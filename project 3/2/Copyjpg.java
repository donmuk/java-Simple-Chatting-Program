import java.io.*;
public class Copyjpg { 
    private RandomAccessFile output,input;
    private int in,count,star;
    Copyjpg() throws IOException{
    	try {
			output = new RandomAccessFile("test.jpg","r");
			input = new RandomAccessFile("copy.jpg","rw");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		};
		byte [] data = new byte[1024];
		star=0;
		while((in=output.read(data))!=-1){
			input.write(data,0,in);
			count+=in;
			float per=((float)count/output.length())*100;
			if((star-(int)(per/10))==-1) {
				star=(int)(per/10);
				System.out.println(star*10+"%"+" * ");
			}
		}
    }
    public static void  main(String args[]) throws IOException {
    	new Copyjpg();
    }
} 