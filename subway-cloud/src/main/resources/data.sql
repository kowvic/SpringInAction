delete from SandWich_Order_SandWiches;
delete from SandWich_Ingredients;
delete from SandWich;
delete from SandWich_Order;

delete from Ingredient;
insert into Ingredient(id, name, type)
	values('HOOT', 'Honey Oat', 'BREAD');
insert into Ingredient(id, name, type)
	values('ORGA', 'Organic Bread', 'BREAD');
insert into Ingredient(id, name, type)
	values('WHIT', 'White Bread', 'BREAD');
insert into Ingredient(id, name, type)
	values('MEAT', 'Meat', 'PROTEIN');
insert into Ingredient(id, name, type)
	values('BEEF', 'Beef', 'PROTEIN');
insert into Ingredient(id, name, type)
	values('CHIC', 'Chicken', 'PROTEIN');
insert into Ingredient(id, name, type)
	values('LETT', 'Letuce', 'VEGGIES');
insert into Ingredient(id, name, type)
	values('CUCU', 'Cucumbers', 'VEGGIES');
insert into Ingredient(id, name, type)
	values('ONIO', 'Onions', 'VEGGIES');
insert into Ingredient(id, name, type)
	values('AMER', 'American Cheese', 'CHEESE');
insert into Ingredient(id, name, type)
	values('SHRE', 'Shredded Cheese', 'CHEESE');
insert into Ingredient(id, name, type)
	values('MOZZ', 'Mozzarella Cheese', 'CHEESE');
insert into Ingredient(id, name, type)
	values('SWON', 'Sweet Onion', 'SAUCE');
insert into Ingredient(id, name, type)
	values('HOMU', 'Honey Mustard', 'SAUCE');
insert into Ingredient(id, name, type)
	values('SWCH', 'Sweet Chilli', 'SAUCE');	
	