relational-graph
================

Comparison of performance between a relational and a graph database regarding friends of friends requests


############### NEO4J
friends-1: List(Map(count(distinct m) -> 50)), 857 ms

friends-2: List(Map(count(distinct m) -> 1430)), 475 ms

friends-3: List(Map(count(distinct m) -> 1999)), 1881 ms

################ H2 
friends-1: 50, 106 ms

friends-2: 1430, 620 ms

friends-3: 1999, 20630 ms

