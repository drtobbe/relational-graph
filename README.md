Relational-Graph
================

Comparison of performance between a relational and a graph database regarding friends of friends requests

In order to try, type this in your console:

		$ git clone https://github.com/drtobbe/relational-graph.git
		$ cd relational-graph
		$ mvn clean install
		$ mvn exec:exec

### Neo4j
friends-1: List(Map(count(distinct m) -> 50)), 857 ms

friends-2: List(Map(count(distinct m) -> 1430)), 475 ms

friends-3: List(Map(count(distinct m) -> 1999)), 1881 ms

### H2 
friends-1: 50, 106 ms

friends-2: 1430, 620 ms

friends-3: 1999, 20630 ms

### Neo4j Server

If you want to browse the Neo4j graph-database use just created:

		$ mvn -f server.xml exec:exec
		$ open http://localhost:7575/webadmin/#/data/search/1/

