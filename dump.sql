-- public.appointments definition

-- Drop table

-- DROP TABLE appointments;

CREATE TABLE appointments (
	endtime timestamp(6) NOT NULL,
	starttime timestamp(6) NOT NULL,
	id uuid NOT NULL,
	description varchar(255) NOT NULL,
	CONSTRAINT appointments_pkey PRIMARY KEY (id)
);


-- public.jettysessions definition

-- Drop table

-- DROP TABLE jettysessions;

CREATE TABLE jettysessions (
	sessionid varchar(120) NOT NULL,
	contextpath varchar(60) NOT NULL,
	virtualhost varchar(60) NOT NULL,
	lastnode varchar(60) NULL,
	accesstime int8 NULL,
	lastaccesstime int8 NULL,
	createtime int8 NULL,
	cookietime int8 NULL,
	lastsavedtime int8 NULL,
	expirytime int8 NULL,
	maxinterval int8 NULL,
	"map" bytea NULL,
	CONSTRAINT jettysessions_pkey PRIMARY KEY (sessionid, contextpath, virtualhost)
);
CREATE INDEX idx_jettysessions_expiry ON public.jettysessions USING btree (expirytime);
CREATE INDEX idx_jettysessions_session ON public.jettysessions USING btree (sessionid, contextpath);


-- public.logs definition

-- Drop table

-- DROP TABLE logs;

CREATE TABLE logs (
	"type" int2 NOT NULL,
	"timestamp" timestamp(6) NOT NULL,
	id uuid NOT NULL,
	userid uuid NULL,
	message varchar(255) NOT NULL,
	CONSTRAINT logs_pkey PRIMARY KEY (id),
	CONSTRAINT logs_type_check CHECK (((type >= 0) AND (type <= 8)))
);


-- public.users definition

-- Drop table

-- DROP TABLE users;

CREATE TABLE users (
	darkmode bool NOT NULL,
	islanguagegerman bool NOT NULL,
	"role" int2 NOT NULL,
	id uuid NOT NULL,
	"name" varchar(64) NOT NULL,
	surname varchar(64) NOT NULL,
	passwordhash varchar(128) NOT NULL,
	email varchar(255) NOT NULL,
	CONSTRAINT users_email_key UNIQUE (email),
	CONSTRAINT users_pkey PRIMARY KEY (id),
	CONSTRAINT users_role_check CHECK (((role >= 0) AND (role <= 3)))
);


-- public.customers definition

-- Drop table

-- DROP TABLE customers;

CREATE TABLE customers (
	contactstatus int2 NOT NULL,
	assigned_manager_id uuid NULL,
	id uuid NOT NULL,
	"name" varchar(64) NOT NULL,
	phonenumber varchar(64) NULL,
	surname varchar(64) NOT NULL,
	address varchar(255) NULL,
	company varchar(255) NOT NULL,
	email varchar(255) NULL,
	CONSTRAINT customers_contactstatus_check CHECK (((contactstatus >= 0) AND (contactstatus <= 3))),
	CONSTRAINT customers_email_key UNIQUE (email),
	CONSTRAINT customers_pkey PRIMARY KEY (id),
	CONSTRAINT fkmhuo13ju7wa748s1u8d3bdm5j FOREIGN KEY (assigned_manager_id) REFERENCES users(id)
);


-- public.transactions definition

-- Drop table

-- DROP TABLE transactions;

CREATE TABLE transactions (
	amount float8 NOT NULL,
	status int2 NOT NULL,
	"timestamp" timestamp(6) NOT NULL,
	customer_id uuid NOT NULL,
	id uuid NOT NULL,
	user_id uuid NOT NULL,
	description varchar(512) NULL,
	CONSTRAINT transactions_pkey PRIMARY KEY (id),
	CONSTRAINT transactions_status_check CHECK (((status >= 0) AND (status <= 2))),
	CONSTRAINT fkpnnreq9lpejqyjfct60v7n7x1 FOREIGN KEY (customer_id) REFERENCES customers(id),
	CONSTRAINT fkqwv7rmvc8va8rep7piikrojds FOREIGN KEY (user_id) REFERENCES users(id)
);


-- public.appointment_participants definition

-- Drop table

-- DROP TABLE appointment_participants;

CREATE TABLE appointment_participants (
	appointment_id uuid NOT NULL,
	customer_id uuid NULL,
	id uuid NOT NULL,
	user_id uuid NULL,
	"role" varchar(255) NOT NULL,
	CONSTRAINT appointment_participants_pkey PRIMARY KEY (id),
	CONSTRAINT appointment_participants_role_check CHECK (((role)::text = ANY ((ARRAY['ORGANIZER'::character varying, 'PARTICIPANT'::character varying])::text[]))),
	CONSTRAINT fk3c7ofbr1kebgtt377eo1rdapg FOREIGN KEY (appointment_id) REFERENCES appointments(id),
	CONSTRAINT fkkx13i8ltpfw0ji3f6yvv47jes FOREIGN KEY (customer_id) REFERENCES customers(id),
	CONSTRAINT fkopcduppygs2ln5laml0mtd674 FOREIGN KEY (user_id) REFERENCES users(id)
);


-- public.customer_notes definition

-- Drop table

-- DROP TABLE customer_notes;

CREATE TABLE customer_notes (
	customer_id uuid NOT NULL,
	id uuid NOT NULL,
	note varchar(512) NOT NULL,
	author bytea NULL,
	CONSTRAINT customer_notes_pkey PRIMARY KEY (id),
	CONSTRAINT fkmlqmw0fgfmurvcmhkeqtdq7qs FOREIGN KEY (customer_id) REFERENCES customers(id)
);