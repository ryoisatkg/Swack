WITH count_roomusers AS (
        SELECT
                joinroom.roomid AS id
                ,COUNT(joinroom.userid) AS cnt
            FROM
                joinroom
            GROUP BY
                id
) viewable_rooms as(
    SELECT
            rooms.roomid
            ,rooms.privated
            ,rooms.directed
            ,(
                joinroom.userid IS NOT NULL
            ) AS joined
        FROM
            rooms LEFT JOIN joinroom
                ON joinroom.userid = 'U0004'
            AND rooms.roomid = joinroom.roomid
        WHERE
            NOT rooms.privated
            OR joinroom.userid IS NOT NULL
);

SELECT
        viewable_rooms.id
        ,name
        ,private
        ,direct
        ,joined
        ,coalesce(
                count_roomusers.cnt, 0)AS cnt
    FROM
        viewable_rooms
            LEFT JOIN count_roomusers
                ON viewable_rooms.id = count_roomusers.id; 
