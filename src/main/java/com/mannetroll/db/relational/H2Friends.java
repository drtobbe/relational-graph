package com.mannetroll.db.relational;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class H2Friends {
    private static final String ORG_H2_DRIVER = "org.h2.Driver";
    private static final String UN = "sa";
    private static final String PW = "sa";

    public H2Friends() {
    }

    public void friends() {
        try {
            // ( 1 ) setup the connection to H2 relational database
            Class.forName(ORG_H2_DRIVER);
            Connection conn = DriverManager.getConnection("jdbc:h2:target/relationalDB/friends", UN, PW);

            Statement st = null;
            ResultSet rs = null;
            long start;
            //1
            st = conn.createStatement();
            start = System.currentTimeMillis();
            rs = st.executeQuery("SELECT COUNT(DISTINCT FRND_ID) FROM FRIEND_OF WHERE PRS_ID = 1");
            if (rs.next()) {
                System.out.println("count1: " + rs.getObject(1));
                System.out.println((System.currentTimeMillis()-start) + " ms");
            }
            st.close();

            //2
            st = conn.createStatement();
            start = System.currentTimeMillis();
            rs = st.executeQuery("SELECT COUNT(DISTINCT F2.FRND_ID) FROM "
                    + "FRIEND_OF F1, FRIEND_OF F2 WHERE F1.PRS_ID = 1 AND F2.PRS_ID = F1.FRND_ID");
            if (rs.next()) {
                System.out.println("count2: " + rs.getObject(1));
                System.out.println((System.currentTimeMillis()-start) + " ms");
            }
            st.close();

            //3
            st = conn.createStatement();
            start = System.currentTimeMillis();
            rs = st.executeQuery("SELECT COUNT(DISTINCT F3.FRND_ID) FROM "
                    + "FRIEND_OF F1, FRIEND_OF F2, FRIEND_OF F3 "
                    + "WHERE F1.PRS_ID = 1 AND F2.PRS_ID = F1.FRND_ID AND F3.PRS_ID = F2.FRND_ID");
            if (rs.next()) {
                System.out.println("count3: " + rs.getObject(1));
                System.out.println((System.currentTimeMillis()-start) + " ms");
            }
            st.close();

            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        H2Friends h2 = new H2Friends();
        h2.friends();
    }
}
