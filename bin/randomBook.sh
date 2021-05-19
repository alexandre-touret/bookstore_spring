#! /bin/bash

curl -s \
  -w "\n" \
  'localhost:8082/api/books/random' | jq
