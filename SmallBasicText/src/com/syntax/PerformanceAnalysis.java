package com.syntax;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class PerformanceAnalysis {
	private static BufferedReader bufferedReader;
	private static SyntaxCompletionDataManager dataManager;
	private static ArrayList<String> searchList;
	private static File file;
	
	/*
	 * Terminal : T
	 * Nonterminal : NT
	 */
	
	public static void main(String[] args) throws IOException {
		
		buildSyntaxData();
		
		searchForSyntax(searchList);
		
	}
	
	public static void buildSyntaxData() throws IOException {
		// 파일에서 구문 추출
		String path = System.getProperty("user.dir");
		file = new File(path + "/input_source/smallbasic_tutorial_programs.txt");
		
		bufferedReader = new BufferedReader(new FileReader(file));
		
		String str;
		String str_arr[];
		boolean integer = false;
		searchList = new ArrayList<>();
		while((str = bufferedReader.readLine()) != null) {
			// 라인의 첫 문자가 숫자이면 실행
			integer = Character.isDigit(str.charAt(0));
			
			if(integer) {
				// 공백을 기준으로 split하여 list에 저장
				str_arr = str.split("\\s");
				
				// 상태 분류 리스트에 맞춰 데이터 저장
				for(int i = 1; i < str_arr.length - 1; i++) {
					if(str_arr[i].equals("Terminal")) {
						str_arr[i] = "T";
					} 
					else if(str_arr[i].equals("Nonterminal")) {
						str_arr[i] = "NT";
					}
				}
				
				// 검색할 구문 추가
				searchList.add(String.join(" ", str_arr));
			}
		}
	} // buildSyntaxData end
	
	public static void searchForSyntax(ArrayList<String> list) throws IOException {
		// 저장한 list 검색 및 존재 여부 출력
		dataManager = new SyntaxCompletionDataManager();
		int total = 0, not = 0;
		
		boolean flag;
		int state, value, size = 0;
		for(int i = 0; i < list.size(); i++) {
			total++;
			
			flag = false;
			value = 0;
			String[] line = list.get(i).split(" ");
			String[] arr = Arrays.copyOfRange(line, 1, line.length - 1);
			ArrayList<String> arrList = new ArrayList<String>(Arrays.asList(arr));
			
			state = Integer.parseInt(line[0]);
			
			if(dataManager.getMap().get(state) != null) {
				size = dataManager.getMap().get(state).size(); // 해당 상태에 대한 후보 개수
				value = dataManager.searchForSyntaxCompletion(arrList, state); // 찾은 후보들 중 몇 번째에 해당하는지
				
				if(value != 0) flag = true;
			}
			
			String result = "";
			if(flag) {
				result = state + " " + String.join(" ", arr) + " Found " + value + " " + size;
			} else {
				result = state + " " + String.join(" ", arr) + " NotFound";
				not++;
			}
			
			System.out.println(result);
		}
		
		System.out.println("\nTotal: " + total);
		System.out.println("Found: " + (total - not));
		System.out.println("NotFound: " + not);
	} // searchForSyntaxCompletion end
	
}
