import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.locationguardpro.R
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import java.util.*

class CalendarFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        val compactCalendarView = view.findViewById<CompactCalendarView>(R.id.calendarView)
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY)

        // Przykładowe dane o przepracowanych godzinach dla różnych dni
        val workHoursMap = hashMapOf(
            getDateInMillis(2024, Calendar.JANUARY, 10) to 2.5,  // 10 stycznia 2024 - przykładowe godziny
            getDateInMillis(2024, Calendar.JANUARY, 10, 1, 30) to 6.0   // 10 stycznia 2024 - przykładowe godziny
        )

        for ((timestamp, workHours) in workHoursMap) {
            val color = getColorForWorkHours(workHours)
            val event = Event(color, timestamp)
            compactCalendarView.addEvent(event)
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

    private fun getDateInMillis(year: Int, month: Int, day: Int, hour: Int = 0, minute: Int = 0, second: Int = 0): Long {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute, second)
        return calendar.timeInMillis
    }

    private fun getColorForWorkHours(workHours: Double): Int {
        return when {
            workHours < 2.0 -> Color.RED     // Przykładowy kolor dla mniej niż 2 godzin
            workHours < 5.0 -> Color.YELLOW  // Przykładowy kolor dla mniej niż 5 godzin
            else -> Color.GREEN              // Przykładowy kolor dla 5 godzin lub więcej
        }
    }
}
