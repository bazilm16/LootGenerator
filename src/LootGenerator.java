import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class LootGenerator {
	private List<String> data;
	private String[] monsterArray;
	private String monster;
	private String treasureClass;
	private AssociationList<String, String[]> armorMap = new AssociationList<String, String[]>();
	private AssociationList<String, String[]> treasureMap = new AssociationList<String, String[]>();
	private String baseStats;
	private String baseItem;
	private String[] prefix;
	private String[] suffix;
	private String prefixStats;
	private String suffixStats;
	private String affixStats;
	private String name;

	public LootGenerator(){
		data = new LinkedList<>();
		monster = null;
	}

	public String getMonster(){
		return monster;
	}

	public String getTreasureClass(){
		return treasureClass;
	}

	public String getBaseItem(){
		return baseItem;
	}

	public String[] getPrefix(){
		return prefix;
	}

	public String[] getSuffix(){
		return suffix;
	}

	public String getAffixStats(){
		return affixStats;
	}

	public String getName(){
		return name;
	}

	public String getBaseStats(){
		return baseStats;
	}

	//leads data from a file and loads it into a list
	public List<String> loadFile(String filename) throws IOException{
		List<String> data = new LinkedList<>();
		Scanner str = new Scanner(new File(filename));
		while(str.hasNextLine()){
			data.add(str.nextLine());
		}
		str.close();
		return data;
	}//loadFile

	public void pickMonster() throws IOException {
		//load the data into the monster list
		this.data = loadFile("data/large/monstats.txt");
		//get the monster from the file
		if(data.size() == 1){
			monsterArray = data.get(0).split("\t");
		}else{
			Random randomNum = new Random();
			monsterArray = data.get(randomNum.nextInt(4)).split("\t");
		}
		//setup the monster
		this.monster = monsterArray[0];
	}//pickMonster

	public void fetchTreasureClass() throws IOException {
		this.treasureClass = monsterArray[3];
		//get the contents of the treasure's file
		this.data = loadFile("data/large/TreasureClassEx.txt");
		//load the data into an association list
		Iterator<String> treasureIter = this.data.iterator();
		String[] arr;
		while(treasureIter.hasNext()){
			//split the string using tab
			arr = treasureIter.next().split("\t");
			//create an array of items
			String[] items = {arr[1], arr[2], arr[3]};
			//load the data into the treasure map data structure
			this.treasureMap.add(arr[0], items);
		}
	}//fetchTreasureClass()

	public void generateBaseItem() throws IOException {
		//first check if we have the base item yet
		if(!this.treasureMap.keys().contains(this.treasureClass)){
			this.baseItem = this.treasureClass;
		}else{
			//randomly chose a base item
			Random randomNum = new Random();
			//get the items array by key
			int index = this.treasureMap.keys().indexOf(this.treasureClass);
			//get the items from the index and place them in the items array
			String[] items = this.treasureMap.values().get(index);
			//get a random item and recursively check if it exists
			this.treasureClass = items[randomNum.nextInt(2) + 1];
			//rerun the procedure till we have a base item
			generateBaseItem();
		}
	}//generateBaseItem(

	public void generateBaseStats() throws IOException {
		this.data = loadFile("data/large/armor.txt");
		Iterator<String> armorIter = this.data.iterator();
		String[] arr;
		while(armorIter.hasNext()){
			//split the string using tab
			arr = armorIter.next().split("\t");
			//create an array of items
			String[] items = {arr[1], arr[2]};
			//load the data into the treasure map data structure
			this.armorMap.add(arr[0], items);
		}
		//create a random number to calculate base stats
		Random randomNum = new Random();
		//get the items array by key
		int index = this.armorMap.keys().indexOf(this.baseItem);
		//get the items from the index and place them in the items array
		String[] defenseVals = this.armorMap.values().get(index);
		int minac = Integer.parseInt(defenseVals[0]);
		int maxac = Integer.parseInt(defenseVals[1]);
		int stat = randomNum.nextInt(maxac - minac) + minac;
		this.baseStats = "Defence : " + stat;
	}

	public void generateAffix()  throws IOException {
		//create a new random variable for the prefix
		Random randomNum = new Random();
		int gen = randomNum.nextInt(2);
		//randomly pick a prefix
		if(gen == 1){
			this.data = loadFile("data/large/MagicPrefix.txt");
			this.prefix = this.data.get(randomNum.nextInt(this.data.size() - 1)).split("\t");
		}else{
			this.prefix = null;
		}
		//create a new random variable for the suffix
		int gen2 = randomNum.nextInt(2);
		gen2 = randomNum.nextInt(2);
		//randomly pick a suffix
		if(gen == 1){
			this.data = loadFile("data/large/MagicSuffix.txt");
			this.suffix = this.data.get(randomNum.nextInt(this.data.size() - 1)).split("\t");
		}else{
			this.suffix = null;
		}
	}

	public void generateName(){
		//string to store the name
		String name = "";
		//if the prefix is not null, append it to the name 
		if(this.prefix != null){
			name += (this.prefix[0] + " ");
		}
		//add the base name
		name += this.baseItem;
		//add the suffix if it exists
		if(this.suffix != null){
			name += (" " + this.suffix[0]);
		}
		//add all of that to the class's name
		this.name = name;
	}

	public void generateAffixStats(){
		Random randomNum = new Random();
		//generate the prefix statistics
		if(this.prefix != null){
			this.prefixStats = this.prefix[0] + " " + calcStats(suffix) + "\n";
		}else{
			this.prefixStats = "";
		}
		//generate the suffix statistics
		if(this.suffix != null){
			this.suffixStats = this.suffix[0] + " " + calcStats(suffix);
		}else{
			this.suffixStats = "";
		}
		//generate the additional affix statistics
		this.affixStats = this.prefixStats + this.suffixStats;
	}

	public int calcStats(String[] affix){
		if(affix[2] == affix[3]){
			return Integer.parseInt(affix[3]);
		}else{
			Random randomNum = new Random();
			//get a random value between the bounds
			return randomNum.nextInt(
					Math.abs(Integer.parseInt(affix[3]) 
							- Integer.parseInt(affix[2]))
					+ Integer.parseInt(affix[2])
					);
		}
	}

	public static void main(String[] args) throws IOException{
		boolean session = true;
		while(session){
			//begin the game 
			LootGenerator game = new LootGenerator();
			game.pickMonster();
			game.fetchTreasureClass();
			game.generateBaseItem();
			game.generateBaseStats();
			game.generateAffix();
			game.generateName();
			game.generateAffixStats();
			//produce the output
			System.out.println("Fighting :" + game.getMonster());
			System.out.println("You have slain :" + game.getMonster() + "!");
			System.out.println(game.getMonster() + " dropped:");
			System.out.println("\n");
			System.out.println(game.getName());
			System.out.println(game.getBaseStats());
			System.out.println(game.getAffixStats());
			
			//prompt the user to fight again
			Scanner scanner = new Scanner(System.in);
			System.out.println("Fight again [y/n]?");
			String command = scanner.next();
			//if the user wants to continue playing, allow them to do so
			if(command.toLowerCase().equals("n")){
				break;
			}else while(!command.toLowerCase().equals("y")){
				//prompt the user to fight again
				System.out.println("Fight again [y/n]?");
				command = scanner.next();
				if(command.toLowerCase().equals("n")){
					break;
				}
			}
			
		}
	}
}
