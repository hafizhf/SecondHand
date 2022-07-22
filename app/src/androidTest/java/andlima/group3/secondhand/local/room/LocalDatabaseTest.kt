package andlima.group3.secondhand.local.room

import andlima.group3.secondhand.local.room.electronictable.ElectronicDao
import andlima.group3.secondhand.local.room.fashiontable.FashionDao
import andlima.group3.secondhand.local.room.foodtable.FoodDao
import andlima.group3.secondhand.local.room.homesupplytable.HomeSuppliesDao
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocalDatabaseTest: TestCase() {

    private lateinit var db: LocalDatabase
    private lateinit var electronic: ElectronicDao
    private lateinit var fashion: FashionDao
    private lateinit var food: FoodDao
    private lateinit var homeSupplies: HomeSuppliesDao

    @Before
    public override fun setUp() {
        super.setUp()
        val context = ApplicationProvider.getApplicationContext<Context>()

        db = Room.inMemoryDatabaseBuilder(context, LocalDatabase::class.java).build()

        electronic = db.electronicDao()
        fashion = db.fashionDao()
        food = db.foodDao()
        homeSupplies = db.homeSuppliesDao()
    }

    @After
    public override fun tearDown() {
        db.close()
    }

    @Test
    fun electronicDao() {
        electronic.getOfflineProduct()
    }

    @Test
    fun fashionDao() {
        fashion.getOfflineProduct()
    }

    @Test
    fun foodDao() {
        food.getOfflineProduct()
    }

    @Test
    fun homeSuppliesDao() {
        homeSupplies.getOfflineProduct()
    }
}