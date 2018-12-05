package nks.griplockiot.createcourse

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_create_course.*
import nks.griplockiot.R
import nks.griplockiot.data.HoleAdapter
import nks.griplockiot.database.AppDatabase
import nks.griplockiot.model.Course
import nks.griplockiot.model.Hole
import kotlin.concurrent.thread

class CreateCourseFragment : Fragment() {

    private lateinit var list: List<Course>
    lateinit var course: Course

    private var courseListCreateCourse: ArrayList<Hole> = ArrayList()
    private var holeIndex: Int = 18

    companion object {
        fun newInstance(): CreateCourseFragment {
            return CreateCourseFragment()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        addCourses(holeIndex)

        setHasOptionsMenu(true)

        // TODO: Create a number picker for par / length

        course_list_create_course.layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)

        val adapter = HoleAdapter(courseListCreateCourse, onClickListener = { view, hole ->
            Toast.makeText(context, "paskaa", Toast.LENGTH_SHORT).show()
        })

        course_list_create_course.adapter = adapter

        minusButton.setOnClickListener {
            if (holeIndex > 0) {
                holeIndex--
                holes.text = holeIndex.toString()
                // Remove last item from RecyclerView
                courseListCreateCourse.removeAt(courseListCreateCourse.size - 1)
                // Update adapters last index item
                adapter.notifyItemRemoved(courseListCreateCourse.size)
            } else {
                Toast.makeText(context, "Course must contain at least one hole", Toast.LENGTH_SHORT).show()
            }
        }

        plusButton.setOnClickListener {
            if (holeIndex < 36) {
                //TODO: Make adapter transitions smoother
                holeIndex++
                holes.text = holeIndex.toString()
                addCourse(adapter.itemCount + 1)
                course_list_create_course.scrollToPosition(courseListCreateCourse.size - 1)
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(context, "Course must contain a maximum of 36 holes", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_course, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            // TODO: Change menu icon
            R.id.menuAddCourse -> {
                Toast.makeText(context, "You clicked menu add course, inserting to DB", Toast.LENGTH_SHORT).show()
                thread {
                    // TODO: Error checking
                    AppDatabase.getInstance(context!!).getCourseDAO().insert(Course(courseNameEditText.text.toString(), calculateTotalPar(courseListCreateCourse), courseListCreateCourse))
                }
            }

            R.id.queryDB -> {
                thread {
                    list = AppDatabase.getInstance(context!!).getCourseDAO().getCourses()
                }
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun addCourses(holes: Int) {
        for (i in 1..holes) {
            with(courseListCreateCourse) {
                add(Hole(i, 3, 63))
            }
        }
    }

    private fun addCourse(index: Int) {
        courseListCreateCourse.add(Hole(index, 3, 50))
    }

    private fun calculateTotalPar(holeList: ArrayList<Hole>): Int {
        val iterator = holeList.listIterator()
        var parTotal = 0

        for (item in iterator) {
            parTotal += item.par
        }
        return parTotal
    }
}