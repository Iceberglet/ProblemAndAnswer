package worksapplication;

import java.util.Scanner;

public class Main {
    private static Long[] array;
    private static Tree rightTree;

    public static void main(String[] args) {
		//System.out.println(toBinary(0L));

        getInputs();

        //Initialize XOR tree
        rightTree = new Tree();
        Long xor = 0L;
        addToTree(xor);

        //Construct backward XOR tree
        for(int i = array.length - 1; i >= 0; --i){
            xor ^= array[i];
            addToTree(xor);
        }

        //Start from left, find max for each prefix xor
        Long best = null;
        xor = 0L;
        for(int i = -1; i < array.length; ++i){
            if(i >= 0)
                xor ^= array[i];
            Long res = getMax(xor);
            if(best == null || res > best){
                best = res;
            }
        }

        System.out.println(best); //*/
    }

    private static void addToTree(Long x){
        String s = toBinary(x);
        rightTree.add(s, 0);
    }

    private static Long getMax(Long xor){
		String s = toBinary(xor);
        return rightTree.getMax(s);
    }

    private static void getInputs(){
        Scanner sc = new Scanner(System.in);
        String firstLine = sc.nextLine();
        int size = Integer.parseInt(firstLine);

        array = new Long[size];
        for(int i = 0; i < size; ++i){
            array[i] = sc.nextLong();
        }
    }

    private static String toBinary(Long l){
		String s = Long.toBinaryString(l);
		while(s.length() < 41){
			s = "0" + s;
		}
		return s;
	}
}

class Tree{
    private Tree[] subtrees;

    Tree(){
        subtrees = new Tree[2];
    }

    void add(String s, int idx){
        if(idx == s.length())
            return;
        int val = Integer.parseInt(String.valueOf(s.charAt(idx)));
        if(subtrees[val] == null)
            subtrees[val] = new Tree();
        subtrees[val].add(s, ++idx);
    }

    //s: SUB-String to find max xor
    Long getMax(String s){
    	if(subtrees[0] == null && subtrees[1] == null)
    		return 0L;

		if(s.length() == 0)
			return 0L;

		int val = Integer.parseInt(String.valueOf(s.charAt(0)));

		//Look for xor
		if(subtrees[1 - val] != null) {
			Long valueAddable = 1L << (s.length() - 1);
			return valueAddable + subtrees[1 - val].getMax(s.substring(1));
		} else return getMax(s.substring(1));
	}
}
