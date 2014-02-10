package barrysoft.algo;

import java.util.Vector;

public class TreeNode <DataType> {

	private	DataType			data;		
	private Vector<TreeNode> 	childs = new Vector<TreeNode>();
	
	public DataType getData() {
		return data;
	}

	public void setData(DataType data) {
		this.data = data;
	}
	
	public TreeNode getChild(int num) {
		if (num >= childs.size())
			return null;
		
		return childs.get(num);
	}
	
	public void addChild(TreeNode child) {
		childs.add(child);
	}
	
}
