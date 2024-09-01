import java.util.Random;
import java.io.File;
import java.io.FileWriter;
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
	public static String[][] loadUnallowedGroups() {
		String[][] groups = new String[1][];
		groups[0] = new String[]{"",""};
		try {
			FileInputStream inputStream = new FileInputStream("oldGroups.txt");
			InputStreamReader reader = new InputStreamReader(inputStream,"UTF-8");
			int curChar;
			boolean firstName = true;
			while((curChar = reader.read()) != -1) {
				if(curChar == '\n') {
					firstName = true;
					String[][] newGroups = new String[groups.length+1][];
					for(int i = 0;i<groups.length;i++) {
						newGroups[i]=groups[i];
					}
					newGroups[groups.length]=new String[]{"",""};
					groups = newGroups;
				} else if(curChar == ',') {
					firstName = false;
				} else {
					if(firstName) {
						groups[groups.length-1][0]=groups[groups.length-1][0]+(char)curChar;
					} else {
						groups[groups.length-1][1]=groups[groups.length-1][1]+(char)curChar;
					}
				}
			}
			reader.close();
			if(groups[groups.length-1][0]=="") {
				String[][] newGroups = new String[groups.length-1][];
				for(int i = 0;i<(groups.length-1);i++) {
					newGroups[i] = groups[i];
				}
				groups = newGroups;
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		return groups;
	}
	public static void saveUnallowedGroups(String[][] oldGroups,String[][] newGroups) {
		int totalLength = newGroups.length+oldGroups.length;
		String[][] groups = new String[totalLength][];
		for(int i = 0;i<oldGroups.length;i++) {
			groups[i] = oldGroups[i];
		}
		for(int i = 0;i<newGroups.length;i++) {
			groups[i+(oldGroups.length)] = newGroups[i];
		}
		try {
			FileWriter saveFileWriter = new FileWriter("oldGroups.txt");
			for(int i = 0;i<groups.length;i++) {
				saveFileWriter.write(groups[i][0]+','+groups[i][1]+'\n');
			}
			saveFileWriter.close();
			System.out.println("Saved new groups.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
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

		/*String[][] unallowedGroups = new String[][]{
			{"hannah","blake"}
		};*/
		String[] names = loadNameList();
		String[][] unallowedGroups = loadUnallowedGroups();
		boolean groupFound = false;
		String[][] pairs = pair(names);
		int panicCounter = 0;
		while(!groupFound) {
			panicCounter++;
			if(panicCounter>100) {
				System.out.println("ERROR: Cannot Generate New Group List!");
			}
			groupFound = true;
			pairs = pair(names);
			for(int i = 0;i<pairs.length;i++) {
				for(int x = 0;x<unallowedGroups.length;x++) {
					System.out.println("Testing:");
					System.out.println("A: "+unallowedGroups[x][0]+","+unallowedGroups[x][1]);
					System.out.println("B: "+pairs[i][0]+","+pairs[i][1]);
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
		//saveUnallowedGroups(unallowedGroups,pairs);
	}
}
