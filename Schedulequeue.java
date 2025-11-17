package Project;

public class Schedulequeue {
    private Node Front;
    private Node Rear;

    public Schedulequeue() {
        Front = null;
        Rear = null;
    }

    public Node getFront() {
        return Front;
    }

    public Node getRear() {
        return Rear;
    }

    // Add schedule to queue
    public void Enqueue( Package data) {
        Node temp = new Node(data);
        if (Front == null) {
            Front = temp;
            Rear = temp;
        } else {
            Rear.setNextNode(temp);
            temp.setPrevNode(Rear); 
            Rear = temp;
        }
    }

    // Remove from front of queue
    public Package Dequeue() {
        if (Front == null) {
            System.out.println("The queue is empty, cannot dequeue.");
            return null;
        }
        Package data = Front.getData();
        Front = Front.getNextNode();
        if (Front != null) {
            Front.setPrevNode(null);
        } else {
            Rear = null; 
        }
        return data;
    }

    // Peek at front element
    public Package QueueFront() {
        if (Front == null) {
            System.out.println("The queue is empty, cannot return value(s).");
            return null;
        }
        return Front.getData();
    }

    // Count all nodes
    public int CountNodes() {
        int count = 0;
        Node current = Front;
        while (current != null) {
            count++;
            current = current.getNextNode();
        }
        return count;
    }
}
