package com.altenull.hallo_osna

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.altenull.hallo_osna.data.Student
import com.altenull.hallo_osna.databinding.ActivityHelpBinding
import com.altenull.hallo_osna.delegate.StudentDelegate
import com.altenull.hallo_osna.delegate.StudentDelegation
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo

class HelpActivity : AppCompatActivity(), StudentDelegation by StudentDelegate() {
    private lateinit var binding: ActivityHelpBinding
    private lateinit var students: List<Student>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        overridePendingTransition(R.anim.help_enter, R.anim.hold)

        students = getStudentsFromIntent(this)

        binding.helpLogo.setOnClickListener {
            YoYo.with(Techniques.Tada)
                .duration(1000L)
                .playOn(it)
        }
        binding.helpSymbol.setOnClickListener {
            YoYo.with(Techniques.RubberBand)
                .duration(1000L)
                .playOn(it)
        }

        binding.helpParticipantsMarquee.text = generateParticipantsMarquee()
        binding.helpParticipantsMarquee.isSelected = true
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.help_exit)
    }

    private fun generateParticipantsMarquee(): String {
        val paddingLong = "        "
        val paddingShort = "    "
        val participants = StringBuilder(paddingLong)

        // Filter HALLO-OSNA creators
        val filteredStudents = students.filterNot {
            (it.name == "김다미") || (it.name == "김헌영")
        }

        for (i in filteredStudents.lastIndex downTo 0) {
            participants.append(filteredStudents[i].name + paddingShort)
        }

        return participants.toString()
    }

    companion object {
        const val TAG = "HelpActivity"
    }
}