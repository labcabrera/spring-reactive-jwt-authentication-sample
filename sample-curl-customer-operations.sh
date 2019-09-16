#/bin/bash

AUTH_HEADER="Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE1Njg2MzAyMjYsImV4cCI6MTU3MjIzMzgyNiwiaXNzIjoic2FtcGxlLWFwcCIsInN1YiI6ImRpc3RyaWJ1dG9yLTAxIiwicm9sZXMiOlsiZGlzdHJpYnV0b3ItMDEiXX0.ZpkZ31gqhZ2ReHNV7dEj5FIPjNP2NYUBkeHrB2gg7eFMwdfdazWWXfEXinrC_Xv7qV-yTFsuS5CMkf8FctH4ew"

CREATION_JSON=$(curl -X POST -H "${AUTH_HEADER}" -H "Content-Type: application/json" \
  -d '{"firstName":"Marcel","lastName":"Proust"}' \
  http://localhost:8080/customers/)

echo
echo "Customer creation response"
echo $CREATION_JSON

CUSTOMER_ID=$(echo $CREATION_JSON \
  | python -m json.tool \
  | grep 'id' \
  | cut -d ':' -f 2 \
  | sed 's/"//g' | sed 's/ //g' | sed 's/,//g')

echo
echo "Customer Id"
echo $CUSTOMER_ID
echo
echo "Customer search:"

curl -H "$AUTH_HEADER" "http://localhost:8080/customers/${CUSTOMER_ID}"

echo
echo
echo "Customer update":

curl -X PUT -H "${AUTH_HEADER}" -H "Content-Type: application/json" \
  -d '{"firstName":"Marcel","lastName":"Proust","gender":"male"}' \
  http://localhost:8080/customers/$CUSTOMER_ID
