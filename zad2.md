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
[Skrypt](https://github.com/mossowski/NoSQL-lab/blob/master/scripts/1.js)

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
![1](https://github.com/mossowski/NoSQL-lab/blob/master/images/1.png)

## Agregacja 2 - 10 najbardziej dislikujących użytkowników 

Agregacja:
```bash
  coll.aggregate(
    { $match: { "modelName": "movies", "action": "Disliked"}},
    { $group: {_id: "$userId", count: {$sum: 1}} },
    { $sort: {count: -1} },
    { $limit: 10}
  );
```
[Skrypt](https://github.com/mossowski/NoSQL-lab/blob/master/scripts/2.js)

Wynik:
```json
    { "_id" : "danceswithflowers", "count" :  968 },
    { "_id" : "andrew_warner",     "count" : 1010 },
    { "_id" : "s34rchnd3str0y",    "count" : 1054 },
    { "_id" : "kevinjloria",       "count" : 1219 },
    { "_id" : "zeus1661ou",        "count" : 1460 },
    { "_id" : "amanda_hauser",     "count" : 1497 },
    { "_id" : "brownbagcomics",    "count" : 1807 },
    { "_id" : "Ang",               "count" : 1884 },
    { "_id" : "Carlson1931",       "count" : 2134 },
    { "_id" : "Xendeus",           "count" : 2576 }
```
Czas:
```bash
 real	3m17.094s
 user	0m0.056s
 sys	0m0.036s
```
Wykres:
![2](https://github.com/mossowski/NoSQL-lab/blob/master/images/2.png)

## Agregacja 3 - 10 reżyserów mających najwięcej filmów

Agregacja:
```bash
 coll.aggregate( 
   { $match: { "modelName": "movies"} },
   { $group: {_id: {"dir": "$director", id: "$title"}, count: {$sum: 1}} },
   { $group: {_id: "$_id.dir" , count: {$sum: 1}} },
   { $sort: {count: -1} },
   { $limit: 10} 
 );
```
[Skrypt](https://github.com/mossowski/NoSQL-lab/blob/master/scripts/3.js)

Wynik:
```json
   {" _id" : "steven spielberg",  "count" : 41   },
   {" _id" : "john ford",         "count" : 42   },
   {" _id" : "ingmar bergman",    "count" : 42   },
   {" _id" : "jesus franco",      "count" : 43   },
   {" _id" : "takashi miike",     "count" : 43   },
   {" _id" : "woody allen",       "count" : 47   },
   {" _id" : "michael curtiz",    "count" : 48   },
   {" _id" : "alfred hitchcock",  "count" : 50   },
   {" _id" : "various directors", "count" : 54   },
   {" _id" : "not available",     "count" : 1474 }
```
Czas:
```bash
   real	  6m14.168s
   user	  0m0.059s
   sys	  0m0.029s

```
Wykres:
![3](https://github.com/mossowski/NoSQL-lab/blob/master/images/3.png)

## Agregacja 4 - ilość wystąpień każdej z akcji

Agregacja:
```bash
   coll.aggregate(
     { $group: {_id: "$action", count: {$sum: 1}} },
     { $sort: {count: -1} }
   );
```
[Skrypt](https://github.com/mossowski/NoSQL-lab/blob/master/scripts/4.js)

Wynik:
```json
    { "_id" : "Checkin",      "count" : 10958039 },
    { "_id" : "Liked",        "count" : 7664733  },
    { "_id" : "Disliked",     "count" : 469093   },
    { "_id" : "Favorited",    "count" : 288096   },
    { "_id" : "Unwanted",     "count" : 270330   },
    { "_id" : "Saved",        "count" : 101944   },
    { "_id" : "Said",         "count" : 73887    },
    { "_id" : "Looked",       "count" : 2972     },
    { "_id" : "Comment",      "count" : 2150     },
    { "_id" : null,           "count" : 40       },
    { "_id" : "Reply",        "count" : 15       },
    { "_id" : "LikedComment", "count" : 1        }
```
Czas:
```bash
   real	  3m13.166s
   user	  0m0.054s
   sys	  0m0.029s
```
Wykres:
![4](https://github.com/mossowski/NoSQL-lab/blob/master/images/4.png)
