import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

public class Mongo {

	public static void main(String[] args) throws UnknownHostException {

       String tagsArray[];
        Map<String, Boolean> uniqueTags = new HashMap<String, Boolean>();
        int allTags = 0;
        long timeEnd;
        long timeStart = System.currentTimeMillis()/1000;
        
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        DB db = mongoClient.getDB("test");     
        DBCollection collecion = db.getCollection("train");
        DBCursor cursor = collecion.find();
        
        try 
        {
            while(cursor.hasNext()) 
            {
                BasicDBObject dbObject = (BasicDBObject)cursor.next();
                tagsArray = dbObject.getString("tags").split(" ");
                dbObject.put("tags", tagsArray);
                collecion.save(dbObject);

                allTags += tagsArray.length;
                
                for(int i=0; i<tagsArray.length; i++) 
                {
                    if(uniqueTags.get(tagsArray[i]) == null)
                        uniqueTags.put(tagsArray[i], true);
                }
            }          
        } 
        finally 
        {
            cursor.close();
        }
        
        timeEnd = System.currentTimeMillis()/1000;
        
        System.out.println("Time: " + (timeEnd-timeStart)/60 + "m" + (timeEnd-timeStart)%60 + "s");
        System.out.println("All tags: " + allTags);
        System.out.println("Unique tags: " + uniqueTags.size());
        
        mongoClient.close();
    }
}