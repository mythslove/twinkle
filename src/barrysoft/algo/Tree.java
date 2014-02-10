package barrysoft.algo;

public class Tree <DataType>{
	
	private TreeNode<DataType> root;

	public Tree() {
		this(null);
	}
	
	public Tree(TreeNode<DataType> root) {
		setRoot(root);
	}
	
	public TreeNode<DataType> getRoot() {
		return root;
	}

	public void setRoot(TreeNode<DataType> root) {
		this.root = root;
	}
	
}
