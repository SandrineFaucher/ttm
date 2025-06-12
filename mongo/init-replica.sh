#!/bin/bash

if mongosh --eval "rs.status()" | grep -q "no replset config has been received"; then
  echo "=> Initializing replica set"
  mongosh --eval "rs.initiate({_id: 'rs0', members: [{ _id: 0, host: 'localhost:27017' }]})"
else
  echo "=> Replica set already initialized"
fi