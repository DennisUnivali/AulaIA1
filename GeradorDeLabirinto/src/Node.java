
public class Node {
	int x;
	int y;
	Node pai;
	public Node(int x, int y, Node pai) {
		super();
		this.x = x;
		this.y = y;
		this.pai = pai;
	}	

	public boolean equals(Node obj) {	
		return obj.x==x&&obj.y==y;
	}
}
