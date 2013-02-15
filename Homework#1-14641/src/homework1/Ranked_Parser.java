package homework1;
import homework1.Tree;
import homework1.TreeNode;
import homework1.Result_Pair;
import java.io.*;
import java.util.*;
/*
 * This class is to implement all ranked retrieval
 */
public class Ranked_Parser {
	
	private String query;//a query provided by users
	private Tree query_tree;//query tree constructed with query
	private int operator_counter;// count the number of operators in query 
	private TreeNode RootNode;//root of this query tree
	
	public Ranked_Parser(String que)//Constructor
	{
		this.query = que;
		this.query_tree = new Tree();
		this.RootNode = new TreeNode("this is root");
        this.query_tree.setRoot(this.RootNode);
		this.query_tree.ConstructTree(this.RootNode, this.query);
		this.operator_counter = 0;
	}
	public void set_query(String que)
	{
		this.query = que;
	}
	public TreeNode get_root()
	{
		return this.RootNode;
	}
	public String Parse_Tree(TreeNode branchNode)//searching results for query tree in ranked retrieval
	{
		String file_path_head = new String("resource/");// set file path 
		if(branchNode.get_isop_node() == true)
		{
			if(branchNode.get_vl() == "OR")// if the operator is OR
			{
				this.operator_counter ++;
				
				File newfile = new File(file_path_head + this.operator_counter+".txt");
				try{
					newfile.createNewFile();
				     for(int i = 0; i < branchNode.get_child().size(); i ++)
				     {
				    	 if(i == 0)//if this operand is the first one, then just put inverted list into the file
				    	 {
				    		 String path0 = new String(Parse_Tree(branchNode.get_child().get(i)));
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
				    		 Exe_OR(newfile, file);// execute Exe_OR to update results
				    	 }
				     }
				}
				catch(IOException e)
				{
					System.out.println("The error in creating Files OR!");
				}
				
				
			}
			else if(branchNode.get_vl() == "AND")// if the operator is AND
			{
				this.operator_counter ++;
				
				File newfile = new File(file_path_head + this.operator_counter+".txt");
				try{
				     newfile.createNewFile();
				     for(int i = 0; i < branchNode.get_child().size(); i ++)
				     {
				    	 if(i == 0)
				    	 {
				    		 String path0 = new String(Parse_Tree(branchNode.get_child().get(i)));
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
				    		 Exe_AND(newfile, file);
				    	 }
				     }
				   }
				catch(IOException e)
				{
					System.out.println("The error in creating Files!");
				}
				
				
			}
			else   // if the operator is NEAR
			{
				int n_near = get_n(branchNode);
				this.operator_counter ++;
				
				File newfile = new File(file_path_head + this.operator_counter+".txt");
				try{
					newfile.createNewFile();
					for(int i = 0; i < branchNode.get_child().size(); i ++)
					{
						
						if(i == 0)
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
						else
						{
							File file = new File(file_path_head + branchNode.get_child().get(i).get_vl() + ".inv");
				    		Exe_NEAR(newfile,file,n_near);
						}
					}
					
				}
				catch(IOException e){
					System.out.println("Error in NEAR!");
				}
				//count the score
				
					ArrayList<String> docID = new ArrayList<String>();
					ArrayList<Integer> docScore = new ArrayList<Integer>();
					File cal_score = new File(file_path_head + this.operator_counter+".txt");
					try{
					FileInputStream fis = new FileInputStream(cal_score);     
		 	        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
		 	        BufferedReader reader = new BufferedReader(isr);
		 	        String temp = new String();
		 	        while((temp = reader.readLine()) != null)
		 	        {
		 	        	int i = 0;
		 	        	int count_space = 0;
		 	        	while(i <= temp.length() - 1)
		 	        	{
		 	        		if(temp.charAt(i) == ' ')
		 	        		{
		 	        			count_space ++;
		 	        			if(count_space == 1)
		 	        			{
		 	        				docID.add(temp.substring(0, i));
		 	        			}
		 	        		}
		 	        			
		 	        		i ++;
		 	        	}
		 	        	docScore.add(count_space - 2);
		 	        }
		 	        reader.close();
		 	        FileWriter fw =  new FileWriter(cal_score);
			        for(int i = 1; i <= docID.size(); i ++)
			        {
			        	fw.write(docID.get(i - 1));
			        	fw.write(' ');
			        	fw.write(docScore.get(i - 1).toString());
			        	fw.write("\n");
			        }
			        fw.close();
					}
					catch(IOException e){
						System.out.println("Error in calculating score!");
					}
				
				
			}
			
			return new String(this.operator_counter + ".txt");
		}
		else
		{
			return new String(branchNode.get_vl() + ".inv");
		}
		
	}
	public void Exe_AND(File newfile,File file)// detailed AND operating in newfile and file
	{
		ArrayList<Integer> result_list = new ArrayList<Integer>();
		ArrayList<Integer> result_score = new ArrayList<Integer>();
		
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
	        		if(extractTF(newfile_str) > extractTF(file_str))
	        		{
	        			result_score.add(extractTF(file_str));
	        		}
	        		else
	        		{
	        			result_score.add(extractTF(newfile_str));
	        		}
	        		file_str = reader.readLine();
	        		newfile_str = readert.readLine();
	        	}
	        }
	        readert.close();
	        reader.close();
	        FileWriter fw =  new FileWriter(newfile);// compared result written in newfile
	        for(int i = 1; i <= result_list.size(); i ++)
	        {
	        	fw.write(result_list.get(i - 1).toString());
	        	fw.write(" ");
	        	fw.write(result_score.get(i - 1).toString());
	        	fw.write("\n");
	        }
	        
	        
	        fw.close();
		}
		catch(IOException e){
			System.out.println("error in Exe_AND");
		}
	}
	public void Exe_OR(File newfile,File file)// detailed OR operating in newfile and file
	{
		ArrayList<Integer> result_list = new ArrayList<Integer>();
		ArrayList<Integer> result_score = new ArrayList<Integer>();
		
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
	        			result_score.add(extractTF(file_str));
	        			file_str = reader.readLine();
	        		}
	        		
	        	}
	        	else if((file_str == null) && (newfile_str != null))
	        	{
	        		while(newfile_str != null)
	        		{
	        			result_list.add(extractInteger(newfile_str));
	        			result_score.add(extractTF(newfile_str));
	        			newfile_str = readert.readLine();
	        		}
	        	}
	        	else
	        	{
	        		if(extractInteger(newfile_str) > extractInteger(file_str))
	        		{
	        			result_list.add(extractInteger(file_str));
	        			result_score.add(extractTF(file_str));
	        			file_str = reader.readLine();
	        		}
	        		else if(extractInteger(newfile_str) < extractInteger(file_str))
	        		{
	        			result_list.add(extractInteger(newfile_str));
	        			result_score.add(extractTF(newfile_str));
	        			newfile_str = readert.readLine();
	        		}
	        		else
	        		{
	        			result_list.add(extractInteger(newfile_str));
	        			if(extractTF(newfile_str) > extractTF(file_str))
	        			{
	        				result_score.add(extractTF(newfile_str));
	        			}
	        			else
	        			{
	        				result_score.add(extractTF(file_str));
	        			}
	        			file_str = reader.readLine();
	        			newfile_str = readert.readLine();
	        		}
	        	}
	        }
	        readert.close();
	        reader.close();
	        FileWriter fw =  new FileWriter(newfile);// write the compared result in newfile
	        for(int i = 1; i <= result_list.size(); i ++)
	        {
	        	fw.write(result_list.get(i - 1).toString());
	        	fw.write(' ');
	        	fw.write(result_score.get(i - 1).toString());
	        	fw.write("\n");
	        }
	        
	        
	        fw.close();
		}
		catch(IOException e){
			System.out.println("error in Exe_OR");
		}
	}
	public void Exe_NEAR(File newfile, File file,int n_near)//detailed NEAR operating in newfile and file
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
	        		String term_result = new String();// save the result
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
	        				if(term_result.isEmpty() == true)
	        				{
	        					term_result = term_result.concat(get_head(file_str));
	        					term_result = term_result.concat(file_pos.get(file_pos_ptr).toString());
	        				}
	        				else
	        				{
	        					term_result = term_result.concat(" ");
	        					term_result = term_result.concat(file_pos.get(file_pos_ptr).toString());
	        				}
	        				newfile_pos_ptr ++;
	        				file_pos_ptr ++;
	        			}
	        		}
	        		if(term_result.isEmpty() == false){
	        			output_str.add(term_result);
	        		}
	        		newfile_str = readert.readLine();
	        		file_str = reader.readLine();
	        	}
	        }
	        readert.close();
	        reader.close();
	        FileWriter fw =  new FileWriter(newfile);
	        
	        for(int i = 1; i <= output_str.size(); i ++)
	        {
	        	fw.write(output_str.get(i - 1));
	        	fw.write("\n");
	        }
	        
	        
	        fw.close();
		}
		catch(IOException e){
			System.out.println("Error in NEAR");
		}
	}
	public int extractInteger(String str)//extract docID from query string
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
	public int extractTF(String str)// extract score(TF) from query string
	{
		
		int i = 0;
		while(str.charAt(i) != ' ')
		{
			i ++;
		}
		i ++;
		int temp = i;
		while((i <= str.length() - 1) && (str.charAt(i) != ' '))
		{
			i ++;
		}
		if(i > str.length() - 1)
		{
			return Integer.parseInt(str.substring(temp));
		}
		else
			
		    return Integer.parseInt(str.substring(temp, i));
	}
	public int get_n(TreeNode branchNode)// get n from NEAR/n
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
	public void add_pos(String file,ArrayList<Integer> file_pos)// add qualified NEAR position in file_pos for later use
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
			while(i <= file.length() - 1 && file.charAt(i) != ' ')
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
	public String get_head(String str)// get the first three element in one line of inverted lists
	{
		int i = 0;
		int count = 0;
		while(count != 3)
		{
			if(str.charAt(i) == ' ')
			{
				count ++;
			}
			i ++;
		}
		return str.substring(0, i);
	}
	public void add_to_newpair(String temp_str, Result_Pair newpair)
	{
		int i = 0;
		while(temp_str.charAt(i) != ' ')
		{
			i ++;
		}
		newpair.set_docID(Integer.parseInt(temp_str.substring(0, i)));
		i ++;
		int flag = i;
		while(i <= temp_str.length() - 1 && temp_str.charAt(i) != ' ')
		{
			i ++;
		}
		if(i > temp_str.length() - 1)
			newpair.set_score(Integer.parseInt(temp_str.substring(flag)));
		else
			newpair.set_score(Integer.parseInt(temp_str.substring(flag, i)));
	}
	public void final_rank(int query_ID)// rank the results according to docID and score
	{
		File rank_file = new File("resource/" + "1"+".txt");
		ArrayList<Result_Pair> score_pair = new ArrayList<Result_Pair>();
		try{
			FileInputStream fis = new FileInputStream(rank_file);     
 	        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
 	        BufferedReader reader = new BufferedReader(isr);
 	        String temp_str = new String();
 	        while((temp_str = reader.readLine()) != null)
 	        {
 	           Result_Pair newpair = new Result_Pair();
 	           add_to_newpair(temp_str,newpair);
 	           score_pair.add(newpair);
 	        }
 	        ArrayList<Integer> ranked_docid = new ArrayList<Integer>();
 	        ArrayList<Integer> ranked_score = new ArrayList<Integer>();
 	        for(int j = 1;(j <= 100) && (score_pair.size() != 0);j ++)
 	        {
 	        	int max_docID = 0;
	        	int max_score = 0;
	        	int max_node_index = 0;
 	        	for(int i = 0;i <= score_pair.size() - 1;i ++)
 	        	{
 	        		
 	        		if(score_pair.get(i).get_score() > max_score)
 	        		{
 	        			max_docID = score_pair.get(i).get_docID();
 	        			max_score = score_pair.get(i).get_score();
 	        			max_node_index = i;
 	        		}
 	        	}
 	        	ranked_docid.add(max_docID);
 	        	ranked_score.add(max_score);
 	        	score_pair.remove(max_node_index);
 	        }
			File finalfile = new File("resource/" + "final_result.txt");
			finalfile.createNewFile();
			try{
				FileWriter fw =  new FileWriter(finalfile,true);
				for(int i = 1; i <= ranked_docid.size() && i <= 50; i ++)
				{	
				/*	fw.write(String.valueOf(query_ID)); // this is the output formant for checking online
					fw.write("\t");
					fw.write("Q0");
					fw.write("\t");
					fw.write(ranked_docid.get(i - 1).toString());
					fw.write("\t");
					fw.write(String.valueOf(i));
					fw.write("\t");
					fw.write(ranked_score.get(i - 1).toString());
					fw.write("\t");
					fw.write("run-1");
					fw.write("\n");   */
					
					fw.write(String.valueOf(i));// this is the output format for sample result
					fw.write("\t");
					fw.write(ranked_docid.get(i - 1).toString());
					fw.write("\t");
					fw.write(ranked_score.get(i - 1).toString());
					fw.write("\n");
				}
	      
				fw.close();
			}
			catch(IOException e)
			{
				System.out.println("Error in final ranking.");
			}
		}
		catch(IOException e){
			System.out.println("Error in final ranking!");
		}
	}
	/*
	 * The final results are store in resource/final_result.txt.
	 * Below are four kinds of input patterns: single query, unstructured AND set,unstructed OR set,structured set
	 * Important!!!  You should delete the final_result.txt after EVERY execution since the final_result.txt is append written pattern  
	 *               
	 */
	public static void main(String[] args){
	//below is the 30 queries in structured set 	
	//	String initial_query = new String("#NEAR/2(obama family tree)");
	//	String initial_query = new String("#NEAR/2(french lick resort and casino)");
	//	String initial_query = new String("#OR(kcs)");
	//  String initial_query = new String("#NEAR/1(air travel information))");
	//	String initial_query = new String("#AND(espn.title sports)");
	//	String initial_query = new String("#AND(arizona game and fish)");
	//	String initial_query = new String("#AND(porker.title tournaments)");
	//	String initial_query = new String("#OR(wedding budget calculator)");
	//	String initial_query = new String("#OR(volvo.title)");
	//	String initial_query = new String("#OR(euclid)");
	//	String initial_query = new String("#NEAR/1(lower heart rate)");
	//	String initial_query = new String("#OR(ps 2 games.title)");
	//	String initial_query = new String("#OR(atari.title)");
	//	String initial_query = new String("#NEAR/2(website design hosting)");
	//	String initial_query = new String("#OR(gps)");
	//	String initial_query = new String("#NEAR/1(pampered chef)");
	//	String initial_query = new String("#AND(dogs #NEAR/1(for adoption))");
	//	String initial_query = new String("#NEAR/1(disneyland hotel)");
	//	String initial_query = new String("#NEAR/1(orange county convention center)");
	//	String initial_query = new String("#NEAR/1(the music man)");
	//	String initial_query = new String("#AND(hospital #NEAR/1(alexian brothers))");
	//	String initial_query = new String("#OR(president of the NEAR/1(united states))");
	//	String initial_query = new String("#AND(charleston sc uss yorktown)");
	//  String initial_query = new String("#OR(how to build a fence)");	
	//	String initial_query = new String("#AND(texas NEAR/1(border patrol))");
	//	String initial_query = new String("#AND(neil.title young.title)");
	//	String initial_query = new String("#NEAR/3(keyboard reviews)");
    //	String initial_query = new String("#OR(joints)");
	//	String initial_query = new String("#AND(er.title tv show)");
		String initial_query = new String("#NEAR/1(south africa)");
		/*********************this part is for single query execution************/
		Pretreatment newpretreat = new Pretreatment(initial_query);
	    newpretreat.Treat_query();
	    String aft_query = newpretreat.get_aft_str();
		
		
		Ranked_Parser parse_Tree = new Ranked_Parser(aft_query);
	    TreeNode testnode = parse_Tree.get_root();
	    
	    parse_Tree.Parse_Tree(parse_Tree.get_root().get_child().get(0));
	    
	    parse_Tree.final_rank(46);  
	    /**************formal input and output for unstructured AND**********************/
     /*   File input_file = new File("resource/queries.txt");
        try{
        	FileInputStream fis = new FileInputStream(input_file);     
        	InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        	BufferedReader reader = new BufferedReader(isr);
        	String queries = new String();
        	int query_ID;
        	long time_begin = System.currentTimeMillis();
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
        	    
        	    Ranked_Parser parse_Tree = new Ranked_Parser(queries);
        	    TreeNode testnode = parse_Tree.get_root();
        	    parse_Tree.Parse_Tree(parse_Tree.get_root().get_child().get(0));
        	    parse_Tree.final_rank(query_ID);
        	    long time_end = System.currentTimeMillis();
        	    long time_consumed = time_end - time_begin;
        	    System.out.println("Run time is :" + time_consumed);
        	}
        }
        catch(IOException e)
        {
        	System.out.println("error!!!!");
        }               */
	    /**************formal input and output for unstructured OR**********************/
      /*  File input_file = new File("resource/queries.txt");
        try{
        	FileInputStream fis = new FileInputStream(input_file);     
        	InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        	BufferedReader reader = new BufferedReader(isr);
        	String queries = new String();
        	int query_ID;
        	long time_begin = System.currentTimeMillis();
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
        		
        		Pretreatment new_pretreat = new Pretreatment(queries);
        	    new_pretreat.Treat_query();
        	    queries = new_pretreat.get_aft_str();
        	    
        	    Ranked_Parser parse_Tree = new Ranked_Parser(queries);
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
        /***********************structured set******************/
    /*    File input_file = new File("resource/struct_set.txt");
        try{
        	FileInputStream fis = new FileInputStream(input_file);     
        	InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        	BufferedReader reader = new BufferedReader(isr);
        	String queries = new String();
        	int query_ID;
        	long time_begin = System.currentTimeMillis();
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
        	    
        	    Ranked_Parser parse_Tree = new Ranked_Parser(queries);
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
