package com.jisu.numberbaseballgame_20200611.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.jisu.numberbaseballgame_20200611.R
import com.jisu.numberbaseballgame_20200611.datas.Chat
import kotlinx.android.synthetic.main.chat_list_item.view.*

class ChatAdapter(val mContext:Context, val resId: Int, val mList: List<Chat>) :ArrayAdapter<Chat>(mContext, resId, mList) {

    val inf = LayoutInflater.from(mContext)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var tempRow = convertView
        tempRow?.let {

        }.let {
            tempRow = inf.inflate(R.layout.chat_list_item, null)
        }
        val row = tempRow!!

        val computerChatLayout = row.findViewById<LinearLayout>(R.id.computerChatLayout)
        val userChatLayout = row.findViewById<LinearLayout>(R.id.userChatLayout)

        val computerChatTxt = row.findViewById<TextView>(R.id.computerChatTxt)
        val userChatTxt = row.findViewById<TextView>(R.id.userChatTxt)

        val data = mList[position]

        when (data.who) {
            "USER" -> {
                userChatLayout.visibility = View.VISIBLE
                computerChatLayout.visibility = View.GONE

                userChatTxt.text = data.content
            }
            "CPU" -> {
                userChatLayout.visibility = View.GONE
                computerChatLayout.visibility = View.VISIBLE

                computerChatTxt.text = data.content
            }
            else -> { Log.e("에러발생", "사용자/컴퓨터 외의 화자 발생")}
        }

        return row
    }

}