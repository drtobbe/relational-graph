package com.mannetroll.db.relational;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.mannetroll.FriendMaker;

public class H2 {
    private static final String ORG_H2_DRIVER = "org.h2.Driver";
    private static final String UN = "sa";
    private static final String PW = "sa";

    public H2() {
    }

    public void script() {
        try {
            Class.forName(ORG_H2_DRIVER);
            Connection conn = DriverManager.getConnection("jdbc:h2:target/relationalDB/friends", UN, PW);
            PreparedStatement st = null;
            st = conn.prepareStatement("CREATE TABLE PERSON(ID INT PRIMARY KEY, NAME VARCHAR(50))");
            st.execute();
            st = conn.prepareStatement("CREATE TABLE FRIEND_OF(PRS_ID INT, FRND_ID INT)");
            st.execute();

            // ( 1 ) setup the connection to H2 relational database
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
            Class.forName(ORG_H2_DRIVER);
            Connection conn = DriverManager.getConnection(
                    "jdbc:h2:target/relationalDB/friends;INIT=RUNSCRIPT FROM 'src/main/resources/friends.sql'", UN, PW);

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
            conn.close();
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
