## Dependencies
- `build.gradle.kts (Module :app)`
  ```kotlin
  implementation("androidx.room:room-ktx:${rootProject.extra["room_version"]}")
  implementation("androidx.room:room-runtime:${rootProject.extra["room_version"]}")
  ksp("androidx.room:room-compiler:${rootProject.extra["room_version"]}")
  ```
- `build.gradle.kts (Project: Bus_Schedule)`
  ```
  buildscript {
      extra.apply {
          ...
          set("room_version", "2.5.1")
      }
  }
  ```
> [!NOTE]
> 의존성들은 최신 버전을 유지하도록 해야 한다.
<br>

## Entity (BusSchedule)

- 테이블의 행, 데이터
- `ColumnInfo`로 변수와 실제 테이블 속성을 매칭

  ```kotlin
  @Entity(tableName="Schedule")
  data class BusSchedule(
      @PrimaryKey(autoGenerate = true)
      val id: Int = 1,
      @NonNull
      @ColumnInfo(name = "stop_name")
      val stopName: String,
      @NonNull
      @ColumnInfo(name = "arrival_time")
      val arrivalTimeInMillis: Int
  )
  ```
  <br>
  
## DAO (BusScheduleDao)

- Data Access Object는 데이터베이스의 데이터들에 접근하기 위한 객체
- `@Insert`, `@Update`, `@Delete` 같은 기본 주석을 포함
- `@Select`를 통해 데이터베이스에 쿼리를 보낼 수 있음
  ```kotlin
  @Dao
  interface BusScheduleDao {
      @Query("SELECT * FROM schedule ORDER BY arrival_time ")
      fun getAllBusSchedules(): Flow<List<BusSchedule>>

      @Query("SELECT * FROM schedule WHERE stop_name  = :stopName")
      fun getBusScheduleByStopName(stopName: String): Flow<List<BusSchedule>>
  }
  ```
<br>

## Database (BusScheduleDatabase)

- 여러 스레드에서 동시에 생성되면 안되는 객체로 `@Volatile` 주석과 `synchronized()` 블록을 사용하여 충돌을 방지
- 엘비스 연산자를 사용하여 객체가 한번만 생성되도록 함
  ```kotlin
  @Database(entities = [BusSchedule::class], version = 1, exportSchema = false)
  abstract class BusScheduleDatabase: RoomDatabase() {
      abstract fun busScheduleDao(): BusScheduleDao

      companion object {
          @Volatile
          private var Instance: BusScheduleDatabase? = null

          fun getDatabase(context: Context): BusScheduleDatabase {
              return Instance ?: synchronized(this) {
                  Room.databaseBuilder(context, BusScheduleDatabase::class.java, "bus_schedule")
                      .createFromAsset("database/bus_schedule.db")
                      .fallbackToDestructiveMigration()
                      .build()
                      .also { Instance = it }
              }
          }
      }
  }
  ```

