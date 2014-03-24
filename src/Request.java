
public class Request {
	
	private int riderNum;
	private int from;
	private int to;
	
	public Request(int rN, int f, int t){
		riderNum = rN;
		from = f;
		to = t;
	}
	
	public int getRiderNum(){
		return riderNum;
	}
	
	public int getFrom(){
		return from;
	}
	
	public int getTo(){
		return to;
	}

}
