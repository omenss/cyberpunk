# ELASTICSEARCH DSL 语法简单使用

## 创建索引
```DSL
PUT /product?pretty
```

## 查询所有索引
```DSL
GET /_cat/indices?v
```
## 给指定索引创建插入数据 document_id 自动生成
```DSL
POST /product/_doc
{
    "id": "1",
    "name": "耐克鞋子",
    "dec": "耐克牌男鞋",
    "price": 100,
    "producer": "福建莆田",
    "tags": [
        "高仿",
        "舒适"
    ],
    "features": [
        {
            "颜色": "红色"
        },
        {
            "尺码": "42"
        }
    ]
}
```
```DSL
POST /product/_doc
{
    "id": "2",
    "name": "阿迪鞋子",
    "dec": "阿迪牌男鞋",
    "price": 90,
    "producer": "福建泉州",
    "tags": [
        "高仿",
        "舒适",
        "性价比高"
    ],
    "features": [
        {
            "颜色": "黑色"
        },
        {
            "尺码": "41"
        }
    ]
}
    
 ```

## 查询索引下的所有数据

```DSL

GET product/_search

```

## bool 查询
```DSL
GET /product/_search
{
    "query": {
        "bool": {
            "must": [
                {
                    "match": {
                        "name": "耐克鞋子"
                    }
                }
            ],
            "must_not": [
                {
                    "match": {
                        "name": "牙膏"
                    }
                }
            ],
            "filter": {
                "range": {
                    "price": {
                        "gte": 20,
                        "lte": 300
                    }
                }
            },
            "should": [
                {
                    "match": {
                        "producer": "莆田"
                    }
                }
            ]
        }
    }
}
```

## 控制字段的**boosting**，控制关键词搜索结果的得分
```DSL
GET /product/_search
{
    "query": {
        "bool": {
            "should": [
                {
                    "match": {
                        "tags": {
                            "query": "性价比高",
                            "boost": 4
                        }
                    }
                },
                {
                    "match": {
                        "tags": {
                            "query": "舒适",
                            "boost": 1
                        }
                    }
                }
            ]
        }
    }
}
```


##  短语检索 只会返回完全匹配的数据
```DSL
GET /product/_search
{
    "query": {
        "match_phrase": {
            "dec": "阿迪牌男鞋"
        }
    }
}
```
## 聚合分析
在使用es的聚合分析功能之前必须将聚合分析的字段的fielddata属性设置为true，否则会报action_request_validation_exception这个异常，设置索引的某个字段的fielddata属性语法如下
```DSL
PUT /product
{
  "mappings": {
    "properties": {
      "name": {
        "type":  "text"
      }
    }
  }
}

```

同时需要注意 进行分组查询的字段类型需要为keyword  如果是text 字段类型 无法开启聚合查询

```DSL
PUT product
{
  "mappings":{
    "properties":{
      "name":{
        "type":"keyword"
      }
    }
  }
}
```

最后就可以通过下面查询语句进行聚合查询
```DSL
GET /product/_search
{
  "size": 0,                   
  "aggs": {
    "group_by_name": {         
      "terms": {
        "field": "name"             }
    }
  }
}
```
查询结果
```JSON
{
  "took": 1,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": {
      "value": 1,
      "relation": "eq"
    },
    "max_score": null,
    "hits": []
  },
  "aggregations": {
    "group_by_name": {
      "doc_count_error_upper_bound": 0,
      "sum_other_doc_count": 0,
      "buckets": [
        {
          "key": "阿迪鞋子",
          "doc_count": 1
        }
      ]
    }
  }
}
```

## 初始化 index 及其mapping
```DSL
PUT product
{
    "mappings": {
        "properties": {
            "name": {
                "type": "keyword"
            },
            "price": {
                "type": "double"
            },
            "desc": {
                "type": "text"
            },
            "tag": {
                "type": "object"
            },
            "feature": {
                "type": "nested",
                "properties": {
                    "feature_name": {
                        "type": "text"
                    },
                    "feature_value": {
                        "type": "keyword"
                    }
                }
            }
        }
    }
}
```



