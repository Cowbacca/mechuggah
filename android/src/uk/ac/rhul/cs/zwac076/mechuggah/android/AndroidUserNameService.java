package uk.ac.rhul.cs.zwac076.mechuggah.android;

import java.util.regex.Pattern;

import uk.ac.rhul.cs.zwac076.mechuggah.highscore.UserNameService;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.Patterns;

final class AndroidUserNameService extends UserNameService {

    private static final String EMAIL_SPLITTER = "@";
    private Context context;

    public AndroidUserNameService(String unknownUserName, Context context) {
        this.context = context;
        init(unknownUserName);
    }

    @Override
    protected String calculateUserName(String unknownUserName) {
        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts = AccountManager.get(context).getAccounts();

        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                String[] accountParts = account.name.split(EMAIL_SPLITTER);
                return accountParts[0];
            }
        }
        return unknownUserName;
    }
}