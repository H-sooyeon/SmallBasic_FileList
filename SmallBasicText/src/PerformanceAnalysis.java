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
		// ���Ͽ��� ���� ����
		String path = TextExtract.class.getResource("").getPath();
		file = new File(path + "smallbasic_tutorial_programs.txt");
		
		bufferedReader = new BufferedReader(new FileReader(file));
		
		String str;
		String str_arr[];
		boolean integer = false;
		searchList = new ArrayList<>();
		while((str = bufferedReader.readLine()) != null) {
			// ������ ù ���ڰ� �����̸� ����
			integer = Character.isDigit(str.charAt(0));
			
			if(integer) {
				// ������ �������� split�Ͽ� list�� ����
				str_arr = str.split("\\s");
				
				// ���� �з� ����Ʈ�� ���� ������ ����
				for(int i = 1; i < str_arr.length - 1; i++) {
					if(str_arr[i].equals("Terminal")) {
						str_arr[i] = "T";
					} 
					else if(str_arr[i].equals("Nonterminal")) {
						str_arr[i] = "NT";
					}
				}
				
				// �˻��� ���� �߰�
				searchList.add(String.join(" ", str_arr));
			}
		}
	} // buildSyntaxData end
	
	public static void searchForSyntax(ArrayList<String> list) throws IOException {
		// ������ list �˻� �� ���� ���� ���
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
				size = dataManager.getMap().get(state).size();
				value = dataManager.searchForSyntaxCompletion(arrList, state);
				
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
