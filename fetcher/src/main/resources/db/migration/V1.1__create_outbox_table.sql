create table outbox_event
(
    id            uuid primary key       not null,
    aggregatetype character varying(255) not null,
    aggregateid   character varying(255) not null,
    payload       jsonb
);
