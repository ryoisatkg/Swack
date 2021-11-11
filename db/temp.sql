(SELECT rooms.roomid, rooms.privated, rooms.directed, (joinroom.userid is not null) as joined
FROM rooms 
	LEFT JOIN joinroom 
		ON joinroom.userid = 'U0004' and rooms.roomid = joinroom.roomid
WHERE NOT rooms.privated OR joinroom.userid is not null)