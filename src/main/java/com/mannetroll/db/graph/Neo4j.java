package com.mannetroll.db.graph;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;

import com.mannetroll.FriendMaker;

public class Neo4j {

    public Neo4j() {
    }

    public void setup() {
        // ( 1 ) setup the connection to Neo4j graph database
        GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(
                "target/graphDB/friends.db").setConfig(GraphDatabaseSettings.node_keys_indexable, "id,name").setConfig(
                GraphDatabaseSettings.node_auto_indexing, "true").setConfig(
                GraphDatabaseSettings.relationship_auto_indexing, "true").newGraphDatabase();
        registerShutdownHook(graphDb);

        //Relationship relation;
        Node node;
        Transaction tx;

        //-----------------------------------------------------------------
        // ( 2 ) add persons "nodes" to database
        tx = graphDb.beginTx();
        for (int i = 0; i < FriendMaker.nodes; i++) {
            node = graphDb.createNode();
            node.setProperty("name", "person-" + (i + 1));
        }
        tx.success();
        tx.finish();

        //-----------------------------------------------------------------
        // ( 3 ) add friends "relations" to each person "node"
        int[][] friendsOf = FriendMaker.makeFriends();
        tx = graphDb.beginTx();
        for (int p = 0; p < FriendMaker.nodes; p++)
            for (int f = 0; f < FriendMaker.edges; f++) {
                graphDb.getNodeById((p + 1)).createRelationshipTo(graphDb.getNodeById((friendsOf[p][f])),
                        RelTypes.FRIEND_OF);

            }
        tx.success();
        tx.finish();
    }

    public static enum RelTypes implements RelationshipType {
        FRIEND_OF;
    }

    private static void registerShutdownHook(final GraphDatabaseService graphDb) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        });
    }

}
