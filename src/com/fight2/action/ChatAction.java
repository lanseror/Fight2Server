package com.fight2.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.fight2.dao.ChatMessageDao;
import com.fight2.dao.UserDao;
import com.fight2.model.ChatMessage;
import com.fight2.model.User;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;

@Namespace("/chat")
public class ChatAction extends BaseAction {
    private static final long serialVersionUID = 5916134694406462553L;
    private static final int MAX_MSG_SIZE = 1000;

    @Autowired
    private ChatMessageDao chatMessageDao;
    @Autowired
    private UserDao userDao;
    private List<ChatMessage> datas;
    private String msg;
    private int index;

    private final Random random = new Random();
    private final String testString = "赵钱孙李周吴郑王冯陈褚卫蒋沈韩杨朱秦尤许何吕施张孔曹严华金魏陶姜戚谢邹喻柏水窦章云苏潘葛奚范彭郎鲁韦昌马苗凤花方俞任袁柳酆鲍史唐费廉岑薛雷贺倪汤滕殷罗毕郝邬安常乐于时傅皮卞齐康伍余元卜顾孟平黄和穆萧尹姚邵湛汪祁毛禹狄米贝明臧计伏成戴谈宋茅庞熊纪舒屈项祝董梁杜阮蓝闵席季麻强贾路娄危江童颜郭梅盛林刁钟徐邱骆高夏蔡田樊胡凌霍虞万支柯昝管卢莫经房裘缪干解应宗丁宣贲邓郁单杭洪包诸左石崔吉钮龚程嵇邢滑裴陆荣翁荀羊於惠甄曲家封芮羿储靳汲邴糜松井段富巫乌焦巴弓牧隗山谷车侯宓蓬全郗班仰秋仲伊宫宁仇栾暴甘钭厉戎祖武符刘景詹束龙叶幸司韶郜黎蓟薄印宿白怀蒲台丛鄂索咸籍赖卓蔺屠蒙池乔阴佟胥能苍双闻莘党翟谭贡劳逄姬申扶堵冉宰郦雍却璩桑桂濮牛寿通边扈燕冀郏浦尚农温别庄晏柴瞿阎充慕连茹习宦艾鱼容向古易慎戈廖庚终暨居衡步都耿满弘匡国文寇广禄阙东殴殳沃利蔚越夔隆师巩厍聂晁勾敖融冷訾辛阚那简饶空曾毋沙乜养鞠须丰巢关蒯相查后荆红游竺权逯盖益桓公万俟司马上官欧阳夏侯诸葛闻人东方赫连皇甫尉迟公羊澹台公冶宗政濮阳淳于单于太叔申屠公孙仲孙轩辕令狐钟离宇文长孙慕容鲜于闾丘司徒司空亓官司寇仉督子车颛孙端木巫马公西漆雕乐正壤驷公良拓跋夹谷宰父谷梁晋楚闫法汝鄢涂钦段干百里东郭南门呼延归海羊舌微生岳帅缑亢况郈有琴梁丘左丘东门西门商牟佘佴伯赏南宫墨哈谯笪年爱阳佟第五言福";

    @Action(value = "send", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String send() {
        final User loginUser = getLoginUser();
        final User user = userDao.get(loginUser.getId());
        final Calendar calendar = Calendar.getInstance();
        final DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        final String dateString = dateFormat.format(calendar.getTime());
        final ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(user.getName());
        chatMessage.setContent(msg);
        chatMessage.setDate(dateString);
        chatMessageDao.add(chatMessage);
        final ActionContext context = ActionContext.getContext();
        context.put("jsonMsg", "ok");
        return SUCCESS;
    }

    @Action(value = "test", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String test() {
        final Calendar calendar = Calendar.getInstance();
        final DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        final String dateString = dateFormat.format(calendar.getTime());
        for (int i = 0; i < 100; i++) {
            final ChatMessage chatMessage = new ChatMessage();
            chatMessage.setSender("Chesley");
            final char[] chars = new char[45];
            for (int j = 0; j < 45; j++) {
                chars[j] = testGetCharacter();
            }
            final String content = String.valueOf(chars);
            chatMessage.setContent(content);
            chatMessage.setDate(dateString);
            chatMessageDao.add(chatMessage);
        }
        final ActionContext context = ActionContext.getContext();
        context.put("jsonMsg", "ok");
        return SUCCESS;
    }

    private char testGetCharacter() {
        return testString.charAt(random.nextInt(testString.length() - 1));
    }

    @Action(value = "get", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String get() {
        final Map<String, Object> data = Maps.newHashMap();
        final ActionContext context = ActionContext.getContext();
        index = getUserIndex(index);

        final int maxId = chatMessageDao.getMaxId();
        if (maxId - index > MAX_MSG_SIZE) {
            index = maxId - MAX_MSG_SIZE;
        }
        final List<ChatMessage> messages = chatMessageDao.listFromId(index, MAX_MSG_SIZE);
        final int msgSize = messages.size();

        if (msgSize == 0) {
            data.put("index", index);
        } else {
            final ChatMessage lastMsg = messages.get(msgSize - 1);
            data.put("index", lastMsg.getId());
        }
        data.put("msg", messages);
        context.put("jsonMsg", new Gson().toJson(data));
        return SUCCESS;
    }

    private int getUserIndex(final int index) {
        final User userLogin = getLoginUser();
        final User user = userDao.get(userLogin.getId());
        final int userIndex = user.getMsgIndex();
        if (userIndex == -1) {
            return userIndex;
        } else {
            if (index > userIndex) {
                user.setMsgIndex(index);
                userDao.update(user);
                return index;
            } else {
                return userIndex;
            }

        }

    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(final String msg) {
        this.msg = msg;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(final int index) {
        this.index = index;
    }

    public ChatMessageDao getChatMessageDao() {
        return chatMessageDao;
    }

    public void setChatMessageDao(final ChatMessageDao chatMessageDao) {
        this.chatMessageDao = chatMessageDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(final UserDao userDao) {
        this.userDao = userDao;
    }

    public List<ChatMessage> getDatas() {
        return datas;
    }

    public void setDatas(final List<ChatMessage> datas) {
        this.datas = datas;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
