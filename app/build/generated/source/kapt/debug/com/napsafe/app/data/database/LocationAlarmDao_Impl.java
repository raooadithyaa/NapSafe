package com.napsafe.app.data.database;

import androidx.annotation.NonNull;
import androidx.room.EntityDeleteOrUpdateAdapter;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.coroutines.FlowUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import com.napsafe.app.data.model.AlarmType;
import com.napsafe.app.data.model.LocationAlarm;
import java.lang.Class;
import java.lang.NullPointerException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class LocationAlarmDao_Impl implements LocationAlarmDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<LocationAlarm> __insertAdapterOfLocationAlarm;

  private final Converters __converters = new Converters();

  private final EntityDeleteOrUpdateAdapter<LocationAlarm> __deleteAdapterOfLocationAlarm;

  private final EntityDeleteOrUpdateAdapter<LocationAlarm> __updateAdapterOfLocationAlarm;

  public LocationAlarmDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfLocationAlarm = new EntityInsertAdapter<LocationAlarm>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `location_alarms` (`id`,`latitude`,`longitude`,`address`,`name`,`radius`,`isActive`,`alarmType`,`volume`,`createdAt`,`geofenceId`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          @NonNull final LocationAlarm entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindText(1, entity.getId());
        }
        statement.bindDouble(2, entity.getLatitude());
        statement.bindDouble(3, entity.getLongitude());
        if (entity.getAddress() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getAddress());
        }
        if (entity.getName() == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.getName());
        }
        statement.bindDouble(6, entity.getRadius());
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(7, _tmp);
        final String _tmp_1 = __converters.fromAlarmType(entity.getAlarmType());
        if (_tmp_1 == null) {
          statement.bindNull(8);
        } else {
          statement.bindText(8, _tmp_1);
        }
        statement.bindDouble(9, entity.getVolume());
        statement.bindLong(10, entity.getCreatedAt());
        if (entity.getGeofenceId() == null) {
          statement.bindNull(11);
        } else {
          statement.bindText(11, entity.getGeofenceId());
        }
      }
    };
    this.__deleteAdapterOfLocationAlarm = new EntityDeleteOrUpdateAdapter<LocationAlarm>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `location_alarms` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          @NonNull final LocationAlarm entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindText(1, entity.getId());
        }
      }
    };
    this.__updateAdapterOfLocationAlarm = new EntityDeleteOrUpdateAdapter<LocationAlarm>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `location_alarms` SET `id` = ?,`latitude` = ?,`longitude` = ?,`address` = ?,`name` = ?,`radius` = ?,`isActive` = ?,`alarmType` = ?,`volume` = ?,`createdAt` = ?,`geofenceId` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          @NonNull final LocationAlarm entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindText(1, entity.getId());
        }
        statement.bindDouble(2, entity.getLatitude());
        statement.bindDouble(3, entity.getLongitude());
        if (entity.getAddress() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getAddress());
        }
        if (entity.getName() == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.getName());
        }
        statement.bindDouble(6, entity.getRadius());
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(7, _tmp);
        final String _tmp_1 = __converters.fromAlarmType(entity.getAlarmType());
        if (_tmp_1 == null) {
          statement.bindNull(8);
        } else {
          statement.bindText(8, _tmp_1);
        }
        statement.bindDouble(9, entity.getVolume());
        statement.bindLong(10, entity.getCreatedAt());
        if (entity.getGeofenceId() == null) {
          statement.bindNull(11);
        } else {
          statement.bindText(11, entity.getGeofenceId());
        }
        if (entity.getId() == null) {
          statement.bindNull(12);
        } else {
          statement.bindText(12, entity.getId());
        }
      }
    };
  }

  @Override
  public Object insertAlarm(final LocationAlarm alarm,
      final Continuation<? super Unit> $completion) {
    if (alarm == null) throw new NullPointerException();
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      __insertAdapterOfLocationAlarm.insert(_connection, alarm);
      return Unit.INSTANCE;
    }, $completion);
  }

  @Override
  public Object deleteAlarm(final LocationAlarm alarm,
      final Continuation<? super Unit> $completion) {
    if (alarm == null) throw new NullPointerException();
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      __deleteAdapterOfLocationAlarm.handle(_connection, alarm);
      return Unit.INSTANCE;
    }, $completion);
  }

  @Override
  public Object updateAlarm(final LocationAlarm alarm,
      final Continuation<? super Unit> $completion) {
    if (alarm == null) throw new NullPointerException();
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      __updateAdapterOfLocationAlarm.handle(_connection, alarm);
      return Unit.INSTANCE;
    }, $completion);
  }

  @Override
  public Flow<List<LocationAlarm>> getAllAlarms() {
    final String _sql = "SELECT * FROM location_alarms ORDER BY createdAt DESC";
    return FlowUtil.createFlow(__db, false, new String[] {"location_alarms"}, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfLatitude = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "latitude");
        final int _columnIndexOfLongitude = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "longitude");
        final int _columnIndexOfAddress = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "address");
        final int _columnIndexOfName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "name");
        final int _columnIndexOfRadius = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "radius");
        final int _columnIndexOfIsActive = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "isActive");
        final int _columnIndexOfAlarmType = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "alarmType");
        final int _columnIndexOfVolume = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "volume");
        final int _columnIndexOfCreatedAt = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "createdAt");
        final int _columnIndexOfGeofenceId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "geofenceId");
        final List<LocationAlarm> _result = new ArrayList<LocationAlarm>();
        while (_stmt.step()) {
          final LocationAlarm _item;
          final String _tmpId;
          if (_stmt.isNull(_columnIndexOfId)) {
            _tmpId = null;
          } else {
            _tmpId = _stmt.getText(_columnIndexOfId);
          }
          final double _tmpLatitude;
          _tmpLatitude = _stmt.getDouble(_columnIndexOfLatitude);
          final double _tmpLongitude;
          _tmpLongitude = _stmt.getDouble(_columnIndexOfLongitude);
          final String _tmpAddress;
          if (_stmt.isNull(_columnIndexOfAddress)) {
            _tmpAddress = null;
          } else {
            _tmpAddress = _stmt.getText(_columnIndexOfAddress);
          }
          final String _tmpName;
          if (_stmt.isNull(_columnIndexOfName)) {
            _tmpName = null;
          } else {
            _tmpName = _stmt.getText(_columnIndexOfName);
          }
          final float _tmpRadius;
          _tmpRadius = (float) (_stmt.getDouble(_columnIndexOfRadius));
          final boolean _tmpIsActive;
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfIsActive));
          _tmpIsActive = _tmp != 0;
          final AlarmType _tmpAlarmType;
          final String _tmp_1;
          if (_stmt.isNull(_columnIndexOfAlarmType)) {
            _tmp_1 = null;
          } else {
            _tmp_1 = _stmt.getText(_columnIndexOfAlarmType);
          }
          _tmpAlarmType = __converters.toAlarmType(_tmp_1);
          final float _tmpVolume;
          _tmpVolume = (float) (_stmt.getDouble(_columnIndexOfVolume));
          final long _tmpCreatedAt;
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt);
          final String _tmpGeofenceId;
          if (_stmt.isNull(_columnIndexOfGeofenceId)) {
            _tmpGeofenceId = null;
          } else {
            _tmpGeofenceId = _stmt.getText(_columnIndexOfGeofenceId);
          }
          _item = new LocationAlarm(_tmpId,_tmpLatitude,_tmpLongitude,_tmpAddress,_tmpName,_tmpRadius,_tmpIsActive,_tmpAlarmType,_tmpVolume,_tmpCreatedAt,_tmpGeofenceId);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public Flow<List<LocationAlarm>> getActiveAlarms() {
    final String _sql = "SELECT * FROM location_alarms WHERE isActive = 1";
    return FlowUtil.createFlow(__db, false, new String[] {"location_alarms"}, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfLatitude = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "latitude");
        final int _columnIndexOfLongitude = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "longitude");
        final int _columnIndexOfAddress = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "address");
        final int _columnIndexOfName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "name");
        final int _columnIndexOfRadius = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "radius");
        final int _columnIndexOfIsActive = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "isActive");
        final int _columnIndexOfAlarmType = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "alarmType");
        final int _columnIndexOfVolume = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "volume");
        final int _columnIndexOfCreatedAt = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "createdAt");
        final int _columnIndexOfGeofenceId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "geofenceId");
        final List<LocationAlarm> _result = new ArrayList<LocationAlarm>();
        while (_stmt.step()) {
          final LocationAlarm _item;
          final String _tmpId;
          if (_stmt.isNull(_columnIndexOfId)) {
            _tmpId = null;
          } else {
            _tmpId = _stmt.getText(_columnIndexOfId);
          }
          final double _tmpLatitude;
          _tmpLatitude = _stmt.getDouble(_columnIndexOfLatitude);
          final double _tmpLongitude;
          _tmpLongitude = _stmt.getDouble(_columnIndexOfLongitude);
          final String _tmpAddress;
          if (_stmt.isNull(_columnIndexOfAddress)) {
            _tmpAddress = null;
          } else {
            _tmpAddress = _stmt.getText(_columnIndexOfAddress);
          }
          final String _tmpName;
          if (_stmt.isNull(_columnIndexOfName)) {
            _tmpName = null;
          } else {
            _tmpName = _stmt.getText(_columnIndexOfName);
          }
          final float _tmpRadius;
          _tmpRadius = (float) (_stmt.getDouble(_columnIndexOfRadius));
          final boolean _tmpIsActive;
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfIsActive));
          _tmpIsActive = _tmp != 0;
          final AlarmType _tmpAlarmType;
          final String _tmp_1;
          if (_stmt.isNull(_columnIndexOfAlarmType)) {
            _tmp_1 = null;
          } else {
            _tmp_1 = _stmt.getText(_columnIndexOfAlarmType);
          }
          _tmpAlarmType = __converters.toAlarmType(_tmp_1);
          final float _tmpVolume;
          _tmpVolume = (float) (_stmt.getDouble(_columnIndexOfVolume));
          final long _tmpCreatedAt;
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt);
          final String _tmpGeofenceId;
          if (_stmt.isNull(_columnIndexOfGeofenceId)) {
            _tmpGeofenceId = null;
          } else {
            _tmpGeofenceId = _stmt.getText(_columnIndexOfGeofenceId);
          }
          _item = new LocationAlarm(_tmpId,_tmpLatitude,_tmpLongitude,_tmpAddress,_tmpName,_tmpRadius,_tmpIsActive,_tmpAlarmType,_tmpVolume,_tmpCreatedAt,_tmpGeofenceId);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public Object getAlarmById(final String id,
      final Continuation<? super LocationAlarm> $completion) {
    final String _sql = "SELECT * FROM location_alarms WHERE id = ?";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (id == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, id);
        }
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfLatitude = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "latitude");
        final int _columnIndexOfLongitude = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "longitude");
        final int _columnIndexOfAddress = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "address");
        final int _columnIndexOfName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "name");
        final int _columnIndexOfRadius = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "radius");
        final int _columnIndexOfIsActive = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "isActive");
        final int _columnIndexOfAlarmType = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "alarmType");
        final int _columnIndexOfVolume = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "volume");
        final int _columnIndexOfCreatedAt = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "createdAt");
        final int _columnIndexOfGeofenceId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "geofenceId");
        final LocationAlarm _result;
        if (_stmt.step()) {
          final String _tmpId;
          if (_stmt.isNull(_columnIndexOfId)) {
            _tmpId = null;
          } else {
            _tmpId = _stmt.getText(_columnIndexOfId);
          }
          final double _tmpLatitude;
          _tmpLatitude = _stmt.getDouble(_columnIndexOfLatitude);
          final double _tmpLongitude;
          _tmpLongitude = _stmt.getDouble(_columnIndexOfLongitude);
          final String _tmpAddress;
          if (_stmt.isNull(_columnIndexOfAddress)) {
            _tmpAddress = null;
          } else {
            _tmpAddress = _stmt.getText(_columnIndexOfAddress);
          }
          final String _tmpName;
          if (_stmt.isNull(_columnIndexOfName)) {
            _tmpName = null;
          } else {
            _tmpName = _stmt.getText(_columnIndexOfName);
          }
          final float _tmpRadius;
          _tmpRadius = (float) (_stmt.getDouble(_columnIndexOfRadius));
          final boolean _tmpIsActive;
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfIsActive));
          _tmpIsActive = _tmp != 0;
          final AlarmType _tmpAlarmType;
          final String _tmp_1;
          if (_stmt.isNull(_columnIndexOfAlarmType)) {
            _tmp_1 = null;
          } else {
            _tmp_1 = _stmt.getText(_columnIndexOfAlarmType);
          }
          _tmpAlarmType = __converters.toAlarmType(_tmp_1);
          final float _tmpVolume;
          _tmpVolume = (float) (_stmt.getDouble(_columnIndexOfVolume));
          final long _tmpCreatedAt;
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt);
          final String _tmpGeofenceId;
          if (_stmt.isNull(_columnIndexOfGeofenceId)) {
            _tmpGeofenceId = null;
          } else {
            _tmpGeofenceId = _stmt.getText(_columnIndexOfGeofenceId);
          }
          _result = new LocationAlarm(_tmpId,_tmpLatitude,_tmpLongitude,_tmpAddress,_tmpName,_tmpRadius,_tmpIsActive,_tmpAlarmType,_tmpVolume,_tmpCreatedAt,_tmpGeofenceId);
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object deleteAlarmById(final String id, final Continuation<? super Unit> $completion) {
    final String _sql = "DELETE FROM location_alarms WHERE id = ?";
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (id == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, id);
        }
        _stmt.step();
        return Unit.INSTANCE;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object updateAlarmStatus(final String id, final boolean isActive,
      final Continuation<? super Unit> $completion) {
    final String _sql = "UPDATE location_alarms SET isActive = ? WHERE id = ?";
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        final int _tmp = isActive ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        if (id == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, id);
        }
        _stmt.step();
        return Unit.INSTANCE;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
