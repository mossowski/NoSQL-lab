// run: mongo imdb agg1.js
  var coll = db.imdb;
  var result = coll.aggregate(
    { $group: {_id: "$action", count: {$sum: 1}} },
    { $sort: {count: -1} }
  );
  printjson(result);
