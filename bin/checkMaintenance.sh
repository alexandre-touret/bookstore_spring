#!/bin/bash
curl -s -w "\n" -H "Content-Type: application/json" localhost:8082/api/maintenance | python -m json.tool
