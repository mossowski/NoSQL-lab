### *Marcin Ossowski*

----

* Wersja MongoDB Production Release (2.6.5) oraz Development Release (2.8.0-rc0)
* Wersja PostgreSQL 9.3.5


# Table of content
- [Zadanie 1](#zad1)
    - [1a](#1a)
    - [1b](#1b)
    - [1c](#1c)
    - [1d](#1d)
  

# Zadanie 1

### Przygotowanie danych dla mongoDB

```bash 
 time cat Train.csv | tr "\n" " " | tr "\r" "\n" | head -n 6034196 > Train2.csv

 real   2m23.399s
 user   0m51.452s
 sys    1m23.592s
```
```bash 
 time ./prepare.sh Train.csv Train2.csv

 real   9m15.063s
 user   1m53.709s
 sys    2m40.215s
```

### 1a
Polega na zaimportowaniu, do systemów baz danych uruchomionych na swoim komputerze, danych z pliku Train.csv bazy:

  *  MongoDB
  *  PostgreSQL

MongoDB Production Release (2.6.5)

```bash
 time mongoimport --type csv -c train --file Train2.csv --headerline
 
 real   16m36.446s
 user   1m38.714s
 sys    0m34.831s
```

PostgreSQL 9.3.5

```bash
 time psql -d postgres -c "copy train(Id,Title,Body,Tags) from '/home/marcin/Downloads/Train.csv' with delimiter ',' csv header;"
 
 real   32m3.404s
 user   0m0.102s
 sys    0m0.085s
```

Ilość miejsca zajmowanego przez kolekcje:

MongoDB

```bash
   show dbs
   
   train 13.947GB
```

PostgreSQL

Bez indeksów:
```bash
   select pg_size_pretty(pg_relation_size('train'));
   
   pg_size_pretty 
   ----------------
   9851 MB
```
Z indeksami:
```bash
   select pg_size_pretty(pg_total_relation_size('train'));
   
   pg_size_pretty 
   ----------------
   11 GB
```
 
### 1b
Zliczyć liczbę zaimportowanych rekordów (Odpowiedź: powinno ich być 6_034_195).

```bash
  db.train.count();
  
  6034195
```

### 1c
(Zamiana formatu danych.) Zamienić string zawierający tagi na tablicę napisów z tagami następnie zliczyć wszystkie tagi i wszystkie różne tagi.

W tym zadaniu należy napisać program, który to zrobi. W przypadku MongoDB należy użyć jednego ze sterowników ze  strony MongoDB Ecosystem. W przypadku PostgreSQL – należy to zrobić w jakikolwiek sposób.

```bash
  All tags: 18862976
  Unique tags: 74275
  Time: 62m48s
```

Do rozwiązania zadania napisałem program w Javie i użyłem sterownika Java MongoDB Driver.

```bash
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
```

Porównanie kolekcji:

```bash
  db.train.findOne();
  
{
    "_id" : 1,
    "Title" : "How to check if an uploaded file is an image without mime type?",
    "Body" : "<p>I'd like to check if an uploaded file is an image file (e.g png, jpg, jpeg, gif, bmp) or another file. The problem is that I'm using Uploadify to upload the files, which changes the mime type and gives a 'text/octal' or something as the mime type, no matter which file type you upload.</p>  <p>Is there a way to check if the uploaded file is an image apart from checking the file extension using PHP?</p> ",
    "Tags" : "php image-processing file-upload upload mime-types"
}
  
{
	"_id" : 1,
	"title" : "How to check if an uploaded file is an image without mime type?",
	"body" : "<p>I'd like to check if an uploaded file is an image file (e.g png, jpg, jpeg, gif, bmp) or another file. The problem is that I'm using Uploadify to upload the files, which changes the mime type and gives a 'text/octal' or something as the mime type, no matter which file type you upload.</p>  <p>Is there a way to check if the uploaded file is an image apart from checking the file extension using PHP?</p> ",
	"tags" : [
		"php",
		"image-processing",
		"file-upload",
		"upload",
		"mime-types"
	]
}
```

### 1d
Wyszukać w sieci dane zawierające obiekty GeoJSON. Następnie dane zapisać w bazie MongoDB.

Dla zapisanych danych przygotować co najmniej 6 różnych geospatial queries (w tym, co najmniej po jednym, dla obiektów Point, LineString i Polygon).


###Przykład 1: $near

#### Zapytanie
```bash
db.places.find(
    { 
        loc: 
        { $near : 
          {
              $geometry: { type: "Point", coordinates: [0,0] }, 
              $maxDistance: 10000000 
          } 
        } 
    }
)
```
[Wynik](https://github.com/mossowski/NoSQL-lab/blob/master/places/places1.geojson)


###Przykład 2: $geoWithin

#### Zapytanie
```bash
db.places.find(
    { 
        loc: 
        { $geoWithin: 
          { 
              $polygon: [ [ -77,36], [-77,43], [-70,43], [-70,36], [ -77,36] ]  
          }
        }           
    }
)
```
[Wynik](https://github.com/mossowski/NoSQL-lab/blob/master/places/places2.geojson)

###Przykład 3: $geoIntersects

#### Zapytanie
```bash
db.places.find(
    { 
        loc: 
        { $geoIntersects: 
          { 
              $geometry: 
              { 
                  "type": "LineString", 
                  "coordinates": [ [-73.938611,40.664167] , [-80.224145,25.787676] ] 
              }  
          }
        }           
    }
)
```
[Wynik](https://github.com/mossowski/NoSQL-lab/blob/master/places/places3.geojson)

###Przykład 4: $near

#### Zapytanie
```bash
db.places.find(
    { 
        loc: 
        { $near : 
          {
              $geometry: { type: "Point", coordinates: [-95,39] }, 
              $minDistance: 2000000 
          } 
        } 
    }
)
```
[Wynik](https://github.com/mossowski/NoSQL-lab/blob/master/places/places4.geojson)

###Przykład 5: $geoWithin

#### Zapytanie
```bash
db.places.find(
    { 
        loc: 
        { $geoWithin: 
          { 
              $geometry: 
              { 
                  "type" : "Polygon", 
                  "coordinates" : [ [ [-125,39],[-110,39],[-110,33],[-125,33],[-125,39] ] ]   
              }  
          }
        }           
    }
)
```
[Wynik](https://github.com/mossowski/NoSQL-lab/blob/master/places/places5.geojson)
