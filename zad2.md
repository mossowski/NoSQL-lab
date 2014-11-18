### *Marcin Ossowski*

---

# Dane

GetGlue and Timestamped Event Data (ok. 11 GB, 19 831 300 json-ów). Są to dane z IMDB z lat 2007–2012, tylko filmy i przedstawienia TV. 

Przykładowy dokument:
```json
{
	"_id" : ObjectId("546b9e22100835c2fd987536"),
	"comment" : "",
	"hideVisits" : "false",
	"modelName" : "tv_shows",
	"displayName" : "",
	"title" : "Criminal Minds",
	"timestamp" : "2008-08-01T06:58:14Z",
	"image" : "http://cdn-1.nflximg.com/us/boxshots/large/70056671.jpg",
	"userId" : "areilly",
	"private" : "false",
	"source" : "http://www.netflix.com/Movie/Criminal_Minds_Season_1/70056671",
	"version" : "2",
	"link" : "http://www.netflix.com/Movie/Criminal_Minds_Season_1/70056671",
	"lastModified" : "2011-12-16T19:41:19Z",
	"action" : "Liked",
	"lctitle" : "criminal minds",
	"objectKey" : "tv_shows/criminal_minds",
	"visitCount" : "1"
}
```

# Import do MongoDB

```bash
  time mongoimport -d imdb -c imdb --type json --file getglue_sample.json
 
  real	  40m25.199s
  user    5m26.882s
  sys	  1m31.029s
```

Sprawdzenie:
```bash
  db.imdb.count()
  19831300
```

# Agregacje

## Agregacja 1 - 10 najpopularniejszych filmów 


Agregacja:
```bash
  coll.aggregate(
    { $match: { "modelName": "movies" } },
    { $group: {_id: "$title", count: {$sum: 1} } },
    { $sort: {count: -1} },
    { $limit: 10}
  );
```

Wynik:
```json
    { "_id" : "The Twilight Saga: Breaking Dawn Part 1",       "count" : 87521 },
    { "_id" : "The Hunger Games",                              "count" : 79340 },
    { "_id" : "Marvel's The Avengers",                         "count" : 64356 },
    { "_id" : "Harry Potter and the Deathly Hallows: Part II", "count" : 33680 },
    { "_id" : "The Muppets",                                   "count" : 29002 },
    { "_id" : "Captain America: The First Avenger",            "count" : 28406 },
    { "_id" : "Avatar",                                        "count" : 23238 },
    { "_id" : "Thor",                                          "count" : 23207 },
    { "_id" : "The Hangover",                                  "count" : 22709 },
    { "_id" : "Titanic",                                       "count" : 20791 }
```
Czas:
```bash
  real	  3m15.473s
  user	  0m0.053s
  sys	  0m0.030s
```
Wykres:
