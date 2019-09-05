create SCHEMA mycurrencies

create TABLE mycurrencies.currencies (
	ABBREVIATION VARCHAR(3) NOT NULL,
	NAME VARCHAR(45) NOT NULL,
	RATE DOUBLE NOT NULL,
	PRIMARY KEY (ABBREVIATION)
);

insert into mycurrencies.currencies values ('USD', 'US Dollar', 1.0);
insert into mycurrencies.currencies values ('ISK', 'Icelandic Krona', 125.85);
insert into mycurrencies.currencies values ('GBP', 'British Pound', 0.83);
insert into mycurrencies.currencies values ('EUR', 'European Euro', 0.89);
insert into mycurrencies.currencies values ('CNY', 'Chinese Yuan', 7.04);
insert into mycurrencies.currencies values ('CAD', 'Canadian Dollar', 1.3);
