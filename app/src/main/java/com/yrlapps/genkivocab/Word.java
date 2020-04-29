package com.yrlapps.genkivocab;

class Word {
   private String kana;
    private String kanji;
   private String romaji;
    private String english;
    private String wordType;
    private int position;

    public Word(String kana, String kanji, String romaji, String english, String wordType,int position){
        this.kana=kana;
        this.kanji=kanji;
        this.romaji=romaji;
        this.english=english;
        this.position = position;
        this.wordType = wordType;
    }

    public String getEnglish() {
        return english;
    }

    public String getRomaji() {
        return romaji;
    }

    public String getKanji() {
        return kanji;
    }

    public String getKana() {
        return kana;
    }

    public int getPosition() {
        return position;
    }

    public String getWordType() {
        return wordType;
    }
}
