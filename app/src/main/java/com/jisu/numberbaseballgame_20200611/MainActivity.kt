package com.jisu.numberbaseballgame_20200611

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.jisu.numberbaseballgame_20200611.adapters.ChatAdapter
import com.jisu.numberbaseballgame_20200611.datas.Chat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

//   시도한 숫자들을 저장하는 배열
    val tryNumberStrArr = ArrayList<String>()

//  몇번 시도했는지 저장할 변수
    var inputCount = 0

//  컴퓨터가 낸 문제 숫자 세개를 저장할 ArrayList
    val computerNumbers = ArrayList<Int>()
//  채팅 내역을 담아줄 ArrayList
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

//            3글자가 아니면 아예 입력 불가
            if(inputNumberStr.length != 3){
                Toast.makeText(mContext, "숫자는 반드시 세 자리여야 합니다.", Toast.LENGTH_SHORT).show()
//                리턴 타입이 없는 함수에서의 return: 함수를 강제 종료시키는 키워드로 사용함
//                @ 어느 함수를 종료시키는 지 명확히 명시
                return@setOnClickListener
            }
            
//            0이 포함되어 있다면 안내처리
            if(inputNumberStr.contains("0")){
                Toast.makeText(mContext, "0은 문제에 포함되지 않습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

//            이미 시도해본 숫자라면 다시 시도하지 않도록 막자
             for(alreadyNumberStr in tryNumberStrArr){

                 if(alreadyNumberStr == inputNumberStr) {
//                     중복을 발견하면 안내하고 강제 종료
                     Toast.makeText(mContext, "${inputNumberStr}은 이미 시도해 본 숫자입니다.", Toast.LENGTH_SHORT).show()
                     return@setOnClickListener
                 }
             }

//            시도해본 숫자 목록에 등록
            tryNumberStrArr.add(inputNumberStr)

            val userChat = Chat("USER", inputNumberStr)

            chatMessageList.add(userChat)

            mChatAdapter.notifyDataSetChanged()

//            여기서도 자동 스크롤 처리
            chatListView.smoothScrollToPosition(chatMessageList.size - 1)

            // 입력을 하고 나면 editText의 내용을 빈칸으로
            //  String 잘 먹히지 않아 setString 으로
            numberInputEdt.setText("")
            checkStrikeAndBall(inputNumberStr.toInt())
        }
   }

    override fun setValues() {

        mChatAdapter = ChatAdapter(mContext, R.layout.chat_list_item, chatMessageList)
        chatListView.adapter = mChatAdapter

        //      컴퓨터에게 문제를 내라고 시키자 => 문자: 3칸짜리 숫자 배열
        makeComputerNumber()
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
        mChatAdapter.notifyDataSetChanged()

        Handler().postDelayed({
            chatMessageList.add(Chat("CPU", "제가 생각하는 세자리 숫자를 맞춰주세요."))
            mChatAdapter.notifyDataSetChanged()
        },1500)

        Handler().postDelayed({
            chatMessageList.add(Chat("CPU", "1~9의 숫자로만 구성되고, 중복된 숫자는 없습니다."))
            mChatAdapter.notifyDataSetChanged()
        },3000)
    }

    fun checkStrikeAndBall(inputNum: Int){

//        시도 횟k수를 한번 증가
        inputCount++

//        inputNum은 세 자리 숫자가 들어온다고 전제
        val inputNumArr = ArrayList<Int>()
//        100의 자리, 10의 자리, 1의 자리
//        각 자리 채우기
        inputNumArr.add(inputNum / 100) // 100의 자리
        inputNumArr.add(inputNum / 10 % 10) // 10의 자리
        inputNumArr.add(inputNum % 10) // 1의 자리

        var strikeCount = 0
        var ballCount = 0

//        사용자 숫자를 들고 => 컴퓨터 숫자를 조회 => 통째로 반복
        for (i in inputNumArr.indices) {
            for (j in computerNumbers.indices) {
                // 같은 숫자인가?
                if(inputNumArr[i] == computerNumbers[j]) {
                    //위치도 같은가?
                    if (i == j){
                        strikeCount++
                    } else {
                        ballCount++
                    }
                }
            }
        }

//        ?S?B인지 변수에 담겨있다. => 채팅메시지로 가공해서 컴퓨터가 답장
         val answer = Chat("CPU", "${strikeCount}S ${ballCount}B 입니다.")

//        답장은 1초 후에 올라오도록 => 1초 후에 실행
        val myHandler = Handler()

        myHandler.postDelayed({
            chatMessageList.add(answer)
            mChatAdapter.notifyDataSetChanged()

//        리스트뷰에 내용물이 추가되고 나서 => 바닥으로 리스트를 끌어내리자.
//        목록 중 맨 마지막 것으로 이동. 목록 중 맨 마지막의 포지션?
            chatListView.smoothScrollToPosition(chatMessageList.size - 1)

            //        S3이면 게임 종료처리
            if(strikeCount == 3) {

                Handler().postDelayed({
                    finishGame()
                },1000)
            }

        }, 1000)
    }

    fun finishGame() {

//        축하 메시지를  CPU가 말해줌
        val congratulation = Chat("CPU", "축하합니다. 정답을 맞추었습니다!")

        chatMessageList.add(congratulation)
        mChatAdapter.notifyDataSetChanged()

//        몇번 만에 맞췄는지?
        val countChat = Chat("CPU", "${inputCount} 번만에 맞추었습니다.")

        chatMessageList.add(countChat)
        mChatAdapter.notifyDataSetChanged()

//        더이상 입력하지 못하도록 처리
        numberInputEdt.isEnabled = false
        okBtn.isEnabled = false

//        종료 알림 토스트
        Toast.makeText(mContext, "이용해주셔서 감사합니다.", Toast.LENGTH_LONG).show()
    }
}
