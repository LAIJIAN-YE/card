package com.example.tryapplication.data;



import com.example.tryapplication.R;

import java.util.ArrayList;

/**
 * 中級單字 內容資料
 */
public class ExampleList_pr {
    private ArrayList<FormatEnglish> englishArrayList;
   public ArrayList<FormatEnglish> englishes(){
       englishArrayList=new ArrayList<>();
       englishArrayList.add(new FormatEnglish("a bird in the hand is worth two in the bush", "雙鳥在林不如一鳥在手", "bird", "鳥，禽", "[bɝd]",  false, R.raw.bird0001));
       englishArrayList.add(new FormatEnglish("She did the work to the best of her ability", "她已盡了力去做那件工作了", "ability", "能力，能耐", "[əˈbɪləti]", false,  R.raw.abilit01));
       englishArrayList.add(new FormatEnglish("My mother often spoke to me about you", "家母常和我談起你。", "about", "關於，對於；在……的附近；在……的周圍", "[əˋbaʊt]",  false,  R.raw.about001));
       englishArrayList.add(new FormatEnglish("The moon is now above the trees.", "月亮正位於樹梢上", "above", "在……上面；在……之上，超過", "[əˋbʌv]", false,  R.raw.above001));
       englishArrayList.add(new FormatEnglish("He works at a bakery", "他在一家麵包房工作。。", "bakery", "麵包（糕點）烘房；麵包（糕點）", "[ˋbekərɪ]",  false,  R.raw.bakery01));
       englishArrayList.add(new FormatEnglish("You can see the sea from our balcony. ", "從我們的陽臺，你可以看到大海", "balcony", "陽臺，露臺", "[ˋbælkənɪ]",  false,  R.raw.balcon01));
       englishArrayList.add(new FormatEnglish("His face is thin and very tanned.", "他的面孔瘦削，曬得黑黑的。", "face", "臉，面孔", "[fes]",  false,  R.raw.face0001));
       englishArrayList.add(new FormatEnglish("The kangaroo is a native of Australia.", "袋鼠是澳大利亞的土生動物", "kangaroo", "袋鼠", "[kæŋgəˋru]", false,R.raw.kangar01));
       englishArrayList.add(new FormatEnglish("She has a five-month-old baby.", "她有一個五個月的嬰兒。", "baby", "嬰兒，幼畜", "[ˋbebɪ]", false,R.raw.baby0001));
       englishArrayList.add(new FormatEnglish("He blamed you for the neglect of duty.", "他責備你怠忽職守。", "blame", "責備，指責", "[blem]", false,R.raw.blame001));
       englishArrayList.add(new FormatEnglish("he truck used a cable to tow the car.", "卡車用纜索拖曳汽車。", "cable", "纜，索，鋼索", "[ˋkeb]", false,R.raw.cable001));
       englishArrayList.add(new FormatEnglish("You should cancel this preposition in the sentence.", "你應該刪去句子中的這個介係詞。", "cancel", "刪去，劃掉，勾銷", "[ˋkæns]", false,R.raw.cancel01));
       englishArrayList.add(new FormatEnglish("I gave him a book.", "我給他一本書。", "give", "給；送給", "[gɪv]", false,R.raw.give0001));
       englishArrayList.add(new FormatEnglish("The rebels fought for freedom.", "反叛者為自由而戰鬥。", "freedom", "自由，獨立自主", "[ˋfridəm]", false,R.raw.freedo01));
       englishArrayList.add(new FormatEnglish("We stayed at a resort hotel during the holidays.", "假期裡我們住在一家觀光旅館。", "hotel", "旅館；飯店", "[hoˋtɛl]", false,R.raw.hotel001));
       englishArrayList.add(new FormatEnglish("Who lent you the bike?", "誰借給你這輛自行車？", "lend", "把……借給", "[lɛnd]", false,R.raw.lend0001));
       englishArrayList.add(new FormatEnglish("Some articles of furniture were lost when we moved.", "我們搬家時有幾件傢俱丟失了。", "furniture", "傢俱", "[ˋfɝnɪtʃɚ]", false,R.raw.furnit01));
       englishArrayList.add(new FormatEnglish("The potatoes were cooked in their jackets.", "這些馬鈴薯是帶皮煮的。", "jacket", "夾克，上衣", "[ˋdʒækɪt]", false,R.raw.jacket01));
       englishArrayList.add(new FormatEnglish("The little girl is afraid of thunder and lightning.", "這小女孩害怕雷聲和閃電", "lightning", "閃電，電光，意外的幸運", "[ˋlaɪtnɪŋ]", false,R.raw.lightn02));
       englishArrayList.add(new FormatEnglish("The young writer learned a great deal from the works by masters in literature", "這位青年作家從文學大師的作品中學到了許多東西。", "master", "大師，能手，名家", "[ˋmæstɚ]", false,R.raw.master01));
       englishArrayList.add(new FormatEnglish("The Allies finally smashed the Nazi war machine.", "同盟國最終粉碎了納粹的戰爭機器。", "machine", "機器，機械", "[məˋʃin]", false,R.raw.machin05));
       englishArrayList.add(new FormatEnglish("She had on an ordinary dress.", "她穿著平常的衣服。", "ordinary", "通常的，平常的", "[ˋɔrdn͵ɛrɪ]", false,R.raw.ordina04));
       englishArrayList.add(new FormatEnglish("The young couple shared housework", "這對年輕夫婦分擔家務事。", "housework", "家事", "[ˋhaʊs͵wɝk]", false,R.raw.housew10));
       englishArrayList.add(new FormatEnglish("A rabbit can make long jumps.", "兔子可以跳得很遠。", "rabbit", "兔，野兔", "[ˋræbɪt]", false,R.raw.rabbit01));
       englishArrayList.add(new FormatEnglish("I read a good article in today's paper.", "我在今天的報紙上讀到了一篇好文章。", "read", "讀，閱讀；朗讀", "[rid]", false,R.raw.read0001));
       englishArrayList.add(new FormatEnglish("Earthquake shocks are often felt in Japan.", "在日本常常感覺到地震引起的震動", "shock", "衝擊，衝撞，震動", "[ʃɑk]", false,R.raw.shock001));
       englishArrayList.add(new FormatEnglish("I am sorry to hear that your brother passed away.", "聽到你兄弟去世的消息，我很難過。", "sorry", "感到難過的，感到可憐的", "[ˋsɑrɪ]", false,R.raw.sorry001));
       englishArrayList.add(new FormatEnglish("She threw me a towel.", "她丟給我一條毛巾。", "throw", "投，擲，拋，扔", "[θro]", false,R.raw.throw001));
       englishArrayList.add(new FormatEnglish("Children must be educated to serve their country when they grow up.", "必須教育孩子長大後為國家服務", "serve", "為……服務", "[sɝv]", false,R.raw.serve001));
       englishArrayList.add(new FormatEnglish("He turned his head and saw a figure approaching in the darkness.", "他轉過頭，看見在黑暗中有個人影走過來。", "turn", "使轉動，使旋轉", "[tɝn]", false,R.raw.turn0001));
       englishArrayList.add(new FormatEnglish("There are two zeros in 1008. ", "在1008中有兩個零。", "zero", "零", "[ˋzɪro]", false,R.raw.zero0001));
       englishArrayList.add(new FormatEnglish("The moon has not risen yet. ", "月亮還沒有升起。", "yet", "還沒", "[jɛt]", false,R.raw.yet00001));
       englishArrayList.add(new FormatEnglish("Jimmy is uncle to all the kids who like to go boating with him.", "那些喜歡和吉米一起乘船遊玩的孩子都把他當叔叔。", "uncle", "伯父，叔父", "[ˋʌŋk!]]", false,R.raw.uncle001));
       englishArrayList.add(new FormatEnglish("He succeeded in getting the job. ", "他謀得了那份工作。", "succeed", "成功，辦妥，獲得成效", "[səkˋsid]", false,R.raw.succee01));
       englishArrayList.add(new FormatEnglish("Those are wild roses.", "那些是野玫瑰。", "wild", "野的，野生的，未被人馴養的", "[waɪld]", false,R.raw.wild0001));
       englishArrayList.add(new FormatEnglish("He had a talent for music.", "他有音樂天才。", "talent", "天才，天資", "[ˋtælənt]", false,R.raw.talent01));
       englishArrayList.add(new FormatEnglish("This is my last semester in college.", "這是我在大學裡的最後一個學期。", "semester", "半學年", "[səˋmɛstɚ]", false,R.raw.semest01));
       englishArrayList.add(new FormatEnglish("May God protect you from harm.", "願上帝保佑你免受傷害。", "protect", "保護，防護", "[prəˋtɛkt]", false,R.raw.protec01));



    return englishArrayList;
   }
}
