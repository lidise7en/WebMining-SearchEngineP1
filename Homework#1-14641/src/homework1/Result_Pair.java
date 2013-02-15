package homework1;
/*
 * This class is designed to store ID-score pairs after selecting from indices
 * these pairs are ready for final ranking
 */
public class Result_Pair {
	private int docID;
	private int docScore;
	
	public Result_Pair()//Constructor
	{
		this.docID = 0;
		this.docScore = 0;
	}
	public void set_docID(int id)
	{
		this.docID = id;
	}
	public int get_docID()
	{
		return this.docID;
	}
	public void set_score(int score)
	{
		this.docScore = score;
	}
	public int get_score()
	{
		return this.docScore;
	}

}
