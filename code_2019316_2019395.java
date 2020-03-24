package paas1;

import java.io.*;
import java.util.*;
import java.text.DecimalFormat;

public class code_2019316_2019395 {
public static void main(String[] args) throws Exception {
		
		/*
		 * The execution of the program begins with main(). This is the first function
		 * to be executed when the assembler is loaded. It contains the function call to
		 * Pass1 and Pass2 of the assembler.
		 */

		int f=-1;
		if(has_end())
			f=paas_one();
		else
			System.out.println("ERROR: The code does not have a stop condition");
		if(f==0)
			paas_two();
	}
	public static int paas_one() throws Exception {
		
		/*
		 * This is the Pass1 of the Two-Pass Assembler. It is called by the main()
		 * function. This main objective of this function is to prepare the SymbolTable,
		 * OnlySymbolTable and Paas1 text files. If there are error in the assembly
		 * code, then they are detected by this function.
		 */
		
		boolean more_input = true;
		long location_counter = 0;
		int g2=-1,g4=-1,e=0;
		String type, symbol, label, opcode, opcode_binary, Line;
		FileInputStream myObj = new FileInputStream("sample.txt");
		Scanner myReader = new Scanner(myObj);
		intialize_table();
		while (myReader.hasNext()) {

			if(location_counter>1000000000)
			{
				e=1;
				System.out.println("ERROR: The memory is full");
				break;
			}
			Line = myReader.nextLine();
			// String Line= read_next_line(location_counter);
			String c = line_comment(Line);
			if (c != null) 
			{

				String[] line = c.split(" ");
				if(line.length>3) 
				{
					e=1;
					System.out.println("ERROR: opcode supplied with too many operands");
					break;
				}
				/*if(line.length==3)//need to figure this out
				{
					int t=check_opcode(line[0]);
					if(t!=0)
					{
						e=1;
						System.out.println("ERROR: opcode supplied with too many operands");
						break;
					}
				}*/
				type = get_instruction_type(c);
				if(type==null)
				{
					if(line.length==2) 
					{
						e=1;
						System.out.println("ERROR:Invalid opcode");
						break;
					}
					else if(line.length==3) 
					{
						e=1;
						System.out.println("ERROR:Invalid opcode");
						break;
					}
					else
					{
						e=1;
						System.out.println("ERROR:Please specify the opcode");
						break;
					}
				}
				if (((type!=null)&&(type.equals("2")==true) || (type.equals("3")==true))) 
				{
					symbol = check_for_symbol(line, location_counter);
					if (symbol != null && symbol.equals("long")==false)
						enter_new_symbol(symbol, location_counter);
					else if(symbol.equals("long")==true)
					{
						e=1;
						System.out.println("ERROR: The specified exceed seven charecters");
						break;
					}
					else
					{
						e=1;
						System.out.println("ERROR:Please specify the operand");
						break;
					}
					label = check_for_label(line, location_counter, type);
					if (label != null)
						g2=enter_new_label(label, location_counter); 
					if(g2==0) 
					{
						e=1;
						System.out.println("ERROR:ERROR : DUPLICATE LABELS CAN NOT EXIST IN THE TABLE");
						break;
					}
					opcode = exteract_opcode(line, type);
					//if(opcode==null)
						//new Exception("ERROR: PLEASE SPECIFY THE OPCODE");
					opcode_binary = binary_of_opcode(opcode);
					//if (opcode_binary == null)
						//knew Exception("ERROR:Invalid opcode");
					write_paas1_file(location_counter, opcode_binary, line, type);
					location_counter = location_counter + 1;

				} 
				else if ((type!=null)&&(type.equals("1")==true)) 
				{
					label = check_for_label(line, location_counter, type);
					if (label != null)
						enter_new_label(label, location_counter);
					opcode = exteract_opcode(line, type);
					if (opcode.equals("STP")==true) 
					{
						if(label==null)
						{
							if(line.length>1)
							{
								System.out.println("ERROR: No operand required");
								break;
							}
						}
						opcode_binary = binary_of_opcode(opcode);
						System.out.println("PAAS 1 ENDS");
						write_paas1_file(location_counter, opcode_binary, line, type);
						location_counter = location_counter + 1;
						more_input = false;
						
					} 
					else 
					{

						if(label==null)
						{
							if(line.length>1)
							{
								System.out.println("ERROR: No operand required");
								break;
							}
						}
						opcode_binary = binary_of_opcode(opcode);
						write_paas1_file(location_counter, opcode_binary, line, type);
						location_counter = location_counter + 1;// if the instruction is not end then it could only be
																// clr which is a 4 bit istruction

					}

				} 
				else if ((type!=null)&&(type.equals("4")))
				{
					String value=null;
					symbol = exteract_symbol(line);
					if(line.length==3)
						value = exteract_value(line);
					g4=search_insert(location_counter, symbol, value);
					if(g4!=0)
					{
						System.out.println("ERROR: Symbol has been defined more than once");
						break;
					}
					location_counter = location_counter + 1;
				}
			}
		}
		return e;

	}
	public static void intialize_table() throws Exception {
		
		/*
		 * This function is used to create the output text files. SymbolTable, Pass1 and
		 * OnlySymbolTable text files are created here if they don�t already exist in
		 * the system. But if such files exist in the system, then these files are
		 * cleared i.e, all the previous data gets erased.
		 */
		
		File file = new File("symboltable.txt");
		file.createNewFile();
		File file1 = new File("paas1.txt");
		file1.createNewFile();
		File file2 = new File("onlysymboltable.txt");
		file2.createNewFile();
		
		
		FileWriter fwOb = new FileWriter("symboltable.txt", false); 
        PrintWriter pwOb = new PrintWriter(fwOb, false);
        pwOb.flush();
        pwOb.close();
        fwOb.close();
        FileWriter fwOb1 = new FileWriter("paas1.txt", false); 
        PrintWriter pwOb1 = new PrintWriter(fwOb, false);
        pwOb1.flush();
        pwOb1.close();
        fwOb1.close();
        FileWriter fwOb2 = new FileWriter("onlysymboltable.txt", false); 
        PrintWriter pwOb2 = new PrintWriter(fwOb, false);
        pwOb2.flush();
        pwOb2.close();
        fwOb2.close();

	}
	public static String line_comment(String line) {
		
		/*
		 * This function is to tell whether the passed string is a comment or not. If
		 * the whole string is a comment ,i.e, it�s first character is �@�, then this
		 * function will return Null. But if that is not the case, then this returns the
		 * paased string after removening all part of the string which is after �@� (if
		 * there is character after �@�).
		 */ 

		String l ="";
		for (int i = 0; i < line.length(); i++) {
			if (line.charAt(i) != '@')
				l += line.charAt(i);
			else if (line.charAt(i) == '@')
				break;
		}
		if(l.length()==0)
			return null;
		else 
			return l;
	}
	public static boolean has_end() throws Exception {
		
		/*
		 * This function returns true if there is an end statement in the assembly
		 * program and returns false if there is no end statement in the assembly
		 * program.
		 */
		FileInputStream fis = new FileInputStream("sample.txt");
		Scanner sc = new Scanner(fis);
		while (sc.hasNextLine()) 
		{
			String Ins = sc.nextLine();
			if (Ins.contains("STP"))
				return true;
			
		}
		sc.close();
		return false;
	}
	public static String get_instruction_type(String l) throws Exception {
		
		/*
		 * In this function, we will search each opcode in the instruction table. And
		 * find which opcode is used in the instruction using find substring function
		 * and then we will return the respective type value mentioned in the text file
		 * of instruction table.
		 */
		
			FileInputStream fis = new FileInputStream("2019316_2019395_instructiontable.txt");
			Scanner sc = new Scanner(fis);
			while (sc.hasNextLine()) 
			{
				String Ins = sc.nextLine();
				String[] ins = Ins.split(" ");
				if (l.contains(ins[1]))
					return ins[2];
				
			}
			sc.close();
			return null;

	}
	public static int check_opcode(String ops) throws Exception
	{
		/*
		 * This function checks each line of instruction in the assembly code to know
		 * whether the opcode is present in the instruction or not. If the opcode is
		 * present, then the function returns the opcode and if it is not present, then
		 * the function returns Null.
		 */
		
		FileInputStream myObj = new FileInputStream("2019316_2019395_instructiontable.txt");
		Scanner myReader = new Scanner(myObj);
		int flag = 0;
		
		while (myReader.hasNextLine()) 
		{
			String data = myReader.nextLine();
			if (data.contains(ops))
				flag = 1;
		}
		myReader.close();
		myObj.close();
		return flag;
	}
	public static String check_for_symbol(String[] l, long loc) {
		
		/*
		 * This function returns Null if the passed array does not contain the symbol.
		 * But if the array contains the symbol then it returns the symbol used in the
		 * instruction.
		 */
		
		if (l.length == 1)  	// as the in instruction of the type 2 or 3 is a operand based instruction so if
								// the instruction has
			
			return null;  		 // only one value which is obviously is opcode so there is no operand hence it
						   		 // should show an error and if the symbol is too long them long is returned
		else
		{
			String symbol=l[(l.length)- 1];
			if(symbol.length()<7)
				return l[(l.length)- 1];
			else
				return "long";
				
		}
			
	}
	public static void enter_new_symbol(String symbol, long loc) throws Exception 
	{
		/*
		 * This function enters the passed symbol in the OnlySymbolTable if it is
		 * already not present in the OnlySymbolTable file.
		 */
		
			FileInputStream myObj = new FileInputStream("onlysymboltable.txt");
			Scanner myReader = new Scanner(myObj);
			int flag = 0;
			
			while (myReader.hasNextLine()) 
			{
				String data = myReader.nextLine();
				if (data.contains(symbol))
					flag = 1;
			}
			myReader.close();
			myObj.close();
			writesymbol(flag,symbol);
			
	}
	public static void writesymbol(int f,String s) throws Exception
	{
		// This just a helper function to enter_new_symbol.
		
		FileWriter fw = new FileWriter("onlysymboltable.txt", true);
		if (f!= 1) 
		{
			fw.write(s+"\r\n");
			fw.close();
		}
	}
	public static String check_for_label(String[] l, long loc, String t) {
		
		/*
		 * This function is to check if the passed line of instruction contains a label
		 * or not. If it contains the label, then it returns the label and if it does
		 * not contain the label, then it returns Null.
		 */
		if (t.equals("2")==true || t.equals("3")==true) 
		{
			if (l.length == 2)				// if type 2 or type 3 instruction has only two parts then there will be no
											// label(it only has opcode and operand)
				return null;
			else if (l.length == 3)			// if type 2 or type 3 instruction has three parts then there will be a label
				return l[0];
			else
				return null;				// just to return any value to follow the syntax ,it has no significance
		}
		else 
		{
			if (l.length == 2)
				return l[0];
			else
				return null;
		}
	}
	public static int enter_new_label(String label, long loc) throws Exception 
	{
		/*
		 * In this function, we will first check whether the given label exists in the
		 * symbol table or not. If the given label is not present in the table, then add
		 * the label in the symbol table and it's location as the value of the
		 * location_counter. If the label already exists in the table then throw an
		 * error as duplicate label names can not exist in the code. NOTE: Labels and
		 * Symbols both are stored in the same file called the Symbol Table file
		 */
		
			FileInputStream myObj = new FileInputStream("symboltable.txt");
			Scanner myReader = new Scanner(myObj);
			int flag = 0;
			Number l;
			String f=null;
			
			while (myReader.hasNextLine()) 
			{
				String data = myReader.nextLine();
				if (data.contains(label))
					flag = 1;
			}
			myReader.close();
			myObj.close();
			if(flag !=1)
			{
				writelabel(label,loc);
				return 1;
			}
			else 
				return 0;
			
	}
	public static void writelabel(String l,long loc) throws Exception
	{
		 // This just a helper function to enter_new_label.
	
		String fa=null;
		int ll=0;
		FileWriter fw = new FileWriter("symboltable.txt", true);
		fa= Long.toBinaryString(loc);
		ll=Integer.parseInt(fa);
		DecimalFormat df = new DecimalFormat("00000000"); 
		fw.write(l+" "+ df.format(ll)+"\r\n");
		fw.close();
	}
	public static String exteract_opcode(String[] l, String t) 
	{
		/*
		 * This function is used to return the opcode in the given instruction. But if
		 * the opcode is not present in the given instruction, then it returns Null.
		 */

		if (t.equals("2") || t.equals("3")) 
		{
			if (l.length == 2)			// in the instruction has only 2 parts i.e no label the opcode is the first part
				return l[0];
			else if (l.length == 3)		// in the instruction has 3 part i.e has label too then opcode is in the second
										// part
				return l[1];
			else
				return null;			// just for syntax,no significance
		} 
		else 
		{
			if (l.length == 1)			// if instruction of type 1 has opcode the opcode in the first part
				return l[0];
			else if (l.length == 2)		// if instruction of the type 1 has label and opcode the opcode in the second
										// part
				return l[1];
			else
				return null;			// just for syntax,no significance
		}
	}
	public static String  binary_of_opcode(String opcode) throws Exception
	{
		/*
		 * This function searches the given opcode in the instruction table and returns
		 * the binary value mentioned in the instruction file against the specified
		 * opcode. But if the given opcode does not exists in the instruction table,
		 * then it returns null and reports an error as it is an invalid opcode.
		 */


			FileInputStream myObj = new FileInputStream( "2019316_2019395_instructiontable.txt");
			Scanner myReader = new Scanner(myObj);
			int flag = 0;
			//int binary=0;
			String binary=null;
			while (myReader.hasNextLine()) 
			{
				String data = myReader.nextLine();
				if (data.contains(opcode))
				{
					flag = 1;
					String[] d  = data.split(" ");
					binary= d[0];
					break;
				}	
			}
			if (flag != 1) {
				myReader.close();
				return null;
			}
			
			else
			{
				//DecimalFormat df = new DecimalFormat("00000000"); 
				myReader.close();	
				//return df.format(binary);
				return binary;
			}
				
			
	}
	public static void write_paas1_file(long loc, String opcode_binary, String[] line, String type) throws Exception {
		/*
		 * This function writes the (location counter value): (binary value of the
		 * opcode): (operand as it is (not in binary terms)) in the paas1 text file. If
		 * there is label in the code file, it will not be written in the paas1 file as
		 * type 2 and type 3 instructions are imperative instruction which will be
		 * written in this way only. NOTE: Since the operands are not in binary terms,
		 * hence they will be converted to the binary addresses in the second pass.
		 */
		FileWriter fw = new FileWriter("paas1.txt", true);
		if (type.equals("2")==true || type.equals("3")==true) 
			{

				fw.write(opcode_binary + " " + line[line.length - 1]+"\n");

				// System.out.println("Data Successfully appended into file");
			} 
		else 
			{
				if (opcode_binary == "0000") 
				{
					fw.write(opcode_binary+"\n");

					// System.out.println("Data Successfully appended into file");
				} 
				else
				{
					fw.write(opcode_binary+"\n");
					// pw.println();
					// pw.println();

					// System.out.println("Data Successfully appended into file");
					
				}

			}
		fw.close();
		}
	public static String exteract_symbol(String[] l) {
		// This function is simply used to extract the symbol from the string.
		return l[1];
	}
	public static String exteract_value(String[] l) {
		// This function is simply used to extract the value from the string
		return l[2];
	}
	public static int search_insert(long loc, String symbol, String val) throws Exception 
	{
		/*
		 * This function searches the variable in the symbol table and on finding it.
		 * The function assigns the variable an 8 bit address against it , i.e, the
		 * address is nothing but 8 bit long location counter value and the function
		 * also assigns the value of the symbol against the symbol after the address.
		 * 
		 */
		
			FileInputStream myObj = new FileInputStream("onlysymboltable.txt");
			Scanner myReader = new Scanner(myObj);
			int k=-1;
			while (myReader.hasNextLine()) 
			{
				String data = myReader.nextLine();
				if (data.contains(symbol))
				{
					k=writein(symbol,loc,val);
					break;
				}
			}
			myReader.close();
			myObj.close();
			return k;

		
	}
	public static int writein(String s,long loc,String val) throws Exception
	{
		// This just a helper function to search_insert().

		FileInputStream myObj = new FileInputStream("symboltable.txt");
		Scanner myReader = new Scanner(myObj);
		int f=0;
		while (myReader.hasNextLine()) 
		{
			String data = myReader.nextLine();
			if (data.contains(s))
			{
				f=1;
				break;
			}
		}
		myReader.close();
		myObj.close();
		if(f==0)
		{
			FileWriter fw = new FileWriter("symboltable.txt",true);
			String fa= Long.toBinaryString(loc);
			int ll=Integer.parseInt(fa);
			DecimalFormat df = new DecimalFormat("00000000"); 
			fw.write(s+" " + df.format(ll) + " " + val+"\n");
			fw.close();
		}
		return f;
	}
	public static void paas_two() throws Exception {
		
		/*
		 * This function�s main objective is to make the output file by using data in
		 * the SymbolTable file, Pass1 file and RegisterTable file. When this fuction
		 * gets finished the machine code of the passed assembly code is available in
		 * the Output file.
		 */
		boolean more_input = true;
		
		intialize_outputfile();
		
		String symbol_binary, Line = null, register_binary;
		int location_counter = 0;
		
		FileInputStream myObj = new FileInputStream("paas1.txt");
		Scanner myReader = new Scanner(myObj);
		
		while (more_input && myReader.hasNext()) {
			Line = myReader.nextLine();
			String[] line = Line.split(" ");
			if (line.length == 2) {
				symbol_binary = check_symboltable(line[1]);
				register_binary = check_registertable(line[1]);
				if (symbol_binary != null)
					write_outputfile(line[0], symbol_binary);
				else if (register_binary != null)
					write_outputfile(line[0], register_binary);
				else
				{
					System.out.println("ERROR: Symbol "+line[1]+" not declared but used");
					break;
				}
				location_counter = location_counter + 1;
			} 
			else 
			{
				if (line[0].equals("1100")==false) 
				{
					write_outputfile(line[0], null);
					location_counter = location_counter + 1;
				} 
				else 
				{
					write_outputfile(line[0], null);
					System.out.println("PAAS 2 ENDS");
					more_input = false;
					// finish_up();
				}
			}
		}
		myReader.close();
		myObj.close();

	}
	public static void intialize_outputfile() throws Exception {
		/*
		 * This function creates the file where the output i.e the machine language code
		 * of the given assembly language code will be written. It creates the file in
		 * the same directory where the other files related to this code are saved for
		 * easy access.
		 */
		File file = new File("output.txt");
		file.createNewFile();
			//System.out.println("file created");
		FileWriter fwOb = new FileWriter("output.txt", false); 
        PrintWriter pwOb = new PrintWriter(fwOb, false);
        pwOb.flush();
        pwOb.close();
        fwOb.close();

	}
	public static String check_symboltable(String s) throws Exception {
		/*
		 * This function check the given symbol s in the symbol table. If the symbol s
		 * is present in the symbol table then it would return the string stored beside
		 * the the name of the symbol . as each line in the symbol table has either
		 * three parts(symbol name:symbol address:symbol value) or two parts(symbol
		 * name: symbol address).The purpose of this function is to return symbol
		 * address if the symbol is present in the symboltable or return null if the
		 * symbol is not not present in the symbol table.
		 */
		FileInputStream myObj = new FileInputStream("symboltable.txt");
		Scanner myReader = new Scanner(myObj);
		String[] line;
		while (myReader.hasNextLine()) 
		{
			String data = myReader.nextLine();
			if (data.contains(s)) 
			{
				line=data.split(" ");
				return line[1];
			}
		}
		
		myReader.close();
		myObj.close();
		return null;
		
		
	}
	public static void write_outputfile(String opcode, String symbol) throws Exception 
	{
		/*
		 * this function writes the final machine code for the present instruction in
		 * the output file. the main task is to write the opcode symbol space separated
		 * as it is in the output file when symbol is not null.but if the symbol is null
		 * the you have to write the opcode only in the output file.
		 * 
		 */
		FileWriter fw = new FileWriter("output.txt", true);
		if(symbol==null)
		{
			fw.write(opcode+" "+"\n");
		}
		else
		{
			fw.write(opcode + " " + symbol+"\n");
		}
		
		fw.close();
	}
	public static String check_registertable(String l) throws Exception {
		/*
		 * this function returns the register location of the particular register
		 * specified in the instruction by searching the registertable file (which
		 * contains the location of the register numbersi.e contains address of R1,R2
		 * etc) but if the String l is not found in the register file the the return
		 * null
		 * 
		 * 
		 */
		FileInputStream myObj = new FileInputStream("2019316_2019395_registertable.txt");
		Scanner myReader = new Scanner(myObj);
		String[] line;
		while (myReader.hasNextLine()) 
		{
			String data = myReader.nextLine();
			if (data.contains(l)) 
			{
				line=data.split(" ");
				return line[1];
			}
		}
		
		myReader.close();
		myObj.close();
		return null;
	}
}

