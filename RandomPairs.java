import java.util.Random;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

class RandomPairs {
	public static String[][] pair(String[] names) {
		Random rand = new Random();
		String[] pairedNames = new String[]{};
		String[][] pairs;
		String randomName;
		while (pairedNames.length < names.length) {
			randomName = names[rand.nextInt(names.length)];
			for(String name: pairedNames) {
				if(name == randomName) {
					randomName="";
					break;
				}
			}
			if(randomName != "") {
				int newLength = pairedNames.length+1;
				String[] newPairedNames = new String[newLength];
				for( int i = 0; i < pairedNames.length; i++) {
					newPairedNames[i] = pairedNames[i];
				}
				newPairedNames[newLength-1] = randomName;
				pairedNames = newPairedNames;
			}
		}
		int pairsLength = (int)Math.ceil((double)pairedNames.length / 2);
		pairs = new String[pairsLength][];
		for(int i = 0;i<pairs.length;i++) {
			pairs[i] = new String[]{"",""};
		}
		for(int i = 0;i < pairedNames.length;i++) {
			int curPair = (int)Math.floor((double)i / 2);
			if((i & 1) == 0) {
				pairs[curPair][0] = pairedNames[i];
			} else {
				pairs[curPair][1] = pairedNames[i];
			}
		}
		return pairs;
	}
	public static String[] loadNameList() {
		//names.txt
		String[] names = new String[]{""};
		try {
			FileInputStream inputStream = new FileInputStream("names.txt");
			InputStreamReader reader = new InputStreamReader(inputStream,"UTF-8");
			int curChar;
			while((curChar = reader.read()) != -1) {
				if(curChar == '\n') {
					String[] newNames = new String[names.length+1];
					for(int i = 0;i<names.length;i++) {
						newNames[i]=names[i];
					}
					newNames[names.length]="";
					names = newNames;
				} else {
					names[names.length-1]=names[names.length-1]+(char)curChar;
				}
			}
			reader.close();
			if(names[names.length-1]=="") {
				String newNames[] = new String[names.length-1];
				for(int i = 0;i<(names.length-1);i++) {
					newNames[i] = names[i];
				}
				names = newNames;
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		return names;
	}
	/*public static String[][] loadUnallowedGroups() {
		//oldGroups.txt
	}
	public static boolean saveUnallowedGroups() {
		//oldGroups.txt
	}*/
	public static void main(String args[]) {
		/*String[] names=new String[]{
			"bob", 
			"pedro", 
			"isaac", 
			"sarah", 
			"jenny", 
			"jake",
			"kelly",
			"hannah",
			"kevin"
		};*/

		String[][] unallowedGroups = new String[][]{
			{"hannah","blake"}
		};
		String[] names = loadNameList();
		//String[][] unallowedGroups = loadUnallowedGroups();
		boolean groupFound = false;
		String[][] pairs = pair(names);
		int panicCounter = 0;
		while(!groupFound) {
			panicCounter++;
			if(panicCounter>100) {
				System.out.println("AAAAAAA");
			}
			groupFound = true;
			pairs = pair(names);
			for(int i = 0;i<pairs.length;i++) {
				for(int x = 0;x<unallowedGroups.length;x++) {
					if(unallowedGroups[x] == pairs[i]) {
						groupFound = false;
						break;
					} else {
						if(unallowedGroups[x][0] == pairs[i][1]) {
							if(unallowedGroups[x][1] == pairs[i][0]) {
								groupFound = false;
								break;
							}
						}
					}
				}
			}
		}
		System.out.println("Pairs:");
		for(int i = 0;i < pairs.length; i++) {
			if(pairs[i][1] != "") {
				System.out.println(pairs[i][0]+" and "+pairs[i][1]);
			} else {
				System.out.println(pairs[i][0]+" does not have a pair and can join a group!");
			}
		}
	}
}
