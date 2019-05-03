
INSERT INTO vendor_contact(id, vendor_address, vendor_city, vendor_contact_name, vendor_country, vendor_description, vendor_email, vendor_name, vendor_phone, vendorurl) VALUES ('vc1', '123 Street Ave', 'Townsville', 'Bob Smith', 'USA', 'Example vendor used for testing', 'bsmith@brapi.org', 'Bobs Vendor', '+12345678910', 'https://brapi.org');
INSERT INTO vendor_specification(id, additional_info, vendor_contact_entity_id) VALUES ('vs1', '{}', 'vc1');
INSERT INTO vendor_specification_service(id, service_description, service_id, service_name, service_platform_marker_type, service_platform_name, specific_requirements, vendor_specification_id) VALUES ('vss1', 'Dummy Service One is a vendor service', 'serviceone', 'Service One', 0, 'Platform One', '{}', 'vs1');
INSERT INTO vendor (id, vendor_name, vendor_baseurl, vendor_spec_id, vendor_service_class) VALUES ('v1', 'BrAPI Test Server', 'https://test-server.brapi.org/brapi/v1/', 'vs1', 0);

INSERT INTO vendor_contact(id, vendor_address, vendor_city, vendor_contact_name, vendor_country, vendor_description, vendor_email, vendor_name, vendor_phone, vendorurl) VALUES ('vc2', '123 Street Ave', 'Townsville', 'Bob Smith', 'USA', 'Example vendor used for testing', 'bsmith@brapi.org', 'Bobs Vendor', '+12345678910', 'https://brapi.org');
INSERT INTO vendor_specification(id, additional_info, vendor_contact_entity_id) VALUES ('vs2', '{}', 'vc2');
INSERT INTO vendor_specification_service(id, service_description, service_id, service_name, service_platform_marker_type, service_platform_name, specific_requirements, vendor_specification_id) VALUES ('vss2', 'Dummy Service One is a vendor service', 'serviceone', 'Service One', 0, 'Platform One', '{}', 'vs2');
INSERT INTO vendor (id, vendor_name, vendor_baseurl, vendor_spec_id, vendor_service_class) VALUES ('v2', 'Localhost Test Server', 'https://localhost:8080/brapi/v1/', 'vs2', 0);

INSERT INTO vendor_contact(id) VALUES ('vc3');
INSERT INTO vendor_specification(id, additional_info, vendor_contact_entity_id) VALUES ('vs3', '{}', 'vc3');
INSERT INTO vendor (id, vendor_name, vendor_baseurl, vendor_spec_id, vendor_service_class) VALUES ('v3', 'Cornell Test Server', 'https://slimstest.biotech.cornell.edu/brapi/v1/', 'vs3', 1);

INSERT INTO vendor_contact(id) VALUES ('vc4');
INSERT INTO vendor_specification(id, additional_info, vendor_contact_entity_id) VALUES ('vs4', '{}', 'vc4');
INSERT INTO vendor (id, vendor_name, vendor_baseurl, vendor_spec_id, vendor_service_class) VALUES ('v4', 'DArT Test Server', 'https://ordering-testing.diversityarrays.com/brapi/v1', 'vs4', 2);

INSERT INTO sample_group(id, date_created, date_modified, description, name) VALUES ('1A', now(), now(), 'sample group 1', 'Demo Sample Group');
INSERT INTO sample(id, concentration, notes, sample_external_id, sample_timestamp, sample_type, taken_by, tissue_type, volume, group_id)	VALUES ('s1', '10', 'notes', 'externalID_1234', now(), 'tissue', 'Bob', 'leaf', '10', '1A');
INSERT INTO sample(id, concentration, notes, sample_external_id, sample_timestamp, sample_type, taken_by, tissue_type, volume, group_id)	VALUES ('s2', '10', 'notes', 'externalID_2345', now(), 'tissue', 'Bob', 'leaf', '10', '1A');
INSERT INTO sample(id, concentration, notes, sample_external_id, sample_timestamp, sample_type, taken_by, tissue_type, volume, group_id)	VALUES ('s3', '10', 'notes', 'externalID_3456', now(), 'tissue', 'Bob', 'leaf', '10', '1A');
