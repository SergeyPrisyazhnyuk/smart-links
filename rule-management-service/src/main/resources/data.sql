INSERT INTO rule (device, browser, region) VALUES ('android', 'chrome', 'ru');
INSERT INTO rule (device, browser, region) VALUES ('iphone', 'yabrowser', 'en');
INSERT INTO rule (device, browser, region) VALUES ('desktop', 'yabrowser', 'ru');
INSERT INTO rule (device, browser, region) VALUES ('desktop', 'chrome', 'ru');
INSERT INTO rule (device, browser, region) VALUES ('iphone', 'safari', 'us');

INSERT INTO route_url(route_rule_id, destination_url) VALUES ((SELECT id FROM rule WHERE device='android' AND browser='chrome' AND region='ru'), '/android/chrome/ru');
INSERT INTO route_url(route_rule_id, destination_url) VALUES ((SELECT id FROM rule WHERE device='iphone' AND browser='yabrowser' AND region='en'), '/iphone/yabrowser/en');
INSERT INTO route_url(route_rule_id, destination_url) VALUES ((SELECT id FROM rule WHERE device='desktop' AND browser='chrome' AND region='ru'), '/desktop/chrome/ru');
INSERT INTO route_url(route_rule_id, destination_url) VALUES ((SELECT id FROM rule WHERE device='desktop' AND browser='yabrowser' AND region='ru'), '/desktop/yabrowser/ru');
INSERT INTO route_url(route_rule_id, destination_url) VALUES ((SELECT id FROM rule WHERE device='iphone' AND browser='safari' AND region='us'), '/iphone/safari/us');
