package com.rota.RotaQrBarcode.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rota.RotaQrBarcode.data.local.dao.ScannedCodeDao
import com.rota.RotaQrBarcode.data.local.entity.ScannedCode

@Database(
    entities = [ScannedCode::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scannedCodeDao(): ScannedCodeDao

    companion object {
        private const val DATABASE_NAME = "rota_barcode_reader.db"

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME
            )
            .fallbackToDestructiveMigration()
            .build()
        }
    }
}