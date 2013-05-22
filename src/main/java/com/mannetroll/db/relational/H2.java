package com.mannetroll.db.relational;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mannetroll.FriendMaker;

public class H2 {
    private static final String ORG_H2_DRIVER = "org.h2.Driver";
    private static final String UN = "sa";
    private static final String PW = "sa";
    private Connection conn;

    public H2() {
    }

    public void script() {
        try {
            if (conn == null) {
                Class.forName(ORG_H2_DRIVER);
                conn = DriverManager.getConnection("jdbc:h2:target/relationalDB/friends", UN, PW);
            }
            PreparedStatement st = null;
            st = conn.prepareStatement("CREATE TABLE PERSON(ID INT PRIMARY KEY, NAME VARCHAR(50))");
            st.execute();
            st = conn.prepareStatement("CREATE TABLE FRIEND_OF(PRS_ID INT, FRND_ID INT)");
            st.execute();

            Statement s = conn.createStatement();
            s.execute("SCRIPT DROP TO 'target/relationalDB/friends.sql'");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setup() {
        try {
            // ( 1 ) setup the connection to H2 relational database
            if (conn == null) {
                Class.forName(ORG_H2_DRIVER);
                conn = DriverManager.getConnection(
                        "jdbc:h2:target/relationalDB/friends;INIT=RUNSCRIPT FROM 'src/main/resources/friends.sql'", UN,
                        PW);
            }

            PreparedStatement st = null;
            //ResultSet rs = null;
            //-----------------------------------------------------------------
            // ( 2 ) add persons to database
            for (int i = 0; i < FriendMaker.nodes; i++) {
                st = conn.prepareStatement("insert into person(id,name) values(" + (i + 1) + ",'person-" + (i + 1)
                        + "')");
                st.execute();
            }

            //-----------------------------------------------------------------
            // ( 3 ) add friends to each person
            int[][] friendsOf = FriendMaker.makeFriends();
            for (int p = 0; p < FriendMaker.nodes; p++)
                for (int f = 0; f < FriendMaker.edges; f++) {
                    if (friendsOf[p][f] == 0) {
                        System.out.println("Zero " + p + "-" + f);
                        System.exit(0);
                    }
                    st = conn.prepareStatement("insert into FRIEND_OF(prs_id,frnd_id) values(" + (p + 1) + ","
                            + friendsOf[p][f] + ")");
                    st.execute();
                }
            //-----------------------------------------------------------------
            st.close();
            conn.commit();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void friends() {
        System.out.println("################ H2 #################");
        try {
            if (conn == null) {
                Class.forName(ORG_H2_DRIVER);
                conn = DriverManager.getConnection("jdbc:h2:target/relationalDB/friends", UN, PW);
            }

            Statement st = null;
            ResultSet rs = null;
            long start;
            //1
            st = conn.createStatement();
            start = System.currentTimeMillis();
            rs = st.executeQuery("SELECT COUNT(DISTINCT FRND_ID) FROM FRIEND_OF WHERE PRS_ID = 1");
            if (rs.next()) {
                System.out.println("friends-1: " + rs.getInt(1) + ", " + (System.currentTimeMillis() - start) + " ms");
            }
            st.close();

            //2
            st = conn.createStatement();
            start = System.currentTimeMillis();
            rs = st.executeQuery("SELECT COUNT(DISTINCT F2.FRND_ID) FROM "
                    + "FRIEND_OF F1, FRIEND_OF F2 WHERE F1.PRS_ID = 1 AND F2.PRS_ID = F1.FRND_ID");
            if (rs.next()) {
                System.out.println("friends-2: " + rs.getObject(1) + ", " + (System.currentTimeMillis() - start)
                        + " ms");
            }
            st.close();

            //3
            st = conn.createStatement();
            start = System.currentTimeMillis();
            rs = st.executeQuery("SELECT COUNT(DISTINCT F3.FRND_ID) FROM "
                    + "FRIEND_OF F1, FRIEND_OF F2, FRIEND_OF F3 "
                    + "WHERE F1.PRS_ID = 1 AND F2.PRS_ID = F1.FRND_ID AND F3.PRS_ID = F2.FRND_ID");
            if (rs.next()) {
                System.out.println("friends-3: " + rs.getObject(1) + ", " + (System.currentTimeMillis() - start)
                        + " ms");
            }
            st.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        H2 h2 = new H2();
        h2.script();
    }
}
