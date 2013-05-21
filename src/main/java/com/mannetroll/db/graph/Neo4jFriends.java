package com.mannetroll.db.graph;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.kernel.impl.util.StringLogger;

public class Neo4jFriends {
    private ExecutionEngine engine;

    public Neo4jFriends() {
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

    public void setup() {
        GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(
                "target/graphDB/friends.db").setConfig(GraphDatabaseSettings.node_keys_indexable, "id,name").setConfig(
                GraphDatabaseSettings.node_auto_indexing, "true").setConfig(
                GraphDatabaseSettings.relationship_auto_indexing, "true").newGraphDatabase();
        registerShutdownHook(graphDb);
        engine = new ExecutionEngine(graphDb, StringLogger.SYSTEM);        
    }

    
    public void friends() {
        ExecutionResult result = null;
        long start;
        start = System.currentTimeMillis();
        result = engine.execute("start n = node(1) match n-[:FRIEND_OF]->m return count(distinct m);");
        if (result.hasNext()) {
            System.out.println("count1: " + result.toList());
            System.out.println((System.currentTimeMillis()-start) + " ms");
        }

        start = System.currentTimeMillis();
        result = engine.execute("start n = node(1) match n-[:FRIEND_OF]->()-[:FRIEND_OF]->m return count(distinct m);");
        if (result.hasNext()) {
            System.out.println("count2: " + result.toList());
            System.out.println((System.currentTimeMillis()-start) + " ms");
        }

        start = System.currentTimeMillis();
        result = engine.execute("start n = node(1) match n-[:FRIEND_OF*3]->m return count(distinct m);");
        if (result.hasNext()) {
            System.out.println("count3: " + result.toList());
            System.out.println((System.currentTimeMillis()-start) + " ms");
        }

        start = System.currentTimeMillis();
        result = engine.execute("start n = node(1) match n-[:FRIEND_OF*4]->m return count(distinct m);");
        if (result.hasNext()) {
            System.out.println("count4: " + result.toList());
            System.out.println((System.currentTimeMillis()-start) + " ms");
        }

    }

    public static void main(String[] args) {
        Neo4jFriends neo4j = new Neo4jFriends();
        neo4j.setup();
        neo4j.friends();
    }

}
