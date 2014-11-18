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

Sprawdzenie:
```bash
  db.imdb.count()
  19831300
```

# Import do MongoDB

```bash
  time mongoimport -d imdb -c imdb --type json --file getglue_sample.json

  real	40m25.199s
  user	5m26.882s
  sys	1m31.029s
```
