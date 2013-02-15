package homework1;
import homework1.Tree;
import homework1.TreeNode;
import homework1.Pretreatment;
import java.io.*;
import java.util.*;
/*
 * This class is to implement all Unranked Retrieval 
 */
public class Parser {

	private String query;     //a query provided by users
	private Tree query_tree;  //query tree constructed with query
	private int operator_counter;// count the number of operators in query 
	private TreeNode RootNode; //root of this query tree
	public Parser(String que)  //Constructor
	{
		this.query = que;
		this.query_tree = new Tree();
		this.RootNode = new TreeNode("this is root");
        this.query_tree.setRoot(this.RootNode);
		this.query_tree.ConstructTree(this.RootNode, this.query);
		this.operator_counter = 0;
		this.query_tree.print_Tree(this.RootNode.get_child().get(0));
	}
	
	public void set_query(String que)
	{
		this.query = que;
	}
	public TreeNode get_root()
	{
		return this.RootNode;
	}
	public String Parse_Tree(TreeNode branchNode) //searching results for query tree
	{
		String file_path_head = new String("resource/"); //set file path in the project
		if(branchNode.get_isop_node() == true)// corresponding method when current node contains operator  
		{
			if(branchNode.get_vl() == "OR") // if operator is OR
			{
				this.operator_counter ++;
				
				File newfile = new File(file_path_head + this.operator_counter+".txt");// save the results of this operator
				try{
				     newfile.createNewFile();
				     for(int i = 0; i < branchNode.get_child().size(); i ++)
				     {
				    	 if(i == 0)
				    	 {
				    		 String path0 = new String(Parse_Tree(branchNode.get_child().get(i)));//recursively call this function
				    		 File file = new File(file_path_head + path0);
				    		 FileInputStream fis = new FileInputStream(file);     
				 	         InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
				 	         BufferedReader reader = new BufferedReader(isr);
				 	         String tempstr = new String("");
				 	         BufferedWriter output = new BufferedWriter(new FileWriter(newfile));
				 	         if(path0.charAt(path0.length()-1) == 'v') //fixation to first line
				 	         {
				 	        	 tempstr = reader.readLine();
				 	         }
				 	         while((tempstr = reader.readLine()) != null)
				 	         {
				 	        	output.write(tempstr);
				 	        	output.write("\n");
				 	         }
				 	         output.close();
				 	         reader.close();
				    	 }
				    	 else
				    	 {
				    		 File file = new File(file_path_head + new String(Parse_Tree(branchNode.get_child().get(i))));//recursively call this function
				    		 Exe_OR(newfile, file); //update the current results by processing OR operator with 2 lists
				    	 }
				     }
				   }
				catch(IOException e)
				{
					System.out.println("The error in creating Files OR!");
				}
				System.out.println(this.operator_counter + ".txt");
				return new String(this.operator_counter + ".txt");
			}
			else if(branchNode.get_vl() == "AND")//if the operator is AND
			{
				this.operator_counter ++;
				
				File newfile = new File(file_path_head + this.operator_counter+".txt");//save the results for this operator
				try{
				     newfile.createNewFile();
				     for(int i = 0; i < branchNode.get_child().size(); i ++)
				     {
				    	 if(i == 0)
				    	 {
				    		 String path0 = new String(Parse_Tree(branchNode.get_child().get(i)));//recursively call this function
				    		 File file = new File(file_path_head + path0);
				    		 FileInputStream fis = new FileInputStream(file);     
				 	         InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
				 	         BufferedReader reader = new BufferedReader(isr);
				 	         String tempstr = new String("");
				 	         BufferedWriter output = new BufferedWriter(new FileWriter(newfile));
				 	         if(path0.charAt(path0.length()-1) == 'v') //fixation to first line
				 	         {
				 	        	 tempstr = reader.readLine();
				 	         }
				 	         while((tempstr = reader.readLine()) != null)
				 	         {
				 	        	output.write(tempstr);
				 	        	output.write("\n");
				 	         }
				 	         output.close();
				 	         reader.close();
				    	 }
				    	 else
				    	 {
				    		 File file = new File(file_path_head + new String(Parse_Tree(branchNode.get_child().get(i))));
				    		 Exe_AND(newfile, file);// processing AND operator in 2 files by calling Exe_AND
				    	 }
				     }
				   }
				catch(IOException e)
				{
					System.out.println("The error in creating Files!");
				}
				
				return new String(this.operator_counter + ".txt");
			}
			else// if the operator is NEAR
			{
				int n_near = get_n(branchNode);
				
				this.operator_counter ++;
				
				File newfile = new File(file_path_head + this.operator_counter+".txt");// save the result of NEAR
				try{
					newfile.createNewFile();
					for(int i = 0; i < branchNode.get_child().size(); i ++)
					{
						
						if(i == 0)// if this is the first child, we just put its result in the file for comparing afterwards
						{
							 String path0 = new String(branchNode.get_child().get(i).get_vl() + ".inv");
				    		 File file = new File(file_path_head + path0);
				    		 FileInputStream fis = new FileInputStream(file);     
				 	         InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
				 	         BufferedReader reader = new BufferedReader(isr);
				 	         String tempstr = new String("");
				 	         BufferedWriter output = new BufferedWriter(new FileWriter(newfile));
				 	         if(path0.charAt(path0.length()-1) == 'v') //fixation to first line
				 	         {
				 	        	 tempstr = reader.readLine();
				 	         }
				 	         while((tempstr = reader.readLine()) != null)
				 	         {
				 	        	output.write(tempstr);
				 	        	output.write("\n");
				 	         }
				 	         output.close();
				 	         reader.close();
						}
						else// if this is not the first operand, just fetch the index and process NEAR by calling Exe_NEAR function
						{
							File file = new File(file_path_head + branchNode.get_child().get(i).get_vl() + ".inv");
				    		Exe_NEAR(newfile,file,n_near);
						}
					}
					
					
				}
				catch(IOException e){
					System.out.println("Error in NEAR!");
				}
				return new String(this.operator_counter + ".txt");
			}
		}
		else                                  //if it is just operand, we simply fetch the inverted list
		{
			return new String(branchNode.get_vl() + ".inv");
		}
	}
	
	public void Exe_AND(File newfile, File file)  //Operator AND Execution 
	{
		ArrayList<Integer> result_list = new ArrayList<Integer>();
		try{
		FileInputStream fist = new FileInputStream(newfile);     
	    InputStreamReader isrt = new InputStreamReader(fist, "UTF-8");
	    BufferedReader readert = new BufferedReader(isrt);	
			
		FileInputStream fis = new FileInputStream(file);     
        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        BufferedReader reader = new BufferedReader(isr);
  
        String newfile_str = new String(readert.readLine());
        String file_str = new String(reader.readLine());
        
        String newfilepath = new String(newfile.getPath());//first line fix
        String filepath = new String(file.getPath());
        if(newfilepath.charAt(newfilepath.length()-1) == 'v')
        	newfile_str = readert.readLine();
        if(filepath.charAt(filepath.length()-1) == 'v')
        	file_str = reader.readLine();
        
        while((newfile_str != null) && (file_str != null))
        {
        	if(extractInteger(newfile_str) > extractInteger(file_str))
        	{
        		file_str = reader.readLine();
        	}
        	else if(extractInteger(newfile_str) < extractInteger(file_str))
        	{
        		newfile_str = readert.readLine();
        	}
        	else
        	{
        		result_list.add(extractInteger(newfile_str));
        		file_str = reader.readLine();
        		newfile_str = readert.readLine();
        	}
        }
        readert.close();
        reader.close();
        FileWriter fw =  new FileWriter(newfile);
        for(int i = 1; i <= result_list.size(); i ++)
        {
        	fw.write(result_list.get(i - 1).toString());
        	fw.write("\n");
        }
        
        
        fw.close();
		}
		catch(IOException e)
		{
			System.out.println("error in Exe_AND");
		}
	}
	public void Exe_OR(File newfile, File file) // Operator OR Execution
	{
		ArrayList<Integer> result_list = new ArrayList<Integer>();
		try{
		FileInputStream fist = new FileInputStream(newfile);     
	    InputStreamReader isrt = new InputStreamReader(fist, "UTF-8");
	    BufferedReader readert = new BufferedReader(isrt);	
			
		FileInputStream fis = new FileInputStream(file);     
        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        BufferedReader reader = new BufferedReader(isr);
  
        String newfile_str = new String(readert.readLine());
        String file_str = new String(reader.readLine());
        
        String newfilepath = new String(newfile.getPath());//first line fix
        String filepath = new String(file.getPath());
        if(newfilepath.charAt(newfilepath.length()-1) == 'v')
        	newfile_str = readert.readLine();
        if(filepath.charAt(filepath.length()-1) == 'v')
        	file_str = reader.readLine();
        
        while((newfile_str != null) || (file_str != null))
        {
        	if((newfile_str == null) && (file_str != null))
        	{
        		while(file_str != null)
        		{
        			result_list.add(extractInteger(file_str));
        			file_str = reader.readLine();
        		}
        	}
        	else if((file_str == null) && (newfile_str != null))
        	{
        		while(newfile_str != null)
        		{
        			result_list.add(extractInteger(newfile_str));
        			newfile_str = readert.readLine();
        		}
        	}
        	else
        	{
        		if(extractInteger(newfile_str) > extractInteger(file_str))
        		{
        			result_list.add(extractInteger(file_str));
        			file_str = reader.readLine();
        		}
        		else if(extractInteger(newfile_str) < extractInteger(file_str))
        		{
        			result_list.add(extractInteger(newfile_str));
        			newfile_str = readert.readLine();
        		}
        		else
        		{
        			result_list.add(extractInteger(newfile_str));
        			file_str = reader.readLine();
        			newfile_str = readert.readLine();
        		}
        	}
        }
        readert.close();
        reader.close();
        FileWriter fw =  new FileWriter(newfile);
        for(int i = 1; i <= result_list.size(); i ++)
        {
        	fw.write(result_list.get(i - 1).toString());
        	fw.write("\n");
        }
        
        
        fw.close();
		}
		catch(IOException e)
		{
			System.out.println("error in Exe_OR");
		}
	}
	public void Exe_NEAR(File newfile, File file,int n_near) // Operator NEAR execution
	{
		
		ArrayList<String> output_str = new ArrayList<String>();
		try{
			FileInputStream fist = new FileInputStream(newfile);     
		    InputStreamReader isrt = new InputStreamReader(fist, "UTF-8");
		    BufferedReader readert = new BufferedReader(isrt);	
				
			FileInputStream fis = new FileInputStream(file);     
	        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
	        BufferedReader reader = new BufferedReader(isr);
	  
	        String newfile_str = new String(readert.readLine());
	        String file_str = new String(reader.readLine());
	        
	        String newfilepath = new String(newfile.getPath());//first line fix
	        String filepath = new String(file.getPath());
	        if(newfilepath.charAt(newfilepath.length()-1) == 'v')
	        	newfile_str = readert.readLine();
	        if(filepath.charAt(filepath.length()-1) == 'v')
	        	file_str = reader.readLine();
	       
	        while((newfile_str != null) && (file_str != null))
	        {
	        	
	        	if(extractInteger(newfile_str) > extractInteger(file_str))
	        	{
	        		file_str = reader.readLine();
	        	}
	        	else if(extractInteger(newfile_str) < extractInteger(file_str))
	        	{
	        		newfile_str = readert.readLine();
	        	}
	        	else
	        	{
	        		
	        		ArrayList<Integer> newfile_pos = new ArrayList<Integer>();
	        		ArrayList<Integer> file_pos = new ArrayList<Integer>();
	        		add_pos(newfile_str,newfile_pos);
	        		add_pos(file_str,file_pos);
	        		
	        		int newfile_pos_ptr = 0;
	        		int file_pos_ptr = 0;
	        		while((newfile_pos_ptr <= newfile_pos.size() - 1) && (file_pos_ptr <= file_pos.size() - 1))
	        		{
	        			if((file_pos.get(file_pos_ptr) - newfile_pos.get(newfile_pos_ptr)) > n_near)
	        			{
	        				newfile_pos_ptr ++;
	        			}
	        			else if((file_pos.get(file_pos_ptr) - newfile_pos.get(newfile_pos_ptr)) < 0)
	        			{
	        				file_pos_ptr ++;
	        			}
	        			else
	        			{
	        				
	        				output_str.add(file_str);
	        				break;
	        			}
	        		}
	        		newfile_str = readert.readLine();
	        		file_str = reader.readLine();
	        	}
	        }
	       
	        readert.close();
	        reader.close();
	        FileWriter fw =  new FileWriter(newfile);// write the results in file
	        
	        for(int i = 1; i <= output_str.size(); i ++)
	        {
	        	fw.write(output_str.get(i - 1));
	        	fw.write("\n");
	        }
	        
	        
	        fw.close();
		}
		catch(IOException e)
		{
			System.out.println("Error in NEAR");
		}
	}
	public int extractInteger(String str)// extract docID from a line in inverted list
	{
		for(int i = 0;i <= str.length() - 1; i ++)
		{
			if(str.charAt(i) == ' ')
			{
				return Integer.parseInt(str.substring(0, i));
			}
		}
		return Integer.parseInt(str);
	}
	public void add_pos(String file,ArrayList<Integer> file_pos)// add NEAR positions into an arraylist
	{
		
		int i = 0;
		int count = 0;
		while(count != 3)
		{
			if(file.charAt(i) == ' ')
			{
				
				count ++;
			}
			i ++;
		}
		
		while(i <= file.length() - 1)
		{
			int temp_i = i;
			while(file.charAt(i) != ' ' && i <= file.length() - 1)
			{
				i ++;
			}
			
			if(i > file.length() - 1)
				file_pos.add(Integer.parseInt(file.substring(temp_i)));
			else
			{
                
				file_pos.add(Integer.parseInt(file.substring(temp_i, i)));
				
			}
			i ++;    
		}
	}
    public int get_n(TreeNode branchNode)// get the integer n in NEAR/n
    {
    	
    	int i = 0;
    	String temp = new String(branchNode.get_vl());
    	for(; i <= temp.length() - 1;i ++)
    	{
    		if(temp.charAt(i) == '/')
    		{
    			i ++;
    			break;
    		}
    	}
    	return Integer.parseInt(temp.substring(i));
    	
    }
    public void final_rank(int query_ID)// fetch the results in 1.txt and rank them and write the ranked results in final_result.txt
    {
    	File readfile = new File("resource/1.txt");//file storing results that have not been ranked
    	File final_result = new File("resource/final_result.txt");// target file to store final results
    	try{
    		FileInputStream fist = new FileInputStream(readfile);     
		    InputStreamReader isr = new InputStreamReader(fist, "UTF-8");
		    BufferedReader reader = new BufferedReader(isr);
		    final_result.createNewFile();
		    FileWriter fw =  new FileWriter(final_result,true);
		    String temp_str = new String();
		    int counter = 1;
		    while(((temp_str = reader.readLine()) != null) && (counter <= 100)) //get the first 100 files into final_result.txt
		    {
		    	int i = 0;
		    	while(( i <= temp_str.length() - 1) &&(temp_str.charAt(i) != ' '))
		    		i ++;
		    	if(i > temp_str.length() - 1)
		    		;
		    	else
		    		temp_str = temp_str.substring(0, i);
		    	
		    	fw.write(String.valueOf(query_ID));
				fw.write("\t");
				fw.write("Q0");
				fw.write("\t");
				fw.write(temp_str);
				fw.write("\t");
				fw.write(String.valueOf(counter));
				fw.write("\t");
				fw.write("1");
				fw.write("\t");
				fw.write("run-1");
				fw.write("\n");  
		    	
				counter ++;
		    }
		    reader.close();
		    fw.close();
    	}
    	catch(IOException e)
    	{
    		System.out.println("Error in final ranking!");
    	}
    	
    }
	public static void main(String[] args){
		/*
		 * The final results are store in resource/final_result.txt.
		 * Below are four kinds of input pattern: single query, unstructured AND set,unstructed OR set,structured set
		 * Important!!!  You should delete the final_result.txt after EVERY execution since the final_result.txt is append written pattern  
		 *               
		 */
	    /*********************this part is for single query execution************/		
		    String initial_query = new String("#AND(espn sports)");
		    Pretreatment newpretreat = new Pretreatment(initial_query);
		    newpretreat.Treat_query();
		    String aft_query = newpretreat.get_aft_str();
		    Parser parse_Tree = new Parser(aft_query);
		    TreeNode testnode = parse_Tree.get_root();
		    
		    parse_Tree.Parse_Tree(parse_Tree.get_root().get_child().get(0));
		    parse_Tree.final_rank(1);    
	        /**************formal input and output for unstructured AND**********************/
	/*	    File input_file = new File("resource/queries.txt");
	        long time_begin = System.currentTimeMillis();
	        try{
	        	FileInputStream fis = new FileInputStream(input_file);     
	        	InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
	        	BufferedReader reader = new BufferedReader(isr);
	        	String queries = new String();
	        	int query_ID;
	        	while((queries = reader.readLine()) != null)
	        	{
	        		int i = 0;
	        		while(queries.charAt(i) != ':')
	        		{
	        			i ++;
	        		}
	        		query_ID = Integer.parseInt(queries.substring(0, i));
	        		queries = queries.substring(i + 1);
	        		String temp_and = new String("#AND(");
	        		temp_and = temp_and.concat(queries);
	        		temp_and = temp_and.concat(")");
	        		queries = temp_and;
	        		System.out.println("The string is" + queries);
	        		Pretreatment new_pretreat = new Pretreatment(queries);
	        	    new_pretreat.Treat_query();
	        	    queries = new_pretreat.get_aft_str();
	        	    
	        	    Parser parse_Tree = new Parser(queries);
	        	    TreeNode testnode = parse_Tree.get_root();
	        	    parse_Tree.Parse_Tree(parse_Tree.get_root().get_child().get(0));
	        	    parse_Tree.final_rank(query_ID);
	        	}
	        	long time_end = System.currentTimeMillis();
	        	long time_consumed = time_end - time_begin;
	        	System.out.println("Run time is :" + time_consumed);
	        }
	        catch(IOException e)
	        {
	        	System.out.println("error!!!!");
	        }      */
	        /**************formal input and output for unstructured OR**********************/
	 /*       File input_file = new File("resource/queries.txt");
	        try{
	        	FileInputStream fis = new FileInputStream(input_file);     
	        	InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
	        	BufferedReader reader = new BufferedReader(isr);
	        	String queries = new String();
	        	long time_begin = System.currentTimeMillis();
	        	int query_ID;
	        	while((queries = reader.readLine()) != null)
	        	{
	        		int i = 0;
	        		while(queries.charAt(i) != ':')
	        		{
	        			i ++;
	        		}
	        		query_ID = Integer.parseInt(queries.substring(0, i));
	        		queries = queries.substring(i + 1);
	        		String temp_and = new String("#OR(");
	        		temp_and = temp_and.concat(queries);
	        		temp_and = temp_and.concat(")");
	        		queries = temp_and;
	        		System.out.println("The string is" + queries);
	        		Pretreatment new_pretreat = new Pretreatment(queries);
	        	    new_pretreat.Treat_query();
	        	    queries = new_pretreat.get_aft_str();
	        	    
	        	    Parser parse_Tree = new Parser(queries);
	        	    TreeNode testnode = parse_Tree.get_root();
	        	    parse_Tree.Parse_Tree(parse_Tree.get_root().get_child().get(0));
	        	    parse_Tree.final_rank(query_ID);
	        	}
	        	 long time_end = System.currentTimeMillis();
	        	 long time_consumed = time_end - time_begin;
	        	 System.out.println("Run time is :" + time_consumed);
	        }
	        catch(IOException e)
	        {
	        	System.out.println("error!!!!");
	        }    */
	        /****************formal input for structured set***************/
	/*	File input_file = new File("resource/struct_set.txt");
        long time_begin = System.currentTimeMillis();
        try{
        	FileInputStream fis = new FileInputStream(input_file);     
        	InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        	BufferedReader reader = new BufferedReader(isr);
        	String queries = new String();
        	int query_ID;
        	while((queries = reader.readLine()) != null)
        	{
        		int i = 0;
        		while(queries.charAt(i) != ':')
        		{
        			i ++;
        		}
        		query_ID = Integer.parseInt(queries.substring(0, i));
        		queries = queries.substring(i + 1);
        		
        		System.out.println("The string is" + queries);
        		Pretreatment new_pretreat = new Pretreatment(queries);
        	    new_pretreat.Treat_query();
        	    queries = new_pretreat.get_aft_str();
        	    
        	    Parser parse_Tree = new Parser(queries);
        	    TreeNode testnode = parse_Tree.get_root();
        	    parse_Tree.Parse_Tree(parse_Tree.get_root().get_child().get(0));
        	    parse_Tree.final_rank(query_ID);
        	}
        	long time_end = System.currentTimeMillis();
        	long time_consumed = time_end - time_begin;
        	System.out.println("Run time is :" + time_consumed);
        }
        catch(IOException e)
        {
        	System.out.println("error!!!!");
        }    */
	}
}
