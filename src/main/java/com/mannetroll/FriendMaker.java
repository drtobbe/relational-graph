package com.mannetroll;

import java.util.LinkedList;

public class FriendMaker {
    public static int nodes = 10000;
    public static int edges = 20;
    public static int[][] friendsOf = new int[nodes][edges];

    public static int[][] makeFriends() {
        if (friendsOf[0][0] != 0) {
            return friendsOf;
        }
        int randomPersonId;
        int f = 0;

        //for each person randomly pick edges friends
        System.out.println("Working one.... ");
        for (int i = 0; i < nodes; i++) {
            System.out.print(" " + i);

            //pick 50 unique friends add them to linkedList
            //LinkedList.contains() will be used to make sure we getting unique friends
            LinkedList<Integer> randomFriends = new LinkedList<Integer>();
            f = 0;
            while (f < edges) {
                randomPersonId = (int) (Math.random() * nodes);
                if (!randomFriends.contains(randomPersonId) && randomPersonId != i && randomPersonId > 0) {
                    randomFriends.add(randomPersonId);
                    //add friends to person i
                    friendsOf[i][f] = randomPersonId;
                    f++;
                }
            }
        }
        System.out.println("");
        System.out.println("Done.");
        return friendsOf;
    }

}
