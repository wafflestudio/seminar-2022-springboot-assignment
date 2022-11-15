drop table seminar_user;
create table seminar_user (
    id bigint primary key auto_increment,
    created_at datetime(6) default CURRENT_TIMESTAMP(6),
    modified_at datetime(6) default CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    logined_at datetime(6) default CURRENT_TIMESTAMP(6),
    username varchar(255) not null ,
    email varchar(256) not null unique ,
    password varchar(256) not null,
    role ENUM('PARTICIPANT', 'INSTRUCTOR', 'BOTH') not null,
    is_registered boolean default true,
    company varchar(256),
    year int
);

drop table instructor_profile;
create table instructor_profile (
    id bigint primary key auto_increment,
    created_at datetime(6) default CURRENT_TIMESTAMP(6),
    modified_at datetime(6) default CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    seminar_user_id bigint unique ,
    FOREIGN KEY (seminar_user_id) REFERENCES seminar_user(id)
);

drop table participant_profile;
create table participant_profile (
    id bigint primary key auto_increment,
    created_at datetime(6) default CURRENT_TIMESTAMP(6),
    modified_at datetime(6) default CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    seminar_user_id bigint unique ,
    FOREIGN KEY (seminar_user_id) REFERENCES seminar_user(id)
);

drop table seminar;
create table seminar (
    id bigint primary key auto_increment,
    created_at datetime(6) default CURRENT_TIMESTAMP(6),
    modified_at datetime(6) default CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    name varchar(30) not null unique ,
    capacity int UNSIGNED ,
    count int UNSIGNED ,
    time varchar(10),
    online boolean default true
);

drop table user_seminar;
create table user_seminar (
    id bigint primary key auto_increment,
    created_at datetime(6) default CURRENT_TIMESTAMP(6),
    modified_at datetime(6) default CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    dropped_at datetime(6) ,
    seminar_user_id bigint ,
    seminar_id bigint ,
    is_active boolean,
    is_participant boolean ,
    FOREIGN KEY (seminar_user_id) REFERENCES seminar_user(id) ON UPDATE CASCADE ON DELETE CASCADE ,
    FOREIGN KEY (seminar_id) REFERENCES seminar(id) ON UPDATE CASCADE ON DELETE CASCADE
);




select * from seminar as s inner join user_seminar us on s.id = us.seminar_id inner join seminar_user su on us.seminar_user_id = su.id
where us.is_participant=false and s.name like concat('%', 'Spring', '%') order by s.created_at DESC;

select us.seminar_id, count(seminar_id) from user_seminar as us where us.is_participant=true group by us.seminar_id;
