package com.example.mobile4

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.ProgressBar
import java.util.concurrent.TimeUnit

class FragmentAsync : Fragment() {

    private var circleProgressBar: ProgressBar? = null
    private var horizontalProgressBar: ProgressBar? = null
    private var listView: ListView? = null

    var messageList = ArrayList<String>()

    private var messageListAdapter: ArrayAdapter<String>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    @SuppressLint("CutPasteId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.fragment_async, container, false)

        circleProgressBar = v.findViewById(R.id.circle_bar)
        horizontalProgressBar = v.findViewById(R.id.horizontal_progress)
        listView = v.findViewById(R.id.message_list)

        messageListAdapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_list_item_1,
            messageList
        )

        listView!!.adapter = messageListAdapter
        executeMessageTask()

        return v
    }

    internal inner class MessageTask : AsyncTask<Void?, Int?, Void?>() {
        override fun onPreExecute() {
            //before the task
            super.onPreExecute()
            circleProgressBar!!.visibility = View.VISIBLE
        }

        override fun onProgressUpdate(vararg values: Int?) {
            //when task is being completed
            super.onProgressUpdate(*values)

            horizontalProgressBar!!.progress = values[0]!!.mod(2) //0 or 1, on every single iteration gets updated

            if (values[0]!!.mod(2) === 0) {
                //on every even one - add a message
                messageListAdapter!!.add("Message ${(values[0]!! / 2)}")
                horizontalProgressBar!!.progress = 0
            }

        }

        protected override fun doInBackground(vararg p0: Void?): Void? {
            try {
                var counter = 0
                for (i in 0..24) {
                    //length of cycle determines how many messages
                    TimeUnit.SECONDS.sleep(1)
                    //one message per two seconds, one message per two iterations, 1 second between iterations
                    publishProgress(++counter)
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            return null
        }

        override fun onPostExecute(aVoid: Void?) {
            //after the whole asyncTask
            super.onPostExecute(aVoid)
            circleProgressBar!!.visibility = View.INVISIBLE
            horizontalProgressBar!!.visibility = View.INVISIBLE
        }
    }

    private fun executeMessageTask() {
        val messageTask: MessageTask = MessageTask()
        messageTask.execute()
    }
}