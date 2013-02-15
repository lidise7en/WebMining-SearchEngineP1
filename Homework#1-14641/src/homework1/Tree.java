package homework1;
import homework1.TreeNode;
/*
 * This class is to describe the tree strcture used in query tree
 */
public class Tree {

	private TreeNode root;// root of the tree
	private String query;// query used to construct the tree
	private int counter;// count the number of nodes in the tree
	private int Op_Num;//the operator number in this tree
	public Tree()
	{
		this.root = null;
		this.query = "";
		this.counter = 0;
		this.Op_Num = 0;
	}
	public boolean No_Operator(String command)
	{
		boolean no_op = true;
		for(int i = 0; i <= command.length() - 1;i ++)
		{
			if(command.charAt(i) == '#')
			{
				no_op = false;
				break;
			}
				
		}
		return no_op;
	}
	public void ConstructTree(TreeNode subroot,String command)
	{
		if(this.No_Operator(command) == true)//default to  OR if no operator
		{
			TreeNode newnode = new TreeNode("OR");
			root.get_child().add(newnode);
			newnode.set_isop_node(true);
			int ind = 0;
			while(ind <= command.length() - 1)
			{
				if(command.charAt(ind) == '(' || command.charAt(ind) == ')' || command.charAt(ind) == ' ')
				{
					ind ++;
				}
				else
				{
					int begin = ind;
					while(command.charAt(ind) != '(' && command.charAt(ind) != ')' && command.charAt(ind) != ' ')
					{
						ind ++;
					}
					TreeNode child = new TreeNode(command.substring(begin, ind));
					child.set_isop_node(false);
					newnode.get_child().add(child);
				}
			}
			this.Op_Num = 1;
		}
		else//common condition
		{
		while(command.charAt(counter) != ')')
		{
			if(command.charAt(counter) == ' ')
				counter ++;
			if(command.charAt(counter) == '#')
			{
				if(command.charAt(counter + 1) == 'O')
				{
					TreeNode newnode = new TreeNode("OR");
					newnode.set_isop_node(true);
					this.Op_Num ++;
					subroot.get_child().add(newnode);
					counter += 4;
					ConstructTree(newnode,command);
					
				}
				
				else if(command.charAt(counter + 1) == 'A')
				{
					TreeNode newnode = new TreeNode("AND");
					newnode.set_isop_node(true);
					this.Op_Num ++;
					subroot.get_child().add(newnode);
					counter += 5;
					ConstructTree(newnode,command);
				}
				else if(command.charAt(counter + 1) == 'N')
				{
					TreeNode newnode = new TreeNode(command.substring(counter + 1, counter + 7));
					newnode.set_isop_node(true);
					this.Op_Num ++;
					subroot.get_child().add(newnode);
					counter += 8;
					ConstructTree(newnode,command);
				}
				
			}
			if(counter == command.length() - 1)
				return;
			if(command.charAt(counter) == ' ')
			     counter ++;
			if(command.charAt(counter) != '#')
			{
				if(command.charAt(counter) == ' ')
				     counter ++;
				int begin = counter;
				
				while((command.charAt(counter) != ' ') && (command.charAt(counter) != ')'))
				{
					counter ++;
				}
				
				String word = new String(command.substring(begin, counter));
				TreeNode newnode = new TreeNode(word);
				newnode.set_isop_node(false);
				subroot.get_child().add(newnode);
			}
		}
		if(counter == command.length() - 1)
		{
			
		}
		else
		    counter ++;
		return;
	  }
	}
	public void setRoot(TreeNode e)
	{
		this.root = e;
	}
	public TreeNode getRoot()
	{
		return this.root;
	}
	public String get_query()
	{
		return this.query;
	}
	public int get_op_num()
	{
		return this.Op_Num;
	}
	public void print_Tree(TreeNode rootnode)
	{
		
		System.out.println(rootnode.get_vl());
		if(rootnode.get_child().size() != 0)
			for(int i = 0;i <= rootnode.get_child().size() - 1;i ++)
			{
				print_Tree(rootnode.get_child().get(i));
			}
	}
	 public static void main(String[] args){// This is just for test
	
	    String query1 = "#AND(#AND(air wedding) #AND(dogs africa))";
	    Tree tree1 = new Tree();
	    TreeNode RootNode = new TreeNode("");
        tree1.setRoot(RootNode);
        tree1.ConstructTree(RootNode, query1);
        
        tree1.print_Tree(RootNode);
        
	}
	
}
