package com.jisu.numberbaseballgame_20200611

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.jisu.numberbaseballgame_20200611.adapters.ChatAdapter
import com.jisu.numberbaseballgame_20200611.datas.Chat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    
//  컴퓨터가 낸 문제 숫자 세개를 저장할 ArrayList
    val computerNumbers = ArrayList<Int>()
    //    채팅 내역을 담아줄 ArrayList
    val chatMessageList = ArrayList<Chat>()

    lateinit var mChatAdapter: ChatAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        okBtn.setOnClickListener {
            val inputNumberStr = numberInputEdt.text.toString()

            val userChat = Chat("USER", inputNumberStr)

            chatMessageList.add(userChat)

            mChatAdapter.notifyDataSetChanged()

            // 입력을 하고 나면 editText의 내용을 빈칸으로
            //  String 잘 먹히지 않아 setString 으로
            numberInputEdt.setText("")
            checkStrikeAndBall(inputNumberStr.toInt())
        }
   }

    override fun setValues() {
//      컴퓨터에게 문제를 내라고 시키자 => 문자: 3칸짜리 숫자 배열
        makeComputerNumber()

        mChatAdapter = ChatAdapter(mContext, R.layout.chat_list_item, chatMessageList)
        chatListView.adapter = mChatAdapter
    }
    
    fun makeComputerNumber(){

//        숫자 3개를 랜덤 생성. => 3번 반복
        for (i in 0..2) {
            while (true) {

//                0*9 <= (Math.random()*9+1).toInt() < 1*9+1
                val randomNum = (Math.random()*9+1).toInt()

                Log.d("랜덤값", randomNum.toString())

                var isNumberOk = true
//                조건에 맞는 숫자를 뽑으면 무한반복 탈출
//                1~9는 이미 랜덤의 범위를 제한해서 만족
//                중복이 아니어야 사용해도 좋은 숫자로 인정해주자
//                중복검사: 문제 숫자 배열에 있는 값들을 다 꺼내서 지금 만든 랜덤값과 비교
//                한번이라도 같은 걸 찾았다면 => 중복, 한번도 없어야 중복이 아니다.

                 for(cpuNum in computerNumbers) {

//                     랜덤값과 문제로 뽑아둔 숫자가 같다면? 중복, 사용하면 안됨
                    if (cpuNum == randomNum) {
                        isNumberOk = false
                        break
                    }
                 }

//                조건에 맞는 숫자를 뽑으면 대입 => 무한반복 탈출
                if(isNumberOk) {
                    computerNumbers.add(randomNum)
                    break
                }
            }
        }
//        문제가 뭔지? 확인 for
         for(num in computerNumbers){
             Log.d("최종선발문제", num.toString())
         }

//        문제를 다 내고, 안내 메세지를 채팅으로 출력.
        chatMessageList.add(Chat("CPU", "숫자 야구 게임에 오신것을 환영합니다."))
        chatMessageList.add(Chat("CPU", "제가 생각하는 세자리 숫자를 맞춰주세요."))
        chatMessageList.add(Chat("CPU", "1~9의 숫자로만 구성되고, 중복된 숫자는 없습니다."))

    }

    fun checkStrikeAndBall(inputNum: Int){

//        inputNum은 세 자리 숫자가 들어온다고 전제
        val inputNumArr = ArrayList<Int>()
//        100의 자리, 10의 자리, 1의 자리
//        각 자리 채우기
        inputNumArr.add(inputNum / 100) // 100의 자리
        inputNumArr.add(inputNum / 10 % 10) // 10의 자리
        inputNumArr.add(inputNum % 10) // 1의 자리
        
    }
}
