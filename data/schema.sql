use cookbook;

drop table if exists recipe;

create table if not exists recipe (
    id int not null auto_increment,
    parent_id int,
    name varchar(255) not null,
    description text,
    created timestamp not null,
    version int not null,
    primary key (id, version)
);

insert into recipe values
  (1, null, 'Fried Chicken', 'Fried Chicken', date('2021-02-27 10:00:00'), 1),
  (1, null, 'Crispiest Fried Chicken', 'Crispiest Fried Chicken', date('2021-02-27 10:00:00'), 2),
  (2, 1, 'with Soy sauce', 'with Soy sauce', date('2021-02-27 11:00:00'), 1),
  (3, 1, 'with Mayo', 'with Mayo', date('2021-02-27 13:00:00'), 1),
  (4, 3, 'and Mustard', 'and Mustard', date('2021-02-27 12:00:00'), 1),
  (5, null, 'Burger', 'Burger', date('2021-02-26 11:28:00'), 1),
  (5, null, 'Hamburger', 'Hamburger', date('2021-02-26 11:28:00'), 2),
  (5, null, 'Cheeseburger', 'Cheeseburger', date('2021-02-26 11:28:00'), 3);
