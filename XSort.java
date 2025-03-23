/**
 * Perform a 2 Way External Merge Sort
 * @author Oliver Surridge - ID: 1607940
 */
import java.io.*;
import java.util.*;

public class XSort {

    public static void main(String[] args) throws IOException{
        //Init the temp files
        File temp1 = File.createTempFile("sort1", ".tmp");
        File temp2 = File.createTempFile("sort2", ".tmp");
        temp1.deleteOnExit();
        temp2.deleteOnExit();

        //Init files used for merge
        File fileA = temp1;
        File fileB = temp2;
        File fileC = File.createTempFile("sort3", ".tmp");
        File fileD = File.createTempFile("sort4", ".tmp");
        fileC.deleteOnExit();
        fileD.deleteOnExit();
                
        int runSize = 0;

        //error checking and taking in inputs
        if(args.length < 1 || args.length > 2){
            System.err.println("Usage: java XSort [runSize] [mergeWay]");
            System.exit(1);
        }
        
        //validate the first input
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
        
        boolean mergeWay = false; 
        if(args.length == 2){
            if(args[1].equals("2")){
                mergeWay = true; //we need to merge
            } else{
                //if mergeWay entered is not 2, we cannot merge
                System.err.println("mergeWay has only been implemented for 2 way");
                System.exit(1);
            }
        }
        

        try {
            //If second argument is provided, merge
            if(mergeWay){
                createInitRuns(runSize, temp1, temp2, mergeWay);
                
                while(true){
                    mergeRuns(fileA, fileB, fileC, fileD);

                    if(isFileEmpty(fileC) || isFileEmpty(fileD)){
                        File sortedFile;
                        if (isFileEmpty(fileC)) {
                            sortedFile = fileD;
                        } else {
                            sortedFile = fileC;
                        }
                        printSortedFile(sortedFile);
                        break;
                    }
                }
            } else{
                System.out.println("No merging was requested so heap sorted initial runs follow:"+"\n");
                createInitRuns(runSize, temp1, temp2, mergeWay);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        } 
    }

    /**
     * produce runs for each line of size (runSize)
     * @param runSize size for each run
     */
    public static void createInitRuns(int size, File temp1, File temp2, boolean merge) throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = "";
        ArrayList<String> buffer = new ArrayList<>(); //used to temp store text for a run
        BufferedWriter writer1 = new BufferedWriter(new FileWriter(temp1));
        BufferedWriter writer2 = new BufferedWriter(new FileWriter(temp2));

        boolean writeToFirst = true; //used to toggle between each temp file
        
        //infinite while loop
        while(true){
            //clear the buffer for a new run
            buffer.clear();

            //read (size) amount of lines 
            while(buffer.size() < size){
                line = reader.readLine();
                //null rather than "" means we have reached the end of the input
                if(line == null){
                    break;
                }
                //add the line to the buffer array
                buffer.add(line);
            }
            //create the run
            String[] runArray = buffer.toArray(new String[0]);

            //sort the run
            BuildMaxHeap(runArray);

            BufferedWriter writer; 
            if(writeToFirst){
                writer = writer1;
            }else{
                writer = writer2;
            }

            //write to alternating files
            for(String sortedLine : runArray){
                writer.write(sortedLine);
                writer.newLine();
            }
            writer.flush();
            writeToFirst = !writeToFirst; //toggle temp file
            
            //output the initial runs if second argument isnt given
            if(!merge){
                for(String sortedLine : runArray){
                    System.out.println(sortedLine);
                }
            }
            
           
            if(line == null){
                break;
            }
        }
        reader.close();
        writer1.close();
        writer2.close();
    }
    /**
     * Perform a 2 way merge
     * @param input1 file1 of initial runs
     * @param input2 file2 of initial runs
     * @param output1 file3 to use as output
     * @param output2 file4 to use as output
     * @throws IOException
     */
    public static void mergeRuns(File input1, File input2, File output1, File output2) throws IOException{
        //open temp1 and temp2 to read
        BufferedReader reader1 = new BufferedReader(new FileReader(input1));
        BufferedReader reader2 = new BufferedReader(new FileReader(input2));
        BufferedWriter writer1 = new BufferedWriter(new FileWriter(output1));
        BufferedWriter writer2 = new BufferedWriter(new FileWriter(output2));

        boolean writeToFirst = true; //used to alternate between output 1 and 2
        
        String line1 = reader1.readLine();
        String line2 = reader2.readLine();
        
        while(line1 != null || line2 != null){
            List <String> mergedRun = new ArrayList<>();
            
            //merge a run from each file
            while (line1 != null && line2 != null){
                if(line1.compareTo(line2) <= 0){ //if line1 is the same or lower in dictionary order to line2
                    mergedRun.add(line1);
                    line1 = reader1.readLine(); //move to next line
                } else{
                    mergedRun.add(line2);
                    line2 = reader2.readLine();
                }
            }

            //check for remaining lines if none in another
            while(line1 != null /*&& !line1.isEmpty()*/){
                mergedRun.add(line1);
                line1 = reader1.readLine();
            }
            while(line2 != null /*&& !line2.isEmpty()*/){
                mergedRun.add(line2);
                line2 = reader2.readLine();
            }

            BufferedWriter writer; 
            if(writeToFirst){
                writer = writer1;
            }else{
                writer = writer2;
            }

             //write to alternating files
            for (String sortedLine : mergedRun) {
                writer.write(sortedLine);
                writer.newLine();
            }

            writer1.flush();
            writeToFirst = !writeToFirst; //toggle
        }

        reader1.close();
        reader2.close();
        writer1.close();
        writer2.close();

    }
    /**
     * checks whether a file is empty
     * @param file
     * @return true if empty
     * @throws IOException
     */
    public static boolean isFileEmpty(File file) throws IOException {
        return file.length() == 0;
    }

    public static void printSortedFile(File sortedFile) throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader(sortedFile));
        String line;
        while((line = reader.readLine()) != null){
            System.out.println(line);
        }
        reader.close();
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