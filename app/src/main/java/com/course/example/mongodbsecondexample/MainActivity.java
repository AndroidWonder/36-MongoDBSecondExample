package com.course.example.mongodbsecondexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Date;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.result.DeleteResult;
import static com.mongodb.client.model.Updates.*;
import com.mongodb.client.result.UpdateResult;

import org.bson.Document;


public class MainActivity extends AppCompatActivity {

    private final String tag = "Mongo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread t = new Thread(task);
        t.start();

    }


    Runnable task = new Runnable(){
        public void run(){

            try {

                /**** Connect to MongoDB ****/
                MongoClient mongo = new MongoClient("frodo.bentley.edu", 27017);

                /**** Get database ****/
                // if database doesn't exists, MongoDB will create it for you
                MongoDatabase db = mongo.getDatabase("testdb");

                /**** Get collection / table from 'testdb' ****/
                // if collection doesn't exists, MongoDB will create it for you
                MongoCollection<Document> table = db.getCollection("user");

                /**** Insert ****/
                // create a document to store key and value
                Document document = new Document("name", "JoeDePloy");
                document.append("age", 30);
                document.append("createdDate", new Date());
                table.insertOne(document);

                /**** Find and display ****/
                MongoCursor<Document> cursor = table.find().iterator();

                while (cursor.hasNext()) {
                    Log.i(tag, cursor.next().toJson());
                }

                //search on age field
                Document myDoc = table.find(eq("age", 30)).first();
                Log.i(tag, myDoc.toJson());

                // Update
                // search document where name="JoeDePloy" and update it with new values
                Document updateDoc = new Document("name", "HenryQCat");
                updateDoc.append("age", 10);
                updateDoc.append("createdDate", new Date());
                table.updateOne(eq("name","JoeDePloy"),  new Document("$set", updateDoc));

                /**** Find and display ****/
                MongoCursor<Document> updateCursor = table.find().iterator();

                while (updateCursor.hasNext()) {
                    Log.i(tag, updateCursor.next().toJson());
                }

                // delete
                DeleteResult deleteResult = table.deleteMany(eq("name", "HenryQCat"));
                System.out.println(deleteResult.getDeletedCount());

                table.drop();
                Log.i(tag, "Done");


            } catch (MongoException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    };



}
