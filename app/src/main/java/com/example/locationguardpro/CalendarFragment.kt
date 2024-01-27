import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.locationguardpro.MyApplication
import com.example.locationguardpro.R
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CalendarFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        val myApplication = requireActivity().application as MyApplication
        val appDatabase = myApplication.appDatabase
        val workHoursDao = appDatabase.workHoursDao()
        val sharedPreferences =
            requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getLong("USER_ID", -1)

        val compactCalendarView = view.findViewById<CompactCalendarView>(R.id.calendarView)
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY)

        // Uruchom korutynę w zakresie komponentu życia fragmentu
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            // Pobierz dane z bazy danych w kontekście korutyny
            val workHoursList = workHoursDao.getWorkHoursByUserId(userId)

            // Usunięcie duplikatów z listy dat
            val uniqueDatesList = workHoursList.distinct()
            val workHoursMap = HashMap<Long, Double>()
            val uniqueDateStrings = HashSet<String>()
            val uniqueDatesWithoutDuplicates = ArrayList<Date>()

            for (workHours in uniqueDatesList) {
                val dateString = workHours.date

                // Parsowanie daty
                val date = SimpleDateFormat("dd.MM.yyyy").parse(dateString)

                // Dodawanie tylko unikalnych dat do listy
                if (uniqueDateStrings.add(dateString)) {
                    uniqueDatesWithoutDuplicates.add(date)
                }
            }

            for (uniqueDate in uniqueDatesWithoutDuplicates) {

                val calendar = Calendar.getInstance()
                    calendar.time = uniqueDate
                val dateInMillis = calendar.timeInMillis
                val outputFormat = SimpleDateFormat("dd.MM.yyyy")
                val dateInString = outputFormat.format(uniqueDate)
                val myWorkHoursForDate = workHoursDao.getTotalHoursForDate(userId, dateInString)
                Log.d("datetest", myWorkHoursForDate.toString())
                workHoursMap[dateInMillis] = myWorkHoursForDate
            }

            for ((timestamp, workHours) in workHoursMap) {
                val color = getColorForWorkHours(workHours)
                val event = Event(color, timestamp)
                compactCalendarView.addEvent(event)
            }
        }

        compactCalendarView.setListener(object : CompactCalendarView.CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date) {
                val events = compactCalendarView.getEvents(dateClicked)
                // Obsługa kliknięcia na dzień
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date) {
                // Obsługa przewijania miesiąca
            }
        })

        return view
    }

    private fun getColorForWorkHours(workHours: Double): Int {
        return when {
            workHours < 2.0 -> Color.RED     // Przykładowy kolor dla mniej niż 2 godzin
            workHours < 5.0 -> Color.YELLOW  // Przykładowy kolor dla mniej niż 5 godzin
            else -> Color.GREEN              // Przykładowy kolor dla 5 godzin lub więcej
        }
    }
}
