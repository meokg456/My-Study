package mystudy.Connector;

import org.hibernate.Session;

import mystudy.Hibernate.HibernateUtil;

public class DatabaseService {
    private static DatabaseService instance;

    private Session session;

    public static DatabaseService getInstance() {
        if (instance == null) {
            instance = new DatabaseService();
        }
        return instance;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public static void init() {
        instance = new DatabaseService();
    }

    public DatabaseService() {
        setSession(HibernateUtil.getSessionFactory().openSession());
    }

}