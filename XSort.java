public class XSort {

    public static void main(String[] args) {
        // String[] text = {"banana", "apple", "cherry", "mango", "blueberry"};
        // BuildMaxHeap(text);

        // //print the array
        // for (String s : text) {
        //     System.out.print(s + " ");
        // }

        //error checking and taking in inputs
        if(args.length < 1 || args.length > 2){
            System.err.println("Usage: java XSort [runSize] [mergeWay]");
            System.exit(1);
        }
        
        //validate the first input
        int runSize = 0; //hold the runSize entered
        try{
            runSize = Integer.parseInt(args[0]);
            if(runSize < 64 || runSize > 1024){
                System.err.println("runSize must be between 64 and 1024");
                System.exit(1);
            }
        } catch(NumberFormatException e){
            System.err.println("runSize must be of an integer value");
            System.exit(1);
        }

        //validate the second input
        boolean mergeWay = false; //if mergeWay entered is not 2, we cannot merge
        if(args.length == 2){
            if(args[1].equals("2")){
                mergeWay = true;
            } else{
                System.err.println("mergeWay has only been implemented for 2 way");
                System.exit(1);
            }
        }
        
        try {
            createInitRuns(runSize);
        } catch (Exception e) {
            e.printStackTrace(); 
        }        
    }

    /**
     * produce runs for each line of size (runSize)
     * @param runSize size for each run
     */
    public static void createInitRuns(int size) {
        
    }
    /**
     * Builds max heap from unsorted array
     * @param array the array to be converted to a maxHeap
     */
    public static void BuildMaxHeap(String[] array){
        int arrayLength = array.length;

        // Build a max heap by heapifying from the last non leaf node up to the root.
        for(int i = arrayLength/2 - 1; i >= 0; i--){
            Heapify(array, arrayLength , i);
        }

        for(int i = arrayLength - 1; i > 0; i--){
            //move the current root to the end of the array
            Swap(array, 0, i);
            //rebuild the heap
            Heapify(array, i, 0);
        }
    }
    /**
     * rebuilds the heap
     * @param array the array containing the heap
     * @param n the size of the heap
     * @param i the index of the subtree root to heapify
     */
    public static void Heapify(String[] array, int n, int i){
        int largest = i; //initially set the largest as the root
        int leftChild = 2 * i + 1;
        int rightChild = 2 * i + 2;

        //if the left child is larger than the root
        if(leftChild < n && array[leftChild].compareTo(array[largest]) > 0){
            largest = leftChild;
        }

        //if the right child is larger than the root
        if(rightChild < n && array[rightChild].compareTo(array[largest]) > 0){
            largest = rightChild;
        }

        //if the 'largest' variable has been changed
        if(largest != i){
            Swap(array, i, largest);
            
            //recursively heapify until the root is the largest 
            Heapify(array, n, largest);
        }

    }
    
   /**
    * Swaps two elements in the array
    * @param array 
    * @param firstElement index of first element
    * @param secondElement index of second element
    */
    public static void Swap(String[] array, int firstElement, int secondElement){
        String temp = array[firstElement];
        array[firstElement] = array [secondElement];
        array[secondElement] = temp;
    }


    


}