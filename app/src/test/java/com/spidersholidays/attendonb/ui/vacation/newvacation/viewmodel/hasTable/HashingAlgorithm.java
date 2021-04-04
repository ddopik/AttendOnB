package com.spidersholidays.attendonb.ui.vacation.newvacation.viewmodel.hasTable;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

@RunWith(AndroidJUnit4.class)
public class HashingAlgorithm {

    @Test
    public void HashingTest() {

        //given
        /*
         * Input String in with Any Char Type even spaces
         *
         * */


        // when
        /*
         * get char with the heighst number of occurance
         *  --> print it's value
         *
         * Note --> if two char matched in the number of occurance
         *         --print the char the have the lowest  the lower ASCII Code
         * */


// bucket
// hash function
// hash Table
    }
    ////////////////////////////////////////////////////////////////////////////


    // Driver code
    @Test
    public static void main(String[] args) {

        String s = "2233009477772";
        Node root = null;

        // Add individual characters
        // to the String one by one
        // in level order
        for (int i = 0; i < s.length(); i++) {
            root = addinlvlorder(root, s.charAt(i));
        }
        // Print the level order
        // of the constructed
        // binary tree
        printlvlorder(root);
        getRedundancyRatio(printlvlorder(root));
    }




    /**
      Function to print the
     level order traversal of the Binary Tree
     */
    static HashMap<Character, Integer> printlvlorder(Node root) {


        HashMap<Character, Integer> tempMap = new HashMap<Character, Integer>();
        // Add the root to the queue
        Queue<Node> Q = new LinkedList<Node>();
        Q.add(root);

        while (!Q.isEmpty()) {
            Node temp = Q.peek();

            // If the cnt of the character
            // is more then one, display cnt
            if (temp.cnt > 1) {
//                System.out.print((temp.data + "" + temp.cnt));
                tempMap.put(temp.data, temp.cnt);
            }

            // If the cnt of character
            // is one, display character only
            else {
//                System.out.print((char) temp.data);
                tempMap.put(temp.data, 1);

            }
            Q.remove();

            // Add the left child to
            // the queue for further
            // processing
            if (temp.left != null) {
                Q.add(temp.left);
            }

            // Add the right child to
            // the queue for further
            // processing
            if (temp.right != null) {
                Q.add(temp.right);
            }
        }
        return tempMap;
    }

    // Function to add a new
    // node to the Binary Tree
    static Node add(char data) {

        // Create a new node and
        // populate its data part,
        // set cnt as 1 and left
        // and right children as null
        Node newnode = new Node();
        newnode.data = data;
        newnode.cnt = 1;
        newnode.left = newnode.right = null;

        return newnode;
    }

    // Function to add a node
    // to the Binary Tree in
    // level order
    static Node addinlvlorder(Node root, char data) {

        if (root == null) {
            return add(data);
        }

        // Use the queue data structure
        // for level order insertion
        // and push the root of tree to Queue
        Queue<Node> Q = new LinkedList<Node>();
        Q.add(root);

        while (!Q.isEmpty()) {

            Node temp = Q.peek();
            Q.remove();

            // If the character to be
            // inserted is present,
            // update the cnt
            if (temp.data == data) {
                temp.cnt++;
                break;
            }

            // If the left child is
            // empty add a new node
            // as the left child
            if (temp.left == null) {
                temp.left = add(data);
                break;
            } else {
                // If the character is present
                // as a left child, update the
                // cnt and exit the loop
                if (temp.left.data == data) {
                    temp.left.cnt++;
                    break;
                }

                // Add the left child to
                // the queue for further
                // processing
                Q.add(temp.left);
            }

            // If the right child is empty,
            // add a new node to the right
            if (temp.right == null) {
                temp.right = add(data);
                break;
            } else {
                // If the character is present
                // as a right child, update the
                // cnt and exit the loop
                if (temp.right.data == data) {
                    temp.right.cnt++;
                    break;
                }

                // Add the right child to
                // the queue for further
                // processing
                Q.add(temp.right);
            }
        }

        return root;
    }

    /**
     * get the redundancy ratio of the given list
     *
     * @mList : each element on the list consiste of <Key/ balue>
     * as key  : denote to node value
     * value : denote to node appearance count
     * <p>
     * return redundancy ratio ....
     */
    private static double getRedundancyRatio(HashMap<Character, Integer> mList) {

        int distinctElementsSum = 0;
        for (int value : mList.values()) {
            distinctElementsSum += value;
        }
        double redundancyRatio = (double) mList.size() / distinctElementsSum;
        System.out.print(("redundancyRatio --->" + redundancyRatio));
        return redundancyRatio;
    }





//    ===================
//   == Typical soultion ==
//    ====================
//    ----> intialize a scanner to get user input
//    ----> first input should be the  redundancyRatio  as a real number greater than 1
//    ----> detect when user Enter next number and call addinlvlorder() to initialize the Tree with the given number
//    ----> now fot each number get inserted you should call  getRedundancyRatio(printlvlorder(root));
//          --- and then compare it's value with redundancyRatio the user has entered previously
//          --- if value < redundancyRatio ---> allow the program to scan another user input
//          ----if value > redundancyRatio then you can follow the steps that assignment mentioned
//                 {  you can input -1 as your input integer. In this case, the program prints the
//                current ratio with a message indicating that your numbers have “only few repetitions”.
//                }

}
