create schema dbo
;

create table dbo.Airport(
    ident          varchar(20) primary key,
    iata_code      varchar(20)  ,
    local_code     varchar(50),
    gps_code       varchar(50),
    name           varchar(128),
    "type"         varchar(50),
    latitude_deg   double,
    longitude_deg  double,
    elevation_ft   double,
    continent      varchar(50),
    iso_country    varchar(3),
    iso_region     varchar(50),
    municipality   varchar(100),
)
;
