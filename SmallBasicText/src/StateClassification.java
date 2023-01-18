import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;

public class StateClassification {
	
	/*
	 * Terminal : T
	 * Nonterminal : NT
	 */

	public static void main(String[] args) throws IOException {
		// 파일에서 상태 추출
		String path = TextExtract.class.getResource("").getPath();
		File file = new File(path + "datacollection.txt");
		
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		InputStream is = null;
		
		// 상태 번호, 상태에 대한 텍스트에 저장된 문장
		int state;
		int state_count;
		
		String str;
		String str_arr[];
		boolean integer = false;
		HashMap<Integer, HashMap<ArrayList<String>, Integer>> map = new HashMap<>();
		
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
					map.put(state, new HashMap<ArrayList<String>, Integer>());
					map.get(state).put(result, state_count);
				}
				// 존재하는 경우 해당 state의 map에 result가 존재하는지 확인
				else {
					// result가 존재하면 value값을 state_count만큼 증가
					if(map.get(state).containsKey(result)) {
						int count = map.get(state).get(result);
						map.get(state).put(result, count + state_count);
					}
					// result가 존재하지 않으면 추가
					else {
						map.get(state).put(result, state_count);
					}
				}
			}
		}
		
		// 전체 상태에 대한 map 출력
		/*
		Iterator<Integer> keys = map.keySet().iterator();
        while (keys.hasNext()){
        	int key = keys.next();
        	System.out.println("\nstate : " + key);
        	for (Entry<ArrayList<String>, Integer> entrySet : map.get(key).entrySet()) {
                System.out.println(entrySet.getKey() + " : " + entrySet.getValue());
            }
        }
        */
        
        // 사용자로부터 상태값 입력 받아 빈도수대로 출력
        int user_state;
        Scanner sc = new Scanner(System.in);
        System.out.print("검색할 상태 입력: ");
        user_state = sc.nextInt();
        
        List<Entry<ArrayList<String>, Integer>> list = new ArrayList<Entry<ArrayList<String>, Integer>>(map.get(user_state).entrySet());

        // 비교함수 Comparator를 사용하여 내림 차순으로 정렬
     	Collections.sort(list, new Comparator<Entry<ArrayList<String>, Integer>>() {
     	// compare로 값을 비교
     		public int compare(Entry<ArrayList<String>, Integer> obj1, Entry<ArrayList<String>, Integer> obj2)
     		{
     			// 내림 차순으로 정렬
     			return obj2.getValue().compareTo(obj1.getValue());
     		}
     	});

     	// 결과 출력
     	System.out.println("state : " + user_state);
     	for(Entry<ArrayList<String>, Integer> entry : list) {
     		System.out.println(entry.getKey() + " : " + entry.getValue());
     	}
	}
}
