package com.mannetroll;

import com.mannetroll.db.graph.Neo4j;
import com.mannetroll.db.relational.H2;

public class SetupApp {

    public static void main(String args[]) {
        FriendMaker.makeFriends();
        
        H2 h2 = new H2();
        h2.setup();

        Neo4j neo4j = new Neo4j();
        neo4j.setup();
    }
}
