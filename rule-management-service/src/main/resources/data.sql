INSERT INTO rule (device, browser, region) VALUES ('android', 'chrome', 'ru');
INSERT INTO rule (device, browser, region) VALUES ('iphone', 'yabrowser', 'en');
INSERT INTO rule (device, browser, region) VALUES ('desktop', 'yabrowser', 'ru');
INSERT INTO rule (device, browser, region) VALUES ('desktop', 'chrome', 'ru');
INSERT INTO rule (device, browser, region) VALUES ('desktop', 'safari', 'us');
INSERT INTO rule (device, browser, region) VALUES ('android', 'safari', null);
INSERT INTO rule (device, browser, region) VALUES ('iphone', null, 'ru');
INSERT INTO rule (device, browser, region) VALUES ('desktop', null, null);

INSERT INTO route_url(route_id, destinationURL) VALUES ((SELECT id FROM rule WHERE device='android' AND browser='chrome' AND region='ru'), '/android/chrome/ru');
INSERT INTO route_url(route_id, destinationURL) VALUES ((SELECT id FROM rule WHERE device='iphone' AND browser='yabrowser' AND region='en'), '/iphone/yabrowser/en');

INSERT INTO route_url(route_id, destinationURL) VALUES ((SELECT id FROM rule WHERE device='desktop' AND browser='chrome' AND region='ru'), '/iphone/yabrowser/en');
INSERT INTO route_url(route_id, destinationURL) VALUES ((SELECT id FROM rule WHERE device='desktop' AND browser='yabrowser' AND region='ru'), '/iphone/yabrowser/en');


INSERT INTO route_url(route_id, destinationURL) VALUES ((SELECT id FROM rule WHERE device='desktop' AND browser='safari' AND region='us'), '/desktop/safari/us');
INSERT INTO route_url(route_id, destinationURL) VALUES ((SELECT id FROM rule WHERE device='android' AND browser='safari' AND region IS NULL), '/android/safari');
INSERT INTO route_url(route_id, destinationURL) VALUES ((SELECT id FROM rule WHERE device='iphone' AND browser IS NULL AND region='ru'), '/iphone/ru');
INSERT INTO route_url(route_id, destinationURL) VALUES ((SELECT id FROM rule WHERE device='desktop' AND browser IS NULL AND region IS NULL), '/desktop');