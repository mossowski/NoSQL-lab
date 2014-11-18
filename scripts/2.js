  var coll = db.imdb;
  var result = coll.aggregate(
    { $match: { "modelName": "movies", "action": "Disliked"}},
    { $group: {_id: "$userId", count: {$sum: 1}} },
    { $sort: {count: -1} },
    { $limit: 10}
  );
  printjson(result);
