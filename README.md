### *Marcin Ossowski*

----

# Table of content
- [Zadanie 1](#zad1)
    - [1a](#1a)
    - [1b](#1b)
    - [1c](#1c)
  

# Zadanie 1

### Preparing data for mongoDB

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


```bash
 time mongoimport --type csv -c train --file Train2.csv --headerline
 
 real   16m36.446s
 user   1m38.714s
 sys    0m34.831s
```

```bash
 time psql -d postgres -c "copy train(Id,Title,Body,Tags) from '/home/marcin/Downloads/Train.csv' with delimiter ',' csv header;"
 
 real   32m3.404s
 user   0m0.102s
 sys    0m0.085s
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

Comparison of the collection:

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

  
