package com.syntax;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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
		
		listForSyntaxCompletion();
		
	}
	
	public SyntaxCompletionDataManager() throws IOException {
		buildSyntaxCompletionData();
	}
	
	public static void buildSyntaxCompletionData() throws IOException {
		// 파일에서 상태 추출
		// String path = System.getProperty("user.dir");
		String path = "./../../SmallBasic_FileList/SmallBasicText/input_source/datacollection.txt";
		file = new File(path);
				
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
	
	public static HashMap<Integer, ArrayList<Pair>> getMap() {
		return map;
	}
	
	// 성능 분석 시 사용, 구문 search
	public static int searchForSyntaxCompletion(ArrayList<String> arr, int state) {
        int search_state = state;
	    int value = 0; // 후보 번호
	    
	    // 전달받은 상태에 대해 전달받은 후보 구문이 몇 번째 구문에 해당하는지 찾는다.
		Iterator<Pair> keys = map.get(search_state).iterator();
		int count = 0;
		while(keys.hasNext()) {
			Pair key = keys.next();
			count++;
			if(key.getFirst().equals(arr)) {
				// 구문이 존재하면 후보 번호 설정
				value = count;
				break;
			}
	    }
		
		return value;
	} // searchForSyntaxCompletion end
	
	// smallBasic 구문 완성 검색 시 사용
	public ArrayList<String> searchForSyntaxCompletion(String str) {
		int search_state = Integer.parseInt(str);
		
		ArrayList<Pair> pair = map.get(search_state);
		ArrayList<String> arr = new ArrayList<String>();
		for(int i = 0; i < pair.size(); i++) {
			// 전달받은 상태에 대한 후보 구문들을 공백으로 나누어 리스트에 저장
			arr.add(String.join(" ", pair.get(i).getFirst()) + " ");
		}
		
		return arr;
	} // searchForSyntaxCompletion end
	
	public static void listForSyntaxCompletion() {
        int user_state = 0;
        final int MAX_STATE = 118;  // 0 ~ 118
        
        while (user_state <= MAX_STATE) {
	        System.out.println("State " + user_state);
	        
	        if (map.get(user_state) != null) {
				for(int i = 0; i < map.get(user_state).size(); i++) {
					System.out.println(map.get(user_state).get(i).getFirst() + " : " + map.get(user_state).get(i).getSecond());
				}
	        }
	        
	        user_state = user_state + 1;
        }
	} // listForSyntaxCompletion end
	
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
