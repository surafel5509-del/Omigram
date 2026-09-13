package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.local.dao.CallDao
import com.example.data.local.dao.ChatDao
import com.example.data.local.dao.ContactDao
import com.example.data.local.dao.MessageDao
import com.example.data.local.dao.ProfileDao
import com.example.data.local.entity.CallEntity
import com.example.data.local.entity.ChatEntity
import com.example.data.local.entity.ContactEntity
import com.example.data.local.entity.MessageEntity
import com.example.data.local.entity.ProfileEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ProfileEntity::class,
        ChatEntity::class,
        MessageEntity::class,
        ContactEntity::class,
        CallEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class OmigoDatabase : RoomDatabase() {

    abstract fun profileDao(): ProfileDao
    abstract fun chatDao(): ChatDao
    abstract fun messageDao(): MessageDao
    abstract fun contactDao(): ContactDao
    abstract fun callDao(): CallDao

    companion object {
        @Volatile
        private var INSTANCE: OmigoDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope = CoroutineScope(Dispatchers.IO)): OmigoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OmigoDatabase::class.java,
                    "omigo_chat_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            scope.launch {
                                populateInitialData(getDatabase(context, scope))
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        suspend fun populateInitialData(database: OmigoDatabase) {
            database.profileDao().insertProfiles(SampleData.users)
            database.chatDao().insertChats(SampleData.chats)
            database.messageDao().insertMessages(SampleData.messages)
            database.contactDao().insertContacts(SampleData.contacts)
            database.callDao().insertCalls(SampleData.calls)
        }
    }
}
