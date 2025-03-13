## Summary 

In joinery industry all the cabinets come in various widths and sizes. Each cabinet is split in two categories:

1. board components:

   Cabinets are made of 2 sides, bottom part, top part, and backing. There are various materials, however one is dominate.

2. hardware

   such as hinges, drawer runners etc


When a joiner places an order to the board company, most of the time they need to calculate manually (or very slow google sheet) the cabinets individual board components.

For example a cabinet of W600xD580xH800mm will have the following board components

| HEIGHT | WIDTH | QT | EDGE LONG (H) | EDGE SHORT (L) | DESCRIPTION |
|:-------|:------|:--:|:-------------:|:---------------|:------------|
| 800    | 580   | 2  |       X       |                | SIDE        |
| 570    | 580   | 1  |       X       |                | BOTTOM      | 
| 570    | 80    | 1  |       X       |                | TOP         |
| 570    | 563   | 1  |       X       |                | SHELF       |
| 570    | 584   | 1  |      N/A      |                | BACKING     |

In this web application the user can inout the finished cabinets dimensions and the board component list will be generated. 


## Languages & Technologies 
Java Spring

## Run Postgres on docker
Docker 
```declarative
docker pull postgres
docker run --name postgres-db -e POSTGRES_PASSWORD=mysecretpassword -e POSTGRES_USER=postgres -d -p 5432:5432 postgres
```
This is based on the latest docker image `postgres:17.4`