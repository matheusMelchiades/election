create table election (
  id integer identity primary key,
  year integer(4) not null ,
  state_code varchar(5) not null,
  description varchar(255) not null,
  constraint check_year check(year >= 2000 and year <=2200)
);

create table vote (
  id integer intendity primary key,
  electionId integer not null,
  voterId integer not null,
  candidateId integer not null
);