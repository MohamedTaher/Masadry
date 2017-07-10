package com.example.root.newsapp.helper;

import android.content.Context;
import android.content.res.Resources;

/**
 * Created by root on 4/10/17.
 */

public class Language {
    public static void ChangeLanguage(Context _context, String lang) {
        Context context = LocaleHelper.setLocale(_context, lang);
        Resources resources = context.getResources();
    }
}
