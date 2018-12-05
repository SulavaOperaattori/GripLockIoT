package nks.griplockiot.createcourse

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.fragment_view_course.*
import nks.griplockiot.R
import nks.griplockiot.data.CourseAdapter
import nks.griplockiot.database.AppDatabase
import nks.griplockiot.model.Course
import java.io.Serializable

class ViewCourseFragment : Fragment() {

    companion object {
        fun newInstance(): ViewCourseFragment {
            return ViewCourseFragment()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Show course yhteenveto and show details on click, long click to delete a course
        course_list_view_course.layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)

        // TODO: Add total course length
        val arrayList: ArrayList<Course> = ArrayList(AppDatabase.getInstance(activity!!.applicationContext).getCourseDAO().getCourses())

        val adapter = CourseAdapter(arrayList, onLongClickListener = { view, course ->
            val intent = Intent(context, ViewCourseActivity::class.java)
            intent.putExtra("course", course as Serializable)
            startActivity(intent)
        })
        course_list_view_course.adapter = adapter

    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        menu?.findItem(R.id.menuAddCourse)?.isVisible = false
        super.onPrepareOptionsMenu(menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_view_course, container, false)
    }
}
