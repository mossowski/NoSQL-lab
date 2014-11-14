### *Marcin Ossowski*

----

# Table of content
- [Zadanie 1](#zad1)
    - [1a](#1a)
    - [1b](#1b)
  

# Zadanie 1

### Preparing data

```bash 
 time cat Train.csv | tr "\n" " " | tr "\r" "\n" | head -n 6034196 > Train2.csv

 real   2m23.399s
 user   0m51.452s
 sys    1m23.592s
```

### 1a

```bash
 time mongoimport --type csv -c Train --file Train2.csv --headerline
 
 real   16m36.446s
 user   1m38.714s
 sys    0m34.831s
 ```
 
 ### 1b
  
