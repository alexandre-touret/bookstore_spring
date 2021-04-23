#! /bin/bash
curl -s -w "\n" localhost:8082/q/openapi?format=json | jq | more
