### *Marcin Ossowski*

----

# Table of content
- [Zadanie 1](#zad1)
    - [1a](#1a)
    - [1b](#1b)
  

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

```bash
 time mongoimport --type csv -c train --file Train2.csv --headerline
 
 real   16m36.446s
 user   1m38.714s
 sys    0m34.831s
```

```bash
 time psql -d postgres -c "copy train(Id,Title,Body,Tags) from '/home/marcin/Downloads/Train.csv' with delimiter ',' csv header;"
 
 real   13m43.682s
 user   0m0.058s
 sys    0m0.042s
```
 
### 1b

```bash
  db.train.count();
  
  6034195
```
  
