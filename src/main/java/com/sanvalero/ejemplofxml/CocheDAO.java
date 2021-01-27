package com.sanvalero.ejemplofxml;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.sanvalero.ejemplofxml.domain.Coche;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.exists;

public class CocheDAO {

    private MongoClient mongoClient;
    private MongoDatabase db;
    private static final String DATABASE_NAME = "caca";
    private MongoCollection<Coche> collection;

//    public void conectar() {
////        mongoClient = new MongoClient();
//        mongoClient = new MongoClient("localhost", 27017);
//        db = mongoClient.getDatabase(DATABASE_NAME);
//    }

    public void conectar() {
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoClient cliente = new MongoClient("localhost",
                MongoClientOptions.builder().codecRegistry(pojoCodecRegistry).build());
        db = cliente.getDatabase(DATABASE_NAME);
    }

    public void desconectar() {
        mongoClient.close();
    }

    public void guardarCoche(Coche coche) {
//        Document documento = new Document()
//                .append("marca", coche.getMarca())
//                .append("modelo", coche.getModelo())
//                .append("matricula", coche.getMatricula())
//                .append("tipo", coche.getTipo());
//        db.getCollection(DATABASE_NAME).insertOne(documento);

        collection = db.getCollection("cacaPrueba", Coche.class);
        collection.insertOne(coche);
    }

    public void eliminarCoche(Coche coche) throws SQLException {
        collection = db.getCollection("cacaPrueba", Coche.class);
        collection.deleteOne(eq("_id", coche.getId()));
    }

    public void modificarCoche(Coche cocheAntiguo, Coche cocheNuevo) throws SQLException {
        collection = db.getCollection("cacaPrueba", Coche.class);
        collection.replaceOne(eq("_id", cocheAntiguo.getId()), cocheNuevo);
    }

    public List<Coche> obtenerCoches() {
//        List<Coche> coches = new ArrayList<>();
////
////        Document documento = new Document();
////        FindIterable findIterable = db.getCollection(DATABASE_NAME).find(documento);
////
////        Iterator<Document> iter = findIterable.iterator();
////        while (iter.hasNext()) {
////            Document doc = iter.next();
////            Coche coche = new Coche();
////            coche.setId(doc.getObjectId("_id"));
////            coche.setModelo(doc.getString("modelo"));
////            coche.setMarca(doc.getString("marca"));
////            coche.setMatricula(doc.getString("matricula"));
////            coche.setTipo(doc.getString("tipo"));
////            coches.add(coche);
////        }
////
////        return coches;

        collection = db.getCollection("cacaPrueba", Coche.class);
        return collection.find().into(new ArrayList<>());
    }

    //db.getCollection('cacaPrueba').countDocuments({marca: "asd"})
    public long existeCoche(Coche coche) throws SQLException {
        long existe = 0;
        collection = db.getCollection("cacaPrueba", Coche.class);
        FindIterable findIterable = collection.find(Filters.eq("matricula", coche.getMatricula()));
        Iterator iterator = findIterable.iterator();
        while (iterator.hasNext()){
            iterator.next();
            existe++;
        }
        return existe;
    }
}
