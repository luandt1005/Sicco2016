package com.sicco.erp.util;

import java.util.Arrays;

import com.sicco.erp.R;

import android.content.Context;


public class AccentRemover {

	String[] SPECIAL_CHARACTERS;
	String[] REPLACEMENTS;
	
	private AccentRemover(Context context){
		SPECIAL_CHARACTERS = context.getResources().getStringArray(R.array.special_characters);
		REPLACEMENTS = context.getResources().getStringArray(R.array.characters);
	}
	private static AccentRemover mInstance;
	public static AccentRemover getInstance(Context context){
		if(mInstance == null) mInstance =  new AccentRemover(context);
		return mInstance;
				
	}
	public String removeAccentRet(String ch) {
		int index = Arrays.binarySearch(SPECIAL_CHARACTERS, ch);
		if (index >= 0) {
			ch = REPLACEMENTS[index];
		}
		return ch;
	}

	public String removeAccent(String s) {
		StringBuilder sb = new StringBuilder(s);
		for (int i = 0; i < sb.length(); i++) {
			sb.setCharAt(i, removeAccentRet(sb.substring(i, i+1)).charAt(0));
		}
		return sb.toString();
	}

}
