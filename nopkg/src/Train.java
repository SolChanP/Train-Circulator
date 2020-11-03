import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class Train {
	protected int line = 0; //호선 
	protected int cur_station = 0;//현재 역
	protected String dir = null;
	protected int tp_cur_sta = 0;//시간 계산을 위한 현재
	protected int rem_next_time = 0;//다음역까지 남은시간
	protected List<Route>[] route;//열차 운행에 사용할 경로, 상행 하행 얕은 복사 이용.
	protected List<Route>[] route_up;//상행 경로.
	protected List<Route>[] route_down;//하행 경로. 
	protected List<Station> station;//역 이름 저장.
	protected int route_cnt;//경로 수.
	protected int station_cnt;//역의 수.
	
	public Train(int line, String dir) {
		this.setLine(line);
		
	}
	//DB 갱신 메서드.
	public void setData(Statement stmt) {
		String db_cur_station = null;
		String db_rem_time = null;
		if(this.dir.equals("상행")) {
			db_cur_station = "UPDATE MJ.metro SET cur_station =' " + this.getSname(cur_station) +
					"' WHERE line = '"+ line +"Up'";
			db_rem_time = "UPDATE MJ.metro SET rem_next_time =' " + this.rem_next_time +
					"' WHERE line = '"+ line +"Up'";
		}
		else {
			db_cur_station = "UPDATE MJ.metro SET cur_station =' " + this.getSname(cur_station) +
					"' WHERE line = '"+ line + "Down'";
			db_rem_time = "UPDATE MJ.metro SET rem_next_time =' " + this.rem_next_time +
					"' WHERE line = '"+ line +"Down'";
		}
		try {
			stmt.executeUpdate(db_cur_station);
			stmt.executeUpdate(db_rem_time);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void setCurStation(String dir) {
		if(dir.equals("상행")) {
			cur_station = 1;
		}
		else {
			cur_station = route_cnt;
		}
	}
	public void setLine(int line) {
		this.line = line;
	}
	public int getLine() {
		return line;
	}
	public int getCurStation() {//현재 역 반환 메서드.
		return cur_station;
	}
	public int getRemTime() {//다음역까지 남은 시간 반환 메서드.
		return rem_next_time;
	}
	//열차 방향 설정 메서드
	public void setRouteDir(String dir) {
		if(dir.equals("상행")) {
			this.route = route_up;
			this.dir = "상행";
		}
		else {
			this.route = route_down;
			this.dir = "하행";
		}
	}
	//다음역까지 남은 시간 변환 메서드.
	public abstract void setRemNextTime();
	public abstract void buildRoute() throws IOException;
	//역이름 인덱스 생성 메서드.
	public void buildName() throws IOException{
		String temp_s;
		InputStream	inputStream = null;
		inputStream = new FileInputStream("/Users/sol/google/2-2학기/팀프로젝트1/metro/metro/indexN/lineN" + line + ".txt");
		InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader stationData = new BufferedReader(inputreader);
		while ((temp_s = stationData.readLine()) != null) // Subway.txt의 라인 수 만큼 temp_n증가.
			station_cnt++;
        station = new ArrayList<>(station_cnt + 1);
        // 데이터 입력 단계.
		inputStream = new FileInputStream("/Users/sol/google/2-2학기/팀프로젝트1/metro/metro/indexN/lineN" + line + ".txt");
        inputreader = new InputStreamReader(inputStream);
        stationData = new BufferedReader(inputreader);
		while ((temp_s = stationData.readLine()) != null) {
		    String[] split = temp_s.split("	");// tap으로 구분.
		    String name = split[0];
		    station.add(new Station(name));// 경로(라인) 양방향이기 때문에,
		}
	}
	//인덱스 - 역이름 검색 메서드.
	public String getSname(int index) {
		return station.get(index).getName();
	}
	
	//열차 운행 메서드.
	public void run(Statement stmt) {
		this.setRemNextTime();
		this.setData(stmt);
		
	}
}
