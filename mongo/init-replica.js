if(rs.status().includes("no replset config has been received")) {
  rs.initiate({_id: 'rs0', members: [{ _id: 0, host: 'localhost:27017' }]})
}
