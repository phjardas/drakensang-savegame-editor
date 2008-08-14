CREATE TABLE _Instance_Money (
  Guid BLOB PRIMARY KEY ON CONFLICT REPLACE,
  _ID TEXT,
  _Level TEXT,
  _Layers TEXT,
  Transform BLOB,
  Id TEXT,
  Graphics TEXT,
  Placeholder TEXT,
  Physics TEXT,
  Name TEXT,
  LookAtText TEXT,
  PickingRange REAL,
  MaxStackCount INTEGER,
  Value INTEGER,
  StackCount INTEGER,
  IconBrush TEXT,
  PickingHeight REAL,
  SoundUI TEXT,
  StorageGUID BLOB,
  Lootable INTEGER,
  Robable INTEGER,
  VelocityVector BLOB,
  PhysicCategory TEXT,
  StorageSlotId INTEGER,
  IsTradeItem INTEGER,
  SpellTargetOffset BLOB,
  IsSplitting INTEGER,
  InfiniteStack INTEGER,
  InfoIdentified TEXT,
  LocalizeLookAtText INTEGER
);
CREATE INDEX _Instance_Money_Id ON _Instance_Money ( Id );
CREATE INDEX _Instance_Money_Name ON _Instance_Money ( Name );
CREATE INDEX _Instance_Money_StorageGUID ON _Instance_Money ( StorageGUID );
CREATE INDEX _Instance_Money__Level ON _Instance_Money ( _Level );
