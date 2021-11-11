with userid as (
	select 
		-- IDから数字を取り出して, 1 足した後 'U' を先頭に文字列結合する
		'U' || 
		to_char(
			to_number(
				substr(
					max(userid),
					2
				), 
				'9000'
			) + 1,
			'FM0999'
		) 
		as nextid 
	from users
)
insert into users (userid, username, mailaddress, password)
select userid.nextid, 'ja' as username, 'ja@ja' as mailaddress, 'ja' as password
from userid