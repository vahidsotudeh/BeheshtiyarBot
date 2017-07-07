import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import retrofit2.Call;
import retrofit2.Retrofit;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Microsoft on 07/07/2017.
 */
public class BeheshtiyarBot extends TelegramLongPollingBot {
    public BeheshtiyarBot(DefaultBotOptions options) {
        super(options);
    }
    Retrofit retrofit;
    SiteApis apiList;
    Gson gson;
    public BeheshtiyarBot() {
        gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://127.0.0.1:8080")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

         apiList = retrofit.create(SiteApis.class);


    }

    public void onUpdateReceived(Update update) {
        System.out.println(update.getMessage().getText());
        if (update.hasMessage()){
            if (update.getMessage().hasText()){
                if (update.getMessage().getText().equals("/start")){
                    SendMessage sendmsg=new SendMessage();
                    sendmsg.setText("سلام، به بهشتی یار خوش آمدید");
                    sendmsg.setChatId(String.valueOf(update.getMessage().getChatId()));
                    org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup re = new org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup();
                    List<KeyboardRow> keyboardRowList = new ArrayList<>();
                    KeyboardRow male_female = new KeyboardRow();
                    KeyboardButton male = new KeyboardButton();
                    male.setText("لیست 20 آگهی اخیر");
                    male_female.add(male);
                    keyboardRowList.add(male_female);
                    re.setKeyboard(keyboardRowList);
                    re.setResizeKeyboard(true);
                    sendmsg.setReplyMarkup(re);
                    try {
                        sendMessage(sendmsg);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }

                }else if (update.getMessage().getText().equals("لیست 20 آگهی اخیر")){
                    Call<List<Advertisement>> ads=apiList.getAdsList(0,5);
                    System.out.println(ads.request().url());
                    List<Advertisement> adsList= null;
                    try {
                        adsList = ads.execute().body();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println(adsList.size());
                    SendPhoto sendMsg=new SendPhoto();
                    sendMsg.setNewPhoto(new File("./khtb.jpg"));
                    sendMsg.setChatId(update.getMessage().getChatId());
                    for (Advertisement ad :adsList ) {
                        List<List<InlineKeyboardButton>> test=new ArrayList<>();
                        InlineKeyboardButton b1=new InlineKeyboardButton();
                        b1.setText("مشاهده اطلاعات کامل و صفحه آگهی");
                        b1.setUrl("127.0.0.1:8080/ad/"+ad.id);
                        List<InlineKeyboardButton> l1=new ArrayList<>();
                        l1.add(b1);
                        test.add(l1);
                        InlineKeyboardMarkup re=new InlineKeyboardMarkup();
                        re.setKeyboard(test);
                        sendMsg.setReplyMarkup(re);
                        sendMsg.setCaption("عنوان آگهی :"+ad.title+"\n\n"+
                                        "قیمت:"+ad.price+"\n\n"+
                                        "دسته بندی:"+ad.category.title+"\n\n"+
                                        "دانشگاه:"+ad.location.university+"\n\n"+
                                        "دانشکده:"+ad.location.department+"\n\n"+
                                        "@BeheshtiyarBot"+
                                        ""
                        );
                        try {
                            sendPhoto(sendMsg);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public String getBotUsername() {
        return null;
    }

    public String getBotToken() {
        return "406597435:AAG7WJ0JEwhjcHKV_c9S2Ageu6T5uJR8KfE";
    }
}
