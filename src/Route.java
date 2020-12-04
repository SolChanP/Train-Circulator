

public class Route {
	private int end;//도착역
	private int time;//시간
	
	
	public Route(int end, String dir, int time) {
		this.setEnd(end);
		this.setTime(time);
	}
	
	public int getEnd() {
		return end;
	}
	public int getTime() {
		return time;
	}
	
	public void setTime(int time) {
		this.time = time;
	}
	public void setEnd(int end) {
		this.end = end;
	}
}
