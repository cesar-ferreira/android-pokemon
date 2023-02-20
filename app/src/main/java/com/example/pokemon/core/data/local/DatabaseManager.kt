package com.example.pokemon.core.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PokemonTable::class], version = 1, exportSchema = false)
abstract class DatabaseManager : RoomDatabase() {
    abstract val pokemonDao: PokemonDao
}

private lateinit var INSTANCE: DatabaseManager

fun getDatabase(context: Context): DatabaseManager {
    synchronized(DatabaseManager::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                DatabaseManager::class.java,
                "databaseManager").
            allowMainThreadQueries()
                .build()
        }
    }
    return INSTANCE
}