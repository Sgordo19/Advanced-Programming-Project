package Project;

public class PackageNode {

	static class Node {
		private Package data;
		private Node nextNode;

		public Node() {
			this.data = new Package(data);
			this.nextNode = null;
		}

		public Node(Package data, Node nextNode) {
			this.data = new Package(data);
			this.nextNode = nextNode;
		}

		public Node(int package_id, int p_weight, int p_quantity, int zone, String destination, boolean p_status,
				int package_type)

		{
			data = new Package(package_id, p_weight, p_quantity, zone, destination, p_status, package_type);
			nextNode = null;
		}

		// copy constructor
		public Node(Node node) {
			data = node.data;
			nextNode = node.nextNode;
		}

		public Package getData() {
			return data;
		}

		public Node getNextNode() {
			return nextNode;
		}

		public void setData(Package data) {
			this.data = data;
		}

		public void setNextNode(Node nextNode) {
			this.nextNode = nextNode;
		}

	}
}
