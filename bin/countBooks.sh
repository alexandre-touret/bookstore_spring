#! /bin/bash

curl -s \
  -w "\n" \
  'localhost:8082/api/books/count' \
  -H 'accept: application/json' | jq
