
*通用API

** 唯一性检查

HEAD /v1/[entity]/[value]

response 
  http status 200 不重复
  http status 409  重复
  
* API 设计规范

** excel导出

查询API + /excel, 参数同查询API一致


GET /v1/transaction   查询API
对应的excel 导出API 为

GET /v1/transaction/excel