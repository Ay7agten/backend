package com.kelpiegang.tacoresume.DbLayer;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

public class MongoConfig {

    private Morphia morphia;
    private Datastore datastore;
    private static MongoConfig instance;

    private MongoConfig() {
        try {
            this.morphia = new Morphia();
            String dbURI = System.getenv("MONGODB_URI");
            this.datastore = morphia.createDatastore(new MongoClient(new MongoClientURI(dbURI)), "tacoresume");
            this.datastore.ensureIndexes();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static MongoConfig getInstance() {
        if (instance == null) {
            instance = new MongoConfig();
        }
        return instance;
    }

    public Datastore getDatastore() {
        return this.datastore;
    }

}
