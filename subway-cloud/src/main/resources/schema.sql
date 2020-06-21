create table if not exists Ingredient(
	id varchar(4) not null,
	name varchar(25) not null,
	type varchar(10) not null
);

create table if not exists SandWich(
	id identity,
	name varchar(50) not null,
	createdAt timestamp not null
);

create table if not exists SandWich_Ingredients(
	sandwich bigint not null,
	ingredient varchar(4) not null
);

alter table SandWich_Ingredients
	add foreign key(sandwich) references SandWich(id);
alter table SandWich_Ingredients
	add foreign key(ingredient) references Ingredient(id);
	
create table if not exists SandWich_Order(
	id identity,
	deliveryName varchar(50) not null,
	deliveryStreet varchar(50) not null,
	deliveryCity varchar(50) not null,
	deliveryState varchar(50) not null,
	deliveryZip varchar(10) not null,
	ccNumber varchar(16) not null,
	ccExpiration varchar(5) not null,
	ccCVV varchar(3) not null,
	placedAt timestamp not null
);

create table if not exists SandWich_Order_SandWiches(
	sandwichOrder bigint not null,
	sandwich bigint not null
);

alter table SandWich_Order_SandWiches
	add foreign key(sandwichOrder) references SandWich_Order(id);
alter table SandWich_Order_SandWiches
	add foreign key(sandwich) references SandWich(id);






