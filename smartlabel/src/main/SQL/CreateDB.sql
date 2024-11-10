-- Missing: Production

-- Storage multiple:
--	ARM002
--	SCR001
--	CABLE002
--	ENC001
--	COOL002

-- Smartlabel multiple:
--	SCR001
--	NET001
--	GEAR001

-- Orders backlog:
--	CAM001

-- SmartlabelSN with backlog:
--	38

DROP TABLE Orders;
DROP TABLE Smartlabel;
DROP TABLE Storage;
DROP TABLE Delivery;
DROP TABLE BOM;
DROP TABLE Module;
DROP TABLE Material;

CREATE TABLE Material (
	MaterialNo TEXT PRIMARY KEY,
	Materialname TEXT,
	Weight BIGINT
);

CREATE TABLE Module (
	ModuleID SERIAL NOT NULL PRIMARY KEY,
	MaterialNo TEXT,
	FOREIGN KEY (MaterialNo) REFERENCES Material(MaterialNo)
);

CREATE TABLE BOM (
	BomID BIGSERIAL NOT NULL PRIMARY KEY,
	ModuleID INT,
	MaterialNo TEXT,
	Quantity INT,
	FOREIGN KEY (ModuleID) REFERENCES Module(ModuleID),
	FOREIGN KEY (MaterialNo) REFERENCES Material(MaterialNo)
);

CREATE TABLE Delivery (
	DeliveryID Serial NOT NULL PRIMARY KEY,
	MaterialNo TEXT,
	Quantity INT,
	DeliveryDate DATE,
	DeliveryState TEXT,
	FOREIGN KEY (MaterialNo) REFERENCES Material(MaterialNo)
);

CREATE TABLE Storage (
	StorageID BIGSERIAL NOT NULL PRIMARY KEY,
	MaterialNo TEXT,
	Storage TEXT,
	Quantity INT,
	Date DATE,
	FOREIGN KEY (MaterialNo) REFERENCES Material(MaterialNo)
);

CREATE TABLE Smartlabel (
	SmartlabelID BIGSERIAL NOT NULL PRIMARY KEY,
	MaterialNo TEXT,
	SmartlabelSN TEXT,
	StorageProduction TEXT,
	Quantity INT,
	OrderQty INT,
	Date DATE,
	Active BOOLEAN,
	Sensor BOOLEAN,
	MeasuermentPeriode INT,
	FOREIGN KEY (MaterialNo) REFERENCES Material(MaterialNo)
);

CREATE TABLE Orders (
	OrderID SERIAL NOT NULL PRIMARY KEY,
	MaterialNo TEXT,
	SmartlabelID INT,
	DeliveryID INT,
	Quantity INT,
	Date DATE,
	OrderState TEXT,
	FOREIGN KEY (MaterialNo) REFERENCES Material(MaterialNo),
	FOREIGN KEY (SmartlabelID) REFERENCES Smartlabel(SmartlabelID),
	FOREIGN KEY (DeliveryID) REFERENCES Delivery(DeliveryID)
);

INSERT INTO material (MaterialNo, materialname, weight) VALUES
('ARM001', 'Roboterarm Gehäuse (aus Stahl)', 15000000),
('ARM002', 'Gelenke (x6, Hauptarmeinheit)', 800000),
('CTRL-MOD001', 'Elektronisches Steuermodul', 500000),
('DRIVE-MOD001', 'Antriebssystem', 3000000),
('SENS-MOD001', 'Sensor- und Kameramodul', 300000),
('COM-MOD001', 'Kommunikationsmodul', 200000),
('CABLE001', 'Kabelbaum für Signale und Strom', 1500000),
('SAFETY001', 'Not-Aus-Schalter', 50000),
('COOL001', 'Kühlkörper (für Steuerplatine)', 120000),
('PSU001', 'Netzteil (24V, 40A)', 1200000),
('SCR001', 'Gelenkschrauben (Satz)', 10000),
('DISP001', 'Externes Display (für Kontrolle)', 500000),
('CABLE002', 'Kabelkanäle', 80000),
('PCB001', 'Hauptplatine (Leiterplatte, 8-lagig)', 150000),
('CPU001', 'Prozessor ARM Cortex-A76', 25000),
('RAM001', 'RAM 4GB (LPDDR4)', 15000),
('FLASH001', 'Flash-Speicher 32GB', 10000),
('FPGA001', 'FPGA-Modul', 40000),
('NET001', 'Ethernet-Port', 5000),
('CAN001', 'CAN-Bus-Schnittstelle', 5000),
('USB001', 'USB 3.0 Ports (x2)', 8000),
('VR001', 'Spannungsregler (5V, 3.3V)', 5000),
('COOL002', 'Kühlkörper', 100000),
('RES001', 'Widerstand 10kΩ (SMD)', 20),
('CAP001', 'Kondensator 100nF (SMD)', 50),
('MOTOR001', 'Servomotoren (400W)', 500000),
('GEAR001', 'Getriebeeinheit', 700000),
('ENC001', 'Encoder für Positionsüberwachung', 150000),
('INV001', 'Motorsteuerung (Inverter)', 400000),
('BRG001', 'Kugellager für Gelenke', 50000),
('OIL001', 'Hydrauliköl für Gelenkbewegungen', 1000000),
('CAM001', 'Kamera (12 MP, HDR)', 100000),
('LIDAR001', 'Lidar-Sensor (3D)', 200000),
('PROX001', 'Näherungssensoren (Infrarot)', 25000),
('TEMP001', 'Temperatursensor', 5000),
('ETH001', 'Ethernet-Schnittstelle (Gigabit)', 20000),
('WLAN001', 'WLAN-Modul (WiFi 6)', 15000),
('BT001', 'Bluetooth 5.0 Modul', 10000),
('ARM-BOM', 'Roboterarm', 10000000000);

INSERT INTO Module (MaterialNo) VALUES
('ARM-BOM'),
('CTRL-MOD001'),
('DRIVE-MOD001'),
('SENS-MOD001'),
('COM-MOD001');

INSERT INTO BOM (ModuleID, MaterialNo, Quantity) VALUES
(1, 'ARM001', 1),
(1, 'ARM002', 6),
(1, 'CTRL-MOD001', 1),
(1, 'DRIVE-MOD001', 1),
(1, 'SENS-MOD001', 1),
(1, 'COM-MOD001', 1),
(1, 'CABLE001', 1),
(1, 'SAFETY001', 1),
(1, 'COOL001', 1),
(1, 'PSU001', 1),
(1, 'SCR001', 24),
(1, 'DISP001', 1),
(1, 'CABLE002', 10),
(2, 'PCB001', 1),
(2, 'CPU001', 1),
(2, 'RAM001', 2),
(2, 'FLASH001', 1),
(2, 'FPGA001', 1),
(2, 'NET001', 1),
(2, 'CAN001', 1),
(2, 'USB001', 2),
(2, 'VR001', 2),
(2, 'COOL002', 1),
(2, 'RES001', 50),
(2, 'CAP001', 40),
(3, 'MOTOR001', 6),
(3, 'GEAR001', 6),
(3, 'ENC001', 6),
(3, 'INV001', 6),
(3, 'BRG001', 12),
(3, 'OIL001', 1),
(4, 'CAM001', 1),
(4, 'LIDAR001', 1),
(4, 'PROX001', 6),
(4, 'TEMP001', 1),
(5, 'ETH001', 1),
(5, 'WLAN001', 1),
(5, 'BT001', 1);

INSERT INTO Storage (MaterialNo, Storage, Quantity, Date) VALUES
('ARM001', 			'1', 	3, 		CURRENT_DATE),
('ARM002', 			'40', 	1, 		CURRENT_DATE),
('ARM002', 			'41', 	4, 		CURRENT_DATE),
('ARM002', 			'42', 	2, 		CURRENT_DATE),
('ARM002', 			'2', 	1, 		CURRENT_DATE),
('CTRL-MOD001', 	'3', 	50,		CURRENT_DATE),
('DRIVE-MOD001',	'4', 	20,		CURRENT_DATE),
('SENS-MOD001', 	'5', 	4,		CURRENT_DATE),
('COM-MOD001', 		'6', 	6, 		CURRENT_DATE),
('CABLE001', 		'7', 	103,	CURRENT_DATE),
('SAFETY001', 		'8', 	21, 	CURRENT_DATE),
('COOL001', 		'9', 	23, 	CURRENT_DATE),
('PSU001', 			'10', 	254, 	CURRENT_DATE),
('SCR001', 			'43', 	86, 	CURRENT_DATE),
('SCR001', 			'44', 	5, 		CURRENT_DATE),
('SCR001', 			'45', 	15, 	CURRENT_DATE),
('SCR001', 			'46', 	456, 	CURRENT_DATE),
('SCR001', 			'47', 	569, 	CURRENT_DATE),
('SCR001', 			'48', 	456, 	CURRENT_DATE),
('SCR001', 			'49', 	2315, 	CURRENT_DATE),
('SCR001', 			'11', 	268, 	CURRENT_DATE),
('DISP001', 		'12', 	15, 	CURRENT_DATE),
('CABLE002', 		'50', 	358, 	CURRENT_DATE),
('CABLE002', 		'13', 	1546, 	CURRENT_DATE),
('PCB001', 			'14', 	45, 	CURRENT_DATE),
('CPU001', 			'15', 	85, 	CURRENT_DATE),
('RAM001', 			'16', 	56, 	CURRENT_DATE),
('FLASH001', 		'17', 	159, 	CURRENT_DATE),
('FPGA001', 		'18', 	896, 	CURRENT_DATE),
('NET001', 			'19', 	24, 	CURRENT_DATE),
('CAN001', 			'20', 	89, 	CURRENT_DATE),
('USB001',			'21', 	623, 	CURRENT_DATE),
('VR001', 			'22', 	354, 	CURRENT_DATE),
('COOL002',			'54', 	45, 	CURRENT_DATE),
('COOL002',			'23', 	86, 	CURRENT_DATE),
('RES001', 			'24', 	532, 	CURRENT_DATE),
('CAP001', 			'25', 	548, 	CURRENT_DATE),
('MOTOR001', 		'26', 	56, 	CURRENT_DATE),
('GEAR001', 		'27', 	84, 	CURRENT_DATE),
('ENC001', 			'51', 	95, 	CURRENT_DATE),
('ENC001', 			'52', 	48, 	CURRENT_DATE),
('ENC001', 			'53', 	53, 	CURRENT_DATE),
('ENC001', 			'28', 	15, 	CURRENT_DATE),
('INV001', 			'29', 	5, 		CURRENT_DATE),
('BRG001', 			'30', 	5, 		CURRENT_DATE),
('OIL001', 			'31', 	59, 	CURRENT_DATE),
('CAM001', 			'32', 	4, 		CURRENT_DATE),
('LIDAR001', 		'33', 	59, 	CURRENT_DATE),
('PROX001', 		'34', 	6, 		CURRENT_DATE),
('TEMP001', 		'35', 	354, 	CURRENT_DATE),
('ETH001', 			'36', 	48, 	CURRENT_DATE),
('WLAN001', 		'37', 	56, 	CURRENT_DATE),
('BT001', 			'38', 	214, 	CURRENT_DATE),
('ARM-BOM', 		'39', 	15, 	CURRENT_DATE);

INSERT INTO Smartlabel (MaterialNo, SmartlabelSN, StorageProduction, Quantity, OrderQty, 
	Date, Active, Sensor, MeasuermentPeriode) VALUES
('ARM001',			'1',	'45',	1,		50,	CURRENT_DATE,	false,	false,	null),
('ARM002',			'2',	'44',	3,		50,	CURRENT_DATE,	false,	false,	null),
('CTRL-MOD001',		'3',	'43',	5,		50,	CURRENT_DATE,	false,	false,	null),
('DRIVE-MOD001',	'4',	'42',	4,		50,	CURRENT_DATE,	false,	false,	null),
('SENS-MOD001',		'5',	'41',	8,		50,	CURRENT_DATE,	false,	false,	null),
('COM-MOD001',		'6',	'40',	6,		50,	CURRENT_DATE,	false,	false,	null),
('CABLE001',		'7',	'39',	23,		50,	CURRENT_DATE,	false,	false,	null),
('SAFETY001',		'8',	'38',	254,	50,	CURRENT_DATE,	false,	false,	null),
('COOL001',			'9',	'37',	45,		50,	CURRENT_DATE,	false,	false,	null),
('PSU001',			'10',	'36',	86,		50,	CURRENT_DATE,	false,	false,	null),
('SCR001',			'11',	'35',	24,		50,	CURRENT_DATE,	false,	false,	null),
('SCR001',			'12',	'34',	85,		50,	CURRENT_DATE,	false,	false,	null),
('SCR001',			'13',	'33',	48,		50,	CURRENT_DATE,	false,	false,	null),
('SCR001',			'14',	'32',	231,	50,	CURRENT_DATE,	false,	false,	null),
('DISP001',			'15',	'31',	18,		50,	CURRENT_DATE,	false,	false,	null),
('CABLE002',		'16',	'30',	285,	50,	CURRENT_DATE,	false,	false,	null),
('PCB001',			'17',	'29',	62,		50,	CURRENT_DATE,	false,	false,	null),
('CPU001',			'18',	'28',	4,		50,	CURRENT_DATE,	false,	false,	null),
('RAM001',			'19',	'27',	52,		50,	CURRENT_DATE,	false,	false,	null),
('FLASH001',		'20',	'26',	4,		50,	CURRENT_DATE,	false,	false,	null),
('FPGA001',			'21',	'25',	23,		50,	CURRENT_DATE,	false,	false,	null),
('NET001',			'22',	'24',	5,		50,	CURRENT_DATE,	false,	false,	null),
('NET001',			'23',	'23',	6,		50,	CURRENT_DATE,	false,	false,	null),
('CAN001',			'24',	'22',	8,		50,	CURRENT_DATE,	false,	false,	null),
('USB001',			'25',	'21',	45,		50,	CURRENT_DATE,	false,	false,	null),
('VR001',			'26',	'20',	24,		50,	CURRENT_DATE,	false,	false,	null),
('COOL002',			'27',	'19',	29,		50,	CURRENT_DATE,	false,	false,	null),
('RES001',			'28',	'18',	89,		50,	CURRENT_DATE,	false,	false,	null),
('CAP001',			'29',	'17',	297,	50,	CURRENT_DATE,	false,	false,	null),
('MOTOR001',		'30',	'16',	86,		50,	CURRENT_DATE,	false,	false,	null),
('GEAR001',			'31',	'15',	23,		50,	CURRENT_DATE,	false,	false,	null),
('GEAR001',			'32',	'14',	18,		50,	CURRENT_DATE,	false,	false,	null),
('GEAR001',			'33',	'13',	32,		50,	CURRENT_DATE,	false,	false,	null),
('ENC001',			'34',	'12',	41,		50,	CURRENT_DATE,	false,	false,	null),
('INV001',			'35',	'11',	25,		50,	CURRENT_DATE,	false,	false,	null),
('BRG001',			'36',	'10',	32,		50,	CURRENT_DATE,	false,	false,	null),
('OIL001',			'37',	'9',	48,		50,	CURRENT_DATE,	false,	false,	null),
('CAM001',			'38',	'8',	2,		50,	CURRENT_DATE,	false,	false,	null),
('LIDAR001',		'39',	'7',	6,		50,	CURRENT_DATE,	false,	false,	null),
('PROX001',			'40',	'6',	52,		50,	CURRENT_DATE,	false,	false,	null),
('TEMP001',			'41',	'5',	48,		50,	CURRENT_DATE,	false,	false,	null),
('ETH001',			'42',	'4',	58,		50,	CURRENT_DATE,	false,	false,	null),
('WLAN001',			'43',	'3',	45,		50,	CURRENT_DATE,	false,	false,	null),
('BT001',			'44',	'2',	25,		50,	CURRENT_DATE,	false,	false,	null),
('ARM-BOM',			'45',	'1',	84,		50,	CURRENT_DATE,	false,	false,	null);

INSERT INTO Delivery (MaterialNo, Quantity, DeliveryDate, DeliveryState) VALUES
('CAM001',		100,	'01.01.2025',	'scheduled'),
('FLASH001',	50,		'01.01.2025',	'scheduled');

INSERT INTO Orders (MaterialNo, SmartlabelID, DeliveryID, Quantity, Date, OrderState) VALUES
('SCR001',		11,	null,	500,	CURRENT_DATE,	'scheduled'),
('NET001',		23,	null,	25,		CURRENT_DATE,	'scheduled'),
('FLASH001',	20,	2,		50,		CURRENT_DATE,	'backlog'),
('ARM001',		1,	null,	10,		CURRENT_DATE,	'scheduled'),
('CABLE002',	16,	null,	25,		CURRENT_DATE,	'scheduled');

SELECT * FROM Orders;