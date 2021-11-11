WITH joined_privaterooms AS (
    SELECT
        rooms.roomid
    FROM
        rooms
    JOIN joinroom
      ON joinroom.userid = ?
         AND rooms.directed = false
         AND rooms.privated
         AND rooms.roomid = joinroom.roomid
)
,publicrooms AS (
SELECT
    rooms.roomid
FROM
    rooms
WHERE
    NOT rooms.directed
    AND NOT rooms.privated
)
,canviewrooms AS (
SELECT
	roomid
FROM
    joined_privaterooms
UNION ALL
SELECT
    roomid
FROM
    publicrooms
)
SELECT
rooms.roomid AS id
,rooms.roomname AS name
,rooms.directed AS direct
,rooms.privated AS private
FROM
canviewrooms
    JOIN rooms
        ON canviewrooms.roomid = rooms.roomid;
