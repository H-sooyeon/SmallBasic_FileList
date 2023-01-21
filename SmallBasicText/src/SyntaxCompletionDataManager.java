import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class SyntaxCompletionDataManager {
	private static HashMap<Integer, ArrayList<Pair>> map;
	private static BufferedReader bufferedReader;
	private static File file;
	
	/*
	 * Terminal : T
	 * Nonterminal : NT
	 */

	public static void main(String[] args) throws IOException {
		
		buildSyntaxCompletionData();
		
		searchForSyntaxCompletion();
		
	}
	
	public static void buildSyntaxCompletionData() throws IOException {
		// 파일에서 상태 추출
		String path = TextExtract.class.getResource("").getPath();
		file = new File(path + "datacollection.txt");
				
		bufferedReader = new BufferedReader(new FileReader(file));
				
		// 상태 번호, 상태에 대한 텍스트에 저장된 문장
		int state;
		int state_count;
				
		String str;
		String str_arr[];
		boolean integer = false;
		map = new HashMap<>();
				
		while((str = bufferedReader.readLine()) != null) {
			// 라인의 첫 문자가 숫자이면 실행
			integer = Character.isDigit(str.charAt(0));
					
			if(integer) {
				// 공백을 기준으로 split하여 list에 저장
				str_arr = str.split("\\s");
				state = Integer.parseInt(str_arr[0]);
				state_count = Integer.parseInt(str_arr[str_arr.length-1]);
				ArrayList<String> result = new ArrayList<>();
						
				for(int i = 1; i < str_arr.length - 1; i++) {
					if(str_arr[i].equals("Terminal")) {
								result.add("T");
					} 
					else if(str_arr[i].equals("Nonterminal")) {
								result.add("NT");
					} 
					else {
						result.add(str_arr[i]);
					}
				}
						
				// state값이 존재하지 않으면 추가
				if(!map.containsKey(state)) {
					map.put(state, new ArrayList<>());
					map.get(state).add(new Pair(result, state_count));
				}
				// 존재하는 경우 해당 state의 list에 result가 존재하는지 확인
				else {
					// result가 존재하면 value값을 state_count만큼 증가 및 내림차순 정렬
					boolean flag = false;
					Iterator<Pair> keys = map.get(state).iterator();
							
					while(keys.hasNext()) {
						Pair key = keys.next();
						if(key.getFirst().equals(result)) {
							int count = key.getSecond();
							key.setSecond(count + state_count);
							map.get(state).sort(null);
							flag = true;
							break;
						}
					}
							
					// result가 존재하지 않으면 추가 및 내림차순 정렬
					if(!flag) {
						map.get(state).add(new Pair(result, state_count));
						map.get(state).sort(null);
					}
				}
			}
		}
	} // buildSyntaxCompletionData end
	
	public static void searchForSyntaxCompletion() {
        // 사용자로부터 상태값 입력 받아 빈도수대로 출력
        int user_state;
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter a state: ");
        user_state = sc.nextInt();
        
		for(int i = 0; i < map.get(user_state).size(); i++) {
			System.out.println(map.get(user_state).get(i).getFirst() + " : " + map.get(user_state).get(i).getSecond());
		}
	} // searchForSyntaxCompletion end
	
 	static class Pair implements Comparable<Pair>{
 		private ArrayList<String> first;
 		private int second;
 		
 		public Pair(ArrayList<String> first, int second) {
 			this.first = first;
 			this.second = second;
 		}
 		
 		public void setFirst(ArrayList<String> first) {
 			this.first = first;
 		}
 		
 		public void setSecond(int second) {
 			this.second = second;
 		}
 		
 		public ArrayList<String> getFirst() {
 			return first;
 		}
 		
 		public int getSecond() {
 			return second;
 			}

		public int compareTo(Pair p) {
			return Integer.compare(p.second, this.second);
		}
 		
 	} // Pair end
}
