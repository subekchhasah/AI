package com.example.petcare.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.petcare.data.local.dao.CareRoutineDao
import com.example.petcare.data.local.dao.CareTaskDao
import com.example.petcare.data.local.dao.ExpenseDao
import com.example.petcare.data.local.dao.MedicalRecordDao
import com.example.petcare.data.local.dao.PetDao
import com.example.petcare.data.local.dao.PetLocationDao
import com.example.petcare.data.local.dao.UserDao
import com.example.petcare.data.local.entities.CareRoutine
import com.example.petcare.data.local.entities.CareTask
import com.example.petcare.data.local.entities.Expense
import com.example.petcare.data.local.entities.MedicalRecord
import com.example.petcare.data.local.entities.Pet
import com.example.petcare.data.local.entities.PetLocation
import com.example.petcare.data.local.entities.User
import com.example.petcare.utils.PasswordHasher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Database(
    entities = [
        User::class,
        Pet::class,
        CareRoutine::class,
        CareTask::class,
        MedicalRecord::class,
        Expense::class,
        PetLocation::class
    ],
    version = 9,
    exportSchema = false
)
abstract class PetCareDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun petDao(): PetDao
    abstract fun careRoutineDao(): CareRoutineDao
    abstract fun careTaskDao(): CareTaskDao
    abstract fun medicalRecordDao(): MedicalRecordDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun petLocationDao(): PetLocationDao

    companion object {
        @Volatile
        private var INSTANCE: PetCareDatabase? = null

        fun getDatabase(context: Context): PetCareDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PetCareDatabase::class.java,
                    "petcare_database.db"
                )
                    .addCallback(DatabaseCallback(context))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val context: Context
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateDatabase(database)
                    }
                }
            }
        }

        private suspend fun populateDatabase(db: PetCareDatabase) {
            // 1. Seed User: Emily
            val emily = User(
                userId = 1L,
                name = "Emily",
                email = "emily@example.com",
                passwordHash = PasswordHasher.hashPassword("Password123")
            )
            db.userDao().insertUser(emily)

            // 2. Seed 20 Diverse Pets with Detailed Dietary Preferences & Breeds & Images
            val petsToSeed = listOf(
                Pet(
                    petId = 1L,
                    userId = 1L,
                    name = "Max",
                    species = "Dog",
                    breed = "Labrador Retriever",
                    dateOfBirth = "2021-04-15",
                    gender = "Male",
                    weight = 31.0,
                    dietaryPreferences = "Dry kibble twice daily (8am, 6pm); portion-controlled because the breed gains weight easily; carrot sticks as treats",
                    allergies = "None",
                    vaccinationHistory = "Rabies (2025-05-10), DHPP (2025-05-10)",
                    medicalNotes = "Needs daily joint supplement.",
                    favouriteToys = "Tennis ball, Chew rope",
                    notes = "Friendly, loves swimming.",
                    imageUri = "pet_max"
                ),
                Pet(
                    petId = 2L,
                    userId = 1L,
                    name = "Bella",
                    species = "Dog",
                    breed = "Golden Retriever",
                    dateOfBirth = "2022-02-10",
                    gender = "Female",
                    weight = 28.5,
                    dietaryPreferences = "Salmon-based kibble for coat health; wet food topper in the evening; fish oil supplement",
                    allergies = "None",
                    vaccinationHistory = "Rabies (2025-06-12), DHPP (2025-06-12)",
                    medicalNotes = "Regular coat brushing required.",
                    favouriteToys = "Plush duck, Frisbee",
                    notes = "Gentle and calm personality.",
                    imageUri = "pet_husky"
                ),
                Pet(
                    petId = 3L,
                    userId = 1L,
                    name = "Rocky",
                    species = "Dog",
                    breed = "German Shepherd",
                    dateOfBirth = "2020-11-05",
                    gender = "Male",
                    weight = 35.0,
                    dietaryPreferences = "High-protein large-breed kibble; two meals a day; no feeding within 1 hour of exercise",
                    allergies = "None",
                    vaccinationHistory = "Rabies (2025-03-01), DHPP (2025-03-01)",
                    medicalNotes = "Routine hip monitoring.",
                    favouriteToys = "KONG toy, Tug rope",
                    notes = "Alert and highly trainable.",
                    imageUri = "pet_rocky"
                ),
                Pet(
                    petId = 4L,
                    userId = 1L,
                    name = "Daisy",
                    species = "Dog",
                    breed = "Beagle",
                    dateOfBirth = "2023-01-18",
                    gender = "Female",
                    weight = 11.2,
                    dietaryPreferences = "Measured portions only, prone to overeating; low-fat kibble; slow-feeder bowl",
                    allergies = "Grain sensitivity",
                    vaccinationHistory = "Rabies (2025-07-20)",
                    medicalNotes = "Use slow feeder bowl.",
                    favouriteToys = "Squeaky ball, Snuffle mat",
                    notes = "Energetic scent hound.",
                    imageUri = "pet_daisy"
                ),
                Pet(
                    petId = 5L,
                    userId = 1L,
                    name = "Bruno",
                    species = "Dog",
                    breed = "Rottweiler",
                    dateOfBirth = "2021-08-30",
                    gender = "Male",
                    weight = 45.0,
                    dietaryPreferences = "Large-breed kibble with joint support; boiled chicken and rice as an occasional treat",
                    allergies = "None",
                    vaccinationHistory = "Rabies (2025-04-14)",
                    medicalNotes = "Joint supplements in morning food.",
                    favouriteToys = "Heavy duty rubber bone",
                    notes = "Loyal guardian dog."
                ),
                Pet(
                    petId = 6L,
                    userId = 1L,
                    name = "Coco",
                    species = "Dog",
                    breed = "Pomeranian",
                    dateOfBirth = "2023-05-12",
                    gender = "Female",
                    weight = 2.8,
                    dietaryPreferences = "Small-bite toy-breed kibble; three small meals a day; dental chews",
                    allergies = "None",
                    vaccinationHistory = "DHPP (2025-08-01)",
                    medicalNotes = "Daily dental chews.",
                    favouriteToys = "Mini squeaky bear",
                    notes = "Fluffy and affectionate.",
                    imageUri = "pet_coco"
                ),
                Pet(
                    petId = 7L,
                    userId = 1L,
                    name = "Luna",
                    species = "Cat",
                    breed = "Siamese",
                    dateOfBirth = "2022-08-20",
                    gender = "Female",
                    weight = 4.2,
                    dietaryPreferences = "Wet food morning and evening; dry kibble available during the day; fresh water fountain",
                    allergies = "Dairy",
                    vaccinationHistory = "FVRCP (2025-06-01)",
                    medicalNotes = "Requires ear cleaning once a month.",
                    favouriteToys = "Feather wand, Catnip mouse",
                    notes = "Vocal and loves sunbathing near window."
                ),
                Pet(
                    petId = 8L,
                    userId = 1L,
                    name = "Simba",
                    species = "Cat",
                    breed = "Persian",
                    dateOfBirth = "2021-12-01",
                    gender = "Male",
                    weight = 4.5,
                    dietaryPreferences = "Flat-faced breed kibble that is easy to pick up; hairball-control formula",
                    allergies = "None",
                    vaccinationHistory = "FVRCP (2025-05-15)",
                    medicalNotes = "Daily eye wipe and grooming.",
                    favouriteToys = "Laser pointer, Wool ball",
                    notes = "Calm indoor lap cat."
                ),
                Pet(
                    petId = 9L,
                    userId = 1L,
                    name = "Milo",
                    species = "Cat",
                    breed = "Maine Coon",
                    dateOfBirth = "2020-09-14",
                    gender = "Male",
                    weight = 8.0,
                    dietaryPreferences = "High-protein large-cat food; wet and dry mix; cooked fish once a week",
                    allergies = "None",
                    vaccinationHistory = "Rabies (2025-09-10), FVRCP (2025-09-10)",
                    medicalNotes = "Routine grooming for long coat.",
                    favouriteToys = "Giant cat tree, Crinkle ball",
                    notes = "Gentle giant."
                ),
                Pet(
                    petId = 10L,
                    userId = 1L,
                    name = "Nala",
                    species = "Cat",
                    breed = "British Shorthair",
                    dateOfBirth = "2022-10-25",
                    gender = "Female",
                    weight = 5.0,
                    dietaryPreferences = "Weight-control dry food; measured portions; minimal treats",
                    allergies = "None",
                    vaccinationHistory = "FVRCP (2025-04-02)",
                    medicalNotes = "Weight monitoring.",
                    favouriteToys = "Catnip tunnel",
                    notes = "Quiet and independent."
                ),
                Pet(
                    petId = 11L,
                    userId = 1L,
                    name = "Oreo",
                    species = "Cat",
                    breed = "Ragdoll",
                    dateOfBirth = "2023-03-08",
                    gender = "Male",
                    weight = 5.8,
                    dietaryPreferences = "Grain-free wet food; indoor-cat kibble; catnip treats on weekends",
                    allergies = "Grains",
                    vaccinationHistory = "FVRCP (2025-07-11)",
                    medicalNotes = "Indoor cat.",
                    favouriteToys = "Soft plush mouse",
                    notes = "Loves being held like a ragdoll."
                ),
                Pet(
                    petId = 12L,
                    userId = 1L,
                    name = "Thumper",
                    species = "Rabbit",
                    breed = "Holland Lop",
                    dateOfBirth = "2023-04-01",
                    gender = "Male",
                    weight = 1.8,
                    dietaryPreferences = "Unlimited timothy hay; fresh leafy greens daily; small portion of pellets",
                    allergies = "None",
                    vaccinationHistory = "RHDV2 (2025-05-01)",
                    medicalNotes = "Check teeth regularly.",
                    favouriteToys = "Chew sticks, Willow ball",
                    notes = "Loves hopping around garden."
                ),
                Pet(
                    petId = 13L,
                    userId = 1L,
                    name = "Snowball",
                    species = "Rabbit",
                    breed = "Netherland Dwarf",
                    dateOfBirth = "2023-06-15",
                    gender = "Female",
                    weight = 1.1,
                    dietaryPreferences = "Hay-based diet; romaine and parsley; no iceberg lettuce",
                    allergies = "Iceberg lettuce",
                    vaccinationHistory = "RHDV2 (2025-06-01)",
                    medicalNotes = "Keep fresh hay available always.",
                    favouriteToys = "Cardboard tunnel",
                    notes = "Small and sweet."
                ),
                Pet(
                    petId = 14L,
                    userId = 1L,
                    name = "Kiwi",
                    species = "Bird",
                    breed = "Budgerigar",
                    dateOfBirth = "2023-08-01",
                    gender = "Male",
                    weight = 0.04,
                    dietaryPreferences = "Seed and pellet mix; fresh millet spray; chopped broccoli and carrot",
                    allergies = "None",
                    vaccinationHistory = "Avian Checkup (2025-08-10)",
                    medicalNotes = "Fresh water twice daily.",
                    favouriteToys = "Mirror bell, Ladder",
                    notes = "Loves chirping in morning."
                ),
                Pet(
                    petId = 15L,
                    userId = 1L,
                    name = "Sunny",
                    species = "Bird",
                    breed = "Cockatiel",
                    dateOfBirth = "2022-11-20",
                    gender = "Female",
                    weight = 0.09,
                    dietaryPreferences = "Pellets as the main diet; small amount of seeds; fresh apple slices without seeds",
                    allergies = "Apple seeds (toxic)",
                    vaccinationHistory = "Avian Checkup (2025-07-05)",
                    medicalNotes = "Never feed apple seeds.",
                    favouriteToys = "Rope perch, Wooden chew",
                    notes = "Whistles tune."
                ),
                Pet(
                    petId = 16L,
                    userId = 1L,
                    name = "Mango",
                    species = "Bird",
                    breed = "African Grey",
                    dateOfBirth = "2021-01-10",
                    gender = "Male",
                    weight = 0.4,
                    dietaryPreferences = "Pellets, fresh fruit and vegetables; calcium-rich foods; no avocado or chocolate",
                    allergies = "Avocado, Chocolate (toxic)",
                    vaccinationHistory = "Avian Checkup (2025-04-18)",
                    medicalNotes = "High intelligence, needs daily mental stimulation.",
                    favouriteToys = "Foraging puzzle, Bell ring",
                    notes = "Imitates words and doorbell."
                ),
                Pet(
                    petId = 17L,
                    userId = 1L,
                    name = "Peanut",
                    species = "Hamster",
                    breed = "Syrian",
                    dateOfBirth = "2024-01-05",
                    gender = "Male",
                    weight = 0.15,
                    dietaryPreferences = "Hamster pellet mix; small pieces of cucumber; occasional mealworms",
                    allergies = "None",
                    vaccinationHistory = "N/A",
                    medicalNotes = "Night active.",
                    favouriteToys = "Running wheel, Tunnel",
                    notes = "Loves running on wheel."
                ),
                Pet(
                    petId = 18L,
                    userId = 1L,
                    name = "Ginger",
                    species = "Guinea Pig",
                    breed = "Abyssinian",
                    dateOfBirth = "2023-07-10",
                    gender = "Female",
                    weight = 1.0,
                    dietaryPreferences = "Unlimited hay; vitamin C-rich veg such as bell pepper daily; guinea pig pellets",
                    allergies = "None",
                    vaccinationHistory = "Routine Checkup (2025-07-10)",
                    medicalNotes = "Requires daily Vitamin C supplement/bell pepper.",
                    favouriteToys = "Hay ball, Fleece pouch",
                    notes = "Makes cute squeaking noises."
                ),
                Pet(
                    petId = 19L,
                    userId = 1L,
                    name = "Nemo",
                    species = "Fish",
                    breed = "Fantail Goldfish",
                    dateOfBirth = "2023-09-01",
                    gender = "Unknown",
                    weight = 0.05,
                    dietaryPreferences = "Sinking goldfish pellets twice daily; blanched peas weekly; no overfeeding",
                    allergies = "Overfeeding",
                    vaccinationHistory = "N/A",
                    medicalNotes = "Clean tank filter weekly.",
                    favouriteToys = "Aquarium castle, Artificial plants",
                    notes = "Swims gracefully."
                ),
                Pet(
                    petId = 20L,
                    userId = 1L,
                    name = "Shelly",
                    species = "Turtle",
                    breed = "Red-Eared Slider",
                    dateOfBirth = "2022-05-01",
                    gender = "Female",
                    weight = 1.5,
                    dietaryPreferences = "Aquatic turtle pellets; leafy greens; dried shrimp as an occasional treat",
                    allergies = "None",
                    vaccinationHistory = "Reptile Checkup (2025-05-01)",
                    medicalNotes = "UVB basking lamp 10 hours daily.",
                    favouriteToys = "Basking dock, Floating log",
                    notes = "Loves basking under warm lamp."
                )
            )
            petsToSeed.forEach { db.petDao().insertPet(it) }

            // 3. Seed Routines
            val maxRoutine = CareRoutine(
                routineId = 1L,
                petId = 1L,
                routineName = "MAX DAILY ROUTINE",
                description = "Daily exercise and feeding schedule for Max",
                frequency = "Daily",
                startDate = "2026-01-01"
            )
            val lunaRoutine = CareRoutine(
                routineId = 2L,
                petId = 7L,
                routineName = "LUNA DAILY ROUTINE",
                description = "Daily litter and feeding schedule for Luna",
                frequency = "Daily",
                startDate = "2026-01-01"
            )
            db.careRoutineDao().insertRoutine(maxRoutine)
            db.careRoutineDao().insertRoutine(lunaRoutine)

            // Today's Date YYYY-MM-DD
            val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            // 4. Seed Today's Care Tasks for Emily
            val tasks = listOf(
                CareTask(
                    taskId = 1L,
                    petId = 1L,
                    routineId = 1L,
                    title = "Max Breakfast",
                    description = "2 cups dry kibble with chicken topper",
                    category = "Feeding",
                    scheduledDate = todayStr,
                    scheduledTime = "08:00",
                    frequency = "Daily",
                    requiredSupplies = "Dry kibble, fresh water",
                    priority = "High",
                    isCompleted = false
                ),
                CareTask(
                    taskId = 2L,
                    petId = 7L,
                    routineId = 2L,
                    title = "Luna Breakfast",
                    description = "1 can salmon wet food",
                    category = "Feeding",
                    scheduledDate = todayStr,
                    scheduledTime = "08:30",
                    frequency = "Daily",
                    requiredSupplies = "Salmon wet food",
                    priority = "High",
                    isCompleted = false
                ),
                CareTask(
                    taskId = 3L,
                    petId = 1L,
                    routineId = 1L,
                    title = "Max Morning Walk",
                    description = "30-minute outdoor exercise in local park",
                    category = "Exercise",
                    scheduledDate = todayStr,
                    scheduledTime = "09:00",
                    frequency = "Daily",
                    requiredSupplies = "Leash, waste bags",
                    priority = "Normal",
                    isCompleted = false
                ),
                CareTask(
                    taskId = 4L,
                    petId = 7L,
                    routineId = 2L,
                    title = "Luna Brushing",
                    description = "Brush coat to prevent fur balls",
                    category = "Grooming",
                    scheduledDate = todayStr,
                    scheduledTime = "19:00",
                    frequency = "Daily",
                    requiredSupplies = "Slicker brush",
                    priority = "Low",
                    isCompleted = false
                ),
                CareTask(
                    taskId = 5L,
                    petId = 7L,
                    routineId = 2L,
                    title = "Luna Medication",
                    description = "Ear drops administration",
                    category = "Medication",
                    scheduledDate = todayStr,
                    scheduledTime = "20:00",
                    frequency = "Daily",
                    requiredSupplies = "Ear solution",
                    priority = "High",
                    isCompleted = false
                )
            )

            tasks.forEach { db.careTaskDao().insertTask(it) }

            // 5. Seed Medical Records
            db.medicalRecordDao().insertMedicalRecord(
                MedicalRecord(
                    medicalRecordId = 1L,
                    petId = 1L,
                    recordType = "Vaccination",
                    title = "Annual Rabies & DHPP Booster",
                    date = "2025-05-10",
                    veterinarian = "Dr. Sarah Jenkins",
                    clinic = "Happy Paws Veterinary Clinic",
                    notes = "Passed full health check with flying colors."
                )
            )

            // 6. Seed Expenses
            db.expenseDao().insertExpense(
                Expense(
                    expenseId = 1L,
                    petId = 1L,
                    category = "Food",
                    description = "Royal Canin Labrador Adult Kibble 12kg",
                    amount = 58.50,
                    date = todayStr,
                    notes = "Monthly food supply"
                )
            )
            db.expenseDao().insertExpense(
                Expense(
                    expenseId = 2L,
                    petId = 7L,
                    category = "Veterinary",
                    description = "Routine Wellness Checkup & Ear Cleaning",
                    amount = 75.00,
                    date = todayStr,
                    notes = "Dr. Smith consultation"
                )
            )

            // 7. Seed Geotagged Pet Locations
            db.petLocationDao().insertLocation(
                PetLocation(
                    locationId = 1L,
                    petId = 1L,
                    name = "Happy Paws Veterinary Clinic",
                    type = "Veterinary Clinic",
                    latitude = 51.5074,
                    longitude = -0.1278,
                    address = "123 High Street, London",
                    notes = "Preferred clinic for Max and Luna."
                )
            )
            db.petLocationDao().insertLocation(
                PetLocation(
                    locationId = 2L,
                    petId = 1L,
                    name = "Greenwood Dog Park",
                    type = "Dog Park",
                    latitude = 51.5150,
                    longitude = -0.1410,
                    address = "Greenwood Avenue, London",
                    notes = "Fenced park with agility equipment for Max."
                )
            )
        }
    }
}
